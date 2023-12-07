package com.ezdream.weather.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.ezdream.weather.R
import com.ezdream.weather.databinding.ActivityHomeBinding
import com.ezdream.weather.util.LocateManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var viewModel: HomeActivityVM
    private val locationPermissionCode = 2
    private lateinit var animBlink: Animation
    private var flag = true

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationManager: LocateManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i("TAG", "onCreate: HomeActivity")

        binding = ActivityHomeBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[HomeActivityVM::class.java]
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationManager = LocateManager(this)


        animBlink = AnimationUtils.loadAnimation(
            this,
            R.anim.blink
        );

        binding.clearButton.setOnClickListener {
            viewModel.anim(binding.weatherView, 0, this)
        }

        binding.snowButton.setOnClickListener {
            viewModel.anim(binding.weatherView, 2, this)
        }

        binding.rainButton.setOnClickListener {
            viewModel.anim(binding.weatherView, 1, this)
        }

    }

    override fun onResume() {
        super.onResume()
        locationManager.checkLocationServices()
        if (locationManager.checkLocationPermission()) {
            requestLocation()
        } else {
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
            1001
        )
    }

    private fun requestLocation() {
        locationManager.getDeviceLocation { location: Location? ->
            location?.let {
                val latitude = it.latitude
                val longitude = it.longitude

                flag = true

                CoroutineScope(Dispatchers.IO).launch {
                    val response = async {
                        viewModel.getWeatherFun(
                            this@HomeActivity,
                            binding.tempText,
                            binding.feelText,
                            binding.cityText,
                            binding.imageView,
                            latitude.toString(),
                            longitude.toString(),
                            binding.weatherView
                        )
                    }
                    response.await()
                    runOnUiThread {
                        binding.imageView2.visibility = View.VISIBLE
                        binding.spinKit.visibility = View.GONE
                        binding.imageView2.startAnimation(animBlink)
                    }
                }

            }
        }
    }
}