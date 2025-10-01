package pose

import android.content.Context

actual class PoseEstimator actual constructor(
    platformContext: Any?,
    modelPath: String
) {
    private val appContext: Context = (platformContext as? Context)
        ?: error("PoseEstimator(android): platformContext must be android.content.Context")
    private val modelAssetPath: String = modelPath

    // TODO: подключить MediaPipe Tasks Vision: PoseLandmarker (иниц. из assets/modelAssetPath)
    actual suspend fun estimateOnce(): PoseResult? {
        // Заглушка: вернём null, пока не подключили MediaPipe
        return null
    }

    actual fun close() {
        // Заглушка: освободить ресурсы MediaPipe (когда появятся)
    }
}
