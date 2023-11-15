package com.apex.codeassesment.ui.main

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apex.codeassesment.data.UserRepositoryInterface
import com.apex.codeassesment.data.model.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(private  val userRepository: UserRepositoryInterface) : ViewModel() {

   var coroutineDispatcher: CoroutineDispatcher = Dispatchers.Default

   private val _channel = Channel<UiEvent>()
   val channel = _channel.receiveAsFlow()


   fun initialize() {
      val user = userRepository.getSavedUser()
      viewModelScope.launch(coroutineDispatcher) {
         _channel.send(UiEvent.GetSavedUser(user))
      }
   }

   fun refreshUser(){
      val user = userRepository.getUser(true)
      if(user.name == null)
      {
         return
      }
      viewModelScope.launch(coroutineDispatcher) {
         _channel.send(UiEvent.GetSavedUser(user))
      }

   }


   fun getUsersList(){
      val users = userRepository.getUsers()
      viewModelScope.launch(coroutineDispatcher) {
         _channel.send(UiEvent.GetUserList(users))
      }
   }

}

sealed interface UiEvent {
   data class GetSavedUser(val user: User) : UiEvent
   data class GetUserList(val users: List<User>) : UiEvent
}