package com.explosion204.unitconverter

import android.util.Log
import org.json.JSONObject

class Converter(_rules: JSONObject) {
    private var rules: JSONObject = _rules

    fun convert(type1: String, type2: String, initialValue: Double): Double {
        var result: Double

        result = try {
            var coeff = (rules[type1] as JSONObject)[type2] as Double
            initialValue * coeff
        } catch (e: NoSuchElementException) {
            Log.e("ERROR","No such rule!")
            0.0
        }

        return result
    }
}