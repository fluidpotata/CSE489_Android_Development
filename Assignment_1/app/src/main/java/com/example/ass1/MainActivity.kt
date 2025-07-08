package com.example.ass1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalConfiguration
import android.content.res.Configuration
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import com.example.ass1.ui.theme.Ass1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ass1Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NumberPadApp(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun NumberPadApp(modifier: Modifier = Modifier) {
    var input by rememberSaveable{ mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Text("VangtiChai")
        }


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(Color(0xFFE0E0E0)),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = input,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(end = 16.dp)
            )
        }


        Row(modifier = Modifier.fillMaxSize()) {


            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                updateLeftPanel(amountStr = input)
            }


            Column(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight()
                    .padding(start = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val buttonLabels = listOf(
                    listOf("1", "2", "3"),
                    listOf("4", "5", "6"),
                    listOf("7", "8", "9"),
                    listOf("0", "Reset")
                )

                buttonLabels.forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        row.forEach { label ->
                            Button(
                                onClick = {
                                    input = when (label) {
                                        "Reset" -> ""
                                        else -> input + label
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp)
                            ) {
                                Text(label)
                            }
                        }
                    }
                }
            }
        }
    }
}

fun calculateChange(amount: Int): Map<Int, Int> {
    val notesAvailable = listOf(500, 100, 50, 20, 10, 5, 2, 1)
    val result = mutableMapOf<Int, Int>()
    var remainingAmount = amount
    
    for (note in notesAvailable) {
        val count = remainingAmount / note
        if (count > 0) {
            result[note] = count
            remainingAmount -= count * note
        }
    }

    return result
}


@Composable
fun isLandscape(): Boolean {
    val configuration = LocalConfiguration.current
    return configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
}


@Composable
fun updateLeftPanel(amountStr: String) {
    val amount = amountStr.toIntOrNull() ?: 0
    val change = calculateChange(amount)
    val landscape = isLandscape()

    val denominations = listOf(500, 100, 50, 20, 10, 5, 2, 1)

    if (landscape) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxHeight()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(denominations) { note ->
                val count = change[note] ?: 0
                Text("$note: $count")
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            listOf(500, 100, 50, 20, 10, 5, 2, 1).forEach { note ->
                val count = change[note] ?: 0
                Text("$note: $count")
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Ass1Theme {
        NumberPadApp()
    }
}