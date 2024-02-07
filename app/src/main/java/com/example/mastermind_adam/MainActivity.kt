package com.example.mastermind_adam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fruitCombination = GameLogic.generateRandomFruitCombination()
        setContent {
            FruitCombinationDisplay(fruitCombination)
        }
    }
}

@Composable
fun FruitCombinationDisplay(fruits: List<Fruit>) {
    Column(modifier = Modifier.padding(16.dp)) {
        for (fruit in fruits) {
            Text(text = fruit.name)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val sampleFruits = GameLogic.generateRandomFruitCombination() // Utilisez ceci pour le preview
    FruitCombinationDisplay(sampleFruits)
}
