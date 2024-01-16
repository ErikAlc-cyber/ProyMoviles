package mx.ipn.escom.alcantarae.proymoviles.EmployLists

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LifecycleCoroutineScope
import com.google.gson.JsonParseException
import kotlinx.coroutines.launch
import mx.ipn.escom.alcantarae.proymoviles.MapsActivity
import mx.ipn.escom.alcantarae.proymoviles.apihandler.ApiService.apiService
import mx.ipn.escom.alcantarae.proymoviles.apihandler.Pedido

import mx.ipn.escom.alcantarae.proymoviles.placeholder.PlaceholderContent.PlaceholderItem
import mx.ipn.escom.alcantarae.proymoviles.databinding.FragmentOpenWorkBinding
import java.time.Duration

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class ListPedidos(
    private val activity: Activity,
    private val pedidos: List<Pedido>,
    private val repartidorId: Int,
    private val lifecycleScope: LifecycleCoroutineScope,
    private val clickListener: (Pedido) -> Unit
) : RecyclerView.Adapter<ListPedidos.ViewHolder>() {

    private val CODIGO_DE_SOLICITUD_MAPS = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentOpenWorkBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pedido = pedidos[position]
        holder.idView.text = "No. Pedido: ${pedido.id_pedido}"
        holder.state.text = "Estado: ${pedido.Estado}"
        holder.btnAgregar.setOnClickListener{
            updaterepartidor(pedido.id_pedido)
            updateUbicacion(pedido.id_pedido)
        }
        holder.itemView.setOnClickListener {
            clickListener(pedido)
        }
    }

    override fun getItemCount(): Int = pedidos.size

    inner class ViewHolder(binding: FragmentOpenWorkBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemNumber
        val state: TextView = binding.state2
        val btnAgregar: Button = binding.button
    }

    private fun updaterepartidor(idPedido: Int){
        lifecycleScope.launch {
            try {
                val response = apiService.actualizarRepartidor(idPedido, repartidorId)
                if (response.isSuccessful) {
                    Log.d("Actualización", "Repartidor actualizado correctamente")
                    Toast.makeText(activity, "El pedido se te fue asignado", Toast.LENGTH_SHORT).show()
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

    private fun updateUbicacion(idPedido: Int) {
        val mapIntent = Intent(activity, MapsActivity::class.java)
        mapIntent.putExtra("idPedido", idPedido)
        activity.startActivityForResult(mapIntent, CODIGO_DE_SOLICITUD_MAPS)
    }
}