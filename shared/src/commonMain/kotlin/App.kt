import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import ui.screens.OnboardingScreen
import ui.screens.ChecklistScreen

@Composable
fun App() {
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            var screen by remember { mutableStateOf("onboarding") }

            when (screen) {
                "onboarding" -> OnboardingScreen(onContinue = { screen = "checklist" })
                "checklist" -> ChecklistScreen(onStartCapture = { screen = "capture" })
                "capture" -> androidx.compose.material3.Text("Экран съёмки (заглушка)")
            }
        }
    }
}