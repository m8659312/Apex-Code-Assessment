package com.apex.codeassesment.data

import android.util.Log
import com.apex.codeassesment.data.local.LocalDataSource
import com.apex.codeassesment.data.model.User
import com.apex.codeassesment.data.remote.RemoteDataSource
import com.apex.codeassesment.data.remote.UserApi
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject


// => Done i TODO (2 points) : Add tests
// => Done TODO (3 points) : Hide this class through an interface, inject the interface in the clients instead and remove warnings
class UserRepository @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val userApi: UserApi
) : UserRepositoryInterface {

    private val savedUser = AtomicReference(User())

    override fun getSavedUser() = localDataSource.loadUser()

    override fun getUser(forceUpdate: Boolean): User {
        if (forceUpdate) {

//            val user = remoteDataSource.loadUser()
//            localDataSource.saveUser(user)
//            savedUser.set(user)

            try {

                val userResponse = userApi.loadUser().execute()

                if (userResponse.code() == 200) {
                    val responseBody = userResponse.body()

                    if (responseBody?.results != null && responseBody.results.isNotEmpty()) {
                        val user = responseBody.results[0]

                        localDataSource.saveUser(user)
                        savedUser.set(user)

                    }
                } else {
                    Log.e("TAG", "getUser: error in response code")
                }

            } catch (ex: Exception) {
                Log.e("UserRepo", ex.toString())
            }
        }
        return savedUser.get()
    }

    override fun getUsers(): List<User> {
        try {

            val userResponse = userApi.loadUsers().execute()
            if (userResponse.code() == 200) {
                val responseBody = userResponse.body()

                if (responseBody?.results != null && responseBody.results.isNotEmpty()) {
                    return responseBody.results
                }
            } else {
                Log.e("TAG", "getUser: error in response code")
            }

        } catch (ex: Exception) {
            Log.e("User", ex.toString())
        }
        return remoteDataSource.loadUsers()

    }
}

interface UserRepositoryInterface {
    fun getUser(forceUpdate: Boolean): User
    fun getSavedUser(): User
    fun getUsers(): List<User>
}