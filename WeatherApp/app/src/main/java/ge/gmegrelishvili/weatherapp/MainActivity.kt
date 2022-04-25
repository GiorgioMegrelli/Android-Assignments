package ge.gmegrelishvili.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class MainActivity : AppCompatActivity() {
    private lateinit var menuTodayBt: ImageButton
    private lateinit var menuHourlyBt: ImageButton

    private lateinit var contextController: ContentController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        contextController = WeatherAppContentController(this)

        menuTodayBt = findViewById(R.id.menu_today_button)
        menuHourlyBt = findViewById(R.id.menu_hourly_button)

        menuTodayBt.setOnClickListener {
            contextController.setView(ContentController.View.Today)
        }
        menuHourlyBt.setOnClickListener {
            contextController.setView(ContentController.View.Hourly)
        }
    }

}