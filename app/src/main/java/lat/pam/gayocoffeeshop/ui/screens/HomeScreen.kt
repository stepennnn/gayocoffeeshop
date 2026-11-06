package lat.pam.gayocoffeeshop.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import lat.pam.gayocoffeeshop.model.CartItem
import lat.pam.gayocoffeeshop.model.FoodItem
import lat.pam.gayocoffeeshop.ui.components.AppBottomBar
import lat.pam.gayocoffeeshop.ui.components.AppHeader
import lat.pam.gayocoffeeshop.ui.components.FoodItemCard
import lat.pam.gayocoffeeshop.ui.theme.AmberWarm
import lat.pam.gayocoffeeshop.ui.theme.CoffeeDark
import lat.pam.gayocoffeeshop.ui.theme.CoffeeLight
import lat.pam.gayocoffeeshop.ui.theme.SoftBackground

@Composable
fun HomeScreen(
    userName: String,
    foodList: List<FoodItem>,
    cartItems: List<CartItem>,
    formattedTotal: String,
    onAddToCart: (FoodItem) -> Unit,
    onRemoveFromCart: (FoodItem) -> Unit,
    onNavigateToOrderAddress: () -> Unit,
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    var selectedCategory by remember { mutableStateOf("Semua") }

    val filteredList = when (selectedCategory) {
        "Makanan" -> foodList.filter { it.category == "Makanan" }
        "Minuman" -> foodList.filter { it.category == "Minuman" }
        else -> foodList
    }

    val totalItemsCount = cartItems.sumOf { it.quantity }

    Scaffold(
        bottomBar = {
            AppBottomBar(
                currentRoute = currentRoute,
                onNavigate = onNavigate
            )
        },
        containerColor = SoftBackground
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    top = 16.dp,
                    bottom = if (totalItemsCount > 0) 90.dp else 24.dp,
                    start = 16.dp,
                    end = 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Header: "Halo [Nama],"
                item {
                    AppHeader(
                        userName = userName,
                        subtitle = "Mau ngopi atau nyemil apa hari ini?"
                    )
                }

                // Section Title matching wireframe "Makanan" + Category chips
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Makanan & Minuman",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = CoffeeDark,
                                fontSize = 20.sp
                            ),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            listOf("Semua", "Makanan", "Minuman").forEach { category ->
                                val isSelected = selectedCategory == category
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { selectedCategory = category },
                                    label = {
                                        Text(
                                            text = category,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = CoffeeDark,
                                        selectedLabelColor = CoffeeLight,
                                        containerColor = CoffeeLight,
                                        labelColor = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                            }
                        }
                    }
                }

                // Food Items List (min 5 items displayed)
                items(filteredList, key = { it.id }) { item ->
                    val cartQuantity = cartItems.find { it.foodItem.id == item.id }?.quantity ?: 0
                    FoodItemCard(
                        item = item,
                        quantity = cartQuantity,
                        onAddToCart = onAddToCart,
                        onRemoveFromCart = onRemoveFromCart,
                        onItemClick = { onAddToCart(item) }
                    )
                }
            }

            // Floating Order Banner when cart is not empty
            if (totalItemsCount > 0) {
                Button(
                    onClick = onNavigateToOrderAddress,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CoffeeDark,
                        contentColor = CoffeeLight
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Left side: Item count badge + text
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(AmberWarm)
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = totalItemsCount.toString(),
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = CoffeeDark
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Item Dipilih",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = CoffeeLight,
                                    fontSize = 14.sp
                                )
                            )
                        }

                        // Right side: Total Price + Lanjut Order Arrow
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = formattedTotal,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = AmberWarm,
                                    fontSize = 15.sp
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Order",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = CoffeeLight,
                                    fontSize = 15.sp
                                )
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Lanjut Order",
                                tint = CoffeeLight,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
