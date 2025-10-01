@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package camera

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import androidx.compose.foundation.layout.fillMaxSize
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import platform.AVFoundation.*
import platform.CoreGraphics.CGRectMake
import platform.Foundation.NSError
import platform.QuartzCore.CALayer
import platform.UIKit.UIView
import platform.UIKit.layoutSubviews
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

@Composable
actual fun PlatformCameraPreview(
    controller: CameraController
) {
    UIKitView(
        factory = { CameraPreviewView().apply { start() } },
        modifier = Modifier.fillMaxSize(),
        onRelease = { view -> (view as? CameraPreviewView)?.stop() }
    )
}

/** UIView-хост для слоя превью AVFoundation */
private class CameraPreviewView : UIView(frame = CGRectMake(0.0, 0.0, 0.0, 0.0)) {
    private val session = AVCaptureSession()
    private val previewLayer = AVCaptureVideoPreviewLayer(session = session).apply {
        videoGravity = AVLayerVideoGravityResizeAspectFill
    }

    fun start() {
        // 1) Проверка и запрос разрешения
        when (AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeVideo)) {
            AVAuthorizationStatusAuthorized -> configureAndStart()
            AVAuthorizationStatusNotDetermined -> {
                AVCaptureDevice.requestAccessForMediaType(AVMediaTypeVideo) { granted ->
                    if (granted) {
                        // Гарантируем вызов на main, т.к. работаем с UI/слоями
                        dispatch_async(dispatch_get_main_queue()) { configureAndStart() }
                    }
                }
            }
            else -> {
                // denied/restricted — ничего не делаем (можно показать подсказку позже)
            }
        }
    }

    private fun configureAndStart() {
        // 2) Настройка сессии и входа камеры (задняя камера)
        val device = AVCaptureDevice.defaultDeviceWithMediaType(AVMediaTypeVideo) ?: return

        memScoped {
            val err = alloc<ObjCObjectVar<NSError?>>()
            val input = AVCaptureDeviceInput.deviceInputWithDevice(device, error = err.ptr) as AVCaptureDeviceInput?
            if (input != null && session.canAddInput(input)) {
                session.addInput(input)
            } else {
                return
            }
        }

        // 3) Пресет качества (обычно достаточно High)
        session.sessionPreset = AVCaptureSessionPresetHigh

        // 4) Добавляем слой превью в иерархию view
        this.layer.addSublayer(previewLayer as CALayer)
        previewLayer.frame = bounds

        // (опционально) ориентация: previewLayer.connection?.videoOrientation = AVCaptureVideoOrientationPortrait

        // 5) Старт
        session.startRunning()
    }

    fun stop() {
        session.stopRunning()
        previewLayer.removeFromSuperlayer()
    }

    override fun layoutSubviews() {
        super.layoutSubviews()
        previewLayer.frame = bounds
    }
}