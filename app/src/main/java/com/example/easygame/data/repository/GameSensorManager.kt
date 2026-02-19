package com.example.easygame.data.repository

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.abs

class GameSensorManager(context: Context?) : SensorEventListener {

    private companion object {
        const val ALPHA = 0.15f
        const val DEAD_ZONE = 0.4f
    }

    private val sensorManager =
        (context?.getSystemService(Context.SENSOR_SERVICE) as? SensorManager)
    val sensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val _tiltData = MutableStateFlow(0f)
    val tiltData = _tiltData.asStateFlow()
    private var smoothedRoll = 0f

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent?) {
        _tiltData.value = filterSensorData(event?.values?.getOrNull(0))
    }

    fun startListening() {
        sensorManager?.registerListener(
            this, sensor ?: return, SensorManager.SENSOR_DELAY_GAME
        )
    }

    fun stopListening() {
        sensorManager?.unregisterListener(this)
    }

    private fun filterSensorData(rawX: Float?): Float {
        rawX ?: return 0f
        smoothedRoll = ALPHA * rawX + (1 - ALPHA) * smoothedRoll
        if (abs(smoothedRoll) < DEAD_ZONE) return 0f
        if (smoothedRoll > 0) return smoothedRoll - DEAD_ZONE
        return smoothedRoll + DEAD_ZONE
    }
}
