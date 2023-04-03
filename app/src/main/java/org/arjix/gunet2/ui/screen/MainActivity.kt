package org.arjix.gunet2.ui.screen

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

// Screens
import org.arjix.gunet2.ui.screen.home.HomeScreen
import org.arjix.gunet2.ui.screen.splash.SplashScreen
import org.arjix.gunet2.ui.screen.NaviBarScreens

import org.arjix.gunet2.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

                // Dashboard
                composable(Screen.Home.route) {
                    HomeScreen()
                }
            }
        }
    }
}
