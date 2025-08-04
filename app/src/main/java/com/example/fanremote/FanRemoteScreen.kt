package com.example.fanremote

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

// Custom shadcn-inspired dark theme colors
object FanRemoteTheme {
    val background = Color(0xFF0A0A0A)
    val card = Color(0xFF1A1A1A)
    val cardHover = Color(0xFF262626)
    val border = Color(0xFF2A2A2A)
    val primary = Color(0xFFE4E4E7)
    val secondary = Color(0xFF71717A)
    val accent = Color(0xFF3B82F6)
    val destructive = Color(0xFFEF4444)
    val success = Color(0xFF10B981)
}

data class ButtonState(
    val isPressed: Boolean = false,
    val isActive: Boolean = false
)

@Composable
fun FanRemoteScreen(irManager: IRManager? = null) {
    var buttonStates by remember {
        mutableStateOf(mapOf(
            "off" to ButtonState(),
            "timer" to ButtonState(),
            "speed" to ButtonState(),
            "swing" to ButtonState(),
            "wind" to ButtonState()
        ))
    }

    var fanSpeed by remember { mutableIntStateOf(0) } // 0: OFF, 1: Low, 2: Mid, 3: High
    var timerHours by remember { mutableFloatStateOf(0f) } // 0, 0.5, 1, 2, 4
    var isSwingOn by remember { mutableStateOf(false) }
    var windMode by remember { mutableIntStateOf(0) } // 0: Normal, 1: Rhythm, 2: Sleep

    fun updateButtonState(key: String, pressed: Boolean = false, active: Boolean = false) {
        buttonStates = buttonStates.toMutableMap().apply {
            this[key] = ButtonState(pressed, active)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FanRemoteTheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Text(
            text = "Fan Remote",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = FanRemoteTheme.primary,
            modifier = Modifier.padding(bottom = 8.dp, top = 24.dp)

        )

        Text(
            text = "Control your fan with IR blaster",
            fontSize = 14.sp,
            color = FanRemoteTheme.secondary,
            modifier = Modifier.padding(bottom = 40.dp)
        )

        // Status Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(containerColor = FanRemoteTheme.card),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(50.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StatusItem("Speed", when(fanSpeed) {
                        1 -> "Low"
                        2 -> "Mid"
                        3 -> "High"
                        else -> "OFF"
                    })
                    StatusItem("Timer", if (timerHours == 0f) "OFF" else "${if (timerHours < 1) "${(timerHours * 60).toInt()}m" else "${timerHours.toInt()}h"}")
                }
                Spacer(modifier = Modifier.height(32.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StatusItem("Swing", if (isSwingOn) "ON" else "OFF")
                    StatusItem("Wind", when(windMode) {
                        1 -> "Rhythm"
                        2 -> "Sleep"
                        else -> "Normal"
                    })
                }
            }
        }

        // Control Buttons Grid
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Row 1: OFF button (full width)
            RemoteButton(
                modifier = Modifier.fillMaxWidth(),
                text = "OFF",
                icon = Icons.Default.PowerSettingsNew,
                buttonState = buttonStates["off"] ?: ButtonState(),
                buttonType = ButtonType.Destructive,
                onClick = {
                    updateButtonState("off", pressed = true)
                    fanSpeed = 0
                    timerHours = 0f
                    isSwingOn = false
                    windMode = 0
                    irManager?.sendIRCommand("off")
                }
            )

            // Row 2: SPEED (left) and SWING (right)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                RemoteButton(
                    modifier = Modifier.weight(1f),
                    text = "ON / SPEED",
                    icon = Icons.Default.Speed,
                    buttonState = buttonStates["speed"] ?: ButtonState(),
                    buttonType = ButtonType.Primary,
                    onClick = {
                        updateButtonState("speed", pressed = true)
                        fanSpeed = if (fanSpeed >= 3) 1 else fanSpeed + 1
                        irManager?.sendIRCommand("on")
                    }
                )

                RemoteButton(
                    modifier = Modifier.weight(1f),
                    text = "SWING",
                    icon = Icons.Default.SwapHoriz,
                    buttonState = buttonStates["swing"] ?: ButtonState(),
                    buttonType = if (isSwingOn) ButtonType.Active else ButtonType.Default,
                    onClick = {
                        updateButtonState("swing", pressed = true)
                        isSwingOn = !isSwingOn
                        irManager?.sendIRCommand("swing")
                    }
                )
            }

            // Row 3: TIMER (left) and WIND MODE (right)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                RemoteButton(
                    modifier = Modifier.weight(1f),
                    text = "TIMER",
                    icon = Icons.Default.Timer,
                    buttonState = buttonStates["timer"] ?: ButtonState(),
                    onClick = {
                        updateButtonState("timer", pressed = true)
                        timerHours = when (timerHours) {
                            0f -> 0.5f
                            0.5f -> 1f
                            1f -> 2f
                            2f -> 4f
                            else -> 0f
                        }
                        irManager?.sendIRCommand("timer")
                    }
                )

                RemoteButton(
                    modifier = Modifier.weight(1f),
                    text = "WIND MODE",
                    icon = Icons.Default.Air,
                    buttonState = buttonStates["wind"] ?: ButtonState(),
                    onClick = {
                        updateButtonState("wind", pressed = true)
                        windMode = (windMode + 1) % 3
                        irManager?.sendIRCommand("wind")
                    }
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Footer
        Text(
            text = "Created by Ib",
            fontSize = 12.sp,
            color = FanRemoteTheme.secondary,
            modifier = Modifier.padding(top = 24.dp)
        )
    }
}

@Composable
fun StatusItem(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = FanRemoteTheme.secondary,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            fontSize = 16.sp,
            color = FanRemoteTheme.primary,
            fontWeight = FontWeight.Bold
        )
    }
}

enum class ButtonType {
    Default, Primary, Destructive, Active
}

@Composable
fun RemoteButton(
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector,
    buttonState: ButtonState,
    buttonType: ButtonType = ButtonType.Default,
    onClick: () -> Unit
) {
    val backgroundColor = when (buttonType) {
        ButtonType.Primary -> FanRemoteTheme.accent
        ButtonType.Destructive -> FanRemoteTheme.destructive
        ButtonType.Active -> FanRemoteTheme.success
        ButtonType.Default -> FanRemoteTheme.card
    }

    val contentColor = when (buttonType) {
        ButtonType.Default -> FanRemoteTheme.primary
        else -> Color.White
    }

    Card(
        modifier = modifier
            .height(80.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    width = 1.dp,
                    color = if (buttonType == ButtonType.Default) FanRemoteTheme.border else Color.Transparent,
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = text,
                    tint = contentColor,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = text,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = contentColor
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FanRemotePreview() {
    FanRemoteScreen(irManager = null)
}