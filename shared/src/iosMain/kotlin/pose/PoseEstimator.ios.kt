package pose

actual class PoseEstimator actual constructor(
    platformContext: Any?,
    modelPath: String
) {
    // TODO: подключить MediapipeTasksVision на iOS (pose landmarker) через CocoaPods
    actual suspend fun estimateOnce(): PoseResult? {
        // Заглушка: вернём null, пока не подключили лэндмаркер
        return null
    }

    actual fun close() {
        // Заглушка: освободить ресурсы при необходимости
    }
}
