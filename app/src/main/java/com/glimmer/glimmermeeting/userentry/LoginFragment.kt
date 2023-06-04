package com.glimmer.glimmermeeting.userentry

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.os.Message.obtain
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.glimmer.glimmermeeting.MainActivity
import com.glimmer.glimmermeeting.R
import com.glimmer.glimmermeeting.app.AppFragment
import com.squareup.moshi.JsonClass
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

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
            Toast.makeText(context, "用户名为空", Toast.LENGTH_SHORT).show()
            return
        }

        if (password == "") {
            Toast.makeText(context, "密码为空", Toast.LENGTH_SHORT).show()
            return
        }

        postLogin(username, password)
    }

    private val loginSuccessHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)

            val loginJsonAdapter = MainActivity().moshi.adapter(LoginJson::class.java)

            val loginJson = msg.data.getString("json")?.let { loginJsonAdapter.fromJson(it) }

            Toast.makeText(context, loginJson!!.message, Toast.LENGTH_SHORT).show()

            if (msg.data.getBoolean("state")) {
                val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
                with(sharedPref.edit()) {
                    putString("loginToken", loginJson.token)

                    val dataFormat = SimpleDateFormat("yyyy-MM-dd")
                    putString("loginTime", dataFormat.format(Date()))

                    apply()
                }

                val loginAction = UserentryFragmentDirections.userentryFragmentToAppFragment(loginJson.token!!)
                Navigation.findNavController(requireView()).navigate(loginAction)
            }
        }
    }

    private fun postLogin(username: String, password: String) {
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

                val loginMessage = obtain()
                val loginBundle = Bundle()
                loginBundle.putBoolean("state", loginState)
                loginBundle.putString("json", response.body!!.string())
                loginMessage.data = loginBundle

                loginSuccessHandler.sendMessage(loginMessage)
            }
        })
    }

    fun hideSoftInput(view: View) {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        usernameView = view.findViewById(R.id.loginUsername)
        passwordView = view.findViewById(R.id.loginPassword)
        loginButton = view.findViewById(R.id.loginButton)

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val loginToken = sharedPref.getString("loginToken", null)

        if (loginToken != null) {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            val loginDate = sharedPref.getString("loginTime", null)?.let { dateFormat.parse(it) }

            val loginDiff = (Date().time - loginDate!!.time) / (1000 * 60 * 60 * 24)

            if (loginDiff <= 30) {
                val loginAction = UserentryFragmentDirections.userentryFragmentToAppFragment(loginToken)
                requireParentFragment().findNavController().navigate(loginAction)
            }
        }

        loginButton.setOnClickListener {
            hideSoftInput(loginButton)

            val username = usernameView.text.toString()
            val password = passwordView.text.toString()

            checkInput(username, password)
        }
    }
}