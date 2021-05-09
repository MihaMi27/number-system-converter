package com.vaje.pretvorniknsp.converter

import java.lang.Long.parseLong
import kotlin.math.pow

class Conversions {

    fun binToDec(original: String): String {
        var result: Long = 0
        var possible = true
        for (char in original) {
            if (char != '1' && char != '0') {
                possible = false
            }
        }
        if (possible) {
            for (i in original.indices) {
                if (result < Long.MAX_VALUE) {
                    if (original[i] == '1') {
                        result += 2.0.pow((original.length - 1 - i)).toLong()
                    }
                } else {
                    return "LONG:OVERFLOW"
                }

            }
            return result.toString()
        } else {
            return "BIN:SYN"
        }

    }

    fun hexToDec(original: String): String {
        var result: Long = 0
        var value: Long = 0
        var overflow = false
        var possible = true
        for (i in original.indices) {
            if (original[i].isLetterOrDigit()) {
                when {
                    ('A'..'F').contains(original[i].toUpperCase()) -> {
                        value = original[i].toUpperCase().toLong() - 55
                    }
                    original[i].isDigit() -> {
                        value = parseLong(original[i].toString())
                    }
                    else -> {
                        possible = false
                    }
                }
            }

            if (original[i] != '0') {
                result += (16.0.pow(original.length - 1 - i) * value).toLong()
                if (result >= Long.MAX_VALUE) {
                    overflow = true
                }

            }
        }
        return if (possible) {
            if (overflow) {
                "LONG:OVERFLOW"
            } else {
                result.toString()
            }
        } else {
            "HEX:SYN"
        }
    }


        fun bcdToDec(original: String): String {
            var result = ""
            var possible = true
            val digits = original.split(" ")
            for (digit in digits) {
                for (char in digit) {
                    if (char != '0' && char != '1') {
                        possible = false
                        break
                    }
                }
                if (possible) {
                    result += binToDec(digit)
                } else {
                    result = "BCD:SYN"
                    break
                }
            }
            return result
        }

        fun decToBin(original: Long): String {
            var result = ""
            var number = original
            var remainder: Long
            while (number != 0.toLong()) {
                remainder = number % 2
                number = (number / 2)
                result += remainder.toString()
            }
            return result.reversed()
        }

        fun decToHex(original: Long): String {
            var result = ""
            var number = original
            var remainder: Long
            while (number != 0.toLong()) {
                remainder = number % 16
                number = (number / 16)
                if (remainder > 9) {
                    remainder += 55
                    result += remainder.toChar()
                } else {
                    result += remainder.toString()
                }
            }
            return result.reversed()
        }

        fun decToBcd(original : String): String {
            var result = ""
            var value: Long
            for (char in original) {
                value = (char - 48).toLong()
                result += (decToBin(value).padStart(4, '0')) + " "
            }
            return result
        }
    }

