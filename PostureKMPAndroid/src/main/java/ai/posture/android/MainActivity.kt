package ai.posture.android

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import App

class MainActivity : ComponentActivity() {

    private val requestCamera = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { /* granted -> CameraX стартует из UI; отказ — UI останется Idle */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Запросить разрешение, если ещё не выдано
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestCamera.launch(Manifest.permission.CAMERA)
        }

        setContent { App() }
    }
}