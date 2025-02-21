package st.masoom.hope.Job.presentation.Page

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import st.masoom.hope.Job.presentation.Page.Job.fetchUserRole

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplicationScreen(navController: NavController, userId: String) {
    var userRole by remember { mutableStateOf("Job Seeker") }
    var applicationsList by remember { mutableStateOf<List<Application>>(emptyList()) }
    val context = LocalContext.current
    val firestore = FirebaseFirestore.getInstance()
    //val coroutineScope = rememberCoroutineScope()
    var isEmployer by remember { mutableStateOf(false) }

    LaunchedEffect(userId) {
        fetchUserRole(userId) { role ->
            userRole = role
            isEmployer = (role == "Employer") // Set flag

            // Fetch applications based on role
            fetchApplications(userId, isEmployer, firestore) { fetchedApplications ->
                applicationsList = fetchedApplications
            }
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text("Your Applications", fontSize = 22.sp, fontWeight = FontWeight.Bold) })
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ) {
            items(applicationsList) { application ->  // Correct usage
                ApplicationCard(application, isEmployer, firestore)
            }
        }

    }
}

@Composable
fun ApplicationCard(application: Application, isEmployer: Boolean, firestore: FirebaseFirestore) {
    val context = LocalContext.current
    var status by remember { mutableStateOf(application.status) }

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Job ID: ${application.jobId}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Status: $status", color = Color.Gray)

            if (isEmployer) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            updateApplicationStatus(application.applicationId, "Accepted", firestore) {
                                status = "Accepted"
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF27AE60))
                    ) {
                        Text("Accept")
                    }
                    Button(
                        onClick = {
                            updateApplicationStatus(application.applicationId, "Rejected", firestore) {
                                status = "Rejected"
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE74C3C))
                    ) {
                        Text("Reject")
                    }
                }
            }
        }
    }
}

// Function to update application status
fun updateApplicationStatus(applicationId: String, newStatus: String, firestore: FirebaseFirestore, onSuccess: () -> Unit) {
    firestore.collection("applications").document(applicationId)
        .update("status", newStatus)
        .addOnSuccessListener {
            onSuccess()
        }
        .addOnFailureListener { e ->
            Log.e("Firestore", "Error updating status: ${e.message}")
        }
}

fun fetchApplications(userId: String, isEmployer: Boolean, firestore: FirebaseFirestore, onResult: (List<Application>) -> Unit) {
    firestore.collection("applications")
        .get()
        .addOnSuccessListener { querySnapshot ->
            val allApplications = querySnapshot.documents.mapNotNull { it.toObject(Application::class.java) }

            if (isEmployer) {
                // First: Fetch jobs posted by the employer
                firestore.collection("jobs")
                    .whereEqualTo("employerId", userId)
                    .get()
                    .addOnSuccessListener { jobSnapshot ->
                        val employerJobIds = jobSnapshot.documents.map { it.id }  // Extract all job IDs employer posted
                        val filteredApplications = allApplications.filter { it.jobId in employerJobIds } // Filter applications matching employer's job IDs
                        Log.d("Firestore", "Employer Applications Found: ${filteredApplications.size}")
                        onResult(filteredApplications)
                    }
                    .addOnFailureListener {
                        Log.e("Firestore", "Error fetching employer jobs: ${it.message}")
                        onResult(emptyList())  // No jobs found for employer
                    }
            } else {
                // For Job Seekers: Show only applications they applied for
                val filteredApplications = allApplications.filter { it.jobSeekerId == userId }
                onResult(filteredApplications)
            }
        }
        .addOnFailureListener {
            onResult(emptyList())  // Handle error by returning an empty list
        }
}

// Update Application Status (Employer Action)
fun updateApplicationStatus(
    application: Application,
    newStatus: String,
    firestore: FirebaseFirestore,
    context: Context,
    onStatusChange: (String) -> Unit
) {
    firestore.collection("applications").document(application.applicationId)
        .update("status", newStatus)
        .addOnSuccessListener {
            Toast.makeText(context, "Application $newStatus", Toast.LENGTH_SHORT).show()
            onStatusChange(newStatus)
        }
        .addOnFailureListener { e ->
            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
}

// Data Model
data class Application(
    val applicationId: String = "",
    val jobId: String = "",
    val jobSeekerId: String = "",
    val employerId: String = "",
    val resumeUrl: String = "",
    val status: String = "Pending"
)

