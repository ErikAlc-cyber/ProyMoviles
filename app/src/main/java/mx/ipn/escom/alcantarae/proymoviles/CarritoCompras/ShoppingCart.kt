package mx.ipn.escom.alcantarae.proymoviles.CarritoCompras

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import mx.ipn.escom.alcantarae.proymoviles.R
import mx.ipn.escom.alcantarae.proymoviles.apihandler.ApiServiceInterface
import mx.ipn.escom.alcantarae.proymoviles.apihandler.Producto

/**
 * A fragment representing a list of Items.
 */
class ShoppingCart(private val activity: Activity, private val apiService: ApiServiceInterface, private val user_id: Int) : Fragment() {

    private var columnCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("Debug", "ShoppingCart fragment created with tag: ${tag}")

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_shopping_cart_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = ShoppingCartAdapter(requireActivity(), mutableListOf())
            }
        }
        updateTotal()
        return view
    }

    fun updateShoppingCart(product: Producto) {
        val adapter = (view as? RecyclerView)?.adapter as? ShoppingCartAdapter
        adapter?.addProduct(product)
    }

    fun removeProductFromCart(product: Producto) {
        val adapter = (view as? RecyclerView)?.adapter as? ShoppingCartAdapter
        adapter?.removeProduct(product)
        updateTotal()
    }

    private fun updateTotal() {
        val adapter = (view as? RecyclerView)?.adapter as? ShoppingCartAdapter
        val total = adapter?.calculateTotal() ?: 0.0

        Toast.makeText( requireActivity(), "Total: $${String.format("%.2f", total)}", Toast.LENGTH_SHORT).show()
    }


    companion object {

        const val ARG_COLUMN_COUNT = "column-count"

        @JvmStatic
        fun newInstance(columnCount: Int, activity: Activity, apiService: ApiServiceInterface, user_id: Int) =
            ShoppingCart(activity , apiService, user_id).apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}