package com.lizaveta16.feedthecat.utils

class Constants {
    companion object {
        val GRID = 0
        val LIST = 1
        val MATERIALS = mapOf(
            Materials.WHITE_GOLD to "Белое золото",
            Materials.RED_GOLD to "Красное золото",
            Materials.YELLOW_GOLD to "Желтое золото",
            Materials.SILVER to "Серебро"
        )
        val FOR_WHOM = mapOf(
            ForWhom.FOR_CHILDREN to "Для детей",
            ForWhom.FOR_MAN to "Для мужчин",
            ForWhom.FOR_WOMEN to "Для женщин",
            ForWhom.UNISEX to "Унисекс"
        )
        val CATEGORIES = mapOf(
            Categories.EARRINGS to "Серьги",
            Categories.NECKLACES to "Колье",
            Categories.RINGS to "Кольца",
            Categories.BRACELET to "Браслет"
        )
        val INSERT = mapOf(
            Insert.AGATE to "Агат",
            Insert.BRILLIANT to "Бриллиант",
            Insert.PEARL to "Жемчуг",
            Insert.SAPPHIRE to "Сапфир"
        )
        val SAMPLES = listOf(375, 585, 925)
        val BRANDS = listOf("SOKOLOV", "SKLV", "SOKOLOV diamonds")
        val SIZES = listOf(14.0, 14.5, 15.0, 15.5, 16.0, 16.5, 17.0, 17.5, 18.0, 18.5, 19.0, 19.5, 20.0)
        val ACTIONS = mapOf(
            Actions.EDIT to "edit",
            Actions.ADD to "add",
        )
    }
}

enum class Materials {
    WHITE_GOLD, RED_GOLD, YELLOW_GOLD, SILVER
}

enum class ForWhom {
    FOR_CHILDREN, FOR_WOMEN, FOR_MAN, UNISEX
}

enum class Categories {
    NECKLACES, RINGS, EARRINGS, BRACELET
}

enum class Insert {
    BRILLIANT, SAPPHIRE, PEARL, AGATE
}

enum class Actions {
    ADD, EDIT
}
