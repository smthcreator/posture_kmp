package ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import camera.CameraController
import camera.PreviewState
import kotlinx.coroutines.launch

@Composable
fun CaptureScreen(
    onFinish: () -> Unit,
    onCancel: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val controller = remember { CameraController() }
    val state by remember { mutableStateOf(controller) }
    var previewState by remember { mutableStateOf<PreviewState>(PreviewState.Idle) }
    var lastCapture by remember { mutableStateOf<String?>(null) }

    // Наблюдаем за состоянием превью (Flow → state)
    LaunchedEffect(state) {
        state.previewState.collect { previewState = it }
    }

    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier.fillMaxSize().padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Съёмка (мок превью)", style = MaterialTheme.typography.headlineSmall)

            AssistChip(
                onClick = {},
                label = {
                    Text(
                        when (previewState) {
                            is PreviewState.Idle -> "Idle"
                            is PreviewState.Starting -> "Starting…"
                            is PreviewState.Running -> "Running ${(previewState as PreviewState.Running).fps ?: 0} fps"
                            is PreviewState.Error -> "Error: ${(previewState as PreviewState.Error).message}"
                        }
                    )
                },
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier.weight(1f).height(52.dp)
                ) { Text("Назад") }

                Button(
                    onClick = {
                        scope.launch {
                            if (previewState is PreviewState.Running) {
                                state.stopPreview()
                            } else {
                                val ok = state.ensurePermissions()
                                if (ok) state.startPreview(backCamera = true)
                            }
                        }
                    },
                    modifier = Modifier.weight(1f).height(52.dp)
                ) {
                    Text(
                        if (previewState is PreviewState.Running) "Стоп превью"
                        else "Старт превью"
                    )
                }

                Button(
                    onClick = {
                        scope.launch {
                            val info = state.captureBestFrame()
                            lastCapture = "${info.width}x${info.height} @ ${info.timestampMs}"
                        }
                    },
                    enabled = previewState is PreviewState.Running,
                    modifier = Modifier.weight(1f).height(52.dp)
                ) { Text("Снимок") }
            }

            if (lastCapture != null) {
                Text("Последний кадр: $lastCapture")
            }

            Spacer(Modifier.weight(1f))

            Button(
                onClick = onFinish,
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) { Text("Готово") }
        }
    }
}
