package mx.ipn.escom.alcantarae.proymoviles.CarritoCompras

import android.app.Activity
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import mx.ipn.escom.alcantarae.proymoviles.ClientActivities.client_main
import mx.ipn.escom.alcantarae.proymoviles.apihandler.Producto

import mx.ipn.escom.alcantarae.proymoviles.databinding.FragmentShoppingCartBinding

class ShoppingCartAdapter(
    private val activity: Activity,
    private val products: MutableList<Producto>
) : RecyclerView.Adapter<ShoppingCartAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentShoppingCartBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = products[position]
        holder.idView.text = "$${item.precio}"
        holder.contentView.text = item.nombre_producto
        holder.btnElim.setOnClickListener{
            val mainActivity = activity as? client_main
            mainActivity?.removeProductFromCart(item)
        }
    }

    override fun getItemCount(): Int = products.size

    inner class ViewHolder(binding: FragmentShoppingCartBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemNumber
        val contentView: TextView = binding.content
        val btnElim: Button = binding.button2

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

    fun addProduct(product: Producto) {
        products.add(product)
        notifyDataSetChanged()
    }

    fun removeProduct(product: Producto) {
        products.remove(product)
        notifyDataSetChanged()
    }

    fun calculateTotal(): Double {
        var total = 0.0
        for (product in products) {
            total += product.precio
        }
        return total
    }

    fun getProductsList(): List<Producto> {
        return products.toList()
    }
}