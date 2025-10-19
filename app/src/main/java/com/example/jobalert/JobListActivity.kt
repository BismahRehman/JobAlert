package com.example.jobalert

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jobalert.ui.theme.JobAlertTheme

// -------------------- JOB LIST ACTIVITY --------------------
class JobListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JobAlertTheme {
                JobListScreen()
            }
        }
    }
}

// -------------------- JOB LIST SCREEN --------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobListScreen() {
    val jobList = listOf(
        JobData("Data Scientist", "Careem", "Karachi"),
        JobData("Game Developer", "TenPearls", "Lahore"),
        JobData("Web Developer", "Systems Ltd", "Islamabad")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Applied Jobs", color = Color.Black, fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFF8F8F8))
            )
        }
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding).padding(16.dp)) {
            items(jobList) { job ->
                JobCard(job)
            }
        }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewJobListScreen() {
    JobAlertTheme {
        JobListScreen()
    }
}