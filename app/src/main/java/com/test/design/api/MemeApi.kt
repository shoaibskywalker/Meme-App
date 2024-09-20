package com.test.design.api

import com.test.design.model.Meme
import retrofit2.Call
import retrofit2.http.GET

interface MemeApi {

    @GET("gimme")
    fun getMeme(): Call<Meme>
}