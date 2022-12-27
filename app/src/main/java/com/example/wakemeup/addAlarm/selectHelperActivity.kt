package com.example.wakemeup.addAlarm

import android.Manifest.permission
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import com.example.wakemeup.R
import com.example.wakemeup.databinding.ActivitySelectHelperBinding


class selectHelperActivity : AppCompatActivity() {

    lateinit var adapter : SimpleCursorAdapter
    private lateinit var binding : ActivitySelectHelperBinding
    private lateinit var listview : ListView
    private lateinit var cursor : Cursor
    private lateinit var helperNumArea : EditText
    private lateinit var searchContactArea : EditText
    private lateinit var number : String

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_helper)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_helper)
        listview = binding.contactListview
        helperNumArea = binding.helperNumArea
        searchContactArea = binding.searchInContacts

        searchContactArea.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                // input창에 문자를 입력할때마다 호출된다.
                // search 메소드를 호출한다.
                val text: String = searchContactArea.getText().toString()
//                search(text)
            }
        })

        if (checkSelfPermission(permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(permission.READ_CONTACTS), 1)
            return
        } else {
            showData()
        }

        binding.saveBtn.setOnClickListener {
            number = helperNumArea.text.toString()
            val intent = Intent(this, addAlarmActivity::class.java)
            intent.apply {
                putExtra("number",number)
            }
            setResult(RESULT_OK, intent)
            finish()
        }


        binding.exitToAddAlarm.setOnClickListener {
            finish()
        }
    }

    fun showData() {
        cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null)!!

        if(!this::adapter.isInitialized) {

            val from = arrayOf(
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
            )
            val to = intArrayOf(android.R.id.text1, android.R.id.text2)

            adapter =
                SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, from, to, 0)
            listview.setAdapter(adapter)


            listview.setOnItemClickListener { parent, view, position, id ->
                val helper_idx : Int
                val number_idx : Int
                val helper : String
                helper_idx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                number_idx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                helper = cursor.getString(helper_idx)
                number = cursor.getString(number_idx)
                helperNumArea.setText(number)
            }


        } else {
            adapter.changeCursor(cursor)
            adapter.notifyDataSetChanged()
            Toast.makeText(this, "toaste for show", Toast.LENGTH_SHORT).show()
            cursor.close()
        }

    }


    //permission 받기
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showData()
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