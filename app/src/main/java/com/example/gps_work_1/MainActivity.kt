package com.example.gps_work_1

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity(), LocationListener {

    val LOCATION_PERM_CODE = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERM_CODE)
        } else {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
            showAvailableProviders(locationManager)
        }
    }

    fun requestLocationUpdates(view: android.view.View) {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERM_CODE)
            return
        }

        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            5000L,
            10f,
            this
        )
    }

    override fun onLocationChanged(location: Location) {
        val lat = location.latitude
        val lng = location.longitude
        displayCoord(lat, lng)
        Log.d("my", "Lat: $lat, Lng: $lng")
    }

    fun displayCoord(latitude: Double, longitude: Double) {
        findViewById<TextView>(R.id.lat).text = String.format("Широта: %.5f", latitude)
        findViewById<TextView>(R.id.lng).text = String.format("Долгота: %.5f", longitude)
    }

    fun showAvailableProviders(locationManager: LocationManager) {
        val providers: List<String> = locationManager.allProviders
        val availableProviders = StringBuilder("Доступные провайдеры:\n")

        for (provider in providers) {
            if (locationManager.isProviderEnabled(provider)) {
                availableProviders.append("$provider доступен\n")
            } else {
                availableProviders.append("$provider не доступен\n")
            }
        }

        Log.d("my", availableProviders.toString())

        findViewById<TextView>(R.id.status).text = availableProviders.toString()
    }

    override fun onProviderEnabled(provider: String) {
        super.onProviderEnabled(provider)
        findViewById<TextView>(R.id.status).text = "GPS is enabled"
        Log.d("my", "$provider enabled")
    }

    override fun onProviderDisabled(provider: String) {
        super.onProviderDisabled(provider)
        findViewById<TextView>(R.id.status).text = "Offline - GPS is disabled"
        Log.d("my", "$provider disabled")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERM_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("my", "Permission granted")
            } else {
                Log.d("my", "Permission denied")
            }
        }
    }
}
