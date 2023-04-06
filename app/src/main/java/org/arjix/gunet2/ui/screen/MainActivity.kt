package org.arjix.gunet2.ui.screen

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

// Screens
import org.arjix.gunet2.ui.screen.home.HomeScreen
import org.arjix.gunet2.ui.screen.splash.SplashScreen

import org.arjix.gunet2.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.arjix.gunet2.ui.composable.NetworkStatusBar
import org.arjix.gunet2.ui.composable.connectedToInternet
import org.arjix.gunet2.ui.screen.home.CoursesScreen
import org.arjix.gunet2.util.network.hasNetwork
import org.arjix.gunet2.util.network.listenForNetworkChanges


val titles by lazy { mutableStateListOf("Test") }


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        listenForNetworkChanges(viewModel.viewModelScope, this@MainActivity) {
            connectedToInternet.value = it
        }

        setContent {
            AppTheme {
                Surface {
                    AppNavigation()
                }
            }
        }
    }

    @Composable
    private fun currentRoute(navController: NavHostController): String? {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        return navBackStackEntry?.destination?.route
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    private fun AppNavigation() {
        val navController = rememberNavController()

        Scaffold(
            bottomBar = {
                if (currentRoute(navController) != Screen.Splash.route) {
                    BottomNavigation(
                        navController,
                        listOf(
                            NaviBarScreens.Home,
                            NaviBarScreens.Browse,
                        )
                    )
                }
            },
        ) {
            NavHost(navController = navController, startDestination = Screen.Splash.route) {
                // Splash
                composable(Screen.Splash.route) {
                    SplashScreen(
                        onSplashFinished = {
                            viewModel.viewModelScope.launch {
                                connectedToInternet.value = hasNetwork(this@MainActivity)
                            }

                            val options = NavOptions.Builder()
                                .setPopUpTo(Screen.Splash.route, inclusive = true)
                                .build()
                            navController.navigate(
                                Screen.Home.route,
                                options
                            ) // Move to dashboard
                        }
                    )
                }

                composable(Screen.Home.route) {
                    NetworkStatusBar(connectedToInternet.value)
                    HomeScreen()
                }

                composable(Screen.Browse.route) {
                    NetworkStatusBar(connectedToInternet.value)
                    CoursesScreen(titles)
                }
            }
        }
    }
}
