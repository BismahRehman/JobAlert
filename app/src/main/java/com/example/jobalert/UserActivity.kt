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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jobalert.ui.theme.JobAlertTheme
import com.google.firebase.firestore.FirebaseFirestore

// -------------------- MAIN USER ACTIVITY --------------------
class UserActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JobAlertTheme {
                MainScreen()
            }
        }
    }
}

// -------------------- MAIN SCREEN --------------------
@Composable
fun MainScreen() {
    var selectedTab by remember { mutableStateOf("home") }

    Scaffold(
        topBar = { UserTopBar { println("Notification clicked") } },
        bottomBar = {
            BottomNavigationBar(selectedTab) { selectedTab = it }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedTab) {
                "home" -> UserHomeScreen()
                "jobs" -> JobListScreen()
                "profile" -> ProfileScreen()
            }
        }
    }
}

// -------------------- TOP BAR --------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserTopBar(onNotificationClick: () -> Unit) {
    TopAppBar(
        title = { Text("Job Alert", color = Color.White) },
        actions = {
            IconButton(onClick = onNotificationClick) {
                Icon(
                    imageVector = Icons.Default.NotificationsNone,
                    contentDescription = "Notifications",
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1565C0))
    )
}

// -------------------- BOTTOM NAVIGATION --------------------
@Composable
fun BottomNavigationBar(selectedTab: String, onTabSelected: (String) -> Unit) {
    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(
            selected = selectedTab == "home",
            onClick = { onTabSelected("home") },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = selectedTab == "jobs",
            onClick = { onTabSelected("jobs") },
            icon = { Icon(Icons.Default.Work, contentDescription = "Jobs") },
            label = { Text("Jobs") }
        )
        NavigationBarItem(
            selected = selectedTab == "profile",
            onClick = { onTabSelected("profile") },
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") }
        )
    }
}

// -------------------- HOME SCREEN --------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserHomeScreen() {
    val firestore = FirebaseFirestore.getInstance()
    var searchTitle by remember { mutableStateOf("") }
    var searchLocation by remember { mutableStateOf("") }
    var allJobs by remember { mutableStateOf(listOf<JobData>()) }
    var filteredJobs by remember { mutableStateOf(listOf<JobData>()) }

    // 🔹 Load jobs from Firestore (real-time)
    LaunchedEffect(Unit) {
        firestore.collection("jobs").addSnapshotListener { snapshot, e ->
            if (e == null && snapshot != null) {
                val jobs = snapshot.documents.mapNotNull { doc ->
                    JobData(
                        title = doc.getString("position") ?: "",
                        company = doc.getString("companyName") ?: "",
                        location = doc.getString("location") ?: ""
                    )
                }

                // If no jobs in Firestore, show some demo ones
                allJobs = if (jobs.isEmpty()) getSampleJobs() else jobs
                filteredJobs = allJobs
            }
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        OutlinedTextField(
            value = searchTitle,
            onValueChange = { searchTitle = it },
            label = { Text("Search Job Title") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = searchLocation,
            onValueChange = { searchLocation = it },
            label = { Text("Search Location") },
            leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                filteredJobs = allJobs.filter {
                    (searchTitle.isEmpty() || it.title.contains(searchTitle, ignoreCase = true)) &&
                            (searchLocation.isEmpty() || it.location.contains(searchLocation, ignoreCase = true))
                }
            },
            modifier = Modifier.align(Alignment.End),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0))
        ) {
            Icon(Icons.Default.Search, contentDescription = null)
            Spacer(modifier = Modifier.width(10.dp))
            Text("Search")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (filteredJobs.isEmpty()) "No jobs found" else "Job Posts",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color(0xFF1565C0)
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(filteredJobs) { job ->
                JobCard(job)
            }
        }
    }
}

// -------------------- JOB CARD --------------------
@Composable
fun JobCard(job: JobData) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable {
                val intent = Intent(context, JobDetailActivity::class.java).apply {
                    putExtra("title", job.title)
                    putExtra("company", job.company)
                    putExtra("location", job.location)
                }
                context.startActivity(intent)
            },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(job.title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(job.company, color = Color.Gray)
            Text("📍 ${job.location}", color = Color(0xFF1565C0))
        }
    }
}

// 🔹 Demo jobs for fallback
fun getSampleJobs() = listOf(
    JobData("Android Developer", "TechZone", "Lahore"),
    JobData("AI Engineer", "FutureAI", "Karachi"),
    JobData("Backend Developer", "NetSol", "Islamabad"),
    JobData("UI/UX Designer", "System Ltd", "Lahore")
)


// -------------------- JOB DATA MODEL --------------------
data class JobData(val title: String, val company: String, val location: String)


// -------------------- PREVIEWS --------------------

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewMainScreen() {
    JobAlertTheme {
        MainScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewUserHomeScreen() {
    JobAlertTheme {
        UserHomeScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewJobCard() {
    JobAlertTheme {
        JobCard(
            JobData("Android Developer", "Canyon Games", "Lahore")
        )
    }
}

