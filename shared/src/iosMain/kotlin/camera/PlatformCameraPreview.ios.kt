@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package camera

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import androidx.compose.foundation.layout.fillMaxSize
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVCaptureDeviceInput
import platform.AVFoundation.AVCaptureSession
import platform.AVFoundation.AVCaptureVideoPreviewLayer
import platform.AVFoundation.AVLayerVideoGravityResizeAspectFill
import platform.AVFoundation.AVMediaTypeVideo
import platform.CoreGraphics.CGRectMake
import platform.Foundation.NSError
import platform.QuartzCore.CALayer
import platform.UIKit.UIView
import platform.UIKit.layoutSubviews
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr

@Composable
actual fun PlatformCameraPreview(
    controller: CameraController,
    onAndroidPreviewReady: ((Any) -> Unit)?
) {
    // iOS параметр onAndroidPreviewReady не используется
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
        this.layer.addSublayer(previewLayer as CALayer)
        previewLayer.frame = bounds
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