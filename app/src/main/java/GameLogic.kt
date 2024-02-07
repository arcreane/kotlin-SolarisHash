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

    fun provideSeedHint(secretCombination: List<Fruit>): List<String> {
        return secretCombination.map { if (it.hasSeeds) "With" else "Without" }
    }

    fun providePeelableHint(secretCombination: List<Fruit>): List<String> {
        return secretCombination.map { if (it.isPeelable) "Peelable" else "NotPeelable" }
    }

}

data class GuessResult(
    val guess: List<Fruit>,
    val correctPositions: Int,
    val misplacedPositions: Int
)

data class GameState(
    val secretCombination: List<Fruit> = listOf(),
    var attemptsLeft: Int = 10,
    val history: MutableList<GuessResult> = mutableListOf(),
    var score: Int = 0,
    val gamesWon: Int = 0
)
