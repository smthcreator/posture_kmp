package camera

import androidx.compose.runtime.Composable

@Composable
expect fun PlatformCameraPreview(
    controller: CameraController,
    onAndroidPreviewReady: ((Any) -> Unit)? = null
)