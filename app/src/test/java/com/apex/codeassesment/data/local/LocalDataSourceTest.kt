package com.apex.codeassesment.data.local

import com.apex.codeassesment.data.model.User
import com.squareup.moshi.Moshi
import org.junit.Test


class LocalDataSourceTest {

    @Test
    fun checkUserSavedAndLoadWorkingAsExpected() {
        val moshi = Moshi.Builder().build()
        val fakePreferenceManager = FakePreferenceManager()

        val localDataSource = LocalDataSource(fakePreferenceManager, moshi)
        val user: User = User.createRandom()
        localDataSource.saveUser(user)

        val savedUser = localDataSource.loadUser()

        assert(user.id == savedUser.id)
        assert(user.email == savedUser.email)

    }
}