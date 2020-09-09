package com.explosion204.unitconverter

import android.util.Log
import org.json.JSONObject

class Converter() {
    lateinit var rules: JSONObject

    fun convert(type1: String, type2: String, inital_value: Int): Int {
        var result: Int
        try {
            var coeff = (rules[type1] as JSONObject)[type2] as Int
            result = inital_value * coeff
        }
        catch (e: NoSuchElementException) {
            Log.e("ERROR","No such rule!")
            result = 0
        }

        return result
    }
}