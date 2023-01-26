package com.example.newsapp.data.network.model

import com.google.gson.annotations.SerializedName

data class Source (

  @SerializedName("id"   ) var id   : String? = null,
  @SerializedName("name" ) var name : String? = null

)