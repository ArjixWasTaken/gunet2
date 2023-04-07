package org.arjix.gunet2.ui.screen.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import org.arjix.gunet2.ui.screen.Screen

val loggedIn by lazy { mutableStateOf(true) }

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    navController: NavHostController
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // TODO: Make a working login screen.
        Button(onClick = {
            loggedIn.value = true

            val options = NavOptions.Builder()
                .setPopUpTo(Screen.Login.route, inclusive = true)
                .build()

            navController.navigate(
                Screen.Home.route,
                options
            )
        }) {
            Text(text="Login")
        }
    }
}
