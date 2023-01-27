package com.example.newsapp.data.network.model

import com.google.gson.annotations.SerializedName

data class NewsAPIResponse (

  @SerializedName("status"       ) var status       : String?             = null,
  @SerializedName("totalResults" ) var totalResults : Int                 = 0,
  @SerializedName("articles"     ) var articles     : ArrayList<Articles> = arrayListOf()

)