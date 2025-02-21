package st.masoom.hope.Job.presentation.Page.Home

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import st.masoom.hope.Job.presentation.Page.Home.JoobleRequest
import st.masoom.hope.Job.presentation.Page.Home.JoobleResponse
import st.masoom.hope.Job.presentation.Page.Home.JobItem
import st.masoom.hope.Job.presentation.Page.Home.RetrofitClient

@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    var jobList by remember { mutableStateOf<List<JobItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        fetchJobs { jobs ->
            jobList = jobs
            isLoading = false // Stop loading once data is fetched
        }
    }
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text(
            text = "Latest Job Listings",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )


        LazyColumn {
            if (isLoading) {
                // Show 3 Placeholder Job Cards While Loading
                items(3) { LoadingJobCard() }
            } else {
                jobList?.let { jobs ->
                    if (jobs.isNotEmpty()) {
                        items(jobs) { job -> JobCard(job) }
                    } else {
                        item {
                            Text("No jobs available", color = Color.Gray, modifier = Modifier.padding(16.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingJobCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(120.dp), // Fixed height
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0E0E0)) // Light Gray
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .width(180.dp)
                    .height(20.dp)
                    .background(Color.Gray)
            ) // Fake Job Title
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .width(150.dp)
                    .height(15.dp)
                    .background(Color.LightGray)
            ) // Fake Company
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(15.dp)
                    .background(Color.LightGray)
            ) // Fake Location
        }
    }
}

fun fetchJobs(onResult: (List<JobItem>) -> Unit) {
    val request = JoobleRequest("Android Developer", "India")
    RetrofitClient.api.searchJobs(request).enqueue(object : Callback<JoobleResponse> {
        override fun onResponse(call: Call<JoobleResponse>, response: Response<JoobleResponse>) {
            if (response.isSuccessful) {
                response.body()?.let { onResult(it.jobs) }
            }
        }

        override fun onFailure(call: Call<JoobleResponse>, t: Throwable) {
            Log.e("API Error", t.message ?: "Unknown error")
        }
    })
}

@Composable
fun JobCard(job: JobItem) {
    val context = LocalContext.current
    Card(
        modifier = Modifier.padding(8.dp)
            .fillMaxWidth()
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(job.link)) // Open job link
                context.startActivity(intent)
            },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0E0E0)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = job.title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(text = "Company: ${job.company}")
            Text(text = "Location: ${job.location}")
            //Text(text = "Salary: ${job.salary}")
            Text(text = job.snippet, color = Color.Gray)
        }
    }
}