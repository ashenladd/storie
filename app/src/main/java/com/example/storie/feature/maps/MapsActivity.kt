package com.example.storie.feature.maps

import android.content.DialogInterface
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.storie.R
import com.example.storie.component.AlertDialogFragment
import com.example.storie.databinding.ActivityMapsBinding
import com.example.storie.domain.model.StoryModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val mViewModel by viewModels<MapsViewModel>()
    private val boundsBuilder = LatLngBounds.Builder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        setupObserver()
        setupMapStyle()
    }

    private fun setupMapStyle() {
        try {
            val success =
                mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                        this,
                        R.raw.map_style
                    )
                )
            if (!success) {
                Log.e(
                    TAG,
                    "Style parsing failed."
                )
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(
                TAG,
                "Can't find style. Error: ",
                exception
            )
        }
    }

    private fun setupObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    mViewModel.viewState.collectLatest {
                        addManyMarker(it.listStory)
                    }
                }
                launch {
                    mViewModel.viewEffect.collectLatest {
                        observeEffect(it)
                    }
                }
            }
        }
    }

    private fun observeEffect(it: MapsViewEffect) {
        when (it) {
            is MapsViewEffect.OnError -> {
                showLoading(false)
                showErrorDialog(it.message)
            }

            MapsViewEffect.OnLoading -> {
                showLoading(true)
            }

            is MapsViewEffect.OnSuccess -> {
                showLoading(false)
            }
        }
    }

    private fun showErrorDialog(message: String) {
        val dialog = AlertDialogFragment()
        val bundle = Bundle()
        bundle.putString(
            "title",
            "Error"
        )
        bundle.putString(
            "message",
            message
        )
        bundle.putString(
            "positiveButtonTitle",
            "OK"
        )
        dialog.arguments = bundle
        dialog.show(
            supportFragmentManager,
            "error_dialog"
        )
        dialog.setOnPositiveButtonClickListener(object : AlertDialogFragment.ButtonClickListener {
            override fun onClick(dialog: DialogInterface, which: Int) {
                dialog.dismiss()
                mViewModel.getStories()
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.cpiMap.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun addManyMarker(listStory: List<StoryModel>) {
        Log.d(
            "MapsActivity",
            "addManyMarker: $listStory"
        )

        if (listStory.isNotEmpty()) {
            listStory.forEach {
                val latLng = LatLng(
                    it.lat,
                    it.lon
                )
                mMap.addMarker(
                    MarkerOptions().position(latLng).title(it.name).snippet(it.description)
                )
                boundsBuilder.include(latLng)
            }

            val bounds: LatLngBounds = boundsBuilder.build()
            mMap.animateCamera(
                CameraUpdateFactory.newLatLngBounds(
                    bounds,
                    resources.displayMetrics.widthPixels,
                    resources.displayMetrics.heightPixels,
                    300
                )
            )
        }else{
            Log.d(
                "MapsActivity",
                "addManyMarker: listStory is empty"
            )
        }
    }

    companion object {
        private const val TAG = "MapsActivity"
    }
}