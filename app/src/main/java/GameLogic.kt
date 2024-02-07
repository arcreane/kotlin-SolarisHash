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
        return allFruits.shuffled().take(4)
    }
}
