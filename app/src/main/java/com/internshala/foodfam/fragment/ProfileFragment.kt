package com.internshala.foodfam.fragment


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.internshala.foodfam.R

class ProfileFragment : Fragment() {

    lateinit var name: TextView
    lateinit var phone: TextView
    lateinit var email: TextView
    lateinit var address: TextView
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_profile, container, false)

        sharedPreferences=(activity as Context).getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)

        name=view.findViewById(R.id.txtName)
        phone=view.findViewById(R.id.txtPhone)
        email=view.findViewById(R.id.txtEmail)
        address=view.findViewById(R.id.txtAddress)

        name.text=sharedPreferences.getString("name","User")
        phone.text=sharedPreferences.getString("mobile","8800616469")
        email.text=sharedPreferences.getString("email","abc@xyz.com")
        address.text=sharedPreferences.getString("address","delhi")



        return view
    }


}
