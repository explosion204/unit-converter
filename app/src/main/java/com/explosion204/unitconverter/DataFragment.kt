package com.explosion204.unitconverter

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView

class DataFragment : Fragment() {
    lateinit var initialVal: TextView
    lateinit var convertedVal: TextView
    lateinit var initialUnit: Spinner
    lateinit var convertedUnit: Spinner
    private var isFloat = false
    //lateinit var converter: Converter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var v = inflater.inflate(R.layout.data_fragment, container, false)

        initialVal = v!!.findViewById(R.id.initialVal)
        convertedVal = v!!.findViewById(R.id.convertedVal)
        initialUnit = v!!.findViewById(R.id.initialUnit)
        convertedUnit = v!!.findViewById(R.id.convertedUnit)
        //converter = arguments.get("converter") as Converter

        return v
    }

    fun editInitialValue(num: Int) {
        var newValue: String = initialVal.text.toString()
        when (num) {
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
                if (!initialVal.text.isEmpty()) {
                    if (initialVal.text[initialVal.text.length - 1] == '.')
                        isFloat = false

                    newValue = initialVal.text.substring(0, initialVal.text.length - 1)
                }
                else
                    newValue = ""
            }
            0 -> {
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
//        convertedVal.text = converter.convert(initialUnit.selectedItem as String,
//                                              convertedUnit.selectedItem as String,
//                                              newValue.toInt()).toString()
        convertedVal.text = newValue
    }
}