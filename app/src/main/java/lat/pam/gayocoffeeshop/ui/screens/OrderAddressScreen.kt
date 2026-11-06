package lat.pam.gayocoffeeshop.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HomeWork
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import lat.pam.gayocoffeeshop.model.CartItem
import lat.pam.gayocoffeeshop.ui.components.AppBottomBar
import lat.pam.gayocoffeeshop.ui.theme.AmberWarm
import lat.pam.gayocoffeeshop.ui.theme.CoffeeDark
import lat.pam.gayocoffeeshop.ui.theme.CoffeeLight
import lat.pam.gayocoffeeshop.ui.theme.SoftBackground

@Composable
fun OrderAddressScreen(
    initialName: String,
    initialAddress: String,
    initialNotes: String,
    cartItems: List<CartItem>,
    formattedTotal: String,
    onOrderSubmit: (String, String, String) -> Boolean,
    onOrderSuccess: () -> Unit,
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    var nameInput by remember(initialName) { mutableStateOf(initialName) }
    var addressInput by remember(initialAddress) { mutableStateOf(initialAddress) }
    var notesInput by remember(initialNotes) { mutableStateOf(initialNotes) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val scrollState = rememberScrollState()

    Scaffold(
        bottomBar = {
            AppBottomBar(
                currentRoute = currentRoute,
                onNavigate = onNavigate
            )
        },
        containerColor = SoftBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(20.dp)
        ) {
            // Title matching wireframe "Alamat"
            Text(
                text = "Alamat",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    color = CoffeeDark
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Cart Summary Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CoffeeLight),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.ShoppingBag,
                            contentDescription = null,
                            tint = CoffeeDark
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Ringkasan Pesanan",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = CoffeeDark
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    if (cartItems.isEmpty()) {
                        Text(
                            text = "Belum ada item dipilih. Kembali ke Home untuk memilih pesanan.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        cartItems.forEach { cart ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "${cart.quantity}x ${cart.foodItem.name}",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                                Text(
                                    text = cart.foodItem.formattedPrice,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider(color = AmberWarm.copy(alpha = 0.3f))
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Total Pembayaran",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = formattedTotal,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = CoffeeDark
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Address Form Fields matching wireframe
            Text(
                text = "Detail Alamat Pengiriman",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = CoffeeDark
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Field 1: Nama Lengkap
            OutlinedTextField(
                value = nameInput,
                onValueChange = {
                    nameInput = it
                    errorMessage = null
                },
                label = { Text("Nama Lengkap") },
                placeholder = { Text("Masukkan nama penerima") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = CoffeeDark)
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CoffeeDark,
                    focusedLabelColor = CoffeeDark
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Field 2: Alamat
            OutlinedTextField(
                value = addressInput,
                onValueChange = {
                    addressInput = it
                    errorMessage = null
                },
                label = { Text("Alamat") },
                placeholder = { Text("Masukkan alamat lengkap pengiriman") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.HomeWork, contentDescription = null, tint = CoffeeDark)
                },
                minLines = 2,
                maxLines = 4,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CoffeeDark,
                    focusedLabelColor = CoffeeDark
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Field 3: Patokan atau detail lengkap
            OutlinedTextField(
                value = notesInput,
                onValueChange = { notesInput = it },
                label = { Text("Patokan atau detail lengkap") },
                placeholder = { Text("Contoh: Rumah pagar hitam samping masjid") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = CoffeeDark)
                },
                minLines = 2,
                maxLines = 4,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CoffeeDark,
                    focusedLabelColor = CoffeeDark
                ),
                modifier = Modifier.fillMaxWidth()
            )

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Button: "Order dan Kirim" matching wireframe
            Button(
                onClick = {
                    if (cartItems.isEmpty()) {
                        errorMessage = "Pilih minimal 1 item makanan/minuman terlebih dahulu"
                    } else if (nameInput.isBlank()) {
                        errorMessage = "Nama lengkap wajib diisi"
                    } else if (addressInput.isBlank()) {
                        errorMessage = "Alamat wajib diisi"
                    } else {
                        val success = onOrderSubmit(nameInput, addressInput, notesInput)
                        if (success) {
                            onOrderSuccess()
                        } else {
                            errorMessage = "Gagal memproses pesanan"
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CoffeeDark,
                    contentColor = CoffeeLight
                )
            ) {
                Text(
                    text = "Order dan Kirim",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
