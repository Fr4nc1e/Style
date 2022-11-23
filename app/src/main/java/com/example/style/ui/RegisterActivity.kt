package com.example.style.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.style.databinding.ActivityRegisterBinding
import com.example.style.network.Register.registerUser
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
                    CoroutineScope(job).launch(Dispatchers.IO) {
                        registerUser(
                            txtUserName,
                            txtName,
                            txtEmail,
                            txtPasswd
                        )
                    }.isCompleted
                ) {
                    job.cancel()
                }
            }
        }
    }
}
