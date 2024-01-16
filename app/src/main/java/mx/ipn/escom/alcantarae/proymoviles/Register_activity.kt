package mx.ipn.escom.alcantarae.proymoviles

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mx.ipn.escom.alcantarae.proymoviles.apihandler.ApiService
import retrofit2.Response

class Register_activity : AppCompatActivity() {
    private lateinit var editName: EditText
    private lateinit var editPassReg: EditText
    private lateinit var editPasswConfr: EditText
    private lateinit var chkbClient: CheckBox
    private lateinit var chkbEmplo: CheckBox
    private lateinit var radioGroup: RadioGroup
    private lateinit var btnRegis: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        editName = findViewById(R.id.editName)
        editPassReg = findViewById(R.id.editPassReg)
        editPasswConfr = findViewById(R.id.editPasswConfr)
        chkbClient = findViewById(R.id.chkbClient)
        chkbEmplo = findViewById(R.id.chkbEmplo)
        radioGroup = findViewById(R.id.radioGroup)
        btnRegis = findViewById(R.id.btnRegister)
        btnRegis.setOnClickListener { register() }
    }

    private fun register() {
        val nombre = editName.text.toString()
        val contra = editPassReg.text.toString()
        val verContra = editPasswConfr.text.toString()
        val empleado = chkbEmplo.isChecked
        val cliente = chkbClient.isChecked

        try {
            validarEntrada(nombre, contra, verContra, empleado, cliente)
            realizarRegistroUsuario(nombre, contra, empleado, cliente)
        } catch (e: Exception) {
            mostrarMensaje(this, e.message ?: "Error desconocido")
        }
    }

    private fun validarEntrada(nombre: String, contra: String, verContra: String, empleado: Boolean, cliente: Boolean) {
        if (nombre.isBlank() || contra.isBlank() || verContra.isBlank()) {
            throw IllegalArgumentException("Ningún campo puede estar vacío")
        }

        if (contra != verContra) {
            throw IllegalArgumentException("Las contraseñas no coinciden")
        }

        if (!empleado && !cliente) {
            throw IllegalArgumentException("Selecciona al menos un rol (empleado o cliente)")
        }
    }

    private fun realizarRegistroUsuario(nombre: String, contra: String, empleado: Boolean, cliente: Boolean) {
        val rol = when {
            empleado -> "empleado"
            cliente -> "cliente"
            else -> null
        }

        lifecycleScope.launch {
            try {
                val response = ApiService.apiService.registrarUsuario(nombre, contra, rol!!)
                Log.d("Response", response.toString())
                handleRegisterResponse(response)
            } catch (e: Exception) {
                Log.e("ErrorS", e.message.toString())
                showError("Error al realizar la solicitud: ${e.message}")
            }
        }
    }

    private suspend fun handleRegisterResponse(response: Response<Int>) {
        withContext(Dispatchers.Main) {
            when {
                response.isSuccessful -> {
                    val userId = response.body()
                    showMsg("Tu id de usuario es: $userId") {
                        redirect()
                    }
                }
                response.code() == 500 -> showError("Error interno del servidor")
                else -> showError("Respuesta no exitosa de la API: ${response.code()}")
            }
        }
    }

    private fun showError(message: String) {
        showAlert("Error", message)
    }

    private fun showMsg(message: String, onOkClick: () -> Unit) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Importante")
        builder.setMessage(message)
        builder.setPositiveButton("OK") { _, _ ->
            // Llamamos a la función proporcionada cuando se hace clic en "OK"
            onOkClick()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun showAlert(title: String, message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("Ok", null)
        val dialog = builder.create()
        dialog.show()
    }

    private fun mostrarMensaje(context: Context, mensaje: String) {
        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
    }

    private fun redirect() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
