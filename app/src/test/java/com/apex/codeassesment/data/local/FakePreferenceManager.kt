package com.apex.codeassesment.data.local

class FakePreferenceManager : PreferencesManagerInterface {

    private  var user : String? = null

    override fun saveUser(user: String) {
        this.user = user
    }

    override fun loadUser(): String? {
        return  user
    }

}