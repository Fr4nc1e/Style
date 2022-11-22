package com.example.style.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.style.databinding.ActivityPostBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.theartofdev.edmodo.cropper.CropImage

class PostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostBinding

    private var uriContent: Uri? = null

    private lateinit var imageUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.close.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.post.setOnClickListener {
            upload()
        }

        CropImage.activity().start(this)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            val result = CropImage.getActivityResult(data)
            uriContent = result.uri
            binding.imageAdded.setImageURI(uriContent)
        } else {
            Toast.makeText(this, "Something went wrong , try again!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun upload() {
        if (uriContent != null) {
            val filePath = FirebaseStorage
                .getInstance()
                .getReference("Posts")
                .child(
                    System.currentTimeMillis().toString() + "." +
                        getFileExtension(uriContent!!)
                )

            val uploadTask = filePath.putFile(uriContent!!)
            uploadTask.continueWithTask {
                if (!it.isSuccessful) {
                    throw it.exception!!
                }

                return@continueWithTask filePath.downloadUrl
            }.addOnCompleteListener {
                val downloadUri = it.result
                imageUrl = downloadUri.toString()

                val ref = FirebaseDatabase.getInstance().getReference("Posts")
                val postId: String? = ref.push().key

                val map: HashMap<String, Any> = HashMap()
                postId?.let { it1 -> map["postId"] = it1 }
                map["imageUrl"] = imageUrl
                map["description"] = binding.description.text.toString()
                map["publishers"] = FirebaseAuth.getInstance().currentUser!!.uid

                ref.child(postId!!).setValue(map)

                val mHashTagRef = FirebaseDatabase.getInstance().reference.child("HashTags")
                val hashTags: List<String> = binding.description.hashtags
                if (hashTags.isNotEmpty()) {
                    for (tag in hashTags) {
                        map.clear()

                        map["tag"] = tag.lowercase()
                        map["postId"] = postId

                        mHashTagRef.child(tag.lowercase()).setValue(map)
                    }
                }

                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }.addOnFailureListener {
                Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Image was not selected.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getFileExtension(uriContent: Uri): String {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(this.contentResolver.getType(uriContent))
            .toString()
    }
}
