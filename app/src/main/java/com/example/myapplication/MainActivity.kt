package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.Writer


class MainActivity : AppCompatActivity() {

    var userLogin : EditText? = null
    var userPassword : EditText? = null
    var loginBtn: Button? = null
    var fioEditText : EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        userLogin = findViewById(R.id.editTextUsername)
        userPassword = findViewById(R.id.editTextPassword)
        loginBtn = findViewById(R.id.buttonLogin)
        fioEditText = findViewById(R.id.fioEditText)
    }

    fun register(view: View) {
        val login = userLogin?.text.toString()
        val pass = userPassword?.text.toString()
        val fio = fioEditText?.text.toString()

        val file = File(filesDir, "users.json")

        val jsonString = if (file.exists()) {
            file.bufferedReader().use {
                it.readText()
            }
        } else {
            "{\"users\":[]}"
        }

        val jsonObject = JSONObject(jsonString)
        val usersArray = jsonObject.getJSONArray("users")
        println(jsonObject.toString())
        for (i in 0 until usersArray.length()) {
            val userObject = usersArray.getJSONObject(i)
            val existingLogin = userObject.getString("login")
            if (existingLogin == login) {
                Toast.makeText(this, "Пользователь с таким логином уже существует", Toast.LENGTH_SHORT).show()
                return
            }
        }

        val newUser = JSONObject().apply {
            put("login", login)
            put("pass", pass)
            put("fio", fio)
        }
        usersArray.put(newUser)
        jsonObject.put("users", usersArray)

        try {
            println(jsonObject.toString())
            val fileOutputStream = openFileOutput("users.json", Context.MODE_PRIVATE)
            fileOutputStream.write(jsonObject.toString().toByteArray())
            fileOutputStream.close()
            Toast.makeText(this, "Пользователь $fio успешно зарегистрирован", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Ошибка при сохранении пользователя", Toast.LENGTH_SHORT).show()
        }

        try {
            val writer: Writer = BufferedWriter(FileWriter(file))
            writer.write(jsonObject.toString())
            writer.close()
            Toast.makeText(this, "Пользователь $fio успешно зарегистрирован", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Ошибка при сохранении пользователя", Toast.LENGTH_SHORT).show()
        }
    }


    fun login(View: View) {
        if(userLogin == null || userPassword == null){
            Toast.makeText(this, "поля пустые",
                Toast.LENGTH_SHORT).show()
            return
        }else{

        }

        val login = userLogin?.text.toString();
        val pass = userPassword?.text.toString();

        val file = File(filesDir, "users.json")

        val jsonString = if (file.exists()) {
            file.bufferedReader().use {
                it.readText()
            }
        } else {
            "{\"users\":[]}"
        }

        val jsonObject = JSONObject(jsonString)
        var isAuth = false
        val usersArray = jsonObject.getJSONArray("users")

        for (i in 0 until usersArray.length()) {
            val userObject = usersArray.getJSONObject(i)
            val userJSON = userObject.getString("login")
            val passJSON = userObject.getString("pass")
            val fioJSON = userObject.getString("fio")
            if (userJSON == login && passJSON == pass) {
                Toast.makeText(this, "$fioJSON вошел успешно",
                    Toast.LENGTH_SHORT).show()
                isAuth = true
                val newPage = Intent(this, MainActivity2::class.java)
                startActivity(newPage)
                break
            }
        }
        if (isAuth == false) {
            Toast.makeText(this, "Скорее всего, данные неверны",
                Toast.LENGTH_SHORT).show()
        }
    }
}