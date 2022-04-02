package ge.gmegrelishvili.memorygame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cards = arrayOf(
            R.id.image_r0_c0,
            R.id.image_r0_c1,
            R.id.image_r1_c0,
            R.id.image_r1_c1,
            R.id.image_r2_c0,
            R.id.image_r2_c1,
        )

        val progressController = DefaultProgressController(
            this,
            findViewById(R.id.success_score),
            findViewById(R.id.fail_score)
        )
        val gameManager = GameManager6(cards.map { findViewById(it) }, progressController)
        gameManager.start()

        val restartButton = findViewById<Button>(R.id.restart_button)
        restartButton.setOnClickListener {
            gameManager.restart()
        }
    }
}