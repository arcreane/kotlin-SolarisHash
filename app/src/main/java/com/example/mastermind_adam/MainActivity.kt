package com.example.mastermind_adam

import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
    var showMenu by remember { mutableStateOf(false) } // Pour le menu dans TopAppBar
    var showDialog by remember { mutableStateOf(false) } // Pour contrôler l'affichage du dialogue des fruits

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
                            onClick = { showMenu = false }
                        )
                        DropdownMenuItem(
                            text = { Text("Option 2") },
                            onClick = { showMenu = false }
                        )
                        // Ajouter plus d'options ici si nécessaire
                    }
                }
            )
        },
        bottomBar = {
            BottomFruitCells { showDialog = true }
        }
    ) { innerPadding ->
        FruitCombinationDisplay(fruits, Modifier.padding(innerPadding))
        if (showDialog) { // Ajoutez cette vérification pour afficher le dialogue seulement si showDialog est vrai
            FruitSelectionDialog(GameLogic.allFruits.map { it.name }, onDismiss = { showDialog = false })
        }
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

@Composable
fun BottomFruitCells(onCellClicked: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..4) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .padding(4.dp)
                    .clickable(onClick = onCellClicked)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center,
                content = {}
            )
        }
    }
}

@Composable
fun Row(modifier: Any, horizontalArrangement: Any, verticalAlignment: Alignment.Vertical, content: () -> Unit) {

}

@Composable
fun FruitSelectionDialog(fruits: List<String>, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Fruits") },
        text = {
            Column {
                fruits.forEach { fruit ->
                    Text(fruit, Modifier.padding(2.dp))
                }
            }
        },
        confirmButton = {
            Button(onClick = { onDismiss() }) {
                Text("Close")
            }
        }
    )
}