package st.masoom.hope.Job.presentation.Page.Job

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage


@Composable
fun ApplyJobDialog(job: Job, userId: String, firestore: FirebaseFirestore, onDismiss: () -> Unit) {
    val context = LocalContext.current
    var driveLink by remember { mutableStateOf("") }

    // File Picker for Google Drive (PDF Selection)
    val drivePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            getDriveLink(context, uri) { link ->
                driveLink = link
                Toast.makeText(context, "Resume Selected!", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "No file selected", Toast.LENGTH_SHORT).show()
        }
    }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Apply for ${job.title}") },
        text = {
            Column {
                Text("Upload your Resume (Google Drive link):")
                Spacer(modifier = Modifier.height(8.dp))

                Button(onClick = { drivePickerLauncher.launch("application/pdf") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3498DB)), // Your theme color
                    shape = RoundedCornerShape(8.dp)
                    ) {
                    Text("Select Resume from Drive")
                }

                Spacer(modifier = Modifier.height(8.dp))
                if (driveLink.isNotEmpty()) {
                    Text("Selected Link: $driveLink", color = Color.Blue,style = MaterialTheme.typography.bodySmall)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (driveLink.isNotEmpty()) {
                        saveApplication(job, userId, driveLink, firestore, context)
                        onDismiss()
                    } else {
                        Toast.makeText(context, "Please select a file", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C3E50)), // Your theme color
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Submit")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Cancel", color = Color.White)
            }
        },
        containerColor = Color.White
    )
}
fun getDriveLink(context: Context, uri: Uri, onLinkExtracted: (String) -> Unit) {
    val contentResolver = context.contentResolver
    val cursor = contentResolver.query(uri, null, null, null, null)

    cursor?.use {
        if (it.moveToFirst()) {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            val name = if (nameIndex != -1) it.getString(nameIndex) else "Unknown"

            Log.d("GoogleDrive", "File Name: $name")
            Log.d("GoogleDrive", "File URI: $uri")

            // Extract Google Drive ID from the URI
            val driveId = uri.lastPathSegment?.split(":")?.lastOrNull()
            if (driveId != null) {
                val driveLink = "https://drive.google.com/file/d/$driveId/view?usp=sharing"
                Log.d("GoogleDrive", "Drive Link: $driveLink")
                onLinkExtracted(driveLink)
            } else {
                Log.e("GoogleDrive", "Failed to get Drive ID")
                Toast.makeText(context, "Invalid Drive link", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
fun saveApplication(
job: Job,
userId: String,
resumeUrl: String,
firestore: FirebaseFirestore,
context: Context
) {
    val applicationId = firestore.collection("applications").document().id
    val application = mapOf(
        "applicationId" to applicationId,
        "jobId" to job.jobId,
        "jobSeekerId" to userId,
        "employerId" to job.employerId,
        "status" to "Pending",
        "resumeUrl" to resumeUrl
    )

    firestore.collection("applications").document(applicationId)
        .set(application)
        .addOnSuccessListener {
            Toast.makeText(context, "Application Submitted!", Toast.LENGTH_LONG).show()
        }
        .addOnFailureListener { e ->
            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }
}