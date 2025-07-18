package com.fluidpotata.assignemnt2

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

class broadCastPage {

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun dropDownSelector(onSelect: (String) -> Unit){
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