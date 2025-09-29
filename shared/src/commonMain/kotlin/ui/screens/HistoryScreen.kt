package ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class HistoryItem(
    val id: String,
    val date: String,
    val summary: String
)

@Composable
fun HistoryScreen(
    onBack: () -> Unit
) {
    val mock = remember {
        listOf(
            HistoryItem("1", "2025-09-20 18:22", "QC OK · FHP —° · плечи —°"),
            HistoryItem("2", "2025-09-22 09:10", "QC OK · таз — мм · лат.сдвиг — мм"),
            HistoryItem("3", "2025-09-27 12:45", "QC warn · повторите боковой ракурс")
        )
    }

    Surface(color = MaterialTheme.colorScheme.background) {
        Column(Modifier.fillMaxSize().padding(20.dp)) {
            Text("История измерений", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(12.dp))
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(mock) { item ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { /* позже откроем детальный отчёт */ }
                            .padding(vertical = 10.dp)
                    ) {
                        Text(item.date, style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(4.dp))
                        Text(item.summary, style = MaterialTheme.typography.bodyMedium)
                    }
                    Divider()
                }
            }
        }
    }
}
