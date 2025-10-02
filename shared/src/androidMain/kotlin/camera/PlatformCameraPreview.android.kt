package camera

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat

@Composable
actual fun PlatformCameraPreview(
    controller: CameraController,
    onAndroidPreviewReady: ((Any) -> Unit)?
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Хост для CameraX
    AndroidView(
        factory = { ctx: Context ->
    val previewView = PreviewView(ctx).apply {
        // SCALE_TYPE_FILL_CENTER — без чёрных полос, обрезка по краям
        scaleType = PreviewView.ScaleType.FILL_CENTER
    }
    // Передаём наружу PreviewView (как Any)
    onAndroidPreviewReady?.invoke(previewView)
    previewView
},
        update = { previewView ->
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()

                // Конфиг превью
                val preview = Preview.Builder().build().apply {
                    setSurfaceProvider(previewView.surfaceProvider)
                }

                // Выбор камеры (пока только задняя)
                val selector = CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build()

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(lifecycleOwner, selector, preview)
                } catch (_: Exception) {
                    // В Sprint 2 — молча, позже прокинем ошибку в controller.previewState
                }
            }, ContextCompat.getMainExecutor(context))
        }
    )
}
