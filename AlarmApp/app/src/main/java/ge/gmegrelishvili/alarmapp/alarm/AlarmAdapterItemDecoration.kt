package ge.gmegrelishvili.alarmapp.alarm

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ge.gmegrelishvili.alarmapp.R

class AlarmAdapterItemDecoration(private val context: Context) : RecyclerView.ItemDecoration() {

    private val gap = context.resources.getDimension(R.dimen.alarm_view_gap)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildAdapterPosition(view)
        if (parent.adapter != null && position != parent.adapter!!.itemCount - 1) {
            outRect.bottom = gap.toInt()
        }
    }
}