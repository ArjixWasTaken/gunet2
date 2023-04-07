package org.arjix.gunet2.ui.screen

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import org.arjix.gunet2.R

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Home : Screen("home")
    object Browse : Screen("browse")
}

sealed class NaviBarScreens(val route: String, @StringRes val resourceId: Int, @DrawableRes val icon: Int) {
    object Home : NaviBarScreens(Screen.Home.route, R.string.home_route, R.drawable.ic_baseline_home_24)
    object Browse : NaviBarScreens(Screen.Browse.route, R.string.browse_route, R.drawable.book)
}