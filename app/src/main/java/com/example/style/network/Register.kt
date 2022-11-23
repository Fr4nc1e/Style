package com.example.style.network

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import com.example.style.GlobalApplication
import com.example.style.ui.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object Register {

    private lateinit var mAuth: FirebaseAuth

    private lateinit var mRootRef: DatabaseReference

    suspend fun registerUser(
        txtUserName: String,
        txtName: String,
        txtEmail: String,
        txtPasswd: String
    ) {
        mRootRef = FirebaseDatabase
            .getInstance()
            .reference
        mAuth = FirebaseAuth.getInstance()
        mAuth.createUserWithEmailAndPassword(txtEmail, txtPasswd)
            .addOnSuccessListener {
                val map = HashMap<String, Any>()
                map.apply {
                    put("name", txtName)
                    put("email", txtEmail)
                    put("userName", txtUserName)
                    mAuth.currentUser?.let { it1 -> put("id", it1.uid) }
                    put("bio", "")
                    put("imageUrl", "default")
                }
                mAuth.currentUser
                    ?.let { it1 -> mRootRef.child("Users").child(it1.uid) }
                    ?.setValue(map)
                    ?.addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(
                                GlobalApplication.context,
                                "Update the profile " +
                                    "for better experience.",
                                Toast.LENGTH_SHORT
                            ).show()
                            GlobalApplication.context.startActivity(
                                Intent(GlobalApplication.context, MainActivity::class.java)
                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            )
                            val activity = GlobalApplication.context as Activity
                            activity.finish()
                        }
                    }?.addOnFailureListener {
                        Toast.makeText(GlobalApplication.context, "Something went wrong.", Toast.LENGTH_SHORT).show()
                    }
            }
    }
}
