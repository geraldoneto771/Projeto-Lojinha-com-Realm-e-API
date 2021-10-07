package com.example.lojinhasea.repository.api

import com.example.lojinhasea.models.CategoryModel
import com.example.lojinhasea.models.ProdutoModel
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

// Camada de serviço para produto
interface ProductService{

    // recebe os produtos da API
    @GET("products")
    fun get(): Call<ArrayList<ProdutoModel>>

    // cria um novo produto (OBS: não existe produto sem categoria)
    @POST("products")
    fun create(
        @Body body: JsonObject, // body do tipo ProdutoModel
    ):  Call<ProdutoModel>


}

// chamada de serviço para categoria
interface CategoryService{
    // cria uma nova categoria
    @POST("category")
    fun create(
        @Body body: JsonObject,
    ): Call<CategoryModel>

    // recebe as categorias da API
    @GET("category")
    fun get(): Call<ArrayList<CategoryModel>>
}