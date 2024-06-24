package com.example.bookworms.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookworms.models.entities.User
import com.example.bookworms.services.JoinedUserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ProfileViewModel: ViewModel() {

        private val usersModel = JoinedUserModel()

        fun getUserByUid(uid :String,  callback: (User)-> Unit){
            viewModelScope.launch(Dispatchers.IO) {
                usersModel.getUserByUid(uid){ user ->
                    viewModelScope.launch(Dispatchers.Main) {
                        callback(user)
                    }
                }
            }
        }


}