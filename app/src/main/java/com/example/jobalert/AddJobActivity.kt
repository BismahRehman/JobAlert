package com.example.jobalert

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.jobalert.ui.theme.JobAlertTheme

class AddJobActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            JobAlertTheme {
                AddJobScreen()
            }
        }
    }
}

@Composable
fun AddJobScreen() {
    val firestore = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val userId = auth.currentUser?.uid
    val context = LocalContext.current

    var position by remember { mutableStateOf(TextFieldValue()) }
    var location by remember { mutableStateOf(TextFieldValue()) }
    var requirements by remember { mutableStateOf(TextFieldValue()) }
    var qualifications by remember { mutableStateOf(TextFieldValue()) }
    var companyName by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    // Load employer company name
    LaunchedEffect(Unit) {
        if (userId == null) {
            Toast.makeText(context, "Please log in first", Toast.LENGTH_SHORT).show()
            return@LaunchedEffect
        }
        FirebaseFirestore.getInstance().collection("employers").document(userId).get()
            .addOnSuccessListener { doc ->
                companyName = doc.getString("companyName") ?: ""
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Post a New Job", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = position,
            onValueChange = { position = it },
            label = { Text("Position / Job Title") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = companyName,
            onValueChange = { companyName = it },
            label = { Text("Company Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Location") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = requirements,
            onValueChange = { requirements = it },
            label = { Text("Requirements") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = qualifications,
            onValueChange = { qualifications = it },
            label = { Text("Qualifications") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                if (position.text.isEmpty() || companyName.isEmpty() ||
                    location.text.isEmpty() || requirements.text.isEmpty() || qualifications.text.isEmpty()
                ) {
                    Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                isLoading = true
                val job = hashMapOf(
                    "position" to position.text,
                    "companyName" to companyName,
                    "location" to location.text,
                    "requirements" to requirements.text,
                    "qualifications" to qualifications.text,
                    "timestamp" to System.currentTimeMillis(),
                    "employerId" to userId
                )

                firestore.collection("jobs").add(job)
                    .addOnSuccessListener {
                        isLoading = false
                        Toast.makeText(context, "Job Posted Successfully!", Toast.LENGTH_SHORT).show()
                        context.startActivity(Intent(context, EmployerDashboardActivity::class.java))
                    }
                    .addOnFailureListener {
                        isLoading = false
                        Toast.makeText(context, "Failed to post job", Toast.LENGTH_SHORT).show()
                    }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Text("Post Job")
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddJobScreenPreview() {
    JobAlertTheme {
        AddJobScreen()
    }
}
