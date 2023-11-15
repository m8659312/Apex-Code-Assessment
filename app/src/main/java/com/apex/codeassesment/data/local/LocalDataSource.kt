package com.apex.codeassesment.data.local

import com.apex.codeassesment.data.model.User
import com.apex.codeassesment.data.model.User.Companion.createRandom
import com.squareup.moshi.Moshi
import javax.inject.Inject

// => Done TODO (3 points): Convert to Kotlin
// => Done TODO (2 point): Add tests
// =>Done TODO (1 point): Use the correct naming conventions.
// =>Done TODO (3 points): Inject all dependencies instead of instantiating them.

class LocalDataSource @Inject constructor(

    private val preferencesManager: PreferencesManagerInterface, private val moshi: Moshi
) {
    fun loadUser(): User {
        val serializedUser = preferencesManager.loadUser()
        val jsonAdapter = moshi.adapter(User::class.java)
        return try {
            // => Done TODO (1 point): Address Android Studio warning
            if (serializedUser == null) {
                return createRandom()
            }
            val user = jsonAdapter.fromJson(serializedUser)
            user ?: createRandom()
        } catch (e: Exception) {
            e.printStackTrace()
            createRandom()
        }
    }

    fun saveUser(user: User) {
        val jsonAdapter = moshi.adapter(
            User::class.java
        )
        val serializedUser = jsonAdapter.toJson(user)
        preferencesManager.saveUser(serializedUser)
    }
}