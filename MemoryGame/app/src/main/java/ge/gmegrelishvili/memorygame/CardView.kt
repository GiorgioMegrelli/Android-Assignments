package ge.gmegrelishvili.memorygame

interface CardView {
    fun clearCard()
    fun openCard()
    fun closeCard()
    fun setFrontImageRes(frontImageRes: Int)
    fun hideCard()
    fun equalCardValues(other: CardView?): Boolean
}