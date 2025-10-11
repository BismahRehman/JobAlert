package com.example.jobalert

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jobalert.ui.theme.JobAlertTheme

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
    var searchTitle by remember { mutableStateOf("") }
    var searchLocation by remember { mutableStateOf("") }
    var filteredJobs by remember { mutableStateOf(listOf<JobData>()) }

    val jobList = listOf(
        JobData("Android Developer", "Canyon Games", "Lahore"),
        JobData("Backend Engineer", "TechLogix", "Karachi"),
        JobData("UI/UX Designer", "Systems Ltd", "Islamabad"),
        JobData("AI Intern", "King Revolution Inc", "Lahore"),
        JobData("Frontend Developer", "NetSol", "Lahore"),
        JobData("ML Engineer", "VisionSoft", "Karachi")
    )

    LaunchedEffect(Unit) {
        filteredJobs = jobList
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
                filteredJobs = jobList.filter {
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

// -------------------- JOB LIST SCREEN --------------------
@Composable
fun JobListScreen() {
    val jobList = listOf(
        JobData("Data Scientist", "Careem", "Karachi"),
        JobData("Game Developer", "TenPearls", "Lahore"),
        JobData("Web Developer", "Systems Ltd", "Islamabad")
    )

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(jobList) { job ->
            JobCard(job)
        }
    }
}

// -------------------- PROFILE SCREEN --------------------
@Composable
fun ProfileScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Profile Picture
            Card(
                shape = CircleShape,
                elevation = CardDefaults.cardElevation(8.dp),
                modifier = Modifier.size(100.dp)
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "Profile",
                    tint = Color(0xFF1565C0),
                    modifier = Modifier.fillMaxSize().padding(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // User Info Card
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text("Bismah Rehman", fontWeight = FontWeight.Bold, fontSize = 22.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("bismah@example.com", color = Color.Gray, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Phone: +92 300 1234567", color = Color.Gray, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Location: Lahore, Pakistan", color = Color.Gray, fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Create Employer Account Button
            Button(
                onClick = { println("Create Employer Account Clicked") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0)),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Icon(Icons.Default.AccountCircle, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Create Employer Account", fontSize = 16.sp)
            }
        }
    }
}

// -------------------- JOB CARD --------------------
@Composable
fun JobCard(job: JobData) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = job.title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(text = job.company, fontSize = 15.sp, color = Color.Gray)
            Text(text = job.location, fontSize = 14.sp, color = Color(0xFF1565C0))
        }
    }
}

data class JobData(val title: String, val company: String, val location: String)

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    JobAlertTheme { MainScreen() }
}