package mx.ipn.escom.alcantarae.proymoviles

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mx.ipn.escom.alcantarae.proymoviles.apihandler.ApiService
import mx.ipn.escom.alcantarae.proymoviles.apihandler.Pedido
import mx.ipn.escom.alcantarae.proymoviles.apihandler.verUserResponse
import retrofit2.Response

class detalles_pedido : AppCompatActivity() {

    private lateinit var txtPedi: MaterialTextView
    private lateinit var txtUsr: MaterialTextView
    private lateinit var txtIdEmple: MaterialTextView
    private lateinit var txtDesc: MaterialTextView
    private lateinit var txtEstado: MaterialTextView
    private val scope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalles_pedido)

        val idPedido = intent.getIntExtra("idPedido", 0)

        scope.launch {
            try {
                val apiService = ApiService.apiService
                val response = withContext(Dispatchers.IO) {
                    apiService.infoPedido(idPedido)
                }
                handleResponse(response)
            } catch (e: Exception) {
                handleLoginError(e)
            }
        }
    }

    private suspend fun handleResponse(response: Response<Pedido>) {

        txtPedi = findViewById(R.id.txtIdPedi)
        txtUsr = findViewById(R.id.txtIdUsr)
        txtIdEmple = findViewById(R.id.txtIdEmpl)
        txtDesc = findViewById(R.id.txtDesc)
        txtEstado = findViewById(R.id.txtEsta)

        var btnOk: Button = findViewById(R.id.btnOk)
        btnOk.setOnClickListener { finish() }

        var btnCan: Button = findViewById(R.id.btnCancelar)
        btnCan.setOnClickListener {
            finish()
        }

        withContext(Dispatchers.Main) {
            when {
                response.isSuccessful -> {
                    val pedido = response.body()
                    txtPedi.setText("Pedido #${pedido?.id_pedido}")
                    txtEstado.setText("Estado: ${pedido?.Estado}")
                    txtUsr.setText("Coordenadas cliente: ${pedido?.latitudC} y ${pedido?.longitudC}")
                    txtIdEmple.setText("Coordenadas repartidor: ${pedido?.latitud} y ${pedido?.longitud}")
                    txtDesc.setText(pedido!!.productos.replace(",", "\n"))
                }
                response.code() == 401 -> showError("Credenciales incorrectas")
                response.code() == 500 -> showError("Error interno del servidor")
                response.code() == 404 -> showError("Pedido no encontrado")
                else -> showError("Respuesta no exitosa de la API: ${response.code()}")
            }
        }
    }

    private suspend fun handleLoginError(e: Exception) {
        // Error en la solicitud, maneja según tus necesidades
        withContext(Dispatchers.Main) {
            showError("Error en la solicitud: ${e.message}")
        }
    }

    private fun showError(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(message)
        builder.setPositiveButton("OK", null)
        val dialog = builder.create()
        dialog.show()
    }
}