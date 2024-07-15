package com.example.myapplication.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Binder
import android.os.IBinder


class StepCounterService: Service() {

    private var sensorManager: SensorManager? = null
    private var sensor: Sensor? = null
    private var stepCountCallback: StepCountCallback? = null
    private var listener: SensorEventListener? = null

    private val binder: IBinder = LocalBinder()

    inner class LocalBinder : Binder() {
        fun getService(): StepCounterService = this@StepCounterService
    }

    override fun onCreate() {
        super.onCreate()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event == null) return
                val stepsSinceLastReboot = event.values[0].toLong()
                stepCountCallback?.onStepCountChange(stepsSinceLastReboot)
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
        sensorManager?.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    fun setStepCountCallback(callback: StepCountCallback?) {
        stepCountCallback = callback
    }

}

interface StepCountCallback {
    fun onStepCountChange(steps: Long)
}