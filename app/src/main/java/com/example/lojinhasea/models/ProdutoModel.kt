package com.example.lojinhasea.models

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.bson.types.ObjectId
import java.sql.Time
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month
import java.util.*
import java.util.List.of

//modelo do objeto Produto com seus atributos
open class ProdutoModel(): RealmObject() {

    @PrimaryKey @SerializedName("pk")
    var id: String = ObjectId().toString()
    @SerializedName("name")
    var name: String = ""
    @SerializedName("description")
    var description: String = ""
    @SerializedName("category")
    var category: Int = 0
    @SerializedName("price")
    var price: Float = 0.0f
    @SerializedName("quantity_stock")
    var qttStock: Int = 0
    @SerializedName("quantity_sold")
    var qttSale: Int = 0
    @SerializedName("created")
    var created: String = ""

    // Converte as variáveis da classe para JSON
    fun toJson(): JsonObject{
        val json = JsonObject()
        json.addProperty("pk", id)
        json.addProperty("name", name)
        json.addProperty("description", description)
        json.addProperty("category", category)
        json.addProperty("quantity_sold", qttSale)
        json.addProperty("quantity_stock", qttStock)
        json.addProperty("crated", created)
        return json
    }

}

