package com.explosion204.unitconverter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.json.JSONObject
import com.explosion204.unitconverter.KeyboardFragment.OnNumButtonClickListener

class DataViewModel : ViewModel(), OnNumButtonClickListener {
    private lateinit var converter: Converter
    private lateinit var categories: JSONObject
    var currentCategory: MutableLiveData<JSONObject> = MutableLiveData(JSONObject())

    var initialVal: MutableLiveData<String> = MutableLiveData("")
    var convertedVal: MutableLiveData<String> = MutableLiveData("")
    var initialUnit: MutableLiveData<String> = MutableLiveData("")
    var convertedUnit: MutableLiveData<String> = MutableLiveData("")

    private var isFloat = false;
    var initRequired = true

    fun setCategories(categories: JSONObject) {
        this.categories = categories
        initRequired = false
        currentCategory.value = categories[categories.keys().next()] as JSONObject
    }

    fun setCurrentCategory(categoryName: String) {
        currentCategory.value = categories[categoryName] as JSONObject
        converter = Converter(currentCategory.value!!)
    }

    override fun onNumButtonClick(num: Int) {
        var newValue: String = initialVal.value!!
        when (num) {
            OnNumButtonClickListener.CLEAR_SIGNAL -> {
                newValue = ""
                isFloat = false
            }
            OnNumButtonClickListener.DOT_SIGNAL -> {
                // prevent from using more than one dot (including empty textview)
                if (!isFloat && newValue.isNotEmpty()) {
                    newValue += "."
                    isFloat = true
                }
                else if (!isFloat && newValue.isEmpty()) {
                    newValue += "0."
                    isFloat = true
                }
            }
            OnNumButtonClickListener.DELETE_SIGNAL -> {
                // truncate sequence by 1 character from end (and control dot presence)
                if (newValue.isNotEmpty()) {
                    if (newValue[newValue.length - 1] == '.')
                        isFloat = false

                    newValue = newValue.substring(0, newValue.length - 1)
                }
                else
                    newValue = ""
            }
            else -> {
                if (!(newValue.length == 1 && newValue[0] == '0')) {
                    newValue += num.toString()
                }
            }
        }

        initialVal.value = newValue
        convertInitialValue()
    }

    fun convertInitialValue() {
        var valueToConvert = initialVal.value!!
        if (valueToConvert.isNotEmpty()) {
            if (valueToConvert[valueToConvert.length - 1] == '.') {
                valueToConvert += "0"
            }

            convertedVal.value = converter.convert(initialUnit.value!!,
                convertedUnit.value!!,
                valueToConvert.toDouble()).toString()
        }
        else {
            convertedVal.value = ""
        }
    }
}