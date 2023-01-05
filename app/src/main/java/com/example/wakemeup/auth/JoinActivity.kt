package com.example.wakemeup

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.databinding.DataBindingUtil

import com.example.wakemeup.auth.AuthData
import com.example.wakemeup.databinding.ActivityJoinBinding
import com.example.wakemeup.retrofit.ResponseData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JoinActivity : AppCompatActivity() {

    private lateinit var binding : ActivityJoinBinding
    private lateinit var id : EditText
    private lateinit var password : EditText
    private lateinit var passwordCheck : EditText
    private lateinit var nameArea : EditText
    private lateinit var allCheckBtn : CheckBox
    private lateinit var firstCheckBtn : CheckBox
    private lateinit var secondCheckBtn : CheckBox
    private lateinit var thirdCheckBtn : CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_join)

        id = findViewById<EditText>(R.id.idArea)
        password = findViewById<EditText>(R.id.passwordArea)
        passwordCheck = findViewById<EditText>(R.id.passwordCheckArea)
        nameArea = findViewById<EditText>(R.id.nameArea)
        allCheckBtn = binding.allCheckBtn
        firstCheckBtn = binding.firstCheckBtn
        secondCheckBtn = binding.secondCheckBtn
        thirdCheckBtn = binding.thirdCheckBtn

        binding.goBackBtn.setOnClickListener{
            val intent = Intent(this, IntroActivity::class.java)
            startActivity(intent)
        }
        //각 버튼 눌렀을때 버튼상태 변경
        allCheckBtn.setOnClickListener { onCheckChanged(allCheckBtn) }
        firstCheckBtn.setOnClickListener { onCheckChanged(firstCheckBtn) }
        secondCheckBtn.setOnClickListener { onCheckChanged(secondCheckBtn) }
        thirdCheckBtn.setOnClickListener { onCheckChanged(thirdCheckBtn) }


        binding.createBtn.setOnClickListener {
            Log.d("JOIN", id.text.toString())
            Log.d("JOIN", password.text.toString())
            Log.d("JOIN", nameArea.text.toString())

            if(!password.text.toString().equals(passwordCheck.text.toString())) {
                Toast.makeText(baseContext, "비밀번호, 비밀번호 확인 값 불일치. \n 다시 입력해주세요", Toast.LENGTH_SHORT).show()

            }else if(!allCheckBtn.isChecked) {
                Toast.makeText(baseContext, "약관 내용에 전부 동의해주세요", Toast.LENGTH_SHORT).show()
            }
                else {
                //join Activity로 이동하기
                AuthData().service.join(id.text.toString(),password.text.toString(),nameArea.text.toString())?.enqueue(object : Callback<ResponseData> {
                    override fun onResponse(call: Call<ResponseData>, response: Response<ResponseData>) {
                        if(response.isSuccessful){
                            // 정상적으로 통신이 성공된 경우
                            var result: ResponseData? = response.body()
                            Log.d("YMC", "회원가입 onResponse 성공: " + result);
                            if(result?.success.toString().equals("true")) {
                                Toast.makeText(baseContext, "환영합니다.", Toast.LENGTH_SHORT).show()
                                var intent1 = Intent(baseContext, LoginActivity::class.java)
                                startActivity(intent1)
                            }else{
                                Toast.makeText(baseContext, "아이디 값이 입력되지 않았거나, 현재 존재하는 Id입니다.", Toast.LENGTH_SHORT).show()
                            }
                        }else{
                            // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                            Log.d("YMC", "회원가입 onResponse 실패")
                            Toast.makeText(baseContext, "서버가 불안정합니다.", Toast.LENGTH_SHORT).show()

                        }
                    }

                    override fun onFailure(call: Call<ResponseData>, t: Throwable) {
                        // 통신 실패 (인터넷 끊킴, 예외 발생 등 시스템적인 이유)
                        Log.d("YMC", "회원가입 onFailure 에러: " + t.message.toString());
                        Toast.makeText(baseContext, "인터넷이 불안정합니다.", Toast.LENGTH_SHORT).show()
                    }

                })
            }
        }
    }

    private fun onCheckChanged(compoundButton: CompoundButton) {

    when(compoundButton.id) {
        R.id.allCheckBtn -> {  //전체동의버튼
            if (allCheckBtn.isChecked) {
                firstCheckBtn.isChecked = true
                secondCheckBtn.isChecked = true
                thirdCheckBtn.isChecked = true
            }else {
                firstCheckBtn.isChecked = false
                secondCheckBtn.isChecked = false
                thirdCheckBtn.isChecked = false
            }
        }
        else -> {
            allCheckBtn.isChecked = (
                    firstCheckBtn.isChecked
                            && secondCheckBtn.isChecked
                            && thirdCheckBtn.isChecked)
        }
    }
    }

    // 키보드 내리기 함수
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        return true
    }


}
