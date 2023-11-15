package com.apex.codeassesment.data.remote

import com.apex.codeassesment.data.model.UserApiResult
import retrofit2.Call
import retrofit2.http.GET

interface UserApi {


    // TODO (3 points): Load data from endpoint https://randomuser.me/api?results=10
    // TODO (Optional Bonus: 3 points): Handle succes and failure from endpoints

    @GET("/api")
     fun loadUser(): Call<UserApiResult>

    @GET("/api?results=10")
    fun loadUsers(): Call<UserApiResult>
}