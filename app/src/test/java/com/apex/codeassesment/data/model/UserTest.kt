package com.apex.codeassesment.data.model

import org.junit.Assert.*

import org.junit.Test

class UserTest {

    @Test
    fun checkRandomWorkAsExpected() {

        val user1  = User.createRandom()
        val user2  = User.createRandom()

        assert(user1.name?.first != user2.name?.first)
    }
}