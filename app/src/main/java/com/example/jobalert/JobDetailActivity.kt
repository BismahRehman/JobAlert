package com.example.jobalert

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jobalert.ui.theme.JobAlertTheme

class JobDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JobAlertTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val title = intent.getStringExtra("title") ?: "No Title"
                    val company = intent.getStringExtra("company") ?: "Unknown Company"
                    val location = intent.getStringExtra("location") ?: "Unknown Location"
                    val requirements = intent.getStringExtra("requirements") ?: "Not specified"
                    val qualifications = intent.getStringExtra("qualifications") ?: "Not specified"

                    JobDetailScreen(title, company, location, requirements, qualifications)
                }
            }
        }
    }
}

@Composable
fun JobDetailScreen(
    title: String,
    company: String,
    location: String,
    requirements: String,
    qualifications: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text("Position: $title", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Company: $company", fontSize = 18.sp)
        Text("Location: $location", fontSize = 18.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Requirements:", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
        Text(requirements, fontSize = 16.sp, modifier = Modifier.padding(top = 4.dp))
        Spacer(modifier = Modifier.height(12.dp))
        Text("Qualifications:", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
        Text(qualifications, fontSize = 16.sp, modifier = Modifier.padding(top = 4.dp))
  Text("hi")
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewJobDetail() {
    JobAlertTheme {
        JobDetailScreen(
            title = "Android Developer",
            company = "TechCorp",
            location = "Lahore, Pakistan",
            requirements = "• 2+ years in Android\n• Kotlin, Compose, Firebase",
            qualifications = "• BS in Computer Science\n• Problem-solving skills"
        )
    }
}
