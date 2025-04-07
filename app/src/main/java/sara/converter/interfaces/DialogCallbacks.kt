package sara.converter.interfaces

interface DialogCallbacks {
    fun onPositiveButtonClick()
    fun onNegativeButtonClick()

    @Suppress("EmptyMethod")
    fun onNeutralButtonClick()
}
