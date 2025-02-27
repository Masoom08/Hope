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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobScreen(navController: NavController, userId: String) {
    var userRole by remember { mutableStateOf("Job Seeker") }
    val context = LocalContext.current
    val firestore = FirebaseFirestore.getInstance()

    var jobsList by remember { mutableStateOf<List<Job>>(emptyList()) }
    var filteredJobs by remember { mutableStateOf<List<Job>>(emptyList()) }
    var locations by remember { mutableStateOf<List<String>>(listOf("All")) }
    var isEmployer by remember { mutableStateOf(false) }

    // Search & Filter states
    var searchQuery by remember { mutableStateOf("") }
    var selectedJobType by remember { mutableStateOf("All") }
    var selectedLocation by remember { mutableStateOf("All") }

    val jobTypes = listOf("All", "Full-Time", "Part-Time", "Remote", "Internship")
    val uniqueLocations = jobsList.map { it.location }.distinct().sorted()
    locations = listOf("All") + uniqueLocations

    //val locations = listOf("All", "New York", "Los Angeles", "Chicago", "San Francisco")

    // Fetch User Role
    LaunchedEffect(userId) {
        fetchUserRole(userId) { role ->
            userRole = role
            isEmployer = (role == "Employer")
        }
    }

    // Fetch jobs from Firestore
    LaunchedEffect(Unit) {
        try {
            val jobsSnapshot = firestore.collection("jobs").get().await()
            jobsList = jobsSnapshot.documents.mapNotNull { it.toObject<Job>() }
            filteredJobs = jobsList

            // Extract unique locations
            val uniqueLocations = jobsList.map { it.location }.distinct().sorted()
            locations = listOf("All") + uniqueLocations  // Add "All" as default

        } catch (e: Exception) {
            Toast.makeText(context, "Error fetching jobs: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    // Filter jobs when search query or filters change
    LaunchedEffect(searchQuery, selectedJobType, selectedLocation) {
        filteredJobs = jobsList.filter { job ->
            (searchQuery.isEmpty() || job.title.contains(searchQuery, ignoreCase = true)) &&
                    (selectedJobType == "All" || job.type == selectedJobType) &&
                    (selectedLocation == "All" || job.location == selectedLocation)
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Jobs",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            if (isEmployer) {
                IconButton(onClick = { navController.navigate("add_job") }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Job", tint = Color.Blue)
                }
            }
        }

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search jobs...") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            singleLine = true
        )

        // Filters
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            DropdownFilter("Job Type", jobTypes, selectedJobType) { selectedJobType = it }
            DropdownFilter("Location", locations, selectedLocation) { selectedLocation = it }
        }

        // Job Listings
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(filteredJobs) { job ->
                JobCard(job, isEmployer, userId, firestore)
            }
        }
    }
}

@Composable
fun DropdownFilter(label: String, options: List<String>, selected: String, onSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.wrapContentSize()) {
        OutlinedButton(
            onClick = { expanded = true }) {
            Text(text = "$label: $selected")
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun JobCard(job: Job, isEmployer: Boolean, userId: String, firestore: FirebaseFirestore) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var resume by remember { mutableStateOf("") }

    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FCFF)),
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = job.title, style = MaterialTheme.typography.headlineSmall)
            Text(text = job.description, style = MaterialTheme.typography.bodyMedium)
            Text(text = "Salary: ${job.salary}", color = Color.Gray)
            Text(text = "Type: ${job.type}", color = Color.Gray)
            Text(text = "Location: ${job.location}", color = Color.Gray)

            Spacer(modifier = Modifier.height(8.dp))

            // Show Apply Button for Job Seekers
            if (!isEmployer) {
                Button(
                    onClick = { showDialog = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3498DB), // Sky Blue
                        contentColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Apply Now")
                }
            }

            // Show Remove Job Button only if the logged-in employer owns the job
            if (isEmployer && job.employerId == userId) {
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { removeJob(job.jobId, firestore, context) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Remove Job")
                }
            }
        }
    }

    // Show ApplyJobDialog when showDialog is true
    if (showDialog) {
        ApplyJobDialog(job = job, userId = userId, firestore = firestore, onDismiss = { showDialog = false })
    }
}
