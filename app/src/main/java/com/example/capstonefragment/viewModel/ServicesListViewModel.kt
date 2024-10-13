package com.example.capstonefragment.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.capstonefragment.model.Cart
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class ServicesListViewModel : ViewModel() {
    val db = Firebase.firestore

    val addToCartFlag = MutableLiveData<Boolean>()
    val getCartByUserFlag = MutableLiveData<Boolean>()
    val cartLists = MutableLiveData<ArrayList<Cart>>()

    fun addToCart(serviceID: String, userID: String) {
        val cartList = db.collection("CartList")

        val cartMap = hashMapOf(
            "serviceID" to serviceID,
            "userID" to userID
        )

        cartList.document().set(cartMap)
            .addOnSuccessListener {
                addToCartFlag.postValue(true)
            }
            .addOnFailureListener {
                addToCartFlag.postValue(false)
            }
    }

    fun getCartByUser(userID: String){
        db.collection("CartList")
            .whereEqualTo("userID",userID)
            .get()
            .addOnSuccessListener { documents ->
                var cartList = ArrayList<Cart>()
                for (document in documents){
                    val cartItem = Cart(document.data["userID"].toString(),document.data["serviceID"].toString())
                    cartList.add(cartItem)
                    Log.d("CartList", cartItem.toString())
                }
                cartLists.postValue(cartList)
                getCartByUserFlag.postValue(true)

                Log.d("CartList", "getCartByUser: ${cartList}")
            }
            .addOnFailureListener {
                getCartByUserFlag.postValue(false)
            }
    }

    fun getServiceList(){
        val docRef = db.collection("")
    }

}