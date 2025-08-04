package com.example.fanremote

import android.content.Context
import android.hardware.ConsumerIrManager
import android.util.Log

class IRManager(private val context: Context) {
    private val irManager: ConsumerIrManager? =
        context.getSystemService(Context.CONSUMER_IR_SERVICE) as? ConsumerIrManager

    fun hasIrEmitter(): Boolean {
        return irManager?.hasIrEmitter() == true
    }

    fun getCarrierFrequencies(): Array<ConsumerIrManager.CarrierFrequencyRange>? {
        return irManager?.carrierFrequencies
    }

    // Convert Pronto hex format to microsecond timing array
    private fun convertProntoToMicroseconds(prontoHex: String): IntArray {
        val hexValues = prontoHex.replace(" ", "").chunked(4)

        // Skip the first 4 values (Pronto header format)
        // Index 0: Format (0000 = raw format)
        // Index 1: Frequency (006F = ~38kHz)
        // Index 2: First burst pair count
        // Index 3: Second burst pair count

        val timingValues = hexValues.drop(4).map { hex ->
            // Convert hex to decimal and multiply by time unit
            // For 38kHz carrier, each unit ≈ 26.316 microseconds
            val decimal = hex.toInt(16)
            (decimal * 26.316).toInt()
        }

        return timingValues.toIntArray()
    }

    // Your actual fan IR codes in Pronto hex format
    private val fanIRCodes = mapOf(
        "on" to "0000 006F 0000 0024 002F 0010 002F 0010 0010 0031 002F 0011 002F 0010 0010 0031 000F 0031 0010 0030 0010 0031 000F 0031 002F 0010 0010 012E 0030 0010 0030 0010 000F 0031 002F 0010 0030 0011 000F 0030 0010 002F 0010 0031 000F 0031 0010 0030 0030 0010 000F 012E 0030 0010 002F 0010 0010 0031 002F 0011 002F 0010 0010 002F 0010 0031 000F 0030 0010 002F 0010 0031 002F 0010 0010 0F3C",

        "swing" to "0000 006F 0000 0024 002F 0010 002F 0012 000F 0031 002F 0010 002F 0010 0010 0031 000F 0031 002F 0010 0010 0031 000F 0031 0010 0030 0010 012E 0030 000F 0030 0010 000F 0031 002F 0011 002F 0010 000F 0031 0010 0030 0030 0010 000F 0031 0010 0030 0010 0031 000F 012F 002F 0010 002F 0010 0010 0031 002F 0011 002F 0010 0010 0031 000F 0031 002F 0010 0010 0030 0010 0031 000F 0031 0010 0F3E",

        "timer" to "0000 006F 0000 0024 002F 0010 002F 0011 000F 0030 002F 0010 002F 0011 000F 0030 000F 0030 0010 002F 0030 0010 000F 0030 0010 002F 0010 012D 0030 0010 002F 0010 000F 0030 002F 0011 002F 0010 000F 0030 0010 0030 000F 0030 002F 0010 0010 002F 0010 0030 000F 012F 002F 0010 002F 0010 0010 0030 002F 0010 002F 0010 0010 0030 000F 0030 000F 0030 002F 0011 000F 0030 000F 0030 0010 0F38",

        "wind" to "0000 006F 0000 0024 002F 0010 002F 0011 000F 0030 002F 0010 002F 0011 000F 0030 000F 0030 0010 002F 0010 0030 002F 0010 0010 002F 0010 012D 0030 0010 002F 0010 000F 0030 002F 0011 002F 0010 000F 0030 0010 0030 000F 0030 000F 0030 002F 0010 0010 0030 000F 012E 002F 0010 002F 0010 0010 0030 002F 0010 002F 0010 0010 0030 000F 0030 000F 0030 0010 0030 002F 0010 000F 0030 0010 0F37",

        "off" to "0000 006F 0000 0024 002F 0010 002F 0011 000F 0031 002F 0011 002F 0011 000F 0031 002F 0010 0010 0031 000F 0031 000F 0030 0010 0030 0010 012E 0030 0010 002F 0010 000F 0031 002F 0011 002F 0010 000F 0031 002F 0011 000F 0031 000F 0031 0010 0031 000F 0031 000F 012F 002F 0010 002F 0011 0010 0031 002F 0010 002F 0011 0010 0031 002F 0010 000F 0031 0010 0031 000F 0031 000F 0030 0010 0F3E"
    )

    fun sendIRCommand(command: String) {
        if (!hasIrEmitter()) {
            Log.e("IRManager", "Device doesn't have IR emitter")
            return
        }

        val prontoCode = fanIRCodes[command]
        if (prontoCode != null) {
            try {
                val pattern = convertProntoToMicroseconds(prontoCode)
                // Use 38kHz carrier frequency (extracted from hex code 006F)
                irManager?.transmit(38000, pattern)
                Log.d("IRManager", "Sent IR command: $command with ${pattern.size} pulses")
            } catch (e: Exception) {
                Log.e("IRManager", "Failed to send IR command: ${e.message}")
            }
        } else {
            Log.e("IRManager", "Unknown command: $command")
        }
    }
}