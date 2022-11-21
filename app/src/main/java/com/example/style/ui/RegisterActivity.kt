package com.example.style.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.style.MainActivity
import com.example.style.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private lateinit var mRootRef: DatabaseReference

    private lateinit var mAuth: FirebaseAuth

    private val txtUserName by lazy {
        binding.username.text.toString()
    }

    private val txtName by lazy {
        binding.name.text.toString()
    }

    private val txtEmail by lazy {
        binding.email.text.toString()
    }

    private val txtPasswd by lazy {
        binding.password.text.toString()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mRootRef = FirebaseDatabase
            .getInstance()
            .reference
        mAuth = FirebaseAuth.getInstance()

        binding.loginUser.setOnClickListener {
            startActivity(
                Intent(this, LoginActivity::class.java)
            )
            finish()
        }

        binding.register.setOnClickListener {
            if (
                txtUserName.isEmpty() ||
                txtName.isEmpty() ||
                txtEmail.isEmpty() ||
                txtPasswd.isEmpty()
            ) {
                Toast.makeText(this, "Empty credentials!", Toast.LENGTH_SHORT).show()
            } else if (txtPasswd.length < 6) {
                Toast.makeText(this, "Length of Password must be more than 5!", Toast.LENGTH_SHORT).show()
            } else {
                val job = Job()
                if (
                    CoroutineScope(job).launch(Dispatchers.IO) { registerUser(txtUserName, txtName, txtEmail, txtPasswd) }.isCompleted
                ) {
                    job.cancel()
                }
            }
        }
    }

    private suspend fun registerUser(
        txtUserName: String,
        txtName: String,
        txtEmail: String,
        txtPasswd: String
    ) {
        mAuth.createUserWithEmailAndPassword(txtEmail, txtPasswd)
            .addOnSuccessListener {
                val map = HashMap<String, Any>()
                map.apply {
                    put("name", txtName)
                    put("email", txtEmail)
                    put("userName", txtUserName)
                    mAuth.currentUser?.let { it1 -> put("id", it1.uid) }
                }
                mAuth.currentUser
                    ?.let { it1 -> mRootRef.child("Users").child(it1.uid) }
                    ?.setValue(map)
                    ?.addOnCompleteListener {
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
                    }?.addOnFailureListener {
                        Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show()
                    }
            }
    }
}
