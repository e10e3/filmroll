package fr.epf.matmob.filmroll

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import fr.epf.matmob.filmroll.ui.theme.FilmrollTheme

private const val TAG = "QRScanActivity"

class QRScanActivity : ComponentActivity() {
    private var continueScanning = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FilmrollTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    QRScanningScreen(onScan = { result: Int -> onFilmScanned(result) })
                }
            }
        }
    }

    private fun onFilmScanned(id: Int) {
        if (this.continueScanning) {
            this.continueScanning = false
            Log.d(TAG, "onFilmScanned: showing the card for film #$id")
            this.startActivity(
                Intent(
                    this, FilmCardActivity::class.java
                ).putExtra("TMDBFilmId", id)
            )
        }
    }

    override fun onStart() {
        super.onStart()
        continueScanning = true
    }

}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun QRScanningScreen(onScan: (Int) -> Unit) {
    val cameraPermissionState = rememberPermissionState(
        permission = Manifest.permission.CAMERA
    )

    if (cameraPermissionState.status.isGranted) {
        CameraView(onScan = onScan)
    } else {
        AskPermissions(permState = cameraPermissionState)
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AskPermissions(permState: PermissionState) {
    Column(
        verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val textToShow = if (permState.status.shouldShowRationale) {
            // If the user has denied the permission but the rationale can be shown,
            // then gently explain why the app requires this permission
            stringResource(R.string.ask_camera_permission_short)
        } else {
            // If it's the first time the user lands on this feature, or the user
            // doesn't want to be asked again for this permission, explain that the
            // permission is required
            stringResource(R.string.ask_camera_permission_long)
        }
        Text(textToShow, textAlign = TextAlign.Center)
        Button(
            onClick = { permState.launchPermissionRequest() },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(stringResource(R.string.enable_camera_button_text))
        }
    }
}

@Composable
fun CameraView(onScan: (Int) -> Unit) {
    val localContext = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember {
        ProcessCameraProvider.getInstance(localContext)
    }

    AndroidView(factory = { context ->
        val previewView = PreviewView(context)
        val preview = Preview.Builder().build()
        val selector =
            CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()
        preview.setSurfaceProvider(previewView.surfaceProvider)
        val imageAnalysis =
            ImageAnalysis.Builder().setBackpressureStrategy(STRATEGY_KEEP_ONLY_LATEST).build()
        imageAnalysis.setAnalyzer(
            ContextCompat.getMainExecutor(context),
            QRCodeImageAnalyser { resultValue: Int ->
                onScan(resultValue)
            })
        try {
            cameraProviderFuture.get().bindToLifecycle(
                lifecycleOwner, selector, preview, imageAnalysis
            )
        } catch (e: Exception) {
            Log.i(TAG, "CameraView: ${e.message}", e)
        }
        previewView
    }, modifier = Modifier.fillMaxSize())
}