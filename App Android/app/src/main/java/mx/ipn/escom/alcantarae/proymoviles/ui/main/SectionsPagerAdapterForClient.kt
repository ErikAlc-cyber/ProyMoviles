package mx.ipn.escom.alcantarae.proymoviles.ui.main

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import mx.ipn.escom.alcantarae.proymoviles.ClientActivities.Catalogo
import mx.ipn.escom.alcantarae.proymoviles.CarritoCompras.ShoppingCart
import mx.ipn.escom.alcantarae.proymoviles.apihandler.ApiServiceInterface
import pedidos

class SectionsPagerAdapterForClient(
    private val activity: Activity,
    fm: FragmentManager,
    private val apiService: ApiServiceInterface,
    private val user_id: Int
) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    companion object {
        private const val COUNT = 3
    }

    private val fragmentTitles = arrayOf("Historial", "Pedir", "Carrito de Compras")
    private val fragments: Array<() -> Fragment> = arrayOf(
        { pedidos(apiService, user_id) },
        { Catalogo(activity, apiService) },
        { ShoppingCart(activity,apiService, user_id) }
    )

    override fun getItem(position: Int): Fragment {
        return fragments[position]()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentTitles[position]
    }

    override fun getCount(): Int {
        return COUNT
    }
}
