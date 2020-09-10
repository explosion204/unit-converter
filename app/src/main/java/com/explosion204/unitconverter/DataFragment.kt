package com.explosion204.unitconverter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import android.widget.TextView

class DataFragment : Fragment() {
    lateinit var initialVal: TextView
    lateinit var convertedVal: TextView
    lateinit var initialUnit: Spinner
    lateinit var convertedUnit: Spinner
    private var isFloat = false
    var converter: Converter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var v = inflater.inflate(R.layout.data_fragment, container, false)

        initialVal = v!!.findViewById(R.id.initialVal)
        convertedVal = v!!.findViewById(R.id.convertedVal)
        initialUnit = v!!.findViewById(R.id.initialUnit)
        convertedUnit = v!!.findViewById(R.id.convertedUnit)

        return v
    }

    fun editInitialValue(num: Int) {
        var newValue: String = initialVal.text.toString()
        when (num) {
            -3 -> {
                newValue = ""
                isFloat = false
            }
            -2 -> {
                // prevent from using more than one dot (including empty textview)
                if (!isFloat && !newValue.isEmpty()) {
                    newValue += "."
                    isFloat = true
                }
                else if (!isFloat && newValue.isEmpty()) {
                    newValue += "0."
                    isFloat = true
                }
            }
            -1 -> {
                // truncate sequence by 1 character from end (and control dot presence)
                if (initialVal.text.isNotEmpty()) {
                    if (initialVal.text[initialVal.text.length - 1] == '.')
                        isFloat = false

                    newValue = initialVal.text.substring(0, initialVal.text.length - 1)
                }
                else
                    newValue = ""
            }
            0 -> {
                // prevent next input situation: 000000.1234
                if (!(newValue.length == 1 && newValue[0] == '0')) {
                    newValue += "0"
                }
            }
            else -> {
                newValue += num.toString();
            }
        }

        initialVal.text = newValue

        // converting
        if (newValue.isNotEmpty()) {
            if (newValue[newValue.length - 1] == '.') {
                newValue += "0"
            }

            convertedVal.text = converter?.convert(initialUnit.selectedItem.toString(),
                                convertedUnit.selectedItem.toString(), newValue.toDouble())
                                .toString()
        }
        else {
             convertedVal.text = ""
        }
    }
}