package pose

import platform.Foundation.NSBundle
import platform.Foundation.NSString
import platform.Foundation.stringWithString

actual class PoseEstimator actual constructor(
    platformContext: Any?,
    modelPath: String
) {
    // Полный путь к файлу модели внутри iOS-бандла (если найден)
    private val bundledModelPath: String? = run {
        // modelPath ожидаем как "pose_landmarker.task"
        val name = modelPath.substringBeforeLast(".")
        val ext = modelPath.substringAfterLast(".", missingDelimiterValue = "")
        // Ищем в основном бандле, т.к. Compose копирует commonMain/resources туда
        NSBundle.mainBundle.pathForResource(name, ofType = if (ext.isEmpty()) null else NSString.stringWithString(ext))
    }

    // TODO (следующий шаг): инициализация MediaPipe (MediaPipeTasksVision) через iOS Pods API

    actual suspend fun estimateOnce(): PoseResult? {
        // На iOS сделаем инференс только по переданному изображению (в следующем шаге).
        return null
    }

    actual suspend fun estimateBitmap(image: Any): PoseResult? {
        // В следующем шаге подключим iOS-обработку через MediaPipeTasksVision.
        return null
    }

    actual fun close() {
        // Освободим ресурсы, когда добавим реальную инициализацию
    }

    // Вспомогательный геттер — пригодится для отладки, можно вывести путь на экран
    fun debugModelPath(): String = bundledModelPath ?: "pose_landmarker.task not found in bundle"
}