package com.example.mastermind_adam

data class Fruit(val name: String, val hasSeeds: Boolean, val isPeelable: Boolean, val imageResId: Int) {
    override fun toString(): String {
        return name
    }
}
