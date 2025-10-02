package ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import camera.CameraController
import camera.PreviewState
import camera.PlatformCameraPreview
import kotlinx.coroutines.launch
import pose.PoseEstimator
import pose.PoseResult

@Composable
fun CaptureScreen(
    onFinish: () -> Unit,
    onCancel: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val controller = remember { CameraController() }
    var estimator by remember { mutableStateOf<PoseEstimator?>(null) }
    DisposableEffect(Unit) { onDispose { estimator?.close() } }
    var previewViewRef by remember { mutableStateOf<Any?>(null) }
    var previewState by remember { mutableStateOf<PreviewState>(PreviewState.Idle) }
    var lastCapture by remember { mutableStateOf<String?>(null) }

    // Подписка на состояние превью
    LaunchedEffect(controller) {
        controller.previewState.collect { previewState = it }
    }

    // Полноэкранный слой камеры + полупрозрачные оверлеи управления
    Box(modifier = Modifier.fillMaxSize()) {

        // Камера на весь экран
        PlatformCameraPreview(
    controller = controller,
    onAndroidPreviewReady = { pvAny ->
        previewViewRef = pvAny

        // Инициализируем PoseEstimator ТОЛЬКО на Android (на iOS контекст отсутствует)
        if (estimator == null) {
            val ctx = camera.androidExtractContext(pvAny)
            if (ctx != null) {
                estimator = PoseEstimator(
                    platformContext = ctx,
                    modelPath = "pose_landmarker_full.task" // актуальная full-модель
                )
            }
        }
    }
)

        // Верхняя панель (тонкий оверлей)
        Surface(
            color = Color.Black.copy(alpha = 0.25f),
            contentColor = Color.White,
            tonalElevation = 0.dp,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 8.dp, start = 16.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Съёмка",
                    style = MaterialTheme.typography.titleMedium
                )
                AssistChip(
                    onClick = {},
                    label = {
                        Text(
                            when (previewState) {
                                is PreviewState.Idle -> "Idle"
                                is PreviewState.Starting -> "Starting…"
                                is PreviewState.Running -> "Running"
                                is PreviewState.Error -> "Error"
                            }
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = Color.Black.copy(alpha = 0.35f),
                        labelColor = Color.White
                    )
                )
            }
        }

        // Нижняя панель управления (как в Камере)
        Surface(
            color = Color.Black.copy(alpha = 0.35f),
            contentColor = Color.White,
            tonalElevation = 0.dp,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Лента статуса последнего кадра (необязательно)
                if (lastCapture != null) {
                    Text(
                        "Кадр: $lastCapture",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }

                // Ряд кнопок «Назад» — «Пуск/Стоп» — «Снимок»
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
    onClick = onCancel,
    modifier = Modifier.weight(1f).height(48.dp),
    colors = ButtonDefaults.outlinedButtonColors(
        contentColor = Color.White
    )
) { Text("Назад") }

                    // Центральная «спусковая»/плей кнопка
                    Button(
                        onClick = {
                            scope.launch {
                                if (previewState is PreviewState.Running) {
                                    controller.stopPreview()
                                } else {
                                    val ok = controller.ensurePermissions()
                                    if (ok) controller.startPreview(backCamera = true)
                                }
                            }
                        },
                        modifier = Modifier.weight(1.4f).height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        ),
                        shape = MaterialTheme.shapes.large
                    ) {
                        Text(if (previewState is PreviewState.Running) "Стоп" else "Пуск")
                    }

                    Button(
    onClick = {
        scope.launch {
            val est = estimator
if (est == null) {
    lastCapture = "PoseEstimator не инициализирован (ожидаем Android Preview)"
    return@launch
}

val bmpAny = camera.androidCaptureBitmap(previewViewRef)
if (bmpAny == null) {
    lastCapture = "Кадр ещё не готов (или не Android)"
    return@launch
}

// Инференс позы (MediaPipe, Android)
val pose: PoseResult? = est.estimateBitmap(bmpAny)
            if (pose == null) {
                lastCapture = "Позы не найдено"
            } else {
                lastCapture = "Keypoints: ${pose.keypoints.size} @ ${pose.timestampMs}"
            }
        }
    },
    enabled = previewState is PreviewState.Running,
    modifier = Modifier.weight(1f).height(48.dp),
    colors = ButtonDefaults.buttonColors(
        containerColor = Color.White.copy(alpha = 0.9f),
        contentColor = Color.Black
    )
) { Text("Снимок") }
                }

                // Кнопка «Готово» на всю ширину
                Button(
                    onClick = onFinish,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White.copy(alpha = 0.95f),
                        contentColor = Color.Black
                    )
                ) { Text("Готово") }
            }
        }
    }
}