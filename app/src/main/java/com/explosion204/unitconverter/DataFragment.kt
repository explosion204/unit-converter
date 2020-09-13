package com.explosion204.unitconverter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import org.json.JSONObject


class DataFragment : Fragment(), AdapterView.OnItemSelectedListener {
    private lateinit var initialVal: TextView
    private lateinit var convertedVal: TextView
    private lateinit var initialUnit: Spinner
    private lateinit var convertedUnit: Spinner
    private var isFloat = false
    private var converter: Converter? = null
    private lateinit var clipboard: ClipboardManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var v = inflater.inflate(R.layout.data_fragment, container, false)

        clipboard = context!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        initialVal = v.findViewById(R.id.initialVal)
        convertedVal = v.findViewById(R.id.convertedVal)
        initialUnit = v.findViewById(R.id.initialUnit)
        convertedUnit = v.findViewById(R.id.convertedUnit)

        if (BuildConfig.FLAVOR.equals("premium")) {
            var reverseButtonId = resources.getIdentifier("reverseButton", "id", "com.explosion204.unitconverter")
            v.findViewById<ImageButton>(reverseButtonId).setOnClickListener {
                var tmp = initialUnit.selectedItemPosition
                initialUnit.setSelection(convertedUnit.selectedItemPosition)
                convertedUnit.setSelection(tmp)

                initialVal.text = convertedVal.text
                editConvertedValue()
            }
        }

        initialVal.setOnClickListener(::onTextViewClick)
        convertedVal.setOnClickListener(::onTextViewClick)
        initialUnit.onItemSelectedListener = this
        convertedUnit.onItemSelectedListener = this

        return v
    }


    fun onTextViewClick(view: View) {
        var clip = ClipData.newPlainText("initial or converted value", (view as TextView).text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        editConvertedValue();
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    fun editInitialValue(num: Int) {
        var newValue: String = initialVal.text.toString()
        when (num) {
            -3 -> {
                newValue = ""
                isFloat = false
            }
            -2 -> {
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
        editConvertedValue()
    }

    private fun editConvertedValue() {
        var initialValue = initialVal.text.toString()
        if (initialValue.isNotEmpty()) {
            if (initialValue[initialValue.length - 1] == '.') {
                initialValue += "0"
            }

            convertedVal.text = converter?.convert(initialUnit.selectedItem.toString(),
                convertedUnit.selectedItem.toString(), initialValue.toDouble())
                .toString()
        }
        else {
            convertedVal.text = ""
        }
    }

    fun modifyConverter(category: JSONObject) {
        converter = Converter(category)
        var adapter = ArrayAdapter<String>(activity!!.applicationContext, R.layout.spinner_unit_item,
            category.keys().iterator().asSequence().toList())

        initialUnit.adapter = adapter
        convertedUnit.adapter = adapter
    }
}