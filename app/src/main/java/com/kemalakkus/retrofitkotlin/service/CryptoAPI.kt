package com.kemalakkus.retrofitkotlin.service

import com.kemalakkus.retrofitkotlin.model.CryptoModel
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface CryptoAPI {
    //https://raw.githubusercontent.com/
    // atilsamancioglu/K21-JSONDataSet/master/crypto.json

    //GET, POST, UPDATE, DELETE
    @GET("atilsamancioglu/K21-JSONDataSet/master/crypto.json")
    //suspend fun getData(): Response<List<CryptoModel>>

    fun getData() : Observable<List<CryptoModel>>

}