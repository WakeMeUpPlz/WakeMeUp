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
                            // 정상적으로 통신이 성공된 경우
                            var result: com.example.wakemeup.retrofit.ResponseData? = response.body()
                            Log.d("YMC", "onResponse 성공: " + result.toString());
                            if(result!!.success.toString().equals("true")) {
                                LoginActivity.preferences.setId("id", id.text.toString())
                                getData()
                            }else {
                                Toast.makeText(baseContext, "아이디, 비밀번호 값을 다시 확인해주세요.", Toast.LENGTH_SHORT).show()
                            }
                        }else{
                            // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                            Log.d("YMC", "onResponse 실패")
                            Toast.makeText(baseContext, "서버가 불안정합니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<com.example.wakemeup.retrofit.ResponseData>, t: Throwable) {
                        // 통신 실패 (인터넷 끊킴, 예외 발생 등 시스템적인 이유)
                        Log.d("YMC", "onFailure 에러: " + t.message.toString());
                        Toast.makeText(baseContext, "인터넷이 불안정합니다.", Toast.LENGTH_SHORT).show()
                    }
                })
            }.start()
        }

    }

    fun getData(){

        AuthData().service.getAlarms(id.text.toString())?.enqueue(object : Callback<DBAlarmDataModel> {
            override fun onResponse(call: Call<DBAlarmDataModel>, response: Response<DBAlarmDataModel>) {
                if(response.isSuccessful){
                    // 정상적으로 통신이 성공된 경우
                    var result: DBAlarmDataModel? = response.body()
                    Log.d("YMC", "onResponse 성공: " + result.toString());
                    intent1 = Intent(baseContext, AlarmListActivity::class.java)
                    if(result!!.success.equals("true")) {
                        intent1.putExtra("listItem_code", "request")
                        intent1.putExtra("alarmList", result.result as Serializable)
                        startActivity(intent1)
                    }
                }else{
                    Toast.makeText(baseContext, "서버가 불안정합니다.", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<DBAlarmDataModel>, t: Throwable) {
                // 통신 실패 (인터넷 끊킴, 예외 발생 등 시스템적인 이유)
                Log.d("YMC", "onFailure 에러: " + t.message.toString());
                Toast.makeText(baseContext, "인터넷이 불안정합니다.", Toast.LENGTH_SHORT).show()

            }
        })
    }

    // 키보드 내리기 함수
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        return true
    }

}