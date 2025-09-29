package ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun QualityCheckScreen(
    onProceed: () -> Unit,
    onBack: () -> Unit
) {
    var distanceOk by remember { mutableStateOf(true) }
    var feetVisible by remember { mutableStateOf(true) }
    var headVisible by remember { mutableStateOf(true) }
    var lightingOk by remember { mutableStateOf(true) }

    val allOk = distanceOk && feetVisible && headVisible && lightingOk

    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier.fillMaxSize().padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Проверка качества кадра", style = MaterialTheme.typography.headlineSmall)

            IndicatorRow("Дистанция 2–3 м", distanceOk)
            IndicatorRow("Видны стопы", feetVisible)
            IndicatorRow("Видна макушка", headVisible)
            IndicatorRow("Освещение ок", lightingOk)

            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier.weight(1f).height(52.dp)
                ) { Text("Назад") }
                Button(
                    onClick = onProceed,
                    enabled = allOk,
                    modifier = Modifier.weight(1f).height(52.dp)
                ) { Text(if (allOk) "Продолжить" else "Исправьте условия") }
            }
        }
    }
}

@Composable
private fun IndicatorRow(label: String, ok: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge)
        AssistChip(
            onClick = {},
            label = { Text(if (ok) "OK" else "Fix") },
        )
    }
}
