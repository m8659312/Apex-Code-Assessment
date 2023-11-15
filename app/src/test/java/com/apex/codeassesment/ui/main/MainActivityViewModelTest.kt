package com.apex.codeassesment.ui.main

import com.apex.codeassesment.data.FakeUserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.createTestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class MainActivityViewModelTest {
    private val testDispatcher = TestCoroutineDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testScope = TestCoroutineScope(testDispatcher)


    private lateinit var viewModel: MainActivityViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {

        viewModel = MainActivityViewModel(FakeUserRepository())
        viewModel.coroutineDispatcher = testDispatcher
    }

    @Test
    fun checkFakeData() {

        viewModel.initialize()


        testScope.runBlockingTest {
            val event = viewModel.channel.first()
            assert(event is UiEvent.GetSavedUser)
        }

    }

}