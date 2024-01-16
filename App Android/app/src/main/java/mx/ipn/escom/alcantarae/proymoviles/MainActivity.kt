package mx.ipn.escom.alcantarae.proymoviles

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mx.ipn.escom.alcantarae.proymoviles.ClientActivities.client_main
import mx.ipn.escom.alcantarae.proymoviles.EmployActivities.employ_main
import mx.ipn.escom.alcantarae.proymoviles.apihandler.ApiService
import mx.ipn.escom.alcantarae.proymoviles.apihandler.verUserResponse
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var editName: EditText
    private lateinit var editContra: EditText
    private lateinit var btnStart: Button
    private val scope = MainScope()

    object NavigationRouter {
        fun redirectToRoleActivity(context: Context, role: String, nombre: String, id: String) {
            val intent = when (role) {
                "cliente" -> Intent(context, client_main::class.java)
                "empleado" -> Intent(context, employ_main::class.java)
                else -> Intent(context, MainActivity::class.java)
            }
            if (nombre.isNotEmpty()) {
                intent.putExtra("USERNAME", nombre)
                intent.putExtra("USER_ID", id)
            }
            context.startActivity(intent)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        editName = findViewById(R.id.idNombre)
        editContra = findViewById(R.id.idContra)
        btnStart = findViewById(R.id.btnStart)
        btnStart.setOnClickListener { performLogin() }
    }

    private fun performLogin() {
        scope.launch {
            try {
                val apiService = ApiService.apiService
                val response = withContext(Dispatchers.IO) {
                    apiService.verificarUsuario(editName.text.toString(), editContra.text.toString())
                }
                handleLoginResponse(response)
            } catch (e: Exception) {
                handleLoginError(e)
            }
        }
    }

    private suspend fun handleLoginResponse(response: Response<verUserResponse>) {
        withContext(Dispatchers.Main) {
            when {
                response.isSuccessful -> {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        redirectToRole(loginResponse.rol, loginResponse.usuario, loginResponse.id.toString())
                    } else {
                        showError("Credenciales incorrectas")
                    }
                }
                response.code() == 401 -> showError("Credenciales incorrectas")
                response.code() == 500 -> showError("Error interno del servidor")
                response.code() == 404 -> showError("Usuario no encontrado")
                else -> showError("Respuesta no exitosa de la API: ${response.code()}")
            }
        }
    }

    private fun redirectToRole(role: String, nombre: String, id: String) {
        NavigationRouter.redirectToRoleActivity(this, role, nombre ?: "Usuario", id)
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

    fun redirectToOtherActivity(view: View) {
        val intent = Intent(this, Register_activity::class.java)
        startActivity(intent)
    }

}
