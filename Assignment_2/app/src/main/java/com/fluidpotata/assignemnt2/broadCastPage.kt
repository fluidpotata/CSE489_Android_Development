package com.fluidpotata.assignemnt2

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import android.widget.Toast




@Composable
fun HomeScreen() {
    var selectedOption by remember { mutableStateOf("Battery Info") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        DropDownSelector(onSelect = { selectedOption = it })

        Spacer(modifier = Modifier.height(16.dp))

        when (selectedOption) {
            "Battery" -> BatteryInfoDisplay()
            "Custom" -> BroadcastSenderUI()
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownSelector(onSelect: (String) -> Unit){
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("Battery", "Custom")
    var selectedOption by remember { mutableStateOf(options[0]) }

    ExposedDropdownMenuBox(expanded=expanded, onExpandedChange = {expanded = !expanded}) {
        TextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            label = { Text("Choose Option") },
            trailingIcon = {ExposedDropdownMenuDefaults.TrailingIcon(expanded=expanded)},
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable, enabled = true)
        )

        ExposedDropdownMenu(expanded=expanded, onDismissRequest = {expanded=false}) {
            options.forEach { selection ->
                DropdownMenuItem(
                    text = { Text(selection) },
                    onClick = {
                        selectedOption = selection
                        expanded = false
                        onSelect(selection)
                    }
                )
            }
        }
    }
}

@Composable
fun BatteryInfoDisplay() {
    val context = LocalContext.current
    var batteryLevel by remember { mutableStateOf(-1) }

    DisposableEffect(Unit) {
        val batteryReceiver = object : BroadcastReceiver() {
            override fun onReceive(ctx: Context?, intent: Intent?) {
                val level = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
                batteryLevel = level
            }
        }

        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        context.registerReceiver(batteryReceiver, filter)

        onDispose {
            context.unregisterReceiver(batteryReceiver)
        }
    }

    Text(
        text = if (batteryLevel != -1) "Battery Level: $batteryLevel%" else "Fetching battery info...",
        style = MaterialTheme.typography.bodyLarge
    )
}



fun SendMyBroadcast(context: Context, message: String) {
    val intent = Intent("com.alif.CUSTOM_BROADCAST")
    intent.putExtra("message", message)
    context.sendBroadcast(intent)
}


@Composable
fun BroadcastSenderUI() {
    val context = LocalContext.current
    var inputText by remember { mutableStateOf("") }

    Column {
        TextField(
            value = inputText,
            onValueChange = { inputText = it },
            label = { Text("Enter message") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (inputText.isNotBlank()) {
                    SendMyBroadcast(context, inputText)
                    Toast.makeText(context, "Message sent!", Toast.LENGTH_SHORT).show()
                    inputText = ""  // Optional: clear input field
                }
            }
        ) {
            Text("Send Broadcast")
        }
    }
}

