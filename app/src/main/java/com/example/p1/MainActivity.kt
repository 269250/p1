package com.example.p1

import androidx.compose.ui.unit.sp

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.p1.ui.theme.P1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            P1Theme {
                // Uruchomienie Composable odpowiedzialnego za odczyt i wyświetlanie danych z sensora
                SensorDataDisplay(context = this)
            }
        }
    }
}

@Composable
fun SensorDataDisplay(context: Context) {
    // Stan przechowujący dane z sensora (domyślnie ustawiony na "Brak danych")
    var sensorData by remember { mutableStateOf("Brak danych") }

    // Pobieramy dostęp do systemowego SensorManager
    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    // Pobieramy sensor typu akcelerometr
    val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    // Obsługa zasobów i nasłuchiwania sensora
    DisposableEffect(Unit) {
        // Tworzymy listener nasłuchujący zmiany danych z sensora
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event != null) {
                    // Aktualizujemy stan danymi z akcelerometru (osie X, Y, Z)
                    sensorData = "X: ${event.values[0]} Y: ${event.values[1]} Z: ${event.values[2]}"
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                // Obsługa zmian dokładności sensora (opcjonalnie)
            }
        }

        // Rejestrujemy listener, aby odbierać dane z sensora
        sensorManager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_UI)

        // Funkcja czyszcząca zasoby po zakończeniu działania
        onDispose {
            sensorManager.unregisterListener(listener)
        }
    }

    // Interfejs użytkownika wyświetlający dane z sensora
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),  // Odstęp od krawędzi ekranu
        horizontalAlignment = Alignment.CenterHorizontally,  // Wyrównanie do środka w poziomie
        verticalArrangement = Arrangement.Center  // Wyrównanie do środka w pionie
    ) {
        // Wyświetlenie etykiety informacyjnej
        Text(
            text = "Dane z akcelerometru:",
            fontSize = MaterialTheme.typography.headlineMedium.fontSize // Ustawienie większego rozmiaru tekstu
        )

        Spacer(modifier = Modifier.height(16.dp))  // Odstęp między elementami

        // Wyświetlenie odczytanych danych z sensora
        Text(
            text = sensorData,
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 24.sp) // Zwiększenie rozmiaru do 24 sp
        )
    }
}
