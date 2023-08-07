package com.example.firebasesignin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.firebasesignin.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    // Binding for UI elements
    private lateinit var binding: ActivityMainBinding

    // Firebase components
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // Request ID token for authentication
            .requestEmail() // Request user's email
            .build()

        // Create GoogleSignInClient with the configured options
        mGoogleSignInClient= GoogleSignIn.getClient(this,gso)

        // Initialize FirebaseAuth instance
        mAuth= FirebaseAuth.getInstance()

        // Get the currently signed-in user from Firebase Authentication
        val auth = Firebase.auth
        val user = auth.currentUser

        if (user != null) {
            // If user is signed in, display a welcome message with the user's display name
            val userName = user.displayName
            binding.textView.text = "Welcome, $userName"
        } else {
            // Handle the case where the user is not signed in
        }

        // Set a click listener for the sign-out button
        binding.signOutBtn.setOnClickListener {
            signOutAndStartSignInActivity()
        }
    }

    // Function to sign out and start the sign-in activity
    private fun signOutAndStartSignInActivity() {
        // Sign out from Firebase Authentication
        mAuth.signOut()

        mGoogleSignInClient.signOut().addOnCompleteListener(this) {
            // Optional: Update UI or show a message to the user

            // Start the SignInActivity after sign-out
            val intent = Intent(this@MainActivity, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}