package com.example.bookworms.viewModel

import androidx.lifecycle.ViewModel
import com.example.bookworms.Model.entities.Post
import com.example.bookworms.services.JoinedPostModel


class UploadPostViewModel : ViewModel() {
    val postModel = JoinedPostModel()
    fun uploadPost(post : Post, callback: (Boolean) -> Unit){
        postModel.uploadPost(post, callback)
    }
}
