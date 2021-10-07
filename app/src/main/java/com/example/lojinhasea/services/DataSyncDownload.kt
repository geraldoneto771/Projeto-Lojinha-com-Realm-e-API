package com.example.lojinhasea.services

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.lojinhasea.repository.api.Product.Repository as ProductRepository
import com.example.lojinhasea.repository.api.Category.Repository as CategoryRepository


class DataSyncDownload(context: Context, workerParameters: WorkerParameters): Worker(context, workerParameters) {
    override fun doWork(): Result {
        CategoryRepository().get(MutableLiveData()) // recebe as categorias da API
       //ProductRepository().get(MutableLiveData()) // recebe os produtos da API


        return Result.success()
    }
}