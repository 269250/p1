package com.example.p1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

import com.example.p1.ui.theme.P1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            P1Theme {
                aplikacja()
            }
        }
    }
}

@Composable
fun aplikacja() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "screen1" // Startujemy od ekranu 1
    ) {
        composable("screen1") {
            startscreen(onNavigate = {
                navController.navigate("screen2") // Przejście do ekranu 2
            })
        }
        composable("screen2") {
            edycjatekstu(onNextClick = { inputText ->
                navController.navigate("display/$inputText") // Przejście do ekranu wyświetlania
            })
        }
        composable(
            "display/{text}",
            arguments = listOf(navArgument("text") { type = NavType.StringType })
        ) { backStackEntry ->
            val text = backStackEntry.arguments?.getString("text").orEmpty() // Pobieramy tekst z argumentów
            wyswietl(
                text = text,
                onBackClick = { navController.popBackStack() } // Powrót do poprzedniego ekranu
            )
        }
    }
}

@Composable
fun startscreen(onNavigate: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Witaj! Rozpocznij swoją przygodę."
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(onClick = onNavigate) {
            Text("Idź do ekranu z wpisywaniem tekstu")
        }
    }
}

@Composable
fun edycjatekstu(onNextClick: (String) -> Unit) {
    var text by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Proszę, wpisz swój tekst:",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = text,
            onValueChange = { newText -> text = newText },
            label = { Text("Twój tekst") }, // Dodano etykietę
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(onClick = { onNextClick(text) }) {
            Text("Przejdź do ekranu wyświetlającego tekst")
        }
    }
}

@Composable
fun wyswietl(text: String, onBackClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Wprowadziłeś tekst:"
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = text
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(onClick = onBackClick) {
            Text("Wróć do poprzedniego ekranu")
        }
    }
}
