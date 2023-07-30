package com.pankaj.askgpt_ai

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.view.animation.Interpolator
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.postDelayed
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import io.paperdb.Paper


class MainActivity : AppCompatActivity() {

    var emailData = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Paper.init(applicationContext)



        val maAuth = FirebaseAuth.getInstance()
        val dbRef = FirebaseDatabase.getInstance().reference




        val cardAi = findViewById<CardView>(R.id.cardAi)
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
        val cardLoad = findViewById<CardView>(R.id.cardLoad)
        val cardSplash = findViewById<CardView>(R.id.cardSplash)
        val parent = findViewById<ConstraintLayout>(R.id.parent)
        val cardLogin = findViewById<CardView>(R.id.cardLogin)
        val emailText = findViewById<EditText>(R.id.emailText)
        val passText = findViewById<EditText>(R.id.passwordText)
        val loginbtn = findViewById<MaterialButton>(R.id.loginBtn)
        val forgotPassText = findViewById<TextView>(R.id.forgotPassText)


        cardLogin.visibility = CardView.GONE
        emailText.isEnabled = false
        passText.isEnabled = false
        loginbtn.isEnabled = false
        cardLoad.visibility = CardView.GONE
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
            firstnameEditText.setBackgroundResource(R.drawable.edittext)
            lastnameEditText.setBackgroundResource(R.drawable.edittext)
            passwordEditText.setBackgroundResource(R.drawable.edittext)
            confirmpassEditText.setBackgroundResource(R.drawable.edittext)
            cardSplash.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
            parent.setBackgroundColor(Color.parseColor("#FFFFFF"))
            emailText.setBackgroundResource(R.drawable.edittext)
            passText.setBackgroundResource(R.drawable.edittext)
        }
        else{
            //night stuff
            illustration.setImageResource(R.drawable.illustration_night)
            cardBackImage.setImageResource(R.drawable.back_night)
            emailEditText.setBackgroundResource(R.drawable.edittext_night)
            googleCard.setCardBackgroundColor(Color.parseColor("#606060"))
            firstnameEditText.setBackgroundResource(R.drawable.edittext_night)
            lastnameEditText.setBackgroundResource(R.drawable.edittext_night)
            passwordEditText.setBackgroundResource(R.drawable.edittext_night)
            confirmpassEditText.setBackgroundResource(R.drawable.edittext_night)
            cardSplash.setCardBackgroundColor(Color.parseColor("#000000"))
            parent.setBackgroundColor(Color.parseColor("#000000"))
            emailText.setBackgroundResource(R.drawable.edittext_night)
            passText.setBackgroundResource(R.drawable.edittext_night)
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
        val putawaygarbage = AnimationUtils.loadAnimation(this, R.anim.putawaygarbage)
        val cardloading_anim = AnimationUtils.loadAnimation(this, R.anim.cardloading_anim)
        val illustrationanim_three = AnimationUtils.loadAnimation(this, R.anim.illustrationanim_three)
        val bottom_to_top = AnimationUtils.loadAnimation(this, R.anim.bottom_to_top)
        val top_to_bottom = AnimationUtils.loadAnimation(this, R.anim.top_to_bottom)
        val cardloadanim_up = AnimationUtils.loadAnimation(this, R.anim.cardloadanim_up)

        bottom_to_top.duration = 600
        top_to_bottom.duration = 600


        cardloadanim_up.duration = 400
        cardloading_anim.duration = 400
        putawaygarbage.duration = 0
        edittext2_anim.duration = 1000
        edittext3_anim.duration = 1200
        edittext1_anim.duration = 800
        edittext4_anim.duration = 1400
        illustrationanim_two.duration = 600
        textpolicy_anim.duration = 600
        fade_out.duration = 600
        fade_in.duration = 600
        illustrationanim_three.duration = 600
        illustration_anim.duration = 600



        val cogwheelanim1 = AnimationUtils.loadAnimation(this, R.anim.cogwheelanim1)
        cogwheelanim1.duration = 1500
        val cogwheelanim2 = AnimationUtils.loadAnimation(this, R.anim.cogwheelanim2)
        cogwheelanim2.duration = 1500
        cardSplash.visibility = CardView.GONE
        val internetCard = findViewById<CardView>(R.id.internetCard)
        val retry_internet = findViewById<CardView>(R.id.retry_internet)
        val cancel_internet = findViewById<CardView>(R.id.cancel_internet)

        retry_internet.setOnClickListener{
            if(isNetworkAvailable()){
                internetCard.startAnimation(top_to_bottom)
                internetCard.visibility = View.GONE
                val intent= Intent(applicationContext, MainActivity::class.java)
                finishAffinity()
                startActivity(intent)
            }
            else{
                Toast.makeText(applicationContext, "No connection!", Toast.LENGTH_SHORT).show()
            }
        }
        cancel_internet.setOnClickListener{
            finishAffinity()
        }

        internetCard.visibility = View.GONE

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            cardSplash.visibility = CardView.VISIBLE
        }, 100)

        handler.postDelayed({
            val emailText = Paper.book().read(DatabaseModule().emailKey, "emailKey")
            val passText = Paper.book().read(DatabaseModule().passKey, "passKey")
            if (emailText != null && passText != null) {
                if(emailText.equals("emailKey") || passText.equals("passKey")){
                    cardSplash.startAnimation(fade_out)
                    cardAi.startAnimation(fade_out)
                    cardSplash.setBackgroundColor(Color.parseColor("#0000FFFF"))
                    cardAi.visibility = CardView.GONE
                    cardSplash.visibility = CardView.GONE
                    cardSplash.isEnabled = false
                }
                else{
                    if(isNetworkAvailable()) {


                        maAuth.signInWithEmailAndPassword(emailText, passText)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    val intent = Intent(applicationContext, HomePage::class.java)
                                    startActivity(intent)
                                    handler.postDelayed({
                                        cardAi.startAnimation(putawaygarbage)

                                    }, 2200)
                                    finish()
                                } else {
                                    cardAi.startAnimation(fade_out)
                                    cardSplash.startAnimation(fade_out)
                                    cardSplash.setBackgroundColor(Color.parseColor("#0000FFFF"))
                                    cardAi.visibility = CardView.GONE
                                    cardSplash.visibility = CardView.GONE
                                    cardSplash.isEnabled = false
                                    cardAi.startAnimation(putawaygarbage)

                                    cardSplash.startAnimation(putawaygarbage)
                                    cardSplash.visibility = View.GONE
                                }
                            }
                    }
                    else{
                        internetCard.visibility = View.VISIBLE
                        internetCard.startAnimation(bottom_to_top)
                    }
                }
            }
        }, 2000)




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

            if(cardContinue.visibility == CardView.VISIBLE){
                illustration_anim.setInterpolator(ReverseInterpolator())
                illustration.startAnimation(illustration_anim)
            }
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

                illustration.startAnimation(illustrationanim_three)
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

            if(cardLogin.visibility == CardView.VISIBLE){

                illustration.startAnimation(illustrationanim_three)
                cardLogin.startAnimation(fade_out)
                cardLogin.visibility = CardView.GONE
                emailText.isEnabled = false
                passText.isEnabled = false
                loginbtn.isEnabled = false
                emailEditText.isEnabled = true
                buttonContinue.isEnabled = true
            }
            cardBack.isEnabled = false
        }

        buttonContinue.setOnClickListener(){
            val emailTextData = emailEditText.text.toString()
            cardLoad.visibility = View.VISIBLE
            cardLoad.startAnimation(cardloadanim_up)
            if(emailTextData.isEmpty()){

            }else{
                try {
                    maAuth.fetchSignInMethodsForEmail(emailTextData).addOnCompleteListener {

                        cardLoad.visibility = View.GONE
                        cardLoad.startAnimation(cardloading_anim)
                        if (it.isSuccessful) {
                            if (it.result.signInMethods!!.isEmpty()) {

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
                                emailData = emailTextData
                            } else {

                                cardContinue.isEnabled = false
                                cardContinue.startAnimation(fade_out)
                                illustration.startAnimation(illustrationanim_two)
                                emailEditText.isEnabled = false
                                buttonContinue.isEnabled = false
                                cardLogin.visibility = CardView.VISIBLE
                                cardLogin.startAnimation(fade_in)
                                emailText.startAnimation(edittext1_anim)
                                passText.startAnimation(edittext2_anim)
                                loginbtn.startAnimation(edittext4_anim)
                                forgotPassText.startAnimation(edittext3_anim)
                                emailText.setText(emailTextData)
                                emailText.isEnabled = false
                                passText.isEnabled = true
                                loginbtn.isEnabled = true
                                emailData = emailTextData




                            }
                        }
                    }
                }
                catch (e: Exception){
                    Log.d("dax", e.message.toString())

                }

            }
        }

        loginbtn.setOnClickListener(){
            val passTextData = passText.text.toString()
            val emailTextData = emailText.text.toString()
            cardLoad.visibility = View.VISIBLE
            cardLoad.startAnimation(cardloadanim_up)

            if(passTextData.isEmpty() && emailTextData.isEmpty()){
                cardLoad.visibility = View.GONE
                cardLoad.startAnimation(cardloading_anim)
            }
            else{
                if(passTextData.length < 6){
                    passText.setError("Atleast 6 Characters!")
                    cardLoad.visibility = View.GONE
                    cardLoad.startAnimation(cardloading_anim)
                }else{
                    cardLoad.visibility = View.GONE
                    cardLoad.startAnimation(cardloading_anim)
                    maAuth.signInWithEmailAndPassword(emailTextData, passTextData).addOnCompleteListener{
                        if(it.isSuccessful){

                            Paper.book().write(DatabaseModule().emailKey, emailTextData)
                            Paper.book().write(DatabaseModule().passKey, passTextData)
                            cardLoad.visibility = CardView.GONE
                            cardLoad.startAnimation(cardloading_anim)
                            val intent = Intent(applicationContext, HomePage::class.java)
                            startActivity(intent)
                            finish()
                        }
                        else{
                            Toast.makeText(applicationContext, "Error logging in, try again!", Toast.LENGTH_SHORT).show()
                            cardLoad.visibility = View.GONE
                            cardLoad.startAnimation(cardloading_anim)
                        }
                    }
                }
            }
        }

        btnSignup.setOnClickListener(){


            cardLoad.visibility = CardView.VISIBLE
            cardLoad.startAnimation(cardloadanim_up)
            val firstName = firstnameEditText.text.toString()
            val lastName = lastnameEditText.text.toString()
            val passwordText = passwordEditText.text.toString()
            val confirmPass = confirmpassEditText.text.toString()

            if(firstName.isEmpty() || lastName.isEmpty() || passwordText.isEmpty() || confirmPass.isEmpty()){
                cardLoad.visibility = CardView.GONE
                cardLoad.startAnimation(cardloading_anim)

            }
            else if(!passwordText.equals(confirmPass)){
                confirmpassEditText.setError("Password doesn't match!")
                cardLoad.visibility = CardView.GONE
                cardLoad.startAnimation(cardloading_anim)
            }
            else{
                try {

                    maAuth.createUserWithEmailAndPassword(emailData, passwordText)
                        .addOnCompleteListener {
                            if(it.isSuccessful){
                                maAuth.signInWithEmailAndPassword(emailData, passwordText)
                                val user = maAuth.currentUser
                                if (user != null) {
                                    dbRef.child("Users").child(user.uid).child("FirstName").setValue(firstName)
                                    dbRef.child("Users").child(user.uid).child("LastName").setValue(lastName)
                                    dbRef.child("Users").child(user.uid).child("email").setValue(emailData)
                                    dbRef.child("Users").child(user.uid).child("verification").setValue("unverified")
                                    dbRef.child("Users").child(user.uid).child("saved").setValue("")
                                    dbRef.child("Users").child(user.uid).child("profile").setValue("https://firebasestorage.googleapis.com/v0/b/askgpt-ai.appspot.com/o/personImage.png?alt=media&token=b4fb1713-a619-44bd-9033-d3fd50963dd5")
                                    Paper.book().write(DatabaseModule().emailKey, emailData)
                                    Paper.book().write(DatabaseModule().passKey, passwordText)
                                    cardLoad.visibility = CardView.GONE
                                    cardLoad.startAnimation(cardloading_anim)
                                    val intent = Intent(applicationContext, HomePage::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                                else{
                                    Toast.makeText(applicationContext, "Error signing up, try again!", Toast.LENGTH_SHORT).show()
                                    cardLoad.visibility = CardView.GONE
                                    cardLoad.startAnimation(cardloading_anim)

                                }
                            }

                        }
                }
                catch (e: Exception){
                    Log.d("dax", e.message.toString())
                    cardLoad.visibility = CardView.GONE
                    cardLoad.startAnimation(cardloading_anim)
                }

            }
        }


    }


    fun isNetworkAvailable(): Boolean {
        val connectivityManager =  getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager;
        val activeNetworkInfo = connectivityManager.getActiveNetworkInfo()
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
    //here
    class ReverseInterpolator : Interpolator {
        override fun getInterpolation(paramFloat: Float): Float {
            return Math.abs(paramFloat - 1f)
        }
    }
}