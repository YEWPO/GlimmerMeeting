package com.glimmer.glimmermeeting.userentry

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.glimmer.glimmermeeting.R

class LoginFragment : Fragment(R.layout.login_layout) {

    private lateinit var usernameView: TextView
    private lateinit var passwordView: EditText
    private lateinit var loginButton: Button

    private fun checkInput(username: String, password: String): Boolean {
        if (username == "") {
            Toast.makeText(this.context, "用户名为空", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password == "") {
            Toast.makeText(this.context, "密码为空", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        usernameView = view.findViewById(R.id.loginUsername)
        passwordView = view.findViewById(R.id.loginPassword)
        loginButton = view.findViewById(R.id.loginButton)

        loginButton.setOnClickListener {
            val username = usernameView.text.toString()
            val password = passwordView.text.toString()

            if (checkInput(username, password)) {
                Navigation.findNavController(view).navigate(R.id.userentryFragment_to_appFragment)
            }
        }
    }
}