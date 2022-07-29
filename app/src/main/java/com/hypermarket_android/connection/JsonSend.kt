package com.hypermarket_android.connection

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser

class JsonSend {

    companion object {

        fun getParam(postParam: HashMap<String, String>): JsonObject? {
            val gsonObj = Gson()
            val ja = gsonObj.toJson(postParam)
            val jsonParser = JsonParser()
            return jsonParser.parse(ja).asJsonObject
        }

    }
}