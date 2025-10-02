import Foundation
import MediaPipeTasksVision
import UIKit

/// Простая обёртка для вызова MediaPipe Pose Landmarker на iOS
@objc class IOSPoseEstimator: NSObject {
    private var landmarker: PoseLandmarker?

    override init() {
        super.init()
        loadModel()
    }

    /// Загружаем pose_landmarker.task из ресурсов бандла iOS
    private func loadModel() {
        guard let modelPath = Bundle.main.path(forResource: "pose_landmarker_full", ofType: "task") else {
            print("❌ Не найден pose_landmarker_full.task в Bundle")
            return
        }

        do {
            let options = PoseLandmarkerOptions()
            options.baseOptions.modelAssetPath = modelPath
            options.runningMode = .image
            landmarker = try PoseLandmarker(options: options)
            print("✅ PoseLandmarker успешно инициализирован")
        } catch {
            print("❌ Ошибка инициализации PoseLandmarker: \(error)")
        }
    }

    /// Анализируем кадр (UIImage → позы)
    @objc func estimate(image: UIImage) -> String {
        print("📸 estimate(image:) called from Kotlin side")
        guard let landmarker = landmarker else {
            return "Landmarker not initialized"
        }
        guard let mpImage = try? MPImage(uiImage: image) else {
            return "Failed to wrap UIImage"
}

        do {
            let result = try landmarker.detect(image: mpImage)
            return "Detected \(result.landmarks.count) poses"
        } catch {
            return "Detection error: \(error)"
        }
    }
}