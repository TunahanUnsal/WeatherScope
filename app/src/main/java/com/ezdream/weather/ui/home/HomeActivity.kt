package com.ezdream.weather.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeActivity : AppCompatActivity(), LocationListener {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var viewModel: HomeActivityVM
    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2
    private lateinit var animBlink: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i("TAG", "onCreate: HomeActivity")

        binding = ActivityHomeBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[HomeActivityVM::class.java]
        setContentView(binding.root)

        getLocation()

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

    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                locationPermissionCode
            )
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onLocationChanged(p0: Location) {
        Log.i("TAG", "onLocationChanged: ${p0.latitude}-${p0.longitude}")
        CoroutineScope(Dispatchers.IO).launch {
            val response = async {
                viewModel.getWeatherFun(
                    this@HomeActivity,
                    binding.tempText,
                    binding.feelText,
                    binding.cityText,
                    binding.imageView,
                    p0.latitude.toString(),
                    p0.longitude.toString(),
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