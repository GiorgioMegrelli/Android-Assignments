package ge.gmegrelishvili.memorygame

import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView

class MutableCardView(
    cardViewListener: CardViewListener,
    private var imageView: ImageView
) : CardView {

    companion object {
        const val BackgroundImage = R.drawable.card_background

        class CardViewHider(private var imageView: ImageView) : Runnable {
            override fun run() {
                imageView.visibility = View.INVISIBLE
            }
        }
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

    override fun isOpen(): Boolean {
        return opened
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

    override fun hideCard(sleepTime: Long) {
        if (sleepTime <= 0) {
            imageView.visibility = View.INVISIBLE
        } else {
            hidden = true
            Handler(Looper.getMainLooper()).postDelayed({
                imageView.visibility = View.INVISIBLE
            }, sleepTime)
        }
    }

    override fun equalCard(other: Any?): Boolean {
        if (other == null || other !is MutableCardView) {
            return false
        }
        return frontImageRes == other.frontImageRes
    }
}