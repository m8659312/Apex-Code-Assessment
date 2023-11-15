package com.apex.codeassesment.data

import com.apex.codeassesment.data.model.User


class FakeUserRepository :  UserRepositoryInterface {

    private val user = User.createRandom()
    override fun getUser(forceUpdate: Boolean): User {
//        if (forceUpdate) {
//            val user = remoteDataSource.LoadUser()
//            localDataSource.saveUser(user)
//            savedUser.set(user)
//        }
        return user    }

    override fun getSavedUser(): User {
        return   User.createRandom()
    }

    override fun getUsers(): List<User> {
       return (0..10).map { User.createRandom() }
    }


}