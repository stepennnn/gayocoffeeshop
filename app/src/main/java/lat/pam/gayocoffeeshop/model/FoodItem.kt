package lat.pam.gayocoffeeshop.model

import androidx.annotation.DrawableRes

data class FoodItem(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val formattedPrice: String,
    val category: String, // "Makanan" or "Minuman"
    val iconType: String,
    @get:DrawableRes val imageRes: Int? = null
)

data class CartItem(
    val foodItem: FoodItem,
    val quantity: Int = 1
)

data class OrderDetails(
    val customerName: String,
    val address: String,
    val notes: String,
    val items: List<CartItem>,
    val totalPriceFormatted: String
)
