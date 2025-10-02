package camera

import androidx.camera.view.PreviewView
import android.content.Context
import android.graphics.Bitmap

actual fun androidExtractContext(preview: Any?): Any? {
    return (preview as? PreviewView)?.context as? Context
}

actual suspend fun androidCaptureBitmap(preview: Any?): Any? {
    return (preview as? PreviewView)?.bitmap as? Bitmap
}