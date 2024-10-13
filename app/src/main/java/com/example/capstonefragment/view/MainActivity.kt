package com.example.capstonefragment.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.capstonefragment.R
import com.example.capstonefragment.databinding.ActivityMainBinding
import com.example.capstonefragment.viewModel.SignInViewModel
import com.google.firebase.auth.FirebaseAuth
import android.content.Context
import com.example.capstonefragment.viewModel.NetworkLiveData

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var firebaseAuth: FirebaseAuth

    //ViewModel
    private lateinit var signInViewModel: SignInViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        firebaseAuth = FirebaseAuth.getInstance()
        signInViewModel = ViewModelProvider(this)[SignInViewModel::class.java]



        listenSignInLiveData()
    }

    override fun onStart() {
        super.onStart()

        if (firebaseAuth.currentUser != null) {
            jumpToServicesFragment()
        } else {
            jumpToSignInFragment()
        }

    }

    private fun jumpToServicesFragment() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        if (currentFragment !is ServicesListFragment) {
            val trans = supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, ServicesListFragment())
            trans.addToBackStack(null)
            trans.commit()
        }
    }

    private fun jumpToSignInFragment() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        if (currentFragment !is SignInFragment) {
            val trans = supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, SignInFragment())
            trans.addToBackStack(null)
            trans.commit()
        }
    }

    private fun listenSignInLiveData() {
        signInViewModel.userEmail.observe(this) { userEmail ->
            val sharedata = getSharedPreferences(
                "AccountInfo",
                Context.MODE_PRIVATE
            )  //create shareddate named ‘counterdata’
            val editor = sharedata.edit()
            editor.putString("email", userEmail)   //put date into shareddate.edit, key-vakue pairs
            editor.commit()
        }

        signInViewModel.signInFlag.observe(this) { flag ->
            if (flag) {
                jumpToServicesFragment()
            } else {
                jumpToSignInFragment()
            }
        }
    }
}