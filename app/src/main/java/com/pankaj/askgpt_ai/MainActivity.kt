package com.pankaj.askgpt_ai

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.view.animation.Interpolator
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import io.paperdb.Paper
import java.util.Timer

class MainActivity : AppCompatActivity() {

    var emailData = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Paper.init(applicationContext)



        val maAuth = FirebaseAuth.getInstance()
        val dbRef = FirebaseDatabase.getInstance().reference

        val buttonStart = findViewById<MaterialButton>(R.id.buttonStart)
        val illustration = findViewById<ImageView>(R.id.illustration)
        val textDesc = findViewById<TextView>(R.id.textDesc)
        val textPolicy = findViewById<TextView>(R.id.textPolicy)
        val cardBack = findViewById<CardView>(R.id.cardBack)
        val cardBackImage = findViewById<ImageView>(R.id.cardBackImage)
        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val buttonContinue = findViewById<MaterialButton>(R.id.buttonContinue)
        val cardContinue = findViewById<CardView>(R.id.cardContinue)
        val googleCard = findViewById<CardView>(R.id.googleCard)
        val cardSignup = findViewById<CardView>(R.id.cardSignup)
        val firstnameEditText = findViewById<EditText>(R.id.firstnameEditText)
        val lastnameEditText = findViewById<EditText>(R.id.lastnameEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val confirmpassEditText = findViewById<EditText>(R.id.confirmpassEditText)
        val btnSignup = findViewById<MaterialButton>(R.id.btnSignup)


        cardContinue.visibility = CardView.GONE
        cardBack.visibility = CardView.GONE
        cardContinue.isEnabled = false
        cardBack.isEnabled = false
        cardSignup.isEnabled = false
        cardSignup.visibility = CardView.GONE

        firstnameEditText.isEnabled = false
        lastnameEditText.isEnabled = false
        passwordEditText.isEnabled = false
        confirmpassEditText.isEnabled = false
        btnSignup.isEnabled = false


        if(resources.getString(R.string.mode) == "Day"){
            //day stuff
            illustration.setImageResource(R.drawable.illustration)
            cardBackImage.setImageResource(R.drawable.back)
            emailEditText.setBackgroundResource(R.drawable.edittext)
            googleCard.setCardBackgroundColor(Color.parseColor("#EDEDED"))
        }
        else{
            //night stuff
            illustration.setImageResource(R.drawable.illustration_night)
            cardBackImage.setImageResource(R.drawable.back_night)
            emailEditText.setBackgroundResource(R.drawable.edittext_night)
            googleCard.setCardBackgroundColor(Color.parseColor("#606060"))
        }

        val illustration_anim = AnimationUtils.loadAnimation(this, R.anim.illustration_anim)
        val fade_out = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val fade_in = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val textpolicy_anim = AnimationUtils.loadAnimation(this, R.anim.textpolicy_anim)
        val illustrationanim_two = AnimationUtils.loadAnimation(this, R.anim.illustrationanim_two)
        val edittext1_anim = AnimationUtils.loadAnimation(this, R.anim.edittext1_anim)
        val edittext2_anim = AnimationUtils.loadAnimation(this, R.anim.edittext2_anim)
        val edittext3_anim = AnimationUtils.loadAnimation(this, R.anim.edittext3_anim)
        val edittext4_anim = AnimationUtils.loadAnimation(this, R.anim.edittext4_anim)


        edittext2_anim.duration = 1000
        edittext3_anim.duration = 1200
        edittext1_anim.duration = 800
        edittext4_anim.duration = 1400
        illustrationanim_two.duration = 600
        textpolicy_anim.duration = 600
        fade_out.duration = 600
        fade_in.duration = 600
        illustration_anim.duration = 600


        buttonStart.setOnClickListener(){
            illustration_anim.setInterpolator(null)
            illustration.startAnimation(illustration_anim)
            textDesc.startAnimation(fade_out)
            textpolicy_anim.setInterpolator(null)
            textPolicy.startAnimation(textpolicy_anim)
            buttonStart.startAnimation(fade_out)
            cardBack.startAnimation(fade_in)
            cardBack.visibility = CardView.VISIBLE
            cardContinue.startAnimation(fade_in)
            cardContinue.visibility = CardView.VISIBLE
            buttonStart.visibility = MaterialButton.GONE
            buttonStart.isEnabled = false
            cardContinue.isEnabled = true
            cardBack.isEnabled = true
        }

        cardBack.setOnClickListener(){
            illustration_anim.setInterpolator(ReverseInterpolator())
            illustration.startAnimation(illustration_anim)
            textDesc.startAnimation(fade_in)
            textpolicy_anim.setInterpolator(ReverseInterpolator())
            textPolicy.startAnimation(textpolicy_anim)
            buttonStart.startAnimation(fade_in)
            cardBack.startAnimation(fade_out)
            cardBack.visibility = CardView.GONE
            cardContinue.startAnimation(fade_out)
            cardContinue.visibility = CardView.GONE
            buttonStart.visibility = MaterialButton.VISIBLE
            buttonStart.isEnabled = true
            cardContinue.isEnabled = false


            if(cardSignup.visibility == CardView.VISIBLE){
                cardSignup.startAnimation(fade_out)
                cardSignup.visibility = CardView.GONE
                cardSignup.isEnabled = false
                firstnameEditText.isEnabled = false
                lastnameEditText.isEnabled = false
                passwordEditText.isEnabled = false
                confirmpassEditText.isEnabled = false
                btnSignup.isEnabled = false
                emailEditText.isEnabled = true
                buttonContinue.isEnabled = true
            }
            cardBack.isEnabled = false
        }

        buttonContinue.setOnClickListener(){
            val emailText = emailEditText.text.toString()
            if(emailText.isEmpty()){

            }else{
                cardContinue.isEnabled = false
                cardContinue.startAnimation(fade_out)
                illustration.startAnimation(illustrationanim_two)
                emailEditText.isEnabled = false
                buttonContinue.isEnabled = false
                cardSignup.isEnabled = true
                firstnameEditText.isEnabled = true
                lastnameEditText.isEnabled = true
                passwordEditText.isEnabled = true
                confirmpassEditText.isEnabled = true
                btnSignup.isEnabled = true
                cardSignup.visibility = CardView.VISIBLE
                cardSignup.startAnimation(fade_in)
                firstnameEditText.startAnimation(edittext1_anim)
                lastnameEditText.startAnimation(edittext2_anim)
                passwordEditText.startAnimation(edittext3_anim)
                confirmpassEditText.startAnimation(edittext4_anim)
                btnSignup.startAnimation(edittext4_anim)
                emailData = emailText
            }
        }

        btnSignup.setOnClickListener(){
            val firstName = firstnameEditText.text.toString()
            val lastName = lastnameEditText.text.toString()
            val passwordText = passwordEditText.text.toString()
            val confirmPass = confirmpassEditText.text.toString()

            if(firstName.isEmpty() || lastName.isEmpty() || passwordText.isEmpty() || confirmPass.isEmpty()){

            }
            else if(!passwordText.equals(confirmPass)){
                confirmpassEditText.setError("Password doesn't match!")
            }
            else{
                try {
                    maAuth.createUserWithEmailAndPassword(emailData, passwordText)
                        .addOnCompleteListener {
                            maAuth.signInWithEmailAndPassword(emailData, passwordText)
                            val user = maAuth.currentUser
                            if (user != null) {
                                dbRef.child("Users").child(user.uid).child("FirstName").setValue(firstName)
                                dbRef.child("Users").child(user.uid).child("LastName").setValue(lastName)
                                dbRef.child("Users").child(user.uid).child("email").setValue(emailData)

                                val intent = Intent(applicationContext, HomePage::class.java)
                                startActivity(intent)
                                finish()
                            }
                            else{
                                Toast.makeText(applicationContext, "Error signing up, try again!", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
                catch (e: Exception){
                    Log.d("dax", e.message.toString())
                }
            }
        }

    }
    class ReverseInterpolator : Interpolator {
        override fun getInterpolation(paramFloat: Float): Float {
            return Math.abs(paramFloat - 1f)
        }
    }
}