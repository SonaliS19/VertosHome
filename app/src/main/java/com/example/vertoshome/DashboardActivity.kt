package com.example.vertoshome

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class DashboardActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the status bar color
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.statusBarColor = resources.getColor(R.color.gray, theme)
        setContentView(R.layout.activity_dashboard)
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail().build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

    }

    fun findRooms(view: View) {
        // navigate to FindRooms activity
        val intent = Intent(this, AvailableRooms::class.java)
        startActivity(intent)
    }

    fun showAvailableRooms(view: View) {
        // navigate to AvailableRooms activity
        val intent = Intent(this, AddRoom::class.java)
        startActivity(intent)
    }
    // Sign out the user
    private fun signOut() {
        // Sign out from Firebase Authentication
        auth.signOut()

        // Sign out from Google Sign-In (if applicable)
        mGoogleSignInClient.signOut().addOnCompleteListener(this) {
            // Redirect to the login screen or perform any other necessary actions after logout.
            redirectToLoginScreen()
        }
    }

    // Redirect to the login screen
    private fun redirectToLoginScreen() {
        // You can implement the logic here to redirect the user to the login screen
        // For example, if you have a LoginActivity, you can use the following code:
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Optional: Finish the current MainActivity so the user can't go back to it without signing in again.
    }
    // Handle logout button click
    fun logout(view: View) {
        signOut()
    }
}