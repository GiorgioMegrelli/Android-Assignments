package ge.gmegrelishvili.memorygame

import android.view.View
import android.widget.ImageView

class MutableCardView(
    private val cardViewListener: CardViewListener,
    private val imageView: ImageView
) : CardView {

    companion object {
        const val BackgroundImage = R.drawable.card_background
    }

    private var opened = false
    private var hidden = false
    private var frontImageRes: Int? = null

    init {
        imageView.setOnClickListener {
            if (!hidden) {
                openCard()
                cardViewListener.onClick(this)
            }
        }
    }

    private fun updateImage() {
        val imageSrc: Int =
            if (opened && frontImageRes != null) frontImageRes!!
            else BackgroundImage
        imageView.setImageResource(imageSrc)
    }

    private fun turnCard(open: Boolean) {
        if (opened.xor(open)) {
            opened = open
            updateImage()
        }
    }

    override fun clearCard() {
        hidden = false
        frontImageRes = null
        imageView.visibility = View.VISIBLE
        turnCard(false)
    }

    override fun openCard() {
        turnCard(true)
    }

    override fun closeCard() {
        turnCard(false)
    }

    override fun setFrontImageRes(frontImageRes: Int) {
        this.frontImageRes = frontImageRes
        updateImage()
    }

    override fun hideCard() {
        imageView.visibility = View.INVISIBLE
    }

    override fun equalCardValues(other: CardView?): Boolean {
        if (other == null || other !is MutableCardView) {
            return false
        }
        return frontImageRes == other.frontImageRes
    }
}