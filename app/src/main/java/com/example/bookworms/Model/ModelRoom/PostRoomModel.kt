package com.example.bookworms.Model.ModelRoom

import com.example.bookworms.Model.entities.Post


class PostRoomModel {
    fun getAllPosts(): List<Post> {
        return AppLocalDB.getInstance().postDao().getAllPosts()
    }

    fun insertPost(post: Post) {
        val db = AppLocalDB.getInstance().postDao().insertPost(post)
    }
    fun deletePost(post: Post){
        return AppLocalDB.getInstance().postDao().deletePost(post)
    }
    fun getPostsByUid(uid: String) : List<Post> {
        return AppLocalDB.getInstance().postDao().getPostsByUserId(uid)
    }
    fun updatePost(post: Post){
        return AppLocalDB.getInstance().postDao().updatePost(post)
    }

}
