package hari.stopwatch

import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import hari.stopwatch.ui.theme.StopWatchTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var startTime = mutableStateOf(100000)
        var time = mutableStateOf(startTime.value)
        var timer: CountDownTimer? = null
        var running = mutableStateOf(false)

        fun startTimer() {
            timer = object: CountDownTimer((time.value * 1000).toLong(), 100) {
                override fun onTick(p0: Long) {
                    time.value = (p0/1000).toInt()
                }
                override fun onFinish() {
                    time.value = 0
                }
            }.start()
        }

        fun stopTimer() {
            timer?.cancel()
            timer = null
        }
        
        setContent {
            StopWatchTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    StopWatchUI(
                        timeLeft = startTime.value - time.value,
                        running = running.value,
                        onStartStop =  {
                            if (running.value) {
                                stopTimer()
                                running.value = false
                            }
                            else {
                                startTimer()
                                running.value = true
                            }
                        },
                        onReset = {
                            time.value = startTime.value
                        })
                }
            }
        }
    }
}

@Composable
fun StopWatchUI(timeLeft: Int, running: Boolean, onStartStop: () -> Unit, onReset: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.align(Alignment.Center)) {
            val mins = (timeLeft/60).toString()
            val sec = (timeLeft%60).toString().padStart(2, '0')
            Text(text = "$mins:$sec", fontSize = 72.sp)
            Row {
                Button(onClick = onStartStop) {
                    Text(text = if (running) "Stop" else "Start")
                }
                if (!running) {
                    Button(onClick = onReset) {
                        Text("Reset")
                    }
                }
            }
        }
    }
}