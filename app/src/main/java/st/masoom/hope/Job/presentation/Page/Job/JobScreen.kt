package st.masoom.hope.Job.presentation.Page.Job

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobScreen(navController: NavController, userId: String) {
    var userRole by remember { mutableStateOf("Job Seeker") } // Default role

    val context = LocalContext.current
    val firestore = FirebaseFirestore.getInstance()
    var jobsList by remember { mutableStateOf<List<Job>>(emptyList()) }
    //val currentUserId = UserManager.currentUser?.userId
    var isEmployer by remember { mutableStateOf(false) } // Track if user is Employer

    // Fetch User Role
    LaunchedEffect(userId) {
        fetchUserRole(userId) { role ->
            userRole = role
            isEmployer = (role == "Employer") // Set flag
        }
    }

    // Fetch jobs from Firestore
    LaunchedEffect(Unit) {
        try {
            val jobsSnapshot = firestore.collection("jobs").get().await()
            jobsList = jobsSnapshot.documents.mapNotNull { it.toObject<Job>() }
        } catch (e: Exception) {
            Toast.makeText(context, "Error fetching jobs: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize().background(Color.White),
        topBar = {
            TopAppBar(
                title = { Text("Jobs") },
                actions = {
                    if (isEmployer) {
                        IconButton(onClick = { navController.navigate("add_job") }) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Job")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(jobsList) { job ->
                JobCard(job, isEmployer, userId, firestore)
            }
        }
    }
}

@Composable
fun JobCard(job: Job, isEmployer: Boolean, userId: String, firestore: FirebaseFirestore) {
    val context = LocalContext.current
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FCFF)),
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = job.title, style = MaterialTheme.typography.headlineSmall)
            Text(text = job.description, style = MaterialTheme.typography.bodyMedium)
            Text(text = "Salary: ${job.salary}", color = Color.Gray)
            Text(text = "Location: ${job.location}", color = Color.Gray)

            if (!isEmployer) {
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        applyForJob(job.jobId, userId, firestore, context)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3498DB), // Sky Blue
                        contentColor = Color.White // Text color for contrast
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Apply Now")
                }
            }
        }
    }
}

fun applyForJob(jobId: String, userId: String, firestore: FirebaseFirestore, context: android.content.Context) {
    val jobRef = firestore.collection("jobs").document(jobId)

    jobRef.update("applicants", FieldValue.arrayUnion(userId))
        .addOnSuccessListener {
            Toast.makeText(context, "Applied Successfully!", Toast.LENGTH_SHORT).show()
        }
        .addOnFailureListener { e ->
            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }
}