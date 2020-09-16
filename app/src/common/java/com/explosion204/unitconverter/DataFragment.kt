package com.explosion204.unitconverter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders


class DataFragment : Fragment(), AdapterView.OnItemSelectedListener {
    private lateinit var initialVal: TextView
    private lateinit var convertedVal: TextView
    private lateinit var initialUnit: Spinner
    private lateinit var convertedUnit: Spinner
    private lateinit var dataViewModel: DataViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var v = inflater.inflate(R.layout.data_fragment, container, false)

        dataViewModel = ViewModelProviders.of(activity!!).get(DataViewModel::class.java)

        // observe category changes
        dataViewModel.currentCategory.observe(viewLifecycleOwner, Observer { newCategory ->
            if (activity != null) {
                var adapter = ArrayAdapter<String>(activity!!.applicationContext, R.layout.spinner_unit_item,
                    newCategory.keys().iterator().asSequence().toList())

                initialUnit.adapter = adapter
                convertedUnit.adapter = adapter
                // set appropriate selected item according to view model
                initialUnit.setSelection(adapter.getPosition(dataViewModel.initialUnit.value))
                convertedUnit.setSelection(adapter.getPosition(dataViewModel.convertedUnit.value))
            }
        })

        // observe value changes
        dataViewModel.initialVal.observe(viewLifecycleOwner, Observer { newVal ->
            initialVal.text = newVal
        })
        dataViewModel.convertedVal.observe(viewLifecycleOwner, Observer { newVal ->
            convertedVal.text = newVal
        })

        // get controls
        initialVal = v.findViewById(R.id.initialVal)
        convertedVal = v.findViewById(R.id.convertedVal)
        initialUnit = v.findViewById(R.id.initialUnit)
        convertedUnit = v.findViewById(R.id.convertedUnit)

        initialUnit.onItemSelectedListener = this
        convertedUnit.onItemSelectedListener = this

        return v
    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        dataViewModel.initialUnit.value = initialUnit.selectedItem.toString()
        dataViewModel.convertedUnit.value = convertedUnit.selectedItem.toString()
        dataViewModel.convertInitialValue()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}
}