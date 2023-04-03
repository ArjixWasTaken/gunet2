package org.arjix.gunet2.ui.screen

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import org.arjix.gunet2.R

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Home : Screen("home")
    object Browse : Screen("browse")
}

sealed class NaviBarScreens(val route: String, @StringRes val resourceId: Int, @DrawableRes val icon: Int) {
    object Home : NaviBarScreens("Home", R.string.home_route, R.drawable.ic_baseline_home_24)
    object Browse : NaviBarScreens("Browse", R.string.browse_route, R.drawable.book)
}