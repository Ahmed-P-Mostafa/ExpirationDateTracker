package com.polotika.expirydatetracker.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.polotika.expirydatetracker.R
import com.polotika.expirydatetracker.databinding.ActivityHomeBinding
import com.polotika.expirydatetracker.feature_track.presentation.TrackViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val viewModel: TrackViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TAG", "start onCreate: ")
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        viewModel.init(this)
        createChannel()

        val host: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment?
                ?: return
        // assign nav controller with host
        val navController = host.navController

        // setup nacController with Bottom Navigation View
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        bottomNavigationView.setupWithNavController(navController)
        Log.d("TAG", "end onCreate: ")

    }


    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                "CHANNEL_ID",
                "CHANNEL_NAME",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = "CHANNEL_DESCRIPTION"
            channel.lightColor = Color.GREEN
            channel.enableLights(true)
            channel.enableVibration(true)

            manager.createNotificationChannel(channel)
        }
    }


}