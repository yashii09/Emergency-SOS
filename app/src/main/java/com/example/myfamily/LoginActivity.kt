package com.example.myfamily

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.example.myfamily.databinding.ActivityLoginBinding
import com.example.myfamily.databinding.FragmentHomeBinding
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.ktx.auth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var credentialManager: CredentialManager

    lateinit var binding: ActivityLoginBinding

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE) // API 34
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        credentialManager = CredentialManager.create(this)

        binding.btnContinue.setOnClickListener {
            launchSignIn()
        }
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE) // API 34
    private fun launchSignIn() {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(getString(R.string.default_web_client_id_auth)) // from google-services.json
            .setFilterByAuthorizedAccounts(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        credentialManager.getCredentialAsync(
            context = this,
            request = request,
            cancellationSignal = null,
            executor = mainExecutor,
            callback = object : androidx.credentials.CredentialManagerCallback<
                    androidx.credentials.GetCredentialResponse,
                    GetCredentialException> {

                override fun onResult(result: androidx.credentials.GetCredentialResponse) {
                    handleSignIn(result.credential)
                }

                override fun onError(e: GetCredentialException) {
                    Toast.makeText(this@LoginActivity, "Sign-in failed: ${e.message}", Toast.LENGTH_LONG).show()
                    Log.e(TAG, "Sign-in failed: ${e.message}", e)
                }
            }
        )
    }

    private fun handleSignIn(credential: Credential) {
        if (credential is androidx.credentials.CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            firebaseAuthWithGoogle(googleIdTokenCredential.idToken)
        } else {
            Log.w(TAG, "Credential is not of type Google ID!")
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(firebaseCredential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(this, "Signed in as ${user?.displayName}", Toast.LENGTH_SHORT).show()
                    Log.d("Fire89", "signInWithCredential:success")

                    SharedPref.putBoolean(PrefConstants.IS_USER_LOGGED_IN, true)

                    val name = user?.displayName
                    if(!name.isNullOrEmpty()) {
                        SharedPref.putString("USER_NAME", name)
                        Log.d("Fire89", "Saved user name to SharedPref: $name")
                    }
                    else {
                        Log.d("Fire89", "DisplayName is null or empty")
                    }



                    // TODO: Navigate to your home activity
                    startActivity(Intent(this, MainActivity::class.java))

                    Log.d("Fire89", "firebaseAuthWithGoogle: ${user?.displayName}")
                } else {
                    Toast.makeText(this, "Firebase sign-in failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onStart() {
        super.onStart()
        auth.currentUser?.let {
            Toast.makeText(this, "Already signed in: ${it.displayName}", Toast.LENGTH_SHORT).show()
            // TODO: Navigate to home screen
        }
    }
}
