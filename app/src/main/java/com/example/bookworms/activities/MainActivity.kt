package com.example.bookworms.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.bookworms.R
import com.example.bookworms.databinding.ActivityMainBinding
import com.example.bookworms.fragments.AboutFragment
import com.example.bookworms.fragments.AddPostFragment
import com.example.bookworms.fragments.HomePageFragment
import com.example.bookworms.fragments.MapsFragment
import com.example.bookworms.fragments.MyPostsFragment
import com.example.bookworms.fragments.ProfilePageFragment
import com.example.bookworms.viewModels.UserViewModel

import com.example.bookworms.fragments.ProgressFragment
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var viewBinding: ActivityMainBinding

    private val progressFragment = ProgressFragment()

    private var logoutButton: MaterialButton? = null
    private var profileButton: MaterialButton? = null
    private var homePageButton: MaterialButton? = null
    private var aboutPageButton: MaterialButton? = null
    private lateinit var userViewModel: UserViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        auth = FirebaseAuth.getInstance()

        initParameters()

        val currentUser = auth.currentUser
        if (currentUser != null) showLoggedInButtons()
        else showWelcomeButtons()

        setEventListeners()
        loadUserProfileImage()
    }

    private fun initParameters(){
        logoutButton = findViewById(R.id.logoutButton)
        profileButton = findViewById(R.id.profileButton)
        homePageButton = findViewById(R.id.homePageButton)
        aboutPageButton = findViewById(R.id.aboutPageButton)
        userViewModel = UserViewModel()
    }

    private fun setEventListeners() {
        logoutButton?.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        profileButton?.setOnClickListener {
            replaceFragment(ProfilePageFragment(), "profilePage")
        }

        homePageButton?.setOnClickListener {
            replaceFragment(HomePageFragment(), "homePage")
        }

        aboutPageButton?.setOnClickListener {
            replaceFragment(AboutFragment(), "aboutPage")
        }

        viewBinding.bottomNavigationView.setOnItemSelectedListener{
            when (it.itemId) {
                R.id.homePage -> replaceFragment(HomePageFragment(), "homePage")
                R.id.profilePage -> replaceFragment(ProfilePageFragment(), "profilePage")
                R.id.addPostPage -> replaceFragment(AddPostFragment(), "addPostPage")
                R.id.myPostsPage -> replaceFragment(MyPostsFragment(), "myPostsPage")
                R.id.mapPage -> replaceFragment(MapsFragment(), "mapPage")
                else -> {}
            }
            true
        }
    }


    private fun replaceFragment(fragment: Fragment, tag: String? = null) {
        val transaction = supportFragmentManager.beginTransaction()
        lifecycleScope.launch {
            showProgressFragment()
            delay(500)
            transaction.replace(R.id.mainFrameLayout, fragment, tag)
            transaction.addToBackStack(tag)
            transaction.commit()
            hideProgressFragment()

        }
    }

    private fun showProgressFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.progressCircularFragment, progressFragment)
        fragmentTransaction.commit()
    }

    private fun hideProgressFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.remove(progressFragment)
        fragmentTransaction.commit()
    }
    
    private fun showLoggedInButtons() {
        logoutButton?.visibility = View.VISIBLE
        profileButton?.visibility = View.VISIBLE
        homePageButton?.visibility = View.VISIBLE
        aboutPageButton?.visibility = View.VISIBLE

    }

    private fun showWelcomeButtons() {
        logoutButton?.visibility = View.INVISIBLE
        profileButton?.visibility = View.INVISIBLE
        homePageButton?.visibility = View.INVISIBLE
        aboutPageButton?.visibility = View.INVISIBLE
    }

    private fun loadUserProfileImage(){
        val currentUser = auth.currentUser
        currentUser?.let { user ->
            userViewModel.getUserByUid(user.uid) { userEntity ->
                if (userEntity != null) {
                    if (userEntity.profileImg.isNotEmpty()) {
                        Picasso.get().load(userEntity.profileImg).placeholder(R.drawable.img_default_profile).into(viewBinding.activityMainProfileImageView)
                    } else {
                        // Handle case where profileImg is null or empty
                        // For example, you can load a default image
                        Picasso.get().load(R.drawable.img_default_profile).into(viewBinding.activityMainProfileImageView)
                    }
                }
            }
        }
    }
}
