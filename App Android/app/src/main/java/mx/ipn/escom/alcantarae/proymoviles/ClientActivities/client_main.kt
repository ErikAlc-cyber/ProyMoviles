package mx.ipn.escom.alcantarae.proymoviles.ClientActivities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonParseException
import kotlinx.coroutines.launch
import mx.ipn.escom.alcantarae.proymoviles.MapsActivity
import mx.ipn.escom.alcantarae.proymoviles.R
import mx.ipn.escom.alcantarae.proymoviles.CarritoCompras.ShoppingCart
import mx.ipn.escom.alcantarae.proymoviles.CarritoCompras.ShoppingCartAdapter
import mx.ipn.escom.alcantarae.proymoviles.apihandler.ApiService
import mx.ipn.escom.alcantarae.proymoviles.apihandler.Producto
import mx.ipn.escom.alcantarae.proymoviles.databinding.ActivityClientMainBinding
import mx.ipn.escom.alcantarae.proymoviles.ui.main.SectionsPagerAdapterForClient
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class client_main : AppCompatActivity() {

    private lateinit var binding: ActivityClientMainBinding
    private lateinit var title: TextView
    val CODIGO_DE_SOLICITUD_MAPS = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userName = intent.getStringExtra("USERNAME")
        val userID = intent.getStringExtra("USER_ID")

        binding = ActivityClientMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = findViewById(R.id.txtTitle)
        title.setText("Hola ${userName}")


        val apiService = ApiService.apiService

        val sectionsPagerAdapter = SectionsPagerAdapterForClient(this, supportFragmentManager, apiService, userID!!.toInt())
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)
        val fab: FloatingActionButton = binding.btnCustomClient


        fab.setOnClickListener { view ->
            val total_carrito: Double=obtenerTotalCarrito()
            if (total_carrito <= 0){
                Toast.makeText( this@client_main, "Aun no hay nada en tu carrito", Toast.LENGTH_SHORT).show()
            }else {
                lifecycleScope.launch {
                    try {
                        val productos: String = obtenerIdsProductosEnCarrito()
                        val idPedido: Int = registrarPedido(userID.toInt(), productos)

                        val mapIntent = Intent(this@client_main, MapsActivity::class.java)
                        mapIntent.putExtra("idPedido", idPedido)
                        this@client_main.startActivityForResult(mapIntent, CODIGO_DE_SOLICITUD_MAPS)
                    } catch (e: Exception) {
                        Log.e("Error en coroutines", e.message.toString())
                    }
                }
            }
        }
    }

    private suspend fun registrarPedido(userId: Int, productos: String): Int {
        try {
            val url = "/pedidos/$userId/1/$productos"
            val response = ApiService.apiService.nuevoPedido(url)
            if (response.isSuccessful) {
                Toast.makeText(this@client_main, "Pedido expedido", Toast.LENGTH_SHORT).show()
                // Extraer el idPedido del cuerpo de la respuesta o de donde corresponda en tu API
                val idPedido = response.body()?: -1
                return idPedido
            } else {
                Log.e("Error API", "Error en la respuesta de la API: ${response.code()}")
                val errorBody = response.errorBody()?.string()
                Log.e("Error API", "Error Body: $errorBody")
            }
        } catch (e: Exception) {
            Log.e("Error API", e.message.toString())
        }
        return -1 // Otra indicación de error, puedes ajustar según necesites
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

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
                val response = ApiService.apiService.actualizar_localizacionC(idPedido, latitud.toFloat(), longitud.toFloat())
                if (response.isSuccessful) {
                    Log.d("Actualización", "Ubicación actualizada correctamente")
                    Toast.makeText( this@client_main, "Ubicación actualizada correctamente", Toast.LENGTH_SHORT).show()
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

    fun updateShoppingCart(product: Producto) {
        val shoppingCartFragment = supportFragmentManager.findFragmentByTag("android:switcher:2131231261:2")
        if (shoppingCartFragment is ShoppingCart) {
            shoppingCartFragment.updateShoppingCart(product)
        }
    }

    fun removeProductFromCart(product: Producto) {
        val shoppingCartFragment = supportFragmentManager.findFragmentByTag("android:switcher:2131231261:2")
        if (shoppingCartFragment is ShoppingCart) {
            shoppingCartFragment.removeProductFromCart(product)
        }
    }

    fun obtenerTotalCarrito(): Double {
        val shoppingCartFragment = supportFragmentManager.findFragmentByTag("android:switcher:2131231261:2")
        if (shoppingCartFragment is ShoppingCart) {
            val adapter = (shoppingCartFragment.view as? RecyclerView)?.adapter as? ShoppingCartAdapter
            return adapter?.calculateTotal() ?: 0.0
        }
        return 0.0
    }

    fun obtenerIdsProductosEnCarrito(): String {
        val shoppingCartFragment = supportFragmentManager.findFragmentByTag("android:switcher:2131231261:2")
        if (shoppingCartFragment is ShoppingCart) {
            val adapter = (shoppingCartFragment.view as? RecyclerView)?.adapter as? ShoppingCartAdapter
            val productList = adapter?.getProductsList() ?: emptyList()

            // Construir el string con los IDs
            val idsString = productList.joinToString("/") { it.id.toString() }

            val formattedString = "/$idsString"
                .replace("\\/+".toRegex(), "/") // Eliminar barras diagonales repetidas
                .replace("^\\/".toRegex(), "") // Eliminar barra diagonal al principio, si existe
                .replace("\\/$".toRegex(), "") // Eliminar barra diagonal al final, si existe

            return formattedString
        }
        return ""
    }

}