package ge.gmegrelishvili.memorygame

import android.widget.ImageView
import kotlin.random.Random

class GameManager6(
    imageViews: List<ImageView>,
    private var progressController: ProgressController
) : GameManager, CardViewListener {
    /* Public Field */
    val maxCards = 6
    val pairSize = 2
    val sleepTime = 1000.toLong()

    /* Private Field */
    private val images = arrayOf(
        R.drawable.image_1,
        R.drawable.image_2,
        R.drawable.image_3,
        R.drawable.image_4,
        R.drawable.image_5,
        R.drawable.image_6,
        R.drawable.image_7,
        R.drawable.image_8,
        R.drawable.image_9,
        R.drawable.image_10,
        R.drawable.image_11,
        R.drawable.image_12,
    )
    private val maxRandomNumbers = maxCards / pairSize
    private val random = Random(System.nanoTime())
    private val cardViews = mutableListOf<CardView>()
    private val openedViews = mutableListOf<CardView>()

    init {
        for (i in 0 until maxCards) {
            cardViews.add(MutableCardView(this, imageViews[i]))
        }
    }

    private fun generateRandomNumbers(): Set<Int> {
        val result = mutableSetOf<Int>()
        while (result.size != maxRandomNumbers) {
            result.add(random.nextInt(images.size))
        }
        return result
    }

    private fun buildRandomImageIds(): List<Int> {
        val randoms = generateRandomNumbers()
        val result = mutableListOf<Int>()
        for (i in 0 until pairSize) {
            result.addAll(randoms)
        }
        result.shuffle()
        return result
    }

    override fun start() {
        restart()
    }

    override fun restart() {
        val randomImageIds = buildRandomImageIds()
        for (i in 0 until maxCards) {
            cardViews[i].clearCard()
            cardViews[i].setFrontImageRes(images[randomImageIds[i]])
        }
        openedViews.clear()
        progressController.clearScores()
        progressController.clearHighlight()
    }

    private fun equalOpenedCards(): Boolean {
        for (i in 0 until openedViews.size - 1) {
            for (j in i + 1 until openedViews.size) {
                if (!openedViews[i].equalCard(openedViews[j])) {
                    return false
                }
            }
        }
        return true
    }

    override fun onClick(cardView: CardView) {
        progressController.clearHighlight()

        if (openedViews.size == pairSize) {
            for (card in openedViews) {
                card.closeCard()
            }
            openedViews.clear()
        }

        openedViews.add(cardView)

        if (openedViews.size == pairSize) {
            if (equalOpenedCards()) {
                progressController.success()
                for (card in openedViews) {
                    card.hideCard(sleepTime)
                }
                openedViews.clear()
            } else {
                progressController.fail()
            }
        }
    }
}