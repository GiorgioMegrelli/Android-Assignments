package ge.gmegrelishvili.todoapp.notes

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import ge.gmegrelishvili.todoapp.R
import ge.gmegrelishvili.todoapp.notes.adapter.ListItemAdapter

class NoteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        val resultId = parseInt(intent.getStringExtra(IdResultKey))

        findViewById<ImageView>(R.id.back_button).setOnClickListener {
            onBackPressed()
        }
        findViewById<ImageView>(R.id.pin_button).setOnClickListener {
            // Pass
        }

        setUpAdapters()
    }

    private fun setUpAdapters() {
        val checkedItemsAdapter = ListItemAdapter()
        val uncheckedItemsAdapter = ListItemAdapter()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // Pass
    }

    private fun parseInt(str: String?): Int? {
        if (str == NoId) {
            return null
        }
        return try {
            str?.toInt()
        } catch (e: Exception) {
            null
        }
    }

    companion object {
        const val IdResultKey = "ID_RESULT_KEY"
        const val NoId = ""
    }
}