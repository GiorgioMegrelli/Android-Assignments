package ge.gmegrelishvili.memorygame

import android.widget.TextView
import android.content.Context
import androidx.core.content.ContextCompat

class DefaultProgressController(
    private var context: Context,
    private var successTView: TextView,
    private var failTView: TextView
) : ProgressController {

    private var successScore = 0
    private var failScore = 0

    init {
        updateScores()
    }

    private fun updateScores() {
        this.successTView.text = context.getString(R.string.success_text, this.successScore)
        this.failTView.text = context.getString(R.string.fail_text, this.failScore)
    }

    private fun updateColor(tView: TextView, color: Int) {
        tView.setTextColor(ContextCompat.getColor(context, color))
    }

    override fun success() {
        this.successScore++
        updateColor(this.successTView, R.color.header_font_success_color)
        updateScores()
    }

    override fun fail() {
        this.failScore++
        updateColor(this.failTView, R.color.header_font_fail_color)
        updateScores()
    }

    override fun clearHighlight() {
        updateColor(this.successTView, R.color.header_font_default_color)
        updateColor(this.failTView, R.color.header_font_default_color)
    }

    override fun clearScores() {
        successScore = 0
        failScore = 0
        updateScores()
    }
}