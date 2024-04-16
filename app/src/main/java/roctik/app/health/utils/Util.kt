package roctik.app.health.utils

import androidx.fragment.app.FragmentTransaction
import roctik.app.health.R


object Util {

    fun setFragmentAnimations(fragmentTransaction: FragmentTransaction) {
        fragmentTransaction.setCustomAnimations(
            R.anim.slide_in,
            R.anim.fade_out,
            R.anim.fade_in,
            R.anim.slide_out
        )
    }

    fun formatTimeFromSeconds(seconds: Double): String {
        val hours = (seconds / 3600).toInt()
        val minutes = ((seconds % 3600) / 60).toInt()
        return "${hours}hr ${minutes}min"
    }

}