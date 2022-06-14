package ge.gmegrelishvili.alarmapp.alarm

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ge.gmegrelishvili.alarmapp.database.repository.AlarmRepository
import ge.gmegrelishvili.alarmapp.database.repository.AppConfigRepository

class AlarmViewModel(
    private val appConfigRepository: AppConfigRepository,
    private val alarmRepository: AlarmRepository
) : ViewModel() {

    fun getConfigValue(key: String): Int? {
        return try {
            appConfigRepository.getValue(key)?.toInt()
        } catch (_: NumberFormatException) {
            null
        }
    }

    fun getConfigValue(): Int? {
        return getConfigValue(AppUiModeKey)
    }

    fun insertConfigValue(key: String, value: Int): Boolean {
        return appConfigRepository.insertValue(key, value.toString())
    }

    fun insertConfigValue(value: Int): Boolean {
        return insertConfigValue(AppUiModeKey, value)
    }

    fun getAlarms(): List<AlarmTime24H> {
        return alarmRepository.getAlarms()
    }

    fun insertAlarm(alarm: AlarmTime24H): Boolean {
        return alarmRepository.insertAlarm(alarm)
    }

    fun removeAlarm(alarm: AlarmTime24H): Boolean {
        return alarmRepository.removeAlarm(alarm)
    }

    companion object {
        const val AppUiModeKey = "APP_UI_MODE_KEY"
        const val ExceptionString = "Illegal ViewModel"

        class NoteViewModelFactory(private val activity: Activity) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(AlarmViewModel::class.java)) {
                    val appConfigRepo = AppConfigRepository(activity)
                    val alarmRepo = AlarmRepository(activity)
                    val viewModel = AlarmViewModel(appConfigRepo, alarmRepo)
                    return viewModel as T
                }
                throw IllegalStateException(ExceptionString)
            }
        }
    }
}