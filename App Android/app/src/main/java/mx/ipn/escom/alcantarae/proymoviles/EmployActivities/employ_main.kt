package mx.ipn.escom.alcantarae.proymoviles.EmployActivities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.gson.JsonParseException
import kotlinx.coroutines.launch
import mx.ipn.escom.alcantarae.proymoviles.R
import mx.ipn.escom.alcantarae.proymoviles.apihandler.ApiService
import mx.ipn.escom.alcantarae.proymoviles.apihandler.ApiService.apiService
import mx.ipn.escom.alcantarae.proymoviles.databinding.ActivityEmployMainBinding
import mx.ipn.escom.alcantarae.proymoviles.ui.main.SectionsPagerAdapterForEmployee

class employ_main : AppCompatActivity() {

    private lateinit var binding: ActivityEmployMainBinding
    private lateinit var title: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userName = intent.getStringExtra("USERNAME")
        val userID = intent.getStringExtra("USER_ID")

        binding = ActivityEmployMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = findViewById(R.id.txtTitle)
        title.setText("Hola ${userName}")

        val apiService = ApiService.apiService

        val sectionsPagerAdapter = SectionsPagerAdapterForEmployee(this, supportFragmentManager, apiService, userID!!.toInt())
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val CODIGO_DE_SOLICITUD_MAPS = 1
        if (requestCode == CODIGO_DE_SOLICITUD_MAPS && resultCode == Activity.RESULT_OK) {
            data?.let {
                val latitud = it.getDoubleExtra("latitud", 0.0)
                val longitud = it.getDoubleExtra("longitud", 0.0)
                val idPedido = it.getIntExtra("idPedido", -1)

                // Haz lo que necesites con las coordenadas recibidas
                Log.d("ubicacion", "Coordenadas recibidas - Latitud: $latitud, Longitud: $longitud, IdPedido: $idPedido")
                updateUbicacionEnAPI(idPedido, latitud, longitud)
                // Puedes procesar los datos recibidos aquí
            }
        } else {
            Log.d("ubicacion", "No se recibieron coordenadas")
        }
    }
    private fun updateUbicacionEnAPI(idPedido: Int, latitud: Double, longitud: Double) {
        lifecycleScope.launch {
            try {
                val response = apiService.actualizar_localizacion(idPedido, latitud.toFloat(), longitud.toFloat())
                if (response.isSuccessful) {
                    Log.d("Actualización", "Ubicación actualizada correctamente")
                    Toast.makeText( this@employ_main, "Ubicación actualizada correctamente", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("ErrorS", "Error en la respuesta de la API: ${response.code()}")
                    val errorBody = response.errorBody()?.string()
                    Log.e("ErrorS", "Error Body: $errorBody")
                }
            } catch (e: JsonParseException) {
                Log.e("ErrorS", "Error en el formato JSON de la respuesta")
            } catch (e: Exception) {
                Log.e("Error API", e.message.toString())
            }
        }
    }
}