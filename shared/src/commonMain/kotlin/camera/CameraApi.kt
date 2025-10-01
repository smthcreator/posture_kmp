package camera

import kotlinx.coroutines.flow.Flow

/** Требуемые разрешения камеры/галереи по платформам */
enum class CameraPermission { Camera, Microphone /* на будущее: видео */, Photos }

/** Простое состояние превью */
sealed interface PreviewState {
    object Idle : PreviewState
    object Starting : PreviewState
    data class Running(val fps: Int?) : PreviewState
    data class Error(val message: String) : PreviewState
}

/** Простейший DTO кадра (в Sprint 2 — без реальных байтов) */
data class FrameInfo(
    val width: Int,
    val height: Int,
    val timestampMs: Long
)

/** Контракт кроссплатформенного контроллера камеры */
expect class CameraController() {
    /** Поток состояния превью (Idle/Starting/Running/Error) */
    val previewState: Flow<PreviewState>

    /** Проверка/запрос разрешений; возвращает true, если все разрешения выданы */
    suspend fun ensurePermissions(permissions: Set<CameraPermission> = setOf(CameraPermission.Camera)): Boolean

    /** Запуск превью (по умолчанию — задняя камера) */
    suspend fun startPreview(backCamera: Boolean = true)

    /** Остановка превью */
    suspend fun stopPreview()

    /** Захват «лучшего кадра» (пока заглушка, вернём метаданные) */
    suspend fun captureBestFrame(): FrameInfo
}
