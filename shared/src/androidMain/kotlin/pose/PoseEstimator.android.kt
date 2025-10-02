package pose

import android.content.Context
import android.graphics.Bitmap
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.framework.image.MPImage
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarker

actual class PoseEstimator actual constructor(
    platformContext: Any?,
    modelPath: String
) {
    private val appContext: Context = (platformContext as? Context)
        ?: error("PoseEstimator(android): platformContext must be android.content.Context")

    private val landmarker: PoseLandmarker

    init {
        val base = BaseOptions.builder()
            .setModelAssetPath(modelPath)   // файл модели лежит в assets: pose_landmarker.task
            .build()

        val options = PoseLandmarker.PoseLandmarkerOptions.builder()
            .setBaseOptions(base)
            .setRunningMode(RunningMode.IMAGE) // обработка одиночного изображения
            .setMinPoseDetectionConfidence(0.5f)
            .setMinPosePresenceConfidence(0.5f)
            .setMinTrackingConfidence(0.5f)
            .build()

        landmarker = PoseLandmarker.createFromOptions(appContext, options)
    }

    /** Заглушка (не используется теперь) */
    actual suspend fun estimateOnce(): PoseResult? = null

    /** Обработка Bitmap через MediaPipe → PoseResult */
    actual suspend fun estimateBitmap(image: Any): PoseResult? {
        val bmp = image as? Bitmap ?: return null

        // 1) Bitmap -> MPImage
        val mpImage: MPImage = BitmapImageBuilder(bmp).build()

        // 2) Запуск landmarker
        val result = landmarker.detect(mpImage)
        if (result == null || result.landmarks().isEmpty()) return null

        // Берём первую найденную позу (список может быть пустым)
        val firstPose = result.landmarks()[0]  // List<NormalizedLandmark>
        val map = mutableMapOf<Landmark, Keypoint>()

        // Сопоставляем известные индексы MediaPipe -> наш enum
        fun add(lm: Landmark) {
            val idx = lm.id
            if (idx in 0 until firstPose.size) {
                val p = firstPose[idx]
                map[lm] = Keypoint(p.x(), p.y(), 1f)
            }
        }

        // Минимально нужные точки для Posture AI
        listOf(
            Landmark.NOSE,
            Landmark.LEFT_EYE, Landmark.RIGHT_EYE,
            Landmark.LEFT_EAR, Landmark.RIGHT_EAR,
            Landmark.LEFT_SHOULDER, Landmark.RIGHT_SHOULDER,
            Landmark.LEFT_HIP, Landmark.RIGHT_HIP
        ).forEach { add(it) }

        return PoseResult(
            keypoints = map,
            timestampMs = System.currentTimeMillis()
        )
    }

    actual fun close() {
        try { landmarker.close() } catch (_: Throwable) { /* no-op */ }
    }
}