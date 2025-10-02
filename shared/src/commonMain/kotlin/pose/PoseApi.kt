package pose

/** Ключевые точки тела по стандарту MediaPipe (основные индексы) */
enum class Landmark(val id: Int) {
    NOSE(0),
    LEFT_EYE_INNER(1), LEFT_EYE(2), LEFT_EYE_OUTER(3),
    RIGHT_EYE_INNER(4), RIGHT_EYE(5), RIGHT_EYE_OUTER(6),
    LEFT_EAR(7), RIGHT_EAR(8),
    LEFT_SHOULDER(11), RIGHT_SHOULDER(12),
    LEFT_ELBOW(13), RIGHT_ELBOW(14),
    LEFT_WRIST(15), RIGHT_WRIST(16),
    LEFT_HIP(23), RIGHT_HIP(24),
    LEFT_KNEE(25), RIGHT_KNEE(26),
    LEFT_ANKLE(27), RIGHT_ANKLE(28)
}

/** Одна точка (x,y) в нормализованных координатах [0..1], плюс уверенность */
data class Keypoint(
    val x: Float,
    val y: Float,
    val score: Float
)

/** Результат оценки позы для одного кадра */
data class PoseResult(
    val keypoints: Map<Landmark, Keypoint>,
    val timestampMs: Long
)

/** Общий контракт оценщика позы (будет реализован через MediaPipe на платформах) */
expect class PoseEstimator(
    /** platformContext: Android Context / iOS UIViewController (или null если не нужно) */
    platformContext: Any? = null,
    /** путь/имя модели; по умолчанию 'pose_landmarker.task' из assets/bundle */
    modelPath: String = "pose_landmarker.task",
) {
    /** Выполнить оценку позы по текущему кадру превью/фото (в Sprint 2 – заглушка) */
    suspend fun estimateOnce(): PoseResult?
    /** Оценить позу по переданному изображению (Android: ожидает android.graphics.Bitmap) */
    suspend fun estimateBitmap(image: Any): PoseResult?

    /** Освобождение ресурсов MediaPipe */
    fun close()
}