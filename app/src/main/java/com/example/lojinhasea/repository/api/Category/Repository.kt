package com.example.lojinhasea.repository.api.Category

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.lojinhasea.models.CategoryModel
import com.example.lojinhasea.models.ProdutoModel
import com.example.lojinhasea.repository.api.CategoryService
import com.example.lojinhasea.repository.api.ProductService
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import io.realm.Realm
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.IllegalStateException

class Repository {
    val baseUrl = "http://172.18.190.4:8000/"

    var gson = GsonBuilder()
        .setLenient()
        .create()

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
    private val webService: CategoryService = retrofit.create(
        CategoryService::class.java
    )

    fun create(category: CategoryModel): Boolean{
        var cat = category.toJson()

        val response = webService.create(
            body = cat
        ).execute()

        if (response.isSuccessful){
            try {
                val realm = Realm.getDefaultInstance()
                realm.executeTransaction {
                    val categoryQuery = realm.where(CategoryModel::class.java)
                        .equalTo("id", category.id).findFirst()

                }
                //realm.close()
                Log.e("Cat-Create-successful: ", "Code "+response.code())
                return true
            } catch (e: IllegalStateException){
                Log.e("Cat-Create-error: ",e.toString())
                return false
            }
        } else {

            val text = response.errorBody()?.string()
            return false
        }
    }

    fun get (categoryCheck: MutableLiveData<Boolean>){
        webService.get().enqueue(object : Callback<ArrayList<CategoryModel>> {
            override fun onResponse(
                call: Call<ArrayList<CategoryModel>>,
                response: Response<ArrayList<CategoryModel>>
            ) {


                    if (response.isSuccessful){
                        val realm = Realm.getDefaultInstance()
                        realm.executeTransaction{
                            realm.insertOrUpdate(response.body())
                        }
                        Log.e("Get-successful: ", response.body()?.size.toString())
                        categoryCheck.value = false
                    }


            }


            override fun onFailure(call: Call<ArrayList<CategoryModel>>, t: Throwable) {
                Log.e("GetCat-error: ", "get não funcionando "+t.toString())
                categoryCheck.value = false

            }

        })
    }

}