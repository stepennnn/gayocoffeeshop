package lat.pam.gayocoffeeshop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import lat.pam.gayocoffeeshop.ui.navigation.Screen
import lat.pam.gayocoffeeshop.ui.screens.HomeScreen
import lat.pam.gayocoffeeshop.ui.screens.LoginScreen
import lat.pam.gayocoffeeshop.ui.screens.OrderAddressScreen
import lat.pam.gayocoffeeshop.ui.screens.OrderConfirmationScreen
import lat.pam.gayocoffeeshop.ui.screens.ProfileScreen
import lat.pam.gayocoffeeshop.ui.screens.RegisterScreen
import lat.pam.gayocoffeeshop.ui.screens.SignUpScreen
import lat.pam.gayocoffeeshop.ui.screens.WelcomeScreen
import lat.pam.gayocoffeeshop.ui.theme.GayocoffeeshopTheme
import lat.pam.gayocoffeeshop.ui.viewmodel.AppViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GayocoffeeshopTheme {
                KopiDanNyemilApp()
            }
        }
    }
}

@Composable
fun KopiDanNyemilApp(
    viewModel: AppViewModel = viewModel()
) {
    val navController = rememberNavController()
    val uiState by viewModel.uiState.collectAsState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Welcome.route

    NavHost(
        navController = navController,
        startDestination = Screen.Welcome.route
    ) {
        // Screen 1: Splash / Welcome
        composable(Screen.Welcome.route) {
            WelcomeScreen(
                onStartClick = {
                    navController.navigate(Screen.SignUp.route)
                }
            )
        }

        // Screen 2: Sign Up options
        composable(Screen.SignUp.route) {
            SignUpScreen(
                onRegisterClick = {
                    navController.navigate(Screen.Register.route)
                },
                onLoginClick = {
                    navController.navigate(Screen.Login.route)
                }
            )
        }

        // Screen 3: Login User
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSubmit = { user, pass ->
                    viewModel.loginUser(user, pass)
                },
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        // Screen 4: Register
        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSubmit = { name, user, pass ->
                    viewModel.registerUser(name, user, pass)
                },
                onRegisterSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route)
                }
            )
        }

        // Screen 5: Home
        composable(Screen.Home.route) {
            HomeScreen(
                userName = uiState.currentUserName,
                foodList = uiState.foodList,
                cartItems = uiState.cartItems,
                formattedTotal = viewModel.getFormattedTotal(),
                onAddToCart = { item -> viewModel.addToCart(item) },
                onRemoveFromCart = { item -> viewModel.removeFromCart(item) },
                onNavigateToOrderAddress = {
                    navController.navigate(Screen.OrderAddress.route)
                },
                currentRoute = currentRoute,
                onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo(Screen.Home.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        // Screen 6: Profile
        composable(Screen.Profile.route) {
            ProfileScreen(
                userName = uiState.currentUserName,
                onSaveName = { newName ->
                    viewModel.setUserName(newName)
                },
                onLogout = {
                    viewModel.setUserName("Nama")
                    navController.navigate(Screen.Welcome.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                currentRoute = currentRoute,
                onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo(Screen.Home.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        // Screen 7: Alamat Pengiriman (Order)
        composable(Screen.OrderAddress.route) {
            OrderAddressScreen(
                initialName = uiState.orderCustomerName.ifBlank { uiState.currentUserName },
                initialAddress = uiState.orderAddress,
                initialNotes = uiState.orderNotes,
                cartItems = uiState.cartItems,
                formattedTotal = viewModel.getFormattedTotal(),
                onOrderSubmit = { name, address, notes ->
                    viewModel.updateAddressForm(name, address, notes)
                    viewModel.confirmOrder()
                },
                onOrderSuccess = {
                    navController.navigate(Screen.OrderConfirmation.route)
                },
                currentRoute = currentRoute,
                onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo(Screen.Home.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        // Screen 8: Konfirmasi Pesanan
        composable(Screen.OrderConfirmation.route) {
            OrderConfirmationScreen(
                userName = uiState.currentUserName,
                orderDetails = uiState.lastOrderDetails,
                onOkeClick = {
                    viewModel.resetOrder()
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
