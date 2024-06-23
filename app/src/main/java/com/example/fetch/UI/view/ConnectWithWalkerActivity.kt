package com.example.fetch.UI.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationRequest
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.Debug.getLocation
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat.isLocationEnabled
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fetch.R
import com.example.fetch.databinding.ActivityConnectWithWalkerBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.util.Locale

class ConnectWithWalkerActivity : AppCompatActivity() {
    private val LOCATION_PERMISSION_REQUEST_CODE = 100
    private lateinit var binding: ActivityConnectWithWalkerBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) ||
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false)) {
            // Al menos uno de los permisos fue otorgado, obtener la ubicación
            obtenerUbicacion()
        } else {
            // Permisos denegados, manejar la situación (mostrar un mensaje, etc.)
            Toast.makeText(this, "Permisos de ubicación denegados", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
    private lateinit var mapView: MapView

    @SuppressLint("MissingPermission")
    private fun obtenerUbicacion() {
        val locationRequest = com.google.android.gms.location.LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
            .setWaitForAccurateLocation(true)
            .build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val location: Location? = locationResult.lastLocation
                if (location != null) {
                    // Aquí tienes la ubicación del usuario: location.latitude, location.longitude
                    Toast.makeText(this@ConnectWithWalkerActivity, "Latitud: ${location.latitude}, Longitud: ${location.longitude}", Toast.LENGTH_SHORT).show()
                    val geocoder = Geocoder(this@ConnectWithWalkerActivity, Locale.getDefault())
                    val addresses: List<Address> =
                        geocoder.getFromLocation(location.latitude, location.longitude, 1)!!
                    if (addresses.isNotEmpty()) {
                        val address = addresses[0]
                        val direccion = address.getAddressLine(0)
                        binding.txtLocation.text = direccion

                        //Inicializar mapa
                        val startPoint = GeoPoint(location.latitude, location.longitude)
                        mapView.controller.setCenter(startPoint)

                        //Agregar marcador
                        val startMarker = Marker(mapView)
                        startMarker.position = startPoint
                        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        startMarker.title = "Marker in Paris"
                        mapView.overlays.add(startMarker)
                    }
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())

    }

    override fun onRestart() {
        super.onRestart()
        Configuration.getInstance().load(this, getSharedPreferences("osmdroid", MODE_PRIVATE))
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        Configuration.getInstance().save(this, getSharedPreferences("osmdroid", MODE_PRIVATE))
        mapView.onPause()
    }

    private fun solicitarPermisosUbicacion() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED -> {
                // El permiso de ubicación fina ya está otorgado, obtener la ubicación
                obtenerUbicacion()
            }
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED -> {
                requestPermissionLauncher.launch(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE))
            }
            else -> {
                // Solicitar permisos directamente
                requestPermissionLauncher.launch(arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConnectWithWalkerBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        mapView = findViewById(R.id.map)
        mapView.controller.setZoom(15.0)
        mapView.setMultiTouchControls(true)
        solicitarPermisosUbicacion()

    }


}