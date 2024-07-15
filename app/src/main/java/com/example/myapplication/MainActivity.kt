package com.example.myapplication

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myapplication.service.DateChangeBroadcastReceiver
import com.example.myapplication.service.StepCountCallback
import com.example.myapplication.service.StepCounterService
import com.example.myapplication.ui.theme.MyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity(), StepCountCallback {

    private var stepCounterService: StepCounterService? = null
    private var bound = false
    private val viewModel: MainViewModel by viewModels<MainViewModel> { defaultViewModelProviderFactory }

    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder: StepCounterService.LocalBinder = service as StepCounterService.LocalBinder
            stepCounterService = binder.getService()
            bound = true
            stepCounterService?.setStepCountCallback(this@MainActivity)
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            bound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val currentStep by viewModel.currentStepFlow.collectAsStateWithLifecycle()
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Text(
                        text = currentStep?.steps.toString()
                    )
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val intent = Intent(this, StepCounterService::class.java)
        bindService(intent, serviceConnection, BIND_AUTO_CREATE)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        this.registerReceiver(
            dateChangeBroadcastReceiver,
            dateChangeBroadcastReceiver.getIntentFilter(),
            RECEIVER_NOT_EXPORTED
        )
    }

    override fun onStop() {
        super.onStop()
        if (bound) {
            stepCounterService?.setStepCountCallback(null)
            unbindService(serviceConnection);
            bound = false;
        }
        this.unregisterReceiver(dateChangeBroadcastReceiver)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStepCountChange(steps: Long) {
        val date = SimpleDateFormat("yyMMdd", Locale.getDefault()).format(Date())
        viewModel.updateSteps(date, steps)
    }

    private val dateChangeBroadcastReceiver = object : DateChangeBroadcastReceiver() {

        override fun onDayChanged() {
            val date = SimpleDateFormat("yyMMdd", Locale.getDefault()).format(Date())
            viewModel.getStepByDate(date)
        }
    }
}
