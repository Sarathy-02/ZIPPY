package sara.converter.interfaces

interface EmptyStateChangeListener {
    fun setEmptyStateVisible()
    fun setEmptyStateInvisible()
    fun showNoPermissionsView()
    fun hideNoPermissionsView()
    fun filesPopulated()
}
