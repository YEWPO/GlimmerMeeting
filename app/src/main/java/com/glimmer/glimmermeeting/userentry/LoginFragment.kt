package com.glimmer.glimmermeeting.userentry

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.os.Message.obtain
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.glimmer.glimmermeeting.MainActivity
import com.glimmer.glimmermeeting.R
import com.squareup.moshi.JsonClass
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class LoginFragment : Fragment(R.layout.login_layout) {

    private lateinit var usernameView: TextView
    private lateinit var passwordView: EditText
    private lateinit var loginButton: Button

    @JsonClass(generateAdapter = true)
    data class LoginJson(
        var message: String,
        var token: String?
    )

    private fun checkInput(username: String, password: String) {
        if (username == "") {
            Toast.makeText(this.context, "用户名为空", Toast.LENGTH_SHORT).show()
            return
        }

        if (password == "") {
            Toast.makeText(this.context, "密码为空", Toast.LENGTH_SHORT).show()
            return
        }

        postLogin(username, password)
    }

    private val loginSuccessHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)

            Toast.makeText(context, msg.data.getString("message"), Toast.LENGTH_SHORT).show()

            if (msg.data.getBoolean("state")) {
                Navigation.findNavController(requireView()).navigate(R.id.userentryFragment_to_appFragment)
            }
        }
    }

    private fun postLogin(username: String, password: String) {
        val loginJsonAdapter = MainActivity().moshi.adapter(LoginJson::class.java)

        val loginBody = FormBody.Builder()
            .add("username", username)
            .add("password", password)
            .build()
        val loginRequest = Request.Builder()
            .url("http://api.mcyou.cc:2023/login")
            .post(loginBody)
            .build()

        MainActivity().okHttpClient.newCall(loginRequest).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("Login", e.printStackTrace().toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val loginState = response.isSuccessful
                val loginJson = loginJsonAdapter.fromJson(response.body!!.string())

                val loginMessage = obtain()
                val loginBundle = Bundle()
                loginBundle.putBoolean("state", loginState)
                loginBundle.putString("message", loginJson!!.message)
                loginBundle.putString("token", loginJson.token)
                loginMessage.data = loginBundle

                loginSuccessHandler.sendMessage(loginMessage)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        usernameView = view.findViewById(R.id.loginUsername)
        passwordView = view.findViewById(R.id.loginPassword)
        loginButton = view.findViewById(R.id.loginButton)

        loginButton.setOnClickListener {
            val username = usernameView.text.toString()
            val password = passwordView.text.toString()

            checkInput(username, password)
        }
    }
}