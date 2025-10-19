package com.example.jobalert

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.jobalert.ui.theme.JobAlertTheme

data class JobItem(
    val position: String = "",
    val companyName: String = "",
    val location: String = "",
    val requirements: String = "",
    val qualifications: String = ""
)

class EmployerDashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JobAlertTheme {
                EmployerDashboardScreen()
            }
        }
    }
}

@Composable
fun EmployerDashboardScreen() {
    val context = LocalContext.current
    val firestore = FirebaseFirestore.getInstance()
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    var jobList by remember { mutableStateOf(listOf<JobItem>()) }

    // Load jobs from Firestore
    LaunchedEffect(Unit) {
        firestore.collection("jobs")
            .whereEqualTo("employerId", userId)
            .get()
            .addOnSuccessListener { docs ->
                val jobs = docs.map { doc ->
                    JobItem(
                        position = doc.getString("position") ?: "",
                        companyName = doc.getString("companyName") ?: "",
                        location = doc.getString("location") ?: "",
                        requirements = doc.getString("requirements") ?: "",
                        qualifications = doc.getString("qualifications") ?: ""
                    )
                }
                jobList = jobs
            }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val intent = Intent(context, AddJobActivity::class.java)
                    context.startActivity(intent)
                }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Job")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Your Posted Jobs", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(16.dp))
            if (jobList.isEmpty()) {
                Text("No jobs posted yet.")
            } else {
                LazyColumn {
                    items(jobList) { job ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clickable {
                                    val intent = Intent(context, JobDetailActivity::class.java).apply {
                                        putExtra("title", job.position)
                                        putExtra("company", job.companyName)
                                        putExtra("location", job.location)
                                        putExtra("requirements", job.requirements)
                                        putExtra("qualifications", job.qualifications)
                                    }
                                    context.startActivity(intent)
                                },
                            elevation = CardDefaults.cardElevation(6.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Position: ${job.position}", style = MaterialTheme.typography.titleMedium)
                                Text("Company: ${job.companyName}")
                                Text("Location: ${job.location}")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EmployerDashboardPreview() {
    JobAlertTheme {
        val dummyJobs = listOf(
            JobItem("Android Developer", "TechZone", "Karachi", "Kotlin, Firebase, Compose", "BSCS or equivalent"),
            JobItem("AI Engineer", "FutureAI", "Lahore", "Python, ML, TensorFlow", "BS/MS in AI or Data Science")
        )
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            items(dummyJobs) { job ->
                Card(modifier = Modifier.padding(vertical = 8.dp)) {
                    Column(Modifier.padding(16.dp)) {
                        Text(job.position, style = MaterialTheme.typography.titleMedium)
                        Text(job.companyName)
                        Text(job.location)
                    }
                }
            }
        }
    }
}
