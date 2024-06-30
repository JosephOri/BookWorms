package com.example.bookworms.services
import androidx.lifecycle.MutableLiveData
import com.example.bookworms.BookWormsApp
import com.example.bookworms.Model.ModelRoom.PostRoomModel
import com.example.bookworms.Model.entities.Post
import com.example.bookworms.Model.firebaseModel.PostFirebaseModel
import java.util.LinkedList


class JoinedPostModel {
    companion object {
        var instance: JoinedPostModel = JoinedPostModel()
    }

    private val modelFirebase = PostFirebaseModel()
    private val modelRoom = PostRoomModel()
    private val allPosts = AllPostLiveData()

    fun getAllPosts(): AllPostLiveData{
        return allPosts
    }

    fun deletePost(post: Post, callback: (Boolean) -> Unit) {
        // Delete from Firebase
        modelFirebase.deletePostFromFirebase(post) { isSuccessful ->
            if (isSuccessful) {
                // If deletion from Firebase was successful, delete from local database
                BookWormsApp.getExecutorService().execute {
                    modelRoom.deletePost(post)
                }
            } else {
                // Handle Firebase deletion failure (optional)
            }

            callback(isSuccessful)
        }
    }

    fun editPost(post: Post, callback: (Boolean) -> Unit){
        modelFirebase.updatePost(post){ isSuccessful ->
            if (isSuccessful) {

                // Update the post in the local database
                BookWormsApp.getExecutorService().execute {
                    modelRoom.updatePost(post)
                }
            }
            callback(isSuccessful)
        }
    }

    fun uploadPost(post: Post, callback: (Boolean) -> Unit){
        modelFirebase.uploadPost(post){isSuccessful ->
            if(isSuccessful){
                // Update the post in the local database
                BookWormsApp.getExecutorService().execute {
                    modelRoom.insertPost(post)
                }
            }

            callback(isSuccessful)
        }

    }

    fun getPostsByUid(uid: String): MutableLiveData<List<Post>> {
        val postsLiveData = MutableLiveData<List<Post>>()
        BookWormsApp.getExecutorService().execute {
            val postsByUid = modelRoom.getPostsByUid(uid)
            BookWormsApp.getExecutorService().execute {
                postsLiveData.postValue(postsByUid)
            }
        }

        modelFirebase.getPostsByUid(uid) { posts: List<Post> ->
            postsLiveData.postValue(posts)
            // insert into Room
            BookWormsApp.getExecutorService().execute {
                for (post in posts) {
                    modelRoom.insertPost(post)
                }
            }
        }

        return postsLiveData
    }

    inner class AllPostLiveData: MutableLiveData<List<Post>>() {

        init{
            value = LinkedList<Post>()
        }

        override fun onActive() {
            super.onActive()

            BookWormsApp.getExecutorService().execute{
                val allPosts = modelRoom.getAllPosts()
                postValue(allPosts)
            }

            modelFirebase.getAllPosts{ posts : List<Post> ->
                value = posts

                BookWormsApp.getExecutorService().execute {
                    for (post in posts) {
                        modelRoom.insertPost(post)
                    }
                }
            }
        }
    }

}
