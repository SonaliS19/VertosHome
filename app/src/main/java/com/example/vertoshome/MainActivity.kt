package com.example.vertoshome

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.statusBarColor = resources.getColor(R.color.gray, theme)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail().build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        // Check if the user is already signed in
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // User is already signed in, redirect to DashboardActivity
            val intent = Intent(this, DashboardActivity::class.java)
            intent.putExtra("user", currentUser)
            startActivity(intent)
            finish()
        }
    }

    private var RC_SIGN_IN: Int = 40
    fun loginUser(view: View) {
        Log.d("MainActivity", "Login User")
        val intent = mGoogleSignInClient.signInIntent
        startActivityForResult(intent, RC_SIGN_IN)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account = task.result
                firebaseAuth(account.idToken!!)
            } catch (_: Exception) {
            }
        }
    }

    private fun firebaseAuth(idToken: String) {
        val authCredential = GoogleAuthProvider.getCredential(idToken, null)

        auth.signInWithCredential(authCredential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                redirectToDashboard(user)
            } else {
                Toast.makeText(this, "Error in authentication", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun redirectToDashboard(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(this, DashboardActivity::class.java)
            intent.putExtra("user", user)
            startActivity(intent)
        } else {
            Toast.makeText(this, "No User", Toast.LENGTH_SHORT).show()
        }
    }
}


