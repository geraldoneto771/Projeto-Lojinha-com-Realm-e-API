package com.example.lojinhasea.repository.bd.categoria

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.lojinhasea.models.CategoryModel
import com.example.lojinhasea.models.ProdutoModel
import com.example.lojinhasea.repository.api.ProductService
import com.example.lojinhasea.repository.api.RetrofitClient
import io.realm.Realm
import io.realm.kotlin.where
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Repository {

    //criando e inserindo o produto no banco
    fun create(category: CategoryModel){
        val realm = Realm.getDefaultInstance()

        realm.executeTransaction {
            it.insert(category)
        }
    }

    fun getById(idCategory: Int): CategoryModel{
        val realm = Realm.getDefaultInstance()
        val project = realm.where<CategoryModel>()
            .equalTo("id", idCategory).findFirst()
        project?.let { aux ->
            return aux
        }?: run{
            return CategoryModel()
        }
    }

    fun getByName(nameCategory: String): CategoryModel{
        val realm = Realm.getDefaultInstance()
        val project = realm.where<CategoryModel>()
            .equalTo("name", nameCategory).findFirst()
        project?.let { aux ->
            return aux
        }?: run{
            return CategoryModel()
        }
    }

    //pegando os dados do ProdutoModel e guardando no repositorio
    fun getAll(_category: MutableLiveData<ArrayList<CategoryModel>>){
        val realm = Realm.getDefaultInstance()

        realm.addChangeListener {

            val categorias = realm.where<CategoryModel>().findAll()
            var list = ArrayList<CategoryModel>()

            list.addAll(categorias)
            _category.postValue(list)

        }
    }

    // função com a lógica de venda
    fun saleCategory(
        qttStock: Int,
        qttSale: Int,
        _category: MutableLiveData<ArrayList<CategoryModel>>,
        obj: ProdutoModel
    ) {
        val realm = Realm.getDefaultInstance()

        realm.executeTransaction {
            obj.qttSale += qttSale
            obj.qttStock = (obj.qttStock - qttSale)
        }

        getAll(_category)
    }

    fun alterCategory(category: MutableLiveData<ArrayList<CategoryModel>>, obj: CategoryModel){
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction{
            // continuar daqui
        }
    }

}

