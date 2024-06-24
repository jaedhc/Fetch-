package com.example.fetch.UI.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fetch.R
import com.example.fetch.UI.adapter.UserAdapter
import com.example.fetch.UI.viewmodel.ConnectViewModel
import com.example.fetch.data.model.User
import com.example.fetch.databinding.ActivityConnectWithWalkerBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import java.io.IOException
import java.util.Locale

class ConnectWithWalkerActivity : AppCompatActivity() {
    private val LOCATION_PERMISSION_REQUEST_CODE = 100
    private lateinit var binding: ActivityConnectWithWalkerBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var connectViewModel: ConnectViewModel
    private lateinit var userAdapter: UserAdapter
    private var lastGeoPoint: GeoPoint? = null
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var lastMarker: Marker? = null

    private lateinit var polyline: Polyline
    private val client = OkHttpClient()

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

                    if(location.latitude != 0.00 && location.longitude != 0.00){
                        connectViewModel.changeLocation(location.latitude, location.longitude)
                        connectViewModel.getUsers(location.latitude, location.longitude)
                    }

                    val geocoder = Geocoder(this@ConnectWithWalkerActivity, Locale.getDefault())
                    val addresses: List<Address> =
                        geocoder.getFromLocation(location.latitude, location.longitude, 1)!!
                    if (addresses.isNotEmpty()) {
                        val address = addresses[0]
                        val direccion = address.getAddressLine(0)
                        binding.txtLocation.text = "From: $direccion"

                        //Inicializar mapa
                        val startPoint = GeoPoint(location.latitude, location.longitude)
                        mapView.controller.setCenter(startPoint)

                        //Agregar marcador
                        val startMarker = Marker(mapView)
                        startMarker.position = startPoint
                        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        mapView.overlays.add(startMarker)
                    }

                    latitude = location.latitude
                    longitude = location.longitude
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

        connectViewModel = ViewModelProvider(this).get(ConnectViewModel::class.java)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        mapView = findViewById(R.id.map)
        mapView.controller.setZoom(15.0)
        mapView.setMultiTouchControls(true)
        solicitarPermisosUbicacion()

        userAdapter = UserAdapter()

        connectViewModel.users.observe(this) { users ->

            userAdapter.setDogList(users)

            binding.rvUsers.apply {
                adapter = userAdapter
                layoutManager = LinearLayoutManager(this@ConnectWithWalkerActivity)
            }

        }

        userAdapter.setOnUserClickListener(object: UserAdapter.OnUserClickListener{
            override fun onUserSelected(position: Int, user: List<User>) {
                connectViewModel.schedule(
                    GeoPoint(latitude, longitude),
                    lastGeoPoint!!,
                    binding.txtLocation.text.toString(),
                    binding.txtLocationTo.text.toString(),
                    user[position].id)
            }
        })

        polyline = Polyline().apply {
            outlinePaint.color = Color.BLUE
            outlinePaint.strokeWidth = 8f
        }
        mapView.overlays.add(polyline)

        val mapEventsReceiver = object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint): Boolean {
                // This method is called when the map is clicked
                val latitude = p.latitude
                val longitude = p.longitude

                if(lastMarker != null){
                    mapView.overlays.remove(lastMarker)
                }

                val endMarker = Marker(mapView)
                endMarker.position = GeoPoint(latitude, longitude)
                endMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                mapView.overlays.add(endMarker)

                lastGeoPoint = GeoPoint(latitude, longitude)
                lastMarker = endMarker

                requestRoute(lastGeoPoint!!, GeoPoint(latitude, longitude))

                val geocoder = Geocoder(this@ConnectWithWalkerActivity, Locale.getDefault())
                val addresses: List<Address> =
                    geocoder.getFromLocation(latitude, longitude, 1)!!

                if (addresses.isNotEmpty()) {
                    val address = addresses[0]
                    val direccion = address.getAddressLine(0)
                    binding.txtLocationTo.text = "To: $direccion"

                }

                return true
            }

            override fun longPressHelper(p: GeoPoint): Boolean {
                // This method is called when the map is long pressed
                return false
            }
        }

        val eventsOverlay = MapEventsOverlay(mapEventsReceiver)
        mapView.overlays.add(eventsOverlay)

    }

    private fun requestRoute(startPoint: GeoPoint, endPoint: GeoPoint) {
        val url = "http://router.project-osrm.org/route/v1/driving/${startPoint.longitude},${startPoint.latitude};${endPoint.longitude},${endPoint.latitude}?overview=full&geometries=geojson"

        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@ConnectWithWalkerActivity, "Error fetching route: ${e.message}", Toast.LENGTH_SHORT).show()
                    binding.textView4.text = "No route found ${e.message}"
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    runOnUiThread {
                        Toast.makeText(this@ConnectWithWalkerActivity, "Error fetching route: ${response.message}", Toast.LENGTH_SHORT).show()
                    }
                    return
                }

                val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                val adapter = moshi.adapter(OSRMResponse::class.java)
                val osrmResponse = adapter.fromJson(response.body!!.string())

                runOnUiThread {
                    if (osrmResponse != null && osrmResponse.routes.isNotEmpty()) {
                        val route = osrmResponse.routes[0]
                        val polylinePoints = route.geometry.coordinates.map { coord ->
                            GeoPoint(coord[1], coord[0])
                        }

                        // Update the polyline with the route points
                        polyline.setPoints(polylinePoints)
                        mapView.invalidate()
                    } else {
                        Toast.makeText(this@ConnectWithWalkerActivity, "No route found", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

}

data class OSRMResponse(
    val routes: List<Route>
)

data class Route(
    val geometry: Geometry
)

data class Geometry(
    val coordinates: List<List<Double>>
)