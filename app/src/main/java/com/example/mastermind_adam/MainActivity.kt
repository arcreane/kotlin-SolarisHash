package com.example.mastermind_adam

import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fruitCombination = GameLogic.generateRandomFruitCombination()
        setContent {
            SmallTopAppBarExample(fruitCombination)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmallTopAppBarExample(fruits: List<Fruit>) {
    var showMenu by remember { mutableStateOf(false) } // État pour afficher/cacher le menu

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mastermind") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                actions = {
                    IconButton(onClick = { showMenu = !showMenu }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "Menu")
                    }
                    // Menu déroulant
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Option 1") },
                            onClick = {
                                // Gérer l'action de l'option 1
                                showMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Option 2") },
                            onClick = {
                                // Gérer l'action de l'option 2
                                showMenu = false
                            }
                        )
                        // Ajouter plus d'options ici si nécessaire
                    }
                }
            )
        }
    ) { innerPadding ->
        FruitCombinationDisplay(fruits, Modifier.padding(innerPadding))
    }
}

@Composable
fun FruitCombinationDisplay(fruits: List<Fruit>, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(16.dp)) {
        for (fruit in fruits) {
            Text(text = fruit.name)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val sampleFruits = GameLogic.generateRandomFruitCombination() // Utilisez ceci pour le preview
    SmallTopAppBarExample(sampleFruits)
}
