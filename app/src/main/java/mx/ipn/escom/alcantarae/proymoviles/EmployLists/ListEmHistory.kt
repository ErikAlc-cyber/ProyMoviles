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
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleCoroutineScope
import com.google.gson.JsonParseException
import kotlinx.coroutines.launch
import mx.ipn.escom.alcantarae.proymoviles.MapsActivity
import mx.ipn.escom.alcantarae.proymoviles.apihandler.ApiService
import mx.ipn.escom.alcantarae.proymoviles.apihandler.Pedido

import mx.ipn.escom.alcantarae.proymoviles.databinding.FragmentEmployHistoryBinding

class ListEmHistory(
        private val activity: Activity,
        private val pedidos: List<Pedido>,
        private val lifecycleScope: LifecycleCoroutineScope,
        private val clickListener: (Pedido) -> Unit
) : RecyclerView.Adapter<ListEmHistory.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

                return ViewHolder(
                        FragmentEmployHistoryBinding.inflate(
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

                        if (pedido.Estado.equals("Entregado")){
                                holder.btnEnd.isVisible = false
                        }

                        holder.btnEnd.setOnClickListener {
                                updaterPedido(pedido.id_pedido)
                        }

                        holder.itemView.setOnClickListener {
                                clickListener(pedido)
                        }
        }

        override fun getItemCount(): Int = pedidos.size

        inner class ViewHolder(binding: FragmentEmployHistoryBinding) :
                RecyclerView.ViewHolder(binding.root) {
                        val idView: TextView = binding.itemNumber2
                        val state: TextView = binding.state3
                        val btnEnd: Button = binding.button3
                }

        private fun updaterPedido(idPedido: Int) {
                lifecycleScope.launch {
                        try {
                                val response = ApiService.apiService.terminarPedido(
                                        idPedido,
                                )
                                if (response.isSuccessful) {
                                        Log.d(
                                                "Actualización",
                                                "Repartidor actualizado correctamente"
                                        )
                                        Toast.makeText(activity, "Pedido Completado con exito", Toast.LENGTH_SHORT).show()
                                } else {
                                        // Lógica para manejar una respuesta no exitosa
                                        Log.e(
                                                "ErrorS",
                                                "Error en la respuesta de la API: ${response.code()}"
                                        )
                                        val errorBody = response.errorBody()?.string()
                                        Log.e("ErrorS", "Error Body: $errorBody")
                                }
                        } catch (e: JsonParseException) {
                                // Manejar la excepción específica para JSON malformado
                                Log.e("ErrorS", "Error en el formato JSON de la respuesta")
                        } catch (e: Exception) {
                                // Manejar otras excepciones
                                Log.e("Error API", e.message.toString())
                        }
                }
        }
}
