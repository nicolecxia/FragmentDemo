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

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    public lateinit var firebaseAuth: FirebaseAuth

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

      //if user already login in, no need show the login fragment
        if (firebaseAuth.currentUser != null){
            jumpToServicesFlagment()
        }

        binding.button2.setOnClickListener{
            firebaseAuth.signInWithEmailAndPassword("123@qq.com", "123456")
                .addOnCompleteListener {  it ->
                    if (it.isSuccessful) {
                        binding.textView2.text = "successed"

                    } else {
                        binding.textView2.text = "failed"
                    }
                }
//            signInViewModel.userEmail.value = "testLivedata"
        }


        listenSignInLiveData()
    }

    private fun jumpToServicesFlagment(){
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        if (currentFragment is SignInFragment){
            val trans = supportFragmentManager.beginTransaction().add(R.id.fragmentContainer,ServicesListFragment())
            trans.addToBackStack(null)
            trans.commit()
        }
    }

    private fun listenSignInLiveData() {
        signInViewModel.userEmail.observe(this){ userEmail ->
            binding.textView2.text = userEmail
        }

        signInViewModel.signInErrorMSG.observe(this){ signInErrorMSG ->
            binding.textView2.text = signInErrorMSG
        }

        signInViewModel.signInFlag.observe(this){ flag ->
            if (flag){
                jumpToServicesFlagment()
            }
        }
    }
}