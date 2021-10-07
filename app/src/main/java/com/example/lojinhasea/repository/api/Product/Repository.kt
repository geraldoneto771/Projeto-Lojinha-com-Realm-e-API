package com.example.lojinhasea.repository.api.Product

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.lojinhasea.models.ProdutoModel
import com.example.lojinhasea.repository.api.ProductService
import com.google.gson.GsonBuilder
import io.realm.Realm
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class Repository() {
    val baseUrl = "http://172.18.190.4:8000/"

    // previne um erro, mas eu não faço ideia do que seja
    var gson = GsonBuilder()
        .setLenient()
        .create()

    // baseUrl é a URL Pra conectar a API
    // addConvertFactory converte objeto em json
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    // implementa a interface
    private val webService: ProductService = retrofit.create(
        ProductService::class.java
    )


    fun create(product: ProdutoModel): Boolean{
        val json = product.toJson()
        Log.e("PRODUCT_JSON: ",product.toJson().toString())

        // faz a requisição para a API, passando o objeto de Produto
        val response = webService.create(
            body = json
        ).execute() // o execute executa na thread atual

        // Se a resposta for na faixa dos 200
        if (response.isSuccessful){
            try {
                Log.e("Pro-Create-successful: ", "Code "+response.code())
                return true
            } catch (e: Exception){
                Log.e("Pro-Create-error: ",e.toString())
                return false
            }
        } else { // caso não tenha sido possível conectar a API

            val text = response.errorBody()?.string()
            return false
        }
    }

    fun get (productCheck: MutableLiveData<Boolean>){

        // o enqueue executa em uma nova thread
        webService.get()
            .enqueue(object : Callback<ArrayList<ProdutoModel>> {
                override fun onResponse( // se houver resposta da API, mesmo que não seja status 200
                    call: Call<ArrayList<ProdutoModel>>,
                    response: Response<ArrayList<ProdutoModel>>
                ) {
                    if (response.isSuccessful){ // se o status foi 200
                        val realm = Realm.getDefaultInstance() // instancia o realm
                        realm.executeTransaction{
                            realm.insertOrUpdate(response.body()) // insere os projetos da API no realm

                            Log.e("Get-successful: ", response.body()?.size.toString())
                        }
                    }

                    productCheck.value = false
                }

                // caso não tenha sido possivel conectar a API
                override fun onFailure(call: Call<ArrayList<ProdutoModel>>, t: Throwable) {
                    productCheck.value = false
                    Log.e("Get()-error: ",t.toString())
                }
            })
}

}