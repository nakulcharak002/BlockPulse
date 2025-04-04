package com.example.blockpulse.api

import com.example.blockpulse.fragment.models.MarketModel
import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {
    @GET("data-api/v3/cryptocurrency/listing?start=1&limit=500")
     suspend  fun getMarketData(): Response<MarketModel>
}