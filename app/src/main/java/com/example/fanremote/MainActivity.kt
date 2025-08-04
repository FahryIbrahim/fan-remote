package com.example.fanremote

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.fanremote.ui.theme.FanRemoteTheme



class MainActivity : ComponentActivity() {
    private lateinit var irManager: IRManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        irManager = IRManager(this)

        // Check if device has IR emitter
        if (!irManager.hasIrEmitter()) {
            Toast.makeText(this, "Device doesn't support IR transmission", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "IR Remote Ready!", Toast.LENGTH_SHORT).show()
        }

        setContent {
            FanRemoteTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FanRemoteScreen(irManager = irManager)
                }
            }
        }
    }
}