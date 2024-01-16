package mx.ipn.escom.alcantarae.proymoviles.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            //0 -> pedidos(user_id)
            //1 -> //TODO Cambiar Catalogo
            else -> throw IllegalArgumentException("Posicion invalida: $position")
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Historial"
            1 -> "Pedir"
            else -> null
        }
    }

    override fun getCount(): Int {
        return 3
    }
}