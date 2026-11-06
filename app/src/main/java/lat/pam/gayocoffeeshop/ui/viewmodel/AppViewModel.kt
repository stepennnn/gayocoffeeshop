package lat.pam.gayocoffeeshop.ui.viewmodel

import androidx.lifecycle.ViewModel
import lat.pam.gayocoffeeshop.data.DummyData
import lat.pam.gayocoffeeshop.model.CartItem
import lat.pam.gayocoffeeshop.model.FoodItem
import lat.pam.gayocoffeeshop.model.OrderDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Locale

data class AppState(
    val currentUserName: String = "Nama",
    val currentUsername: String = "",
    val foodList: List<FoodItem> = DummyData.foodList,
    val cartItems: List<CartItem> = emptyList(),
    // Address Form state
    val orderCustomerName: String = "",
    val orderAddress: String = "",
    val orderNotes: String = "",
    // Confirmed Order
    val lastOrderDetails: OrderDetails? = null,
    // Saved users for simple login validation (Username -> Pair(FullName, Password))
    val registeredUsers: Map<String, Pair<String, String>> = mapOf(
        "admin" to Pair("Admin Coffee", "123456"),
        "user" to Pair("Nama", "123456")
    )
)

class AppViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AppState())
    val uiState: StateFlow<AppState> = _uiState.asStateFlow()

    init {
        // Default cart with item 1 to make testing immediate if needed
        addToCart(DummyData.foodList[0])
    }

    fun setUserName(name: String) {
        val trimmed = name.trim()
        if (trimmed.isNotEmpty()) {
            _uiState.update { it.copy(currentUserName = trimmed, orderCustomerName = trimmed) }
        }
    }

    fun registerUser(fullName: String, username: String, pass: String): Boolean {
        if (fullName.isBlank() || username.isBlank() || pass.isBlank()) return false
        
        val newUsers = _uiState.value.registeredUsers.toMutableMap()
        newUsers[username.lowercase()] = Pair(fullName.trim(), pass)
        
        _uiState.update { 
            it.copy(
                registeredUsers = newUsers,
                currentUserName = fullName.trim(),
                currentUsername = username.lowercase(),
                orderCustomerName = fullName.trim()
            )
        }
        return true
    }

    fun loginUser(username: String, pass: String): Boolean {
        if (username.isBlank()) return false
        val userKey = username.trim().lowercase()
        val userPair = _uiState.value.registeredUsers[userKey]
        
        return if (userPair != null) {
            if (userPair.second == pass) {
                _uiState.update {
                    it.copy(
                        currentUserName = userPair.first,
                        currentUsername = userKey,
                        orderCustomerName = userPair.first
                    )
                }
                true
            } else {
                false
            }
        } else {
            // Fallback: If username doesn't exist yet, accept login as fallback and format name
            val formattedName = username.trim().replaceFirstChar { it.uppercase() }
            _uiState.update {
                it.copy(
                    currentUserName = formattedName,
                    currentUsername = userKey,
                    orderCustomerName = formattedName
                )
            }
            true
        }
    }

    fun addToCart(item: FoodItem) {
        _uiState.update { state ->
            val existingIndex = state.cartItems.indexOfFirst { it.foodItem.id == item.id }
            val newCart = state.cartItems.toMutableList()
            if (existingIndex >= 0) {
                val current = newCart[existingIndex]
                newCart[existingIndex] = current.copy(quantity = current.quantity + 1)
            } else {
                newCart.add(CartItem(foodItem = item, quantity = 1))
            }
            state.copy(cartItems = newCart)
        }
    }

    fun removeFromCart(item: FoodItem) {
        _uiState.update { state ->
            val existingIndex = state.cartItems.indexOfFirst { it.foodItem.id == item.id }
            if (existingIndex >= 0) {
                val newCart = state.cartItems.toMutableList()
                val current = newCart[existingIndex]
                if (current.quantity > 1) {
                    newCart[existingIndex] = current.copy(quantity = current.quantity - 1)
                } else {
                    newCart.removeAt(existingIndex)
                }
                state.copy(cartItems = newCart)
            } else {
                state
            }
        }
    }

    fun updateAddressForm(name: String, address: String, notes: String) {
        _uiState.update {
            it.copy(
                orderCustomerName = name,
                orderAddress = address,
                orderNotes = notes
            )
        }
    }

    fun calculateTotal(): Double {
        return _uiState.value.cartItems.sumOf { it.foodItem.price * it.quantity }
    }

    fun getFormattedTotal(): String {
        val total = calculateTotal().toLong()
        return String.format(Locale.getDefault(), "Rp %,d", total).replace(',', '.')
    }

    fun confirmOrder(): Boolean {
        val currentState = _uiState.value
        val name = currentState.orderCustomerName.ifBlank { currentState.currentUserName }
        val address = currentState.orderAddress
        val notes = currentState.orderNotes
        
        if (address.isBlank()) return false
        
        val details = OrderDetails(
            customerName = name,
            address = address,
            notes = notes,
            items = currentState.cartItems,
            totalPriceFormatted = getFormattedTotal()
        )
        
        _uiState.update {
            it.copy(
                lastOrderDetails = details
            )
        }
        return true
    }

    fun resetOrder() {
        _uiState.update {
            it.copy(
                cartItems = emptyList(),
                orderAddress = "",
                orderNotes = ""
            )
        }
    }
}
