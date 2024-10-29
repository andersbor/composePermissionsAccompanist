package com.example.permissionsaccompanist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.permissionsaccompanist.ui.theme.PermissionsAccompanistTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PermissionsAccompanistTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CameraSample(modifier = Modifier.padding(innerPadding))
                    //LocationSample(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

// https://github.com/google/accompanist/blob/main/sample/src/main/java/com/google/accompanist/sample/permissions/RequestPermissionSample.kt

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun CameraSample(modifier: Modifier = Modifier) {
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
    Column(modifier = modifier) {
        if (cameraPermissionState.status.isGranted) {
            Text("Camera permission Granted", modifier = modifier)
            // use the camera
        } else {
            val textToShow = if (cameraPermissionState.status.shouldShowRationale) {
                "The camera is important for this app. Please grant the permission."
            } else {
                "Camera not available"
            }
            Text(textToShow)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                Text("Request permission")
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun LocationSample(modifier: Modifier = Modifier) {
    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )
    Column(modifier = modifier) {
        if (locationPermissionsState.allPermissionsGranted) {
            Text("Thanks! I can access your exact location :D")
            // use location
        } else {
            val allPermissionsRevoked =
                locationPermissionsState.permissions.size ==
                        locationPermissionsState.revokedPermissions.size

            val textToShow = if (!allPermissionsRevoked) {
                // If not all the permissions are revoked, it's because the user accepted the COARSE
                // location permission, but not the FINE one.
                "Yay! Thanks for letting me access your approximate location. " +
                        "But you know what would be great? If you allow me to know where you " +
                        "exactly are. Thank you!"
            } else if (locationPermissionsState.shouldShowRationale) {
                // Both location permissions have been denied
                "Getting your exact location is important for this app. " +
                        "Please grant us fine location. Thank you :D"
            } else {
                // First time the user sees this feature or the user doesn't want to be asked again
                "This feature requires location permission"
            }

            val buttonText = if (!allPermissionsRevoked) {
                "Allow precise location"
            } else {
                "Request permissions"
            }

            Text(text = textToShow)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { locationPermissionsState.launchMultiplePermissionRequest() }) {
                Text(buttonText)
            }
        }
    }
}

@Composable
@Preview
fun PreviewSample() {
    PermissionsAccompanistTheme {
        CameraSample()
    }
}