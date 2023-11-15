package com.apex.codeassesment.data.local

import android.content.Context
import com.apex.codeassesment.ui.main.MainActivity

// => Done TODO (2 point): Add tests
class PreferencesManager (val context: Context) : PreferencesManagerInterface {


  override fun saveUser(user: String) {
    val sharedPreferences = context.getSharedPreferences("random-user-preferences", Context.MODE_PRIVATE)
    sharedPreferences?.edit()?.putString("saved-user", user)?.apply()
  }

  override fun loadUser(): String? {
    val sharedPreferences = context.getSharedPreferences("random-user-preferences", Context.MODE_PRIVATE)
    return sharedPreferences?.getString("saved-user", null)
  }
  fun removeUser() {
    val sharedPreferences = context.getSharedPreferences("random-user-preferences", Context.MODE_PRIVATE)
    sharedPreferences?.edit()?.remove("saved-user")?.apply()
  }
}

interface  PreferencesManagerInterface{

  fun saveUser(user: String)
  fun loadUser(): String?
}