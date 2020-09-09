package com.explosion204.unitconverter

import android.util.Log

class Converter(_types: List<String>, _rules: Map<Pair<String, String>, Int>) {
    private var types = _types
    private var rules = _rules

    fun convert(type1: String, type2: String, inital_value: Int): Int {
        var result: Int
        try {
            var coeff = rules[Pair<String, String>(type1, type2)]
            result = inital_value * coeff!!
        }
        catch (e: NoSuchElementException) {
            Log.e("ERROR","No such rule!")
            result = 0
        }

        return result
    }
}