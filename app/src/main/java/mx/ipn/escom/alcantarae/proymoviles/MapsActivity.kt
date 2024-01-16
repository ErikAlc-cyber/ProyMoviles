package mx.ipn.escom.alcantarae.proymoviles

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.CancellationTokenSource
import mx.ipn.escom.alcantarae.proymoviles.databinding.ActivityMapsBinding
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.Marker

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var permissionsGranted = false
    private lateinit var marker: Marker
    private lateinit var btnRecargar: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializa fusedLocationClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        checkLocationPermission()

    }

    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request permissions if not granted
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                200
            )
        } else {
            permissionsGranted = true
            loadMap()
        }
    }

    @SuppressLint("MissingPermission")
    private fun loadMap() {
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY,
            CancellationTokenSource().token)
            .addOnSuccessListener { fetchedLocation ->
                val sydney = LatLng(fetchedLocation.latitude, fetchedLocation.longitude)
                marker = mMap.addMarker(MarkerOptions().position(sydney).title("Tu localización").draggable(true))!!
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
                btnRecargar = findViewById(R.id.btnRecargar)
                btnRecargar.setOnClickListener {
                    obtenerCoordenadas()
                }
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 200) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, set the flag and reload the map
                permissionsGranted = true
                loadMap()
            } else {
                // Permission denied, handle it as needed (show a message, etc.)
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (permissionsGranted) {
            loadMap()
        }
    }

    private fun obtenerCoordenadas() {
        // Verifica que el marcador no sea nulo
        if (::marker.isInitialized) {
            // Obtiene las coordenadas del marcador actual
            val latitud = marker.position.latitude
            val longitud = marker.position.longitude

            // Haz lo que necesites con las coordenadas
            Log.d("ubicacion", "X: $latitud y Y: $longitud")
            val resultIntent = Intent()
            resultIntent.putExtra("latitud", latitud)
            resultIntent.putExtra("longitud", longitud)

            // Obtén el idPedido del Intent original
            val idPedido = intent.getIntExtra("idPedido", -1)
            resultIntent.putExtra("idPedido", idPedido)

            // Establece el resultado y finaliza la actividad
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        } else {
            // Manejar el caso donde el marcador no está inicializado
            Log.e("ubicacion", "El marcador no está inicializado")
            Toast.makeText(this, "Error al obtener coordenadas", Toast.LENGTH_SHORT).show()
        }
    }
}