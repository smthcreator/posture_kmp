package ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ChecklistScreen(
    onStartCapture: () -> Unit
) {
    var a by remember { mutableStateOf(false) }
    var b by remember { mutableStateOf(false) }
    var c by remember { mutableStateOf(false) }
    var d by remember { mutableStateOf(false) }
    val allOk = a && b && c && d

    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                "Чек-лист перед съёмкой",
                style = MaterialTheme.typography.headlineSmall
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = a, onCheckedChange = { a = it })
                Spacer(Modifier.width(8.dp))
                Text("Камера на уровне пояса, дистанция ~2–3 м")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = b, onCheckedChange = { b = it })
                Spacer(Modifier.width(8.dp))
                Text("Полный рост: видны стопы и макушка")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = c, onCheckedChange = { c = it })
                Spacer(Modifier.width(8.dp))
                Text("Хороший свет, однотонный фон")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = d, onCheckedChange = { d = it })
                Spacer(Modifier.width(8.dp))
                Text("Волосы собраны, одежда обтягивающая")
            }

            Spacer(Modifier.height(12.dp))
            Button(
                onClick = onStartCapture,
                enabled = allOk,
                modifier = Modifier.fillMaxWidth().height(54.dp)
            ) {
                Text(if (allOk) "Начать съёмку" else "Отметьте пункты")
            }
        }
    }
}
