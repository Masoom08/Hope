package st.masoom.hope

import android.Manifest
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.accompanist.permissions.*
import com.google.accompanist.permissions.PermissionStatus.Granted
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun Map(navController: NavController) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    RequestLocationPermission(fusedLocationClient)
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestLocationPermission(fusedLocationClient: FusedLocationProviderClient) {
    val locationPermissionState = rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)

    when(locationPermissionState.status) {
        PermissionStatus.Granted -> {
            ShowMap(fusedLocationClient)
        }
        is PermissionStatus.Denied -> {
            val shouldShowRationale =
                (locationPermissionState.status as PermissionStatus.Denied).shouldShowRationale
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally, // Centers the content horizontally
                verticalArrangement = Arrangement.Center // Centers the content vertically
            ){
                Text(
                    text= if (shouldShowRationale) {
                    "Location access is needed to display your current position."
                } else {
                    "Please grant location permission to use this feature."
                }
                )
                Button(
                    onClick = { locationPermissionState.launchPermissionRequest() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF44336),
                        contentColor = Color.Black
                    )) {
                    Text("Grant Permission")
                }
            }
        }
    }
}

@SuppressLint("MissingPermission")
@Composable
fun ShowMap(fusedLocationClient: FusedLocationProviderClient) {
    var userLocation by remember { mutableStateOf(LatLng(0.0, 0.0)) }

    LaunchedEffect(Unit) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                userLocation = LatLng(location.latitude, location.longitude)
            }
        }
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(userLocation, 15f)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(isMyLocationEnabled = true)
    )
}
