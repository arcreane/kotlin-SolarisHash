package com.example.mastermind_adam

import GameState
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val secretCombination = GameLogic.generateRandomFruitCombination()
        setContent {
            // Using MaterialTheme directly
            MaterialTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colorScheme.background) {
                    SmallTopAppBarExample(secretCombination, GameState(secretCombination = secretCombination))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmallTopAppBarExample(fruits: List<Fruit>, gameState: GameState) {
    var showMenu by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var selectedFruits by remember { mutableStateOf<List<Fruit?>>(List(fruits.size) { null }) }
    var selectedCellIndex by remember { mutableStateOf<Int?>(null) }
    var showValidation by remember { mutableStateOf(false) }
    var history by mutableStateOf<List<List<Fruit?>>>(listOf())


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
                    }
                }
            )
        },
        bottomBar = {
            Column {
                BottomFruitCells(
                    selectedFruits = selectedFruits,
                    onCellClicked = { index ->
                        selectedCellIndex = index
                        showDialog = true
                    },
                    fruits = GameLogic.allFruits
                )
                Button(
                    onClick = {
                        // Ajoutez la sélection actuelle à l'historique
                        history = history + listOf(selectedFruits)
                        // Réinitialisez la sélection actuelle ou effectuez toute autre action nécessaire
                        showValidation = false // Cachez le bouton ou réinitialisez l'état si nécessaire
                    },
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                ) {
                    Text("Validate Selection")
                }

            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            // Display the game info just below the top bar
            GameInfoDisplay(gameState = gameState)
            DisplayHistory(history)

            // Now display the fruit combination or any other main content
            FruitCombinationDisplay(fruits)
            if (showDialog && selectedCellIndex != null) {
                FruitSelectionDialog(
                    fruits = GameLogic.allFruits,
                    onFruitSelected = { fruit ->
                        val updatedSelections = selectedFruits.toMutableList().apply {
                            this[selectedCellIndex!!] = fruit // Update the fruit for the selected cell
                        }
                        selectedFruits = updatedSelections // Update the state
                        showDialog = false // Close the dialog
                    },
                    onDismiss = { showDialog = false } // Close the dialog without making a selection
                )
            }


        }
    }
}

//Display score et attempts
@Composable
fun GameInfoDisplay(gameState: GameState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Score: ${gameState.score}",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "Attempts Left: ${gameState.attemptsLeft}",
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun DisplayHistory(history: List<List<Fruit?>>) {
    history.forEachIndexed { index, selection ->
        Text("Selection $index", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(8.dp))
        Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
            selection.forEach { fruit ->
                fruit?.let {
                    Image(
                        painter = painterResource(id = it.imageResId),
                        contentDescription = it.name,
                        modifier = Modifier.size(100.dp).padding(8.dp)
                    )
                }
            }
        }
    }
}


//Affiche les combinaisons de fruit
@Composable
fun FruitCombinationDisplay(selectedFruits: List<Fruit?>, modifier: Modifier = Modifier) {
    Row(modifier = modifier
        .horizontalScroll(rememberScrollState())
        .padding(16.dp)) {
        for (fruit in selectedFruits) {
            fruit?.let {
                Image(
                    painter = painterResource(id = it.imageResId),
                    contentDescription = it.name,
                    modifier = Modifier
                        .size(100.dp)
                        .padding(8.dp)
                )
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val sampleFruits = GameLogic.generateRandomFruitCombination()
    SmallTopAppBarExample(sampleFruits, gameState)
}



//Gére les cellules du bas
@Composable
fun BottomFruitCells(
    selectedFruits: List<Fruit?>,
    onCellClicked: (Int) -> Unit,
    fruits: List<Fruit>
    ) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        fruits.indices.forEach { index ->
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(color = Color.Gray)
                    .padding(4.dp)
                    .clickable { onCellClicked(index) },
                contentAlignment = Alignment.Center
            ) {
                // Display the selected fruit's image if any
                selectedFruits.getOrNull(index)?.let { fruit ->
                    Image(painter = painterResource(id = fruit.imageResId), contentDescription = fruit.name)
                }
            }
        }
    }
}




//Modal de selection de fruit
@Composable
fun FruitSelectionDialog(fruits: List<Fruit>, onFruitSelected: (Fruit) -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Select a Fruit") },
        text = {
            Column {
                fruits.forEach { fruit ->
                    Button(onClick = { onFruitSelected(fruit) }, modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)) {
                        Text(fruit.name)
                    }
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



//Fruit selectionner passe en background
@Composable
fun SelectedFruitBackground(fruit: Fruit?) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .background(color = MaterialTheme.colorScheme.surfaceVariant)
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        fruit?.let {
            Image(
                painter = painterResource(id = it.imageResId),
                contentDescription = it.name
            )
        }
    }
}




//Code a faire dans un autre fichier plus tard

var gameState by mutableStateOf(GameState())

fun submitGuess(guess: List<Fruit>) {
    if (gameState.attemptsLeft > 0) {
        val result = GameLogic.checkGuess(guess, gameState.secretCombination)
        gameState.history.add(result)
        gameState.attemptsLeft--

        if (result.correctPositions == gameState.secretCombination.size) {
            // L'utilisateur a trouvé la combinaison correcte
            gameState.score += gameState.attemptsLeft
            // Recommencer le jeu ou mettre à jour les statistiques
        }
    }
}

enum class HintType {
    SEED,
    PEEL
}

fun useHint(type: HintType) {
    when (type) {
        HintType.SEED -> gameState.attemptsLeft -= 2
        HintType.PEEL -> gameState.attemptsLeft -= 3
    }
    // Afficher l'indice correspondant
}