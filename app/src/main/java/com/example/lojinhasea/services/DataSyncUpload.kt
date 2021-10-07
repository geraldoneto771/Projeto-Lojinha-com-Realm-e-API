package com.example.lojinhasea.services

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.lojinhasea.models.CategoryModel
import com.example.lojinhasea.models.ProdutoModel
import io.realm.Realm
import com.example.lojinhasea.repository.api.Product.Repository as ProductRepository
import com.example.lojinhasea.repository.api.Category.Repository as CategoryRepository

class DataSyncUpload(context: Context, workerParameters: WorkerParameters): Worker(context, workerParameters) {
    override fun doWork(): Result {

        createCategory()
        //createProduct()

        return Result.success()
    }

    // tenta criar o produto até 3 vezes
    private fun createProduct() {
        val list = Realm.getDefaultInstance().where(ProdutoModel::class.java)
            .findAll() // recupera todos os objetos de produto do realm

        for (product in list) {
            var count = 0
            do {
                count++
                if (count == 3) {
                    break
                }
            } while (!ProductRepository().create(product)) // enquanto o retorno for falso (der erro), ele executa
        }
    }

    private fun createCategory(){
        val list = Realm.getDefaultInstance().where(CategoryModel::class.java)
            .findAll()

        for (category in list) {
            var count = 0
            do {
                count++
                if (count == 3) {
                    break
                }
            } while (!CategoryRepository().create(category))
        }

    }
}