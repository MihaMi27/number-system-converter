package com.vaje.pretvorniknsp.converter

import android.content.res.Configuration
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.*
import com.vaje.pretvorniknsp.R

class Converter : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pretvornik)

        val original: EditText = findViewById(R.id.original)
        val converted: TextView = findViewById(R.id.pretvorba)
        var temp = ""
        var errorOccurred: Boolean

        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> {
                original.setTextColor(Color.WHITE)
                converted.setTextColor(Color.WHITE)
                original.setBackgroundColor(Color.argb(80, 232, 232, 232))
                converted.setBackgroundColor(Color.argb(80, 232, 232, 232))
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                original.setTextColor(Color.BLACK)
                converted.setTextColor(Color.BLACK)
                original.setBackgroundColor(Color.argb(80, 37, 37, 37))
                converted.setBackgroundColor(Color.argb(80, 37, 37, 37))
            }
        }

        fun error(s: String) {
            when (s) {
                "DEC:SYN", "BIN:SYN","HEX:SYN","BCD:SYN" -> {
                    Toast.makeText(applicationContext, "Please enter a valid number", Toast.LENGTH_LONG).show()
                    errorOccurred = true
                }
                "LONG:OVERFLOW" -> {
                    Toast.makeText(applicationContext, "You wrote a number that is too large", Toast.LENGTH_SHORT).show()
                    errorOccurred = true
                }

            }

        }


        var originalText = ""
        original.setOnFocusChangeListener { _, b ->
            if (!b) {
                original.onEditorAction(EditorInfo.IME_ACTION_DONE)
            }

        }


        original.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                originalText = original.text.toString()
                false
            } else {
                false
            }
        }

        val convert: Button = findViewById(R.id.pretvori)
        var convertedText = ""
        val from: RadioGroup = findViewById(R.id.radioGroup2)
        val to: RadioGroup = findViewById(R.id.radioGroup)
        var fromId = -1
        var toId = -1
        from.setOnCheckedChangeListener { group, _ ->
            fromId = group.checkedRadioButtonId
        }
        to.setOnCheckedChangeListener { group, _ ->
            toId = group.checkedRadioButtonId
        }

        convert.setOnFocusChangeListener { _, b ->
            if (b) {
                val conversions = Conversions()
                errorOccurred = false
                if ((fromId + 1) == toId) {
                    Toast.makeText(applicationContext, "You have selected the same number system", Toast.LENGTH_LONG).show()
                } else {
                    when (fromId) {
                        R.id.dec1 -> {
                            for (char in originalText) {
                                if (!('0'..'9').contains(char)) {
                                    error("DEC:SYN")
                                } else {
                                    temp = originalText
                                }
                            }
                        }
                        R.id.bin1 -> {
                            temp = conversions.binToDec(originalText)
                            error(temp)

                        }
                        R.id.hex1 -> {
                            temp = conversions.hexToDec(originalText)
                            error(temp)
                        }
                        R.id.bcd1 -> {
                            temp = conversions.bcdToDec(originalText)
                            error(temp)
                        }
                        -1 -> {
                            Toast.makeText(applicationContext, "Please select the number system", Toast.LENGTH_LONG).show()
                        }
                    }
                    if (!errorOccurred) {
                        when (toId) {
                            R.id.dec2 -> {
                                convertedText = temp
                            }
                            R.id.bin2 -> {
                                try {
                                    convertedText = conversions.decToBin(temp.toLong())
                                } catch (ex: NumberFormatException) {
                                    error("LONG:OVERFLOW")
                                }

                            }
                            R.id.hex2 -> {
                                try {
                                    convertedText = conversions.decToHex(temp.toLong())
                                } catch (ex: NumberFormatException) {
                                    error("LONG:OVERFLOW")
                                }
                            }
                            R.id.bcd2 -> {
                                try {
                                    convertedText = conversions.decToBcd(temp)
                                } catch (ex: NumberFormatException) {
                                    error("LONG:OVERFLOW")
                                }
                            }
                            -1 -> {
                                Toast.makeText(applicationContext, "Please select the number system", Toast.LENGTH_LONG).show()
                            }
                        }
                        converted.text = convertedText
                    }
                }

            }
            converted.requestFocus()
        }
    }
}