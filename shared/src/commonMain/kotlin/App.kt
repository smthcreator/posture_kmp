import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import ui.screens.OnboardingScreen

@Composable
fun App() {
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            var showOnboarding by remember { mutableStateOf(true) }

            if (showOnboarding) {
                OnboardingScreen(onContinue = { showOnboarding = false })
            } else {
                Text("Home (скоро появятся экраны: Checklist, QC, Capture, Results, History)")
            }
        }
    }
}
