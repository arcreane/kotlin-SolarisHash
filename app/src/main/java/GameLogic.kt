import com.example.mastermind_adam.Fruit
import com.example.mastermind_adam.R

object GameLogic {
    val allFruits = listOf(
        Fruit("Strawberry", hasSeeds = false, isPeelable = false, R.drawable.strawberry),
        Fruit("Banana", hasSeeds = false, isPeelable = true, R.drawable.banana),
        Fruit("Raspberry", hasSeeds = false, isPeelable = false, R.drawable.raspberry),
        Fruit("Kiwi", hasSeeds = false, isPeelable = true, R.drawable.kiwi),
        Fruit("Orange", hasSeeds = true, isPeelable = true, R.drawable.orange),
        Fruit("Prune", hasSeeds = true, isPeelable = false, R.drawable.prune),
        Fruit("Grape", hasSeeds = true, isPeelable = false, R.drawable.grape),
        Fruit("Lemon", hasSeeds = true, isPeelable = true, R.drawable.lemon)
    )




    fun generateRandomFruitCombination(): List<Fruit> {
        return allFruits.shuffled().distinctBy { it.imageResId }.take(4)
    }

    fun checkGuess(guess: List<Fruit>, secretCombination: List<Fruit>): GuessResult {
        val correctPositions = guess.zip(secretCombination).count { it.first == it.second }
        val misplacedPositions = guess.filter { it in secretCombination }.size - correctPositions
        return GuessResult(guess, correctPositions, misplacedPositions)
    }

    fun compareSelections(secret: List<Fruit>, guess: List<Fruit?>): ComparisonResult {
        // Initialiser le compte des positions correctes
        var correctPositions = 0

        // Initialiser la liste pour les fruits mal placés ou incorrects
        val misplaced = MutableList(guess.size) { "X" } // Pré-remplir avec "X"

        // Compter les fruits correctement positionnés et identifier les fruits présents (indépendamment de la position)
        val secretCount = secret.groupBy { it }.mapValues { it.value.size }.toMutableMap()
        guess.forEachIndexed { index, fruit ->
            if (fruit != null) {
                if (fruit == secret[index]) {
                    // Fruit correctement positionné
                    correctPositions++
                    misplaced[index] = "" // Remplacer "X" par "" pour indiquer une position correcte
                    secretCount[fruit] = secretCount[fruit]!! - 1
                }
            }
        }

        // Identifier les fruits mal placés
        guess.forEachIndexed { index, fruit ->
            if (fruit != null && misplaced[index] == "X" && secretCount.contains(fruit) && secretCount[fruit]!! > 0) {
                misplaced[index] = "1" // Indique un fruit correct mais mal placé
                secretCount[fruit] = secretCount[fruit]!! - 1
            }
        }

        return ComparisonResult(correctPositions, misplaced)
    }

    fun validateSelection(gameState: GameState, playerSelection: List<Fruit?>) {
        // Assurez-vous que playerSelection ne contient pas de nulls pour la comparaison
        val filteredSelection = playerSelection.filterNotNull()

        // Vérifiez si la sélection du joueur est entièrement correcte
        val isPerfectMatch = filteredSelection == gameState.secretCombination

        // Comparez la sélection du joueur avec la combinaison secrète
        val comparisonResult = compareSelections(gameState.secretCombination, filteredSelection)

        // Ajoutez le résultat de la comparaison à l'historique
        gameState.history.add(comparisonResult)

        // Diminuez le nombre d'essais restants
        gameState.attemptsLeft--

        // Si la sélection du joueur correspond totalement à la combinaison secrète
        if (isPerfectMatch) {
            // Mise à jour du score avec le nombre d'essais restants
            gameState.score = gameState.attemptsLeft
            // Optionnellement, réinitialiser le jeu ou faire d'autres mises à jour ici
        }

    }

    fun provideSeedHint(secretCombination: List<Fruit>): List<String> {
        return secretCombination.map { if (it.hasSeeds) "With" else "Without" }
    }

    fun providePeelableHint(secretCombination: List<Fruit>): List<String> {
        return secretCombination.map { if (it.isPeelable) "Peelable" else "NotPeelable" }
    }



}


data class ComparisonResult(
    val correctPositions: Int,
    val misplaced: List<String> // "X" pour incorrect, ou le nombre de positions mal placées pour les fruits corrects
)


data class GuessResult(
    val guess: List<Fruit>,
    val correctPositions: Int,
    val misplacedPositions: Int
)

data class GameState(
    var secretCombination: List<Fruit> = listOf(),
    var attemptsLeft: Int = 10, // Exemple: 10 essais au début
    var score: Int = 0,
    var history: MutableList<ComparisonResult> = mutableListOf() // Supposons que vous avez ajouté ComparisonResult à votre historique
)

enum class GameStateStatus {
    WON, LOST, PLAYING
}

