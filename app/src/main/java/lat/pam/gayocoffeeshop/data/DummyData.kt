package lat.pam.gayocoffeeshop.data

import lat.pam.gayocoffeeshop.R
import lat.pam.gayocoffeeshop.model.FoodItem

object DummyData {
    val foodList = listOf(
        FoodItem(
            id = "1",
            name = "Kopi Susu Gula Aren",
            description = "Espresso, susu, gula aren",
            price = 22000.0,
            formattedPrice = "Rp 22.000",
            category = "Minuman",
            iconType = "coffee_aren",
            imageRes = R.drawable.item_kopsu_aren
        ),
        FoodItem(
            id = "2",
            name = "Donat Cokelat",
            description = "Donat empuk, glaze cokelat",
            price = 12000.0,
            formattedPrice = "Rp 12.000",
            category = "Makanan",
            iconType = "donat",
            imageRes = R.drawable.item_donat_cokelat
        ),
        FoodItem(
            id = "3",
            name = "Sourdough Loaf",
            description = "Roti fermentasi alami",
            price = 35000.0,
            formattedPrice = "Rp 35.000",
            category = "Makanan",
            iconType = "sourdough",
            imageRes = R.drawable.item_sourdough
        ),
        FoodItem(
            id = "4",
            name = "Cappuccino",
            description = "Espresso, foam susu lembut",
            price = 25000.0,
            formattedPrice = "Rp 25.000",
            category = "Minuman",
            iconType = "cappuccino",
            imageRes = R.drawable.item_cappuccino
        ),
        FoodItem(
            id = "5",
            name = "Matcha Latte",
            description = "Matcha jepang, susu segar",
            price = 27000.0,
            formattedPrice = "Rp 27.000",
            category = "Minuman",
            iconType = "matcha",
            imageRes = R.drawable.item_matcha_latte
        ),
        FoodItem(
            id = "6",
            name = "Croissant Butter",
            description = "Croissant renyah berlapis mentega gurih",
            price = 18000.0,
            formattedPrice = "Rp 18.000",
            category = "Makanan",
            iconType = "croissant",
            imageRes = R.drawable.item_croissant
        ),
        FoodItem(
            id = "7",
            name = "Avocado Coffee",
            description = "Alpukat kocok dengan shot espresso & es krim",
            price = 30000.0,
            formattedPrice = "Rp 30.000",
            category = "Minuman",
            iconType = "avocado",
            imageRes = R.drawable.item_avocado_coffee
        )
    )
}
