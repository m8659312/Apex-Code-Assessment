package com.apex.codeassesment.data.local

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.apex.codeassesment.data.model.User
import org.junit.Before
import org.junit.Test

class PreferencesManagerTest {

    private lateinit var instrumentationContext: Context

    @Before
    fun setup() {
        instrumentationContext = InstrumentationRegistry.getInstrumentation().context
    }

    @Test
    fun checkSaveAndLoadUserWorksAsExpected() {
        val preferencesManager = PreferencesManager(instrumentationContext)

        val user = User.createRandom()
        if (user.name == null || user.name!!.first == null) {
            assert(false)
        }
        preferencesManager.saveUser(user.name!!.first!!)

        val userName = preferencesManager.loadUser()
        assert(user.name!!.first!! == userName)


        preferencesManager.removeUser()
        val emptyUser = preferencesManager.loadUser()
        assert(emptyUser == null)

    }
}