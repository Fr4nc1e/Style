package com.example.style.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.style.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var mAuth: FirebaseAuth

    private val txtEmail: String by lazy {
        binding.email.text.toString()
    }

    private val txtPasswd: String by lazy {
        binding.password.text.toString()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()

        binding.registerUser.setOnClickListener {
            startActivity(
                Intent(this, RegisterActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            )
            finish()
        }

        binding.login.setOnClickListener {
            if (txtPasswd.isEmpty() || txtPasswd.isEmpty()) {
                Toast.makeText(this, "Empty Credential$", Toast.LENGTH_SHORT).show()
            } else {
                val job = Job()
                if (
                    CoroutineScope(job).launch(Dispatchers.IO) {
                        loginUser(txtEmail, txtPasswd)
                    }.isCompleted
                ) {
                    job.cancel()
                }
            }
        }
    }

    private suspend fun loginUser(txtEmail: String, txtPasswd: String) {
        mAuth.signInWithEmailAndPassword(txtEmail, txtPasswd)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(
                        this,
                        "Update the profile " +
                            "for better experience.",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(
                        Intent(this, MainActivity::class.java)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    )
                    finish()
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show()
            }
    }
}
