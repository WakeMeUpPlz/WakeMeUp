package com.example.wakemeup

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.wakemeup.auth.AuthData
import com.example.wakemeup.auth.preferAuth
import com.example.wakemeup.databinding.ActivityLoginBinding
import com.example.wakemeup.retrofit.DBAlarmDataModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    lateinit var intent1 : Intent
    lateinit var id : EditText
    lateinit var password : EditText

    companion object {
        lateinit var preferences : preferAuth
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        var flag = false
        id = findViewById<EditText>(R.id.idArea)
        password = findViewById<EditText>(R.id.passwordArea)
        preferences = preferAuth(this)

        binding.goBackBtn.setOnClickListener{
            val intent = Intent(this, IntroActivity::class.java)
            startActivity(intent)
        }

        val loginBtn = findViewById<Button>(R.id.loginBtn)
        loginBtn.setOnClickListener {
            Log.d("LOGIN", id.text.toString())
            Log.d("LOGIN", password.text.toString())

            Thread {
                AuthData().service.login(id.text.toString(), password.text.toString())?.enqueue(object : Callback<com.example.wakemeup.retrofit.ResponseData> {
                    override fun onResponse(call: Call<com.example.wakemeup.retrofit.ResponseData>, response: Response<com.example.wakemeup.retrofit.ResponseData>) {
                        if(response.isSuccessful){
                            // ??????????????? ????????? ????????? ??????
                            var result: com.example.wakemeup.retrofit.ResponseData? = response.body()
                            Log.d("YMC", "onResponse ??????: " + result.toString());
                            if(result!!.success.toString().equals("true")) {
                                LoginActivity.preferences.setId("id", id.text.toString())
                                getData()
                            }else {
                                Toast.makeText(baseContext, "?????????, ???????????? ?????? ?????? ??????????????????.", Toast.LENGTH_SHORT).show()
                            }
                        }else{
                            // ????????? ????????? ??????(???????????? 3xx, 4xx ???)
                            Log.d("YMC", "onResponse ??????")
                            Toast.makeText(baseContext, "????????? ??????????????????.", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<com.example.wakemeup.retrofit.ResponseData>, t: Throwable) {
                        // ?????? ?????? (????????? ??????, ?????? ?????? ??? ??????????????? ??????)
                        Log.d("YMC", "onFailure ??????: " + t.message.toString());
                        Toast.makeText(baseContext, "???????????? ??????????????????.", Toast.LENGTH_SHORT).show()
                    }
                })
            }.start()
        }

    }

    fun getData(){

        AuthData().service.getAlarms(id.text.toString())?.enqueue(object : Callback<DBAlarmDataModel> {
            override fun onResponse(call: Call<DBAlarmDataModel>, response: Response<DBAlarmDataModel>) {
                if(response.isSuccessful){
                    // ??????????????? ????????? ????????? ??????
                    var result: DBAlarmDataModel? = response.body()
                    Log.d("YMC", "onResponse ??????: " + result.toString());
                    intent1 = Intent(baseContext, AlarmListActivity::class.java)
                    if(result!!.success.equals("true")) {
                        intent1.putExtra("listItem_code", "request")
                        intent1.putExtra("alarmList", result.result as Serializable)
                        startActivity(intent1)
                    }
                }else{
                    Toast.makeText(baseContext, "????????? ??????????????????.", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<DBAlarmDataModel>, t: Throwable) {
                // ?????? ?????? (????????? ??????, ?????? ?????? ??? ??????????????? ??????)
                Log.d("YMC", "onFailure ??????: " + t.message.toString());
                Toast.makeText(baseContext, "???????????? ??????????????????.", Toast.LENGTH_SHORT).show()

            }
        })
    }

    // ????????? ????????? ??????
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        return true
    }

}