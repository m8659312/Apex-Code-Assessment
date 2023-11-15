package com.apex.codeassesment.ui.details

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.apex.codeassesment.R
import com.apex.codeassesment.data.model.Coordinates
import com.apex.codeassesment.data.model.User
import com.apex.codeassesment.databinding.ActivityDetailsBinding
import com.apex.codeassesment.ui.location.LocationActivity
import com.bumptech.glide.Glide

// => Done TODO (3 points): Convert to Kotlin
// => Done TODO (3 points): Remove bugs or crashes if any
// => Done TODO (1 point): Add content description to images
//  TODO (2 points): Add tests
class DetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDetailsBinding.inflate(
            layoutInflater
        )
        setContentView(binding.root)
        val user = intent.getParcelableExtra<User>("saved-user-key")

        // => Done TODO (1 point): Use Glide to load images
//    binding.detailsImage = user.getPicture().getLarge();
        if(user?.picture?.large !== null)
        {
            Glide.with(this).load(user.picture.large).into(binding.detailsImage)
        }
        val name = user?.name
        binding.detailsName.text = getString(R.string.details_name, name!!.first, name.last)
        binding.detailsEmail.text = getString(R.string.details_email, user.gender)
        binding.detailsAge.text = getString(R.string.details_age, user.dob!!.age)
        val coordinates = user.location!!.coordinates
        binding.detailsLocation.text =
            getString(R.string.details_location, coordinates!!.latitude, coordinates.longitude)
        binding.detailsLocationButton.setOnClickListener { x: View? ->
            navigateLocation(
                coordinates
            )
        }
    }

    private fun navigateLocation(coordinates: Coordinates) {
        val intent = Intent(this, LocationActivity::class.java)
            .putExtra("user-latitude-key", coordinates.latitude)
            .putExtra("user-longitude-key", coordinates.longitude)
        startActivity(intent)
    }
}