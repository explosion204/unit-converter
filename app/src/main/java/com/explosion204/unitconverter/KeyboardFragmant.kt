package com.explosion204.unitconverter

import android.os.Bundle
import android.os.ProxyFileDescriptorCallback
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView

class KeyboardFragmant : Fragment(), View.OnClickListener, View.OnLongClickListener {
    lateinit var callback: onNumButtonClickListener

    fun setNumpadClickListener(numpadCallback: onNumButtonClickListener) {
        this.callback = numpadCallback
    }

    interface onNumButtonClickListener {
        fun onNumButtonClick(num: Int)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var v = inflater.inflate(R.layout.keyboard_fragmanet, container, false)

        v.findViewById<Button>(R.id.numButton1).setOnClickListener(this)
        v.findViewById<Button>(R.id.numButton2).setOnClickListener(this)
        v.findViewById<Button>(R.id.numButton3).setOnClickListener(this)
        v.findViewById<Button>(R.id.numButton4).setOnClickListener(this)
        v.findViewById<Button>(R.id.numButton5).setOnClickListener(this)
        v.findViewById<Button>(R.id.numButton6).setOnClickListener(this)
        v.findViewById<Button>(R.id.numButton7).setOnClickListener(this)
        v.findViewById<Button>(R.id.numButton8).setOnClickListener(this)
        v.findViewById<Button>(R.id.numButton9).setOnClickListener(this)
        v.findViewById<Button>(R.id.numButton0).setOnClickListener(this)
        v.findViewById<Button>(R.id.dotButton).setOnClickListener(this)
        v.findViewById<Button>(R.id.deleteButton).setOnClickListener(this)
        v.findViewById<Button>(R.id.deleteButton).setOnLongClickListener(this)

        return v
    }

    override fun onClick(view: View) {
        var btn = view as Button
        when (btn.id) {
            R.id.dotButton -> {
                callback.onNumButtonClick(-2)
            }
            R.id.deleteButton -> {
                callback.onNumButtonClick(-1)
            }
            else -> {
                callback.onNumButtonClick(btn.text.toString().toInt())
            }
        }
    }

    override fun onLongClick(view: View): Boolean {
        if ((view as Button).id == R.id.deleteButton) {
            callback.onNumButtonClick(-3)
        }

        return true;
    }
}