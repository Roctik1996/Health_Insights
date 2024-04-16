package roctik.app.health.di

interface Preferences {
    fun getStartDate(): Long
    fun setStartDate(startDate: Long)
    fun isPermissionDenied(): Boolean
    fun setPermissionDenied(state: Boolean)
}