package com.example.capstonefragment.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.capstonefragment.R
import com.example.capstonefragment.databinding.FragmentServicesListBinding
import com.example.capstonefragment.databinding.FragmentSignInBinding
import com.example.capstonefragment.viewModel.ServicesListViewModel
import com.example.capstonefragment.viewModel.SignInViewModel
import com.google.firebase.auth.FirebaseAuth

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 * Use the [ServicesListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ServicesListFragment : Fragment() {
    private lateinit var binding: FragmentServicesListBinding
    lateinit var firebaseAuth: FirebaseAuth

    //ViewModel
    private lateinit var servicesListViewModel: ServicesListViewModel
    private lateinit var signInViewModel: SignInViewModel

    private lateinit var userEmail: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_services_list, container, false)

        binding = FragmentServicesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        //notice here the ViewModelProvider should be requireActivity() then it can been observed by host Activity
        servicesListViewModel =
            ViewModelProvider(requireActivity())[ServicesListViewModel::class.java]
        signInViewModel = ViewModelProvider(requireActivity())[SignInViewModel::class.java]


        binding.tvUserInfo.text = firebaseAuth.currentUser?.email.toString()
        binding.btnSignOut.setOnClickListener {
            signInViewModel.signOut()
        }

        binding.btnAdd.setOnClickListener{
//            servicesListViewModel.addToCart("123","Test add to cart")
            servicesListViewModel.getCartByUser("123")
        }

        servicesListViewModel.getCartByUserFlag.observe(requireActivity()){ addedFlag ->
          if (!addedFlag){
              binding.tvUserInfo.text = "Get Cart Info Error"
          }
        }

        servicesListViewModel.cartLists.observe(requireActivity()){ cartLists ->
            var value: String = ""
            for (cart in cartLists){
                value += "${cart.userID}/${cart.serviceID} \n"
            }
            binding.textView3.text = value
        }

    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ServicesListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ServicesListFragment().apply {

            }
    }
}