package mx.ipn.escom.alcantarae.proymoviles.ClientLists

import android.app.Activity
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import mx.ipn.escom.alcantarae.proymoviles.ClientActivities.client_main
import mx.ipn.escom.alcantarae.proymoviles.MainActivity
import mx.ipn.escom.alcantarae.proymoviles.apihandler.Producto
import mx.ipn.escom.alcantarae.proymoviles.databinding.FragmentCatalogoBinding

class ListCatalogo(private val activity: Activity,
                   private val products: List<Producto>
) : RecyclerView.Adapter<ListCatalogo.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentCatalogoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Thread.sleep(100)
        val product = products[position]
        holder.contentView.text = "${product.nombre_producto}"
        holder.precioView.text = "$ ${product.precio}"
        holder.imageView.loadBase64Image(product.imagenes.toString())
        holder.addToCartButton.setOnClickListener {
            Log.d("Carrito de compras", "Se agrego ${product.nombre_producto} al carrito")
            val mainActivity = activity as? client_main
            mainActivity?.updateShoppingCart(product)
        }
    }

    override fun getItemCount(): Int = products.size

    inner class ViewHolder(binding: FragmentCatalogoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val contentView: TextView = binding.lstContent
        val precioView: TextView = binding.lstPrecio
        val imageView: ImageView = binding.imageView
        val addToCartButton: Button = binding.btnAddShop
    }

    private fun ImageView.loadBase64Image(base64: String) {
        try {
            val decodedBytes = Base64.decode(base64, Base64.DEFAULT)
            Glide.with(context)
                .asBitmap()
                .load(decodedBytes)
                .into(this)
        } catch (e: Exception) {
            Log.e("Error de imagen", "Error al cargar la imagen: ${e.message}")
        }
    }

}
