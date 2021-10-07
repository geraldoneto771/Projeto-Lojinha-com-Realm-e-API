package com.example.lojinhasea

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class App: Application() {
    //iniciando as configurações do realm no app
    override fun onCreate() {
        super.onCreate()
        //iniciando o realm
        Realm.init(this)

        //configurando o realm
        val conf = RealmConfiguration.Builder()
            .name("app.realm")
            .schemaVersion(0L)
            .allowQueriesOnUiThread(true)
            .allowWritesOnUiThread(true)
            .build()

        //setando as configurações do realm com as informações da conf
        Realm.setDefaultConfiguration(conf)
    }
}