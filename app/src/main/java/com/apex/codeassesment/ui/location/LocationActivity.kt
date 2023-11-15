package com.apex.codeassesment.ui.location

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.apex.codeassesment.R
import com.apex.codeassesment.databinding.ActivityLocationBinding
import com.apex.codeassesment.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


// TODO (Optional Bonus 8 points): Calculate distance between 2 coordinates using phone's location
class LocationActivity : AppCompatActivity() {

  private val   requestCode = 101

  private lateinit var fusedLocationClient: FusedLocationProviderClient


  lateinit var binding: ActivityLocationBinding
  @SuppressLint("MissingPermission")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
     binding = ActivityLocationBinding.inflate(layoutInflater)
    setContentView(binding.root)

    val latitudeRandomUser = intent.getStringExtra("user-latitude-key")
    val longitudeRandomUser = intent.getStringExtra("user-longitude-key")

    binding.apply {

      locationRandomUser.text = getString(R.string.location_random_user, latitudeRandomUser, longitudeRandomUser)

      locationPhone.text = getString(R.string.location_phone, latitudeRandomUser, longitudeRandomUser)

      locationCalculateButton.setOnClickListener {
        Toast.makeText(this@LocationActivity, "=> DoneTODO (8): Bonus - Calculate distance between 2 coordinates using phone's location", Toast.LENGTH_SHORT).show()
      }

      fusedLocationClient = LocationServices.getFusedLocationProviderClient(this@LocationActivity)

      val userLat = latitudeRandomUser?.toDouble()
      val userLng = longitudeRandomUser?.toDouble()

      val userLocation = Location("")
      userLocation.latitude = userLat!!
      userLocation.longitude = userLng!!

      updateLocation(userLocation)

    }

  }


  @SuppressLint("MissingPermission")
  private fun updateLocation(userLocation: Location) {
    if( isLocationPermissionGranted()){
      fusedLocationClient.lastLocation
        .addOnSuccessListener { location : Location ->

          calculateDistance(location, userLocation)
        }
    }
  }
  private fun calculateDistance(deviceLocation: Location, userLocation: Location) {

    val metersInMile = 1609.344;

    val distanceBetweenPoints = deviceLocation.distanceTo(userLocation)

    val distanceInMile = distanceBetweenPoints/ metersInMile

    binding.locationDistance.text = getString(R.string.location_result_miles, distanceInMile)

  }
  private fun isLocationPermissionGranted(): Boolean {
    return if (ActivityCompat.checkSelfPermission(
        this,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
      ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
        this,
        android.Manifest.permission.ACCESS_FINE_LOCATION
      ) != PackageManager.PERMISSION_GRANTED
    ) {
      ActivityCompat.requestPermissions(
        this,
        arrayOf(
          android.Manifest.permission.ACCESS_FINE_LOCATION,
          android.Manifest.permission.ACCESS_COARSE_LOCATION
        ),
        requestCode
      )
      false
    } else {
      true
    }
  }
}
