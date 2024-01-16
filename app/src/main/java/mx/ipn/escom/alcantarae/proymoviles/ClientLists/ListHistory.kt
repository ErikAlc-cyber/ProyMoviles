package mx.ipn.escom.alcantarae.proymoviles.ClientLists

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import mx.ipn.escom.alcantarae.proymoviles.apihandler.Pedido

import mx.ipn.escom.alcantarae.proymoviles.databinding.FragmentPedidosBinding


class ListHistory(
    private val pedidos: List<Pedido>,
    private val clickListener: (Pedido) -> Unit
) : RecyclerView.Adapter<ListHistory.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentPedidosBinding.inflate(
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
        holder.itemView.setOnClickListener {
            clickListener(pedido)
        }
    }

    override fun getItemCount(): Int = pedidos.size

    inner class ViewHolder(binding: FragmentPedidosBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemNumber
        val state: TextView = binding.state
    }

}