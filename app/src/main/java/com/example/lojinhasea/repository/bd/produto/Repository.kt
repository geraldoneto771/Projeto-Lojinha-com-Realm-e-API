package com.example.lojinhasea.repository.bd.produto

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.lojinhasea.models.ProdutoModel
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where

class Repository {

    //criando e inserindo o produto no banco
    fun create(produto: ProdutoModel) {
        val realm = Realm.getDefaultInstance() // instancia o realm

        realm.executeTransaction {
            it.insert(produto) // insere o produto no banco local
        }
    }

    fun get(idProdutoModel: String): ProdutoModel { // recupera um projeto especifico pelo id
        val realm = Realm.getDefaultInstance() // instancia o realm
        val project = realm.where<ProdutoModel>()
            .equalTo("pk", idProdutoModel).findFirst() // compara o ID recebido e retorna o primeiro
        project?.let { aux ->                                   // valor encontrado
            return aux
        } ?: run {
            return ProdutoModel()
        }

    }

    //função deletar produto
    fun delete(
        idProdutoModel: String,
        _produtos: MutableLiveData<ArrayList<ProdutoModel>>
    ): Boolean {

        try {
            val realm = Realm.getDefaultInstance()

            realm.executeTransaction { realm ->
                val result: RealmResults<ProdutoModel> =
                    realm.where(ProdutoModel::class.java)
                        .equalTo("id", idProdutoModel).findAll()
                result.deleteAllFromRealm()
            }
            getAll(_produtos)
            Log.e("Teste DELETE", "sucesso")

            return true
        } catch (e: Exception) {
            Log.e("Teste DELETE", "deu ruim " + e.toString())
            return false
        }


    }



    //pegando os dados do ProdutoModel e guardando no repositorio
    fun getAll(_produtos: MutableLiveData<ArrayList<ProdutoModel>>) {
        val realm = Realm.getDefaultInstance()

        realm.addChangeListener {

            val produtos = realm.where<ProdutoModel>().findAll()
            var list = ArrayList<ProdutoModel>()

            list.addAll(produtos)
            list = ArrayList<ProdutoModel>()
            list.addAll(produtos)
            _produtos.postValue(list)

            _produtos.value = list
        }
    }

    // função com a lógica de venda
    fun saleProduct(
        qttStock: Int,
        qttSale: Int,
        _produtos: MutableLiveData<ArrayList<ProdutoModel>>,
        obj: ProdutoModel
    ) {
        val realm = Realm.getDefaultInstance()

        realm.executeTransaction {
            obj.qttSale += qttSale
            obj.qttStock = (obj.qttStock - qttSale)
        }

        getAll(_produtos)
    }

    //função deletar produto
    fun update(prod: ProdutoModel, _produtos: MutableLiveData<ArrayList<ProdutoModel>>,
               obj: ProdutoModel) {

        val realm = Realm.getDefaultInstance()

        realm.executeTransaction{
            obj.name = prod.name
            obj.description = prod.description
            obj.price = prod.price
            obj.category = prod.category
            obj.qttStock = prod.qttStock

        }
        getAll(_produtos)
    }
}

