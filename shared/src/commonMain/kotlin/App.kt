import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import ui.screens.OnboardingScreen
import ui.screens.ChecklistScreen
import ui.screens.QualityCheckScreen
import ui.screens.CaptureScreen
import ui.screens.ResultsScreen
import ui.screens.HistoryScreen

@Composable
fun App() {
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            var screen by remember { mutableStateOf("onboarding") }

            when (screen) {
                "onboarding" -> OnboardingScreen(onContinue = { screen = "checklist" })
                "checklist" -> ChecklistScreen(onStartCapture = { screen = "qc" })
                "qc" -> QualityCheckScreen(
                    onProceed = { screen = "capture" },
                    onBack = { screen = "checklist" }
                )
                "capture" -> CaptureScreen(
                    onFinish = { screen = "results" },
                    onCancel = { screen = "checklist" }
                )
                "results" -> ResultsScreen(
                    onDone = { screen = "onboarding" },
                    onOpenHistory = { screen = "history" }
                )
                "history" -> HistoryScreen(
                    onBack = { screen = "results" }
                )
            }
        }
    }
}
