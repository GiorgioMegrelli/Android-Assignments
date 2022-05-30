package ge.gmegrelishvili.todoapp.notes

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.contract.ActivityResultContract

class NoteActivityResultContract : ActivityResultContract<String, String>() {
    override fun createIntent(context: Context, input: String?): Intent {
        return Intent(context, NoteActivity::class.java).apply {
            putExtra(UpdateResultKey, input)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): String {
        Log.i("MyLog", intent.toString())
        return intent?.getStringExtra(UpdateResultKey) ?: UpdateResultFalse
    }

    companion object {
        const val UpdateResultKey = "UPDATE_RESULT_KEY"
        const val UpdateResultTrue = true.toString()
        const val UpdateResultFalse = false.toString()
    }
}