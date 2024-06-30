package com.example.bookworms.Model.firebaseModel
import com.example.bookworms.Model.entities.Post
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.LinkedList

class PostFirebaseModel{
    companion object {
        val COLLECTION_NAME: String = "posts"
    }
    val db = Firebase.firestore
    //get all post from firebase
    fun getAllPosts(callback: (List<Post>) -> Unit) {
        val query: Query = db.collection(COLLECTION_NAME)

        query.get().addOnCompleteListener{snapshot->
            if(snapshot.isSuccessful){
                val list = LinkedList<Post>()
                val doc = snapshot.result

                for(postMap in doc){
                    val post = Post(postMap.id, "", "", "", "")
                    post.fromMap(postMap.data)


                    list.add(post)
                }

                callback(list)
            }
        }
    }

    //get all the posts by user id
    fun getPostsByUid(uid: String, callback: (List<Post>) -> Unit) {
        val query: Query = db.collection(COLLECTION_NAME)
            .whereEqualTo("uid", uid)
        query.get().addOnCompleteListener { snapshot ->
            if (snapshot.isSuccessful) {
                val list = mutableListOf<Post>()

                for (document in snapshot.result!!) {
                    val post = Post(document.id, "", "", "", "",)
                    post.fromMap(document.data)
                    list.add(post)
                }

                callback(list)
            } else {
                val exception = snapshot.exception
                println("Error fetching posts for UID: $uid. Exception: $exception")
                // Handle the case where the task was not successful
                callback(emptyList())
            }
        }
    }

    fun deletePostFromFirebase(post: Post, callback: (Boolean) -> Unit) {
        val id = post.id
        db.collection("posts").document(id)
            .delete()
            .addOnSuccessListener {
                callback(true) // Successful deletion from Firebase
            }
            .addOnFailureListener { exception ->
                callback(false) // Handle deletion failure
                println("Error deleting post with ID: $id. Exception: $exception")
            }
    }

    fun updatePost(post: Post, callback: (Boolean) -> Unit){
        val db = Firebase.firestore
        val postDocRef = db.collection(COLLECTION_NAME).document(post.id)
        val updatedPostData = hashMapOf(
            "id" to post.id,
            "img" to post.img,
            "title" to post.title,
            "description" to post.description,
            "uid" to post.uid
        )
        postDocRef.update(updatedPostData as Map<String, Any>)
            .addOnSuccessListener {
                println("Post updated successfully")
                callback(true)
            }
            .addOnFailureListener { exception ->
                println("Error updating post: ${exception.message}")
                callback(false)
            }

    }

    fun uploadPost(post: Post, callback: (Boolean) -> Unit){
        val db = Firebase.firestore
        val docRef = db.collection("posts").document()
        val data = hashMapOf(
            "id" to post.id,
            "img" to post.img,
            "title" to post.title,
            "description" to post.description,
            "uid" to post.uid
        )
        docRef.set(data).addOnSuccessListener {
            println("Post uploaded successfully")
            callback(true)
        }.addOnFailureListener { exception ->
            println("Error uploading post: ${exception.message}")
            callback(false)
        }

    }


}
