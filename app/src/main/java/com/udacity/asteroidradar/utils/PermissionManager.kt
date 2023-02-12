package com.udacity.asteroidradar.utils
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.provider.Settings
object PermissionManager {


    //Checks if the internet is working
    //if not it shows a simple dialog to ask the user to turn on the internet
    //with the option to cancel and get the data from database
    fun checkInternetConnectivity(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected = activeNetwork?.isConnectedOrConnecting == true
        return isConnected
    }

}