package com.example.lojinhasea.models

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.bson.types.ObjectId
import java.util.*

open class CategoryModel:  RealmObject() {
    @PrimaryKey
    @SerializedName("pk")
    var id: Int = (0..10000000).random()
    @SerializedName("name")
    var name: String = ""
    @SerializedName("quantity_stock")
    var qttStock: Int = 0
    @SerializedName("quantity_sold")
    var qttSale: Int = 0
    var created: String = ""

    fun toJson(): JsonObject {
        val json = JsonObject()
     // json.addProperty("pk", id)
        json.addProperty("name", name)
        json.addProperty("quantity_sold", qttSale)
        json.addProperty("quantity_stock", qttStock)
      //  json.addProperty("crated", created)
        return json

    }

    override fun toString(): String {
        return "${name}"
    }
}