import android.accounts.NetworkErrorException
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mx.ipn.escom.alcantarae.proymoviles.ClientLists.ListHistory
import mx.ipn.escom.alcantarae.proymoviles.R
import mx.ipn.escom.alcantarae.proymoviles.apihandler.ApiServiceInterface
import mx.ipn.escom.alcantarae.proymoviles.detalles_pedido
import java.util.concurrent.TimeoutException

class pedidos(private val apiService: ApiServiceInterface, private val user_id: Int) : Fragment() {

    private var columnCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    private suspend fun handleLoginError(e: Exception) {
        withContext(Dispatchers.Main) {
            Log.e("Error API", "${e.message}")
            when (e) {
                is NetworkErrorException -> showError("Error de red: ${e.message}")
                is TimeoutException -> showError("Tiempo de espera agotado: ${e.message}")
                else -> showError("Error en la solicitud: ${e.message}")
            }
        }
    }

    private fun showError(message: String) {
        showAlert("Error", message)
    }

    private fun showAlert(title: String, message: String) {
        if (!isAdded) {
            return
        }

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("Ok", null)
        val dialog = builder.create()

        try {
            dialog.show()
        } catch (e: WindowManager.BadTokenException) {
            // Manejar excepción si es necesario
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pedidos_list, container, false)

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val pedidoList = withContext(Dispatchers.IO) {
                    apiService.historialUsuario(user_id)
                }
                if (view is RecyclerView) {
                    with(view) {
                        layoutManager = when {
                            columnCount <= 1 -> LinearLayoutManager(context)
                            else -> GridLayoutManager(context, columnCount)
                        }
                        adapter = ListHistory(pedidoList){ pedido ->
                            // Abre la nueva actividad aquí
                            val intent = Intent(requireContext(), detalles_pedido::class.java)
                            intent.putExtra("idPedido", pedido.id_pedido)
                            Log.d("Importante", "Se paso el valor: ${pedido.id_pedido}")
                            startActivity(intent)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.d("Error", "No se envió nada")
                e.printStackTrace()
                handleLoginError(e)
            }
        }
        return view
    }

    companion object {
        const val ARG_COLUMN_COUNT = "column-count"

        @JvmStatic
        fun newInstance(columnCount: Int, apiService: ApiServiceInterface, user_id: Int) =
            pedidos(apiService, user_id).apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}
