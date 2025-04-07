package sara.converter.interfaces

interface OnTextToPdfInterface {
    fun onPDFCreationStarted()
    fun onPDFCreated(success: Boolean)
}
