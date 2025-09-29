package ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ResultsScreen(
    onDone: () -> Unit,
    onOpenHistory: () -> Unit
) {
    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier.fillMaxSize().padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Результаты сканирования", style = MaterialTheme.typography.headlineSmall)
            Divider()
            Text("• Кранио-вертебральный угол: —°")
            Text("• Плечевой скос: —° / перекос: — мм")
            Text("• Тазовый перекос: — мм")
            Text("• Латеральный сдвиг: — мм")
            Text("• Качество кадра (QC): OK")
            Spacer(Modifier.height(12.dp))
            Button(onClick = onOpenHistory, modifier = Modifier.fillMaxWidth().height(52.dp)) {
                Text("Открыть историю")
            }
            Button(onClick = onDone, modifier = Modifier.fillMaxWidth().height(52.dp)) {
                Text("На главный экран")
            }
        }
    }
}
