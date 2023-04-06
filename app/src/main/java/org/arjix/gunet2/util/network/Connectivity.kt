package org.arjix.gunet2.util.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.*
import kotlinx.coroutines.*
import java.net.InetAddress
import java.net.UnknownHostException

suspend fun isDnsResolvable(): Boolean {
    return withContext(Dispatchers.IO) {
        try {
            /*
                www.msftconnecttest.com is what windows uses to determine if a network is connected to the internet
                to be fair, windows requests for http://www.msftconnecttest.com/connecttest.txt and checks the content
                but a simple DNS lookup is enough for us, me thinks :^)
            */
            val address = InetAddress.getByName("www.google.com")
            !address.hostAddress.isNullOrEmpty()
        } catch (e: UnknownHostException) {
            false
        }
    }
}

suspend fun hasNetwork(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val network = connectivityManager.activeNetwork ?: return false
    val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

    return activeNetwork.hasCapability(NET_CAPABILITY_VALIDATED) && isDnsResolvable()
}

fun listenForNetworkChanges(scope: CoroutineScope, context: Context, cb: (Boolean) -> Unit) {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networks = mutableMapOf<Network, NetworkCapabilities>()

    connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            val info = networks.getOrDefault(network, null) ?: connectivityManager.getNetworkCapabilities(network) ?: return
            if (!networks.containsKey(network)) {
                networks[network] = info
            }

            if (!info.hasTransport(TRANSPORT_WIFI) &&
                !info.hasTransport(TRANSPORT_CELLULAR) &&
                !info.hasTransport(TRANSPORT_ETHERNET)) return

            cb(true)
        }

        override fun onUnavailable() {
            cb(false)
        }

        override fun onLost(network: Network) {
            if (!networks.containsKey(network)) return

            val info = networks[network]!!
            networks.remove(network)

            if (!info.hasTransport(TRANSPORT_WIFI) &&
                !info.hasTransport(TRANSPORT_CELLULAR) &&
                !info.hasTransport(TRANSPORT_ETHERNET)) return

            cb(false)
        }
    })
}
