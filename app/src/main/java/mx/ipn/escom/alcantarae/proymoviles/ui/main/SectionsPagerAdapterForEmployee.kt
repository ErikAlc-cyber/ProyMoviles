package mx.ipn.escom.alcantarae.proymoviles.ui.main

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import mx.ipn.escom.alcantarae.proymoviles.EmployActivities.employ_history
import mx.ipn.escom.alcantarae.proymoviles.EmployActivities.open_work
import mx.ipn.escom.alcantarae.proymoviles.apihandler.ApiServiceInterface

class SectionsPagerAdapterForEmployee(
    private val activity: Activity,
    fm: FragmentManager,
    private val apiService: ApiServiceInterface,
    private val user_id: Int
) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> employ_history(activity,apiService, user_id)
            1 -> open_work(activity, apiService, user_id)
            else -> throw IllegalArgumentException("Posicion invalida: $position")
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Historial"
            1 -> "Pedidos Abiertos"
            else -> null
        }
    }

    override fun getCount(): Int {
        return 2
    }
}
