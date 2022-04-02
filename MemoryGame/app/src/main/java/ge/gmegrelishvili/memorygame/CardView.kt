package ge.gmegrelishvili.memorygame

interface CardView {
    fun isOpen(): Boolean
    fun clearCard()
    fun openCard()
    fun closeCard()
    fun setFrontImageRes(frontImageRes: Int)
    fun hideCard(sleepTime: Long = 0)
    fun equalCard(other: Any?): Boolean
}