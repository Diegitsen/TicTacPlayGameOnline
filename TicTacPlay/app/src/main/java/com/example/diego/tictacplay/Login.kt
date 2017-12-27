package com.example.diego.tictacplay

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {

    private var mAuth:FirebaseAuth? = null
    private var database = FirebaseDatabase.getInstance()
    private var myRef = database.reference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //connect with the database
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance()
    }

    fun eLogin(view : View)
    {
        loginToFirebase(etEmail.text.toString(), etPassword.text.toString())
    }

    fun loginToFirebase(email:String, password:String)
    {
        mAuth!!.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this)
                {
                    task->
                    if(task.isSuccessful)
                    {
                        var currentUser = mAuth!!.currentUser
                        Toast.makeText(applicationContext, "Exito!", Toast.LENGTH_SHORT).show()
                        if(currentUser!=null)
                            //save in database
                            myRef.child("Users").child(splitString(currentUser.email.toString())).child("Pedido").setValue(currentUser.uid)
                        loadMain()
                    }
                    else
                    {
                        Toast.makeText(applicationContext, "Fail!", Toast.LENGTH_SHORT).show()
                    }

                }
    }

    override fun onStart() {
        super.onStart()
        loadMain()
    }

    /*si el current user es nulo se regitra en el login, de locontario se obvia
    ese paso(puesto que ya se hico previamente) y se pasa de frente al juego*/
    fun loadMain()
    {
        var currentUser = mAuth!!.currentUser

        if(currentUser != null)
        {

            var intent = Intent(this, MainActivity::class.java)
            intent.putExtra("email", currentUser.email)
            intent.putExtra("uid", currentUser.uid)
            startActivity(intent)
            finish()
        }
    }

    fun splitString(str:String):String
    {
        var split = str.split("@")
        return split[0]
    }
/*
    protected fun eAceptar(view:View)
    {
        var userEmail = etEmail.text
    }

    protected fun ePedido(view: View)
    {
        var userEmail = etEmail.text
    }*/
}





