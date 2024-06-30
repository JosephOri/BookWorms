package com.example.bookworms.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.bookworms.R
import com.example.bookworms.viewModel.UploadPostViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

/**
 * A simple `Fragment` subclass.
 * Use the `AddPostFragment.newInstance` factory method to create an instance of this fragment.
 */
class AddPostFragment : Fragment() {

    private lateinit var uploadPostViewModel: UploadPostViewModel
    lateinit var auth: FirebaseAuth
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        initDummyPost()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_post_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        populateUiWithDummyPost(view)

    }

     private fun populateUiWithDummyPost(view: View) {
        val titleInput = view.findViewById<TextInputEditText>(R.id.post_title_input)
        val descriptionInput = view.findViewById<TextInputEditText>(R.id.post_description_input)
        val addPostButton = view.findViewById<View>(R.id.addPostButton)

//        titleInput.setText(dummyPost.title)
//        descriptionInput.setText(dummyPost.description)
        addPostButton.setOnClickListener {
            // Add post to database
            Toast.makeText(context, "Post added successfully", Toast.LENGTH_SHORT).show()
        }

    }
}