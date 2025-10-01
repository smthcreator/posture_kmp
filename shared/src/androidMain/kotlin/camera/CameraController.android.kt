package camera

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

actual class CameraController actual constructor() {

    private val _previewState = MutableStateFlow<PreviewState>(PreviewState.Idle)
    actual val previewState: Flow<PreviewState> = _previewState.asStateFlow()

    actual suspend fun ensurePermissions(
        permissions: Set<CameraPermission>
    ): Boolean {
        // Sprint 2 (stub): считаем, что разрешения уже есть.
        // Позже заменим на Activity Result API + runtime-permissions.
        return true
    }

    actual suspend fun startPreview(backCamera: Boolean) {
        _previewState.value = PreviewState.Starting
        _previewState.value = PreviewState.Running(fps = 30)
    }

    actual suspend fun stopPreview() {
        _previewState.value = PreviewState.Idle
    }

    actual suspend fun captureBestFrame(): FrameInfo {
        return FrameInfo(
            width = 1080,
            height = 1920,
            timestampMs = System.currentTimeMillis()
        )
    }
}
