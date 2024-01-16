package mx.ipn.escom.alcantarae.proymoviles.EmployActivities

import android.accounts.NetworkErrorException
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mx.ipn.escom.alcantarae.proymoviles.ClientLists.ListHistory
import mx.ipn.escom.alcantarae.proymoviles.EmployLists.ListEmHistory
import mx.ipn.escom.alcantarae.proymoviles.R
import mx.ipn.escom.alcantarae.proymoviles.apihandler.ApiServiceInterface
import mx.ipn.escom.alcantarae.proymoviles.detalles_pedido
import java.util.concurrent.TimeoutException

/**
 * A fragment representing a list of Items.
 */
class employ_history(private val activity: Activity, private val apiService: ApiServiceInterface, private val user_id: Int) : Fragment() {

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

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_employ_history_list, container, false)

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val pedidoList = withContext(Dispatchers.IO) {
                    apiService.historialRepartidor(user_id)
                }

                if (view is RecyclerView) {
                    with(view) {
                        layoutManager = when {
                            columnCount <= 1 -> LinearLayoutManager(context)
                            else -> GridLayoutManager(context, columnCount)
                        }
                        adapter = ListEmHistory(activity, pedidoList, lifecycleScope){ pedido ->
                            // Abre la nueva actividad aquí
                            val intent = Intent(requireContext(), detalles_pedido::class.java)
                            intent.putExtra("idPedido", pedido.id_pedido)
                            startActivity(intent)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.d("Error", "No se envio nada")
                e.printStackTrace()
                handleLoginError(e)
            }
        }
        return view
    }

    companion object {

        const val ARG_COLUMN_COUNT = "column-count"

        @JvmStatic
        fun newInstance(activity: Activity, columnCount: Int, apiService: ApiServiceInterface, user_id: Int) =
            employ_history(activity, apiService, user_id).apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}