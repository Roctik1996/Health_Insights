package roctik.app.health.utils

interface ItemClickListener<in T> {
    fun onItemClick(item: T)
}