package com.example.pokemonhanbook.Activities

import android.app.AlertDialog
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.pokemonhanbook.R


class MainActivity : AppCompatActivity()
{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme_NoActionBar);
        val connManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        if (mWifi.isConnected) {
            setContentView(R.layout.mainactivity)            // Do whatever
        }
        else{
            val builder = AlertDialog.Builder(this)

            with(builder)
            {
                builder.setTitle("Error No Wifi Detected!")
                builder.setMessage("Please Come Back Later")
                builder.setPositiveButton("OK") { dialog: DialogInterface?, whichButton: Int ->
                    this@MainActivity.finish();
                    System.exit(0)
                }
                builder.show()
            }
        }

    }
}