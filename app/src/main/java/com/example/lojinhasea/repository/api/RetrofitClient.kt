package com.example.lojinhasea.repository.api

import com.example.lojinhasea.models.ProdutoModel
import io.realm.Realm
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import java.lang.Exception

class RetrofitClient private constructor() {
    val baseUrl = "http://172.18.190.4:8000/"
    val httpClient = OkHttpClient.Builder()
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(httpClient.build())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val webService: ProductService = retrofit.create(
        ProductService::class.java
    )

    fun create(product: ProdutoModel): Boolean{
        //val token = Token.Repository().getToken()!!
        val json = product.toJson()
        val response = webService.create(body = json).execute()

        if(response.isSuccessful){
            try{
                val realm = Realm.getDefaultInstance()
                realm.executeTransaction {
                    val productQuery = realm.where(ProdutoModel::class.java)
                        .equalTo("id", product.id).findFirst()
                }
                realm.close()
                return true
            }catch(e: Exception){
                return false
            }
        }else{
            val text = response.errorBody()?.string()
            return false
        }
    }
}