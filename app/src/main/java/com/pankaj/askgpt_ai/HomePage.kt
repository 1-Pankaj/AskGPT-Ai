package com.pankaj.askgpt_ai

import android.app.ProgressDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import io.paperdb.Paper
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException


class HomePage : AppCompatActivity() {

    var messageList: List<Message>? = null
    var messageAdapter: MessageAdapter? = null
    lateinit var messageRecycleView: RecyclerView
    lateinit var modeChangeCard: CardView
    lateinit var modeImage: ImageView
    lateinit var mode: String
    lateinit var profileInfoImg: ImageView
    lateinit var profileInfoImgCard: CardView


    var fileUri: Uri? = null;


    val JSON: MediaType = "application/json; charset=utf-8".toMediaType();
    var client = OkHttpClient()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)



        try {

            val bottomsheet = findViewById<FrameLayout>(R.id.bottomsheet)
            val click = findViewById<CardView>(R.id.click)
            val messageTypeCard = findViewById<CardView>(R.id.messageTypeCard)
            val settingIcon = findViewById<ImageView>(R.id.settingIcon)
            val parent = findViewById<CoordinatorLayout>(R.id.parent)
            val textStart = findViewById<TextView>(R.id.textStart)
            val iconStart = findViewById<ImageView>(R.id.iconStart)
            val mainIllustration = findViewById<ImageView>(R.id.mainIllustration)
            val unleashText = findViewById<TextView>(R.id.unleashText)
            val powertext = findViewById<TextView>(R.id.powertext)
            val gptText = findViewById<TextView>(R.id.gptText)
            val settings_bottomsheet = findViewById<FrameLayout>(R.id.settings_bottomsheet)
            val settingCard = findViewById<CardView>(R.id.settingCard)
            val closeSettings = findViewById<CardView>(R.id.closeSettings)
            val closeSettingsIcon = findViewById<ImageView>(R.id.closeSettingsIcon)
            messageRecycleView = findViewById<RecyclerView>(R.id.messageRecycleView)
            val messageBox = findViewById<EditText>(R.id.messageBox)
            val sendButton = findViewById<CardView>(R.id.sendButton)
            modeChangeCard = findViewById<CardView>(R.id.modeChangeCard)
            modeImage = findViewById<ImageView>(R.id.mode)
            mode = "text"
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            val googleSignInClient = GoogleSignIn.getClient(applicationContext, gso)
            modeChangeCard.setOnClickListener {
                if (mode == "text") {
                    modeImage.setImageResource(R.drawable.image)
                    mode = "image"
                    Toast.makeText(applicationContext, "Image Generation mode", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    modeImage.setImageResource(R.drawable.text)
                    Toast.makeText(applicationContext, "Chat mode", Toast.LENGTH_SHORT).show()
                    mode = "text"
                }
            }

            messageList = ArrayList<Message>()
            messageAdapter = MessageAdapter(messageList as ArrayList<Message>, applicationContext)
            messageRecycleView.adapter = messageAdapter
            val linearLayoutManager = LinearLayoutManager(this)
            linearLayoutManager.stackFromEnd = true
            messageRecycleView.layoutManager = linearLayoutManager


            settings_bottomsheet.visibility = View.VISIBLE





            BottomSheetBehavior.from(bottomsheet).apply {
                peekHeight = 0
                this.state = BottomSheetBehavior.STATE_HIDDEN
            }
            BottomSheetBehavior.from(settings_bottomsheet).apply {
                peekHeight = 0
                this.state = BottomSheetBehavior.STATE_HIDDEN
            }


            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

            Paper.init(applicationContext)

            val maAuth = FirebaseAuth.getInstance()
            val dbRef = FirebaseDatabase.getInstance().reference

            bottomsheet.visibility = FrameLayout.VISIBLE

            if (resources.getString(R.string.mode) == "Day") {
                //day stuff
                messageTypeCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
                settingIcon.setImageResource(R.drawable.setting)
                bottomsheet.setBackgroundResource(R.drawable.bottom_sheet)
                parent.setBackgroundColor(Color.parseColor("#FFFFFF"))
                textStart.setTextColor(Color.parseColor("#000000"))
                iconStart.setImageResource(R.drawable.arrow_out)
                mainIllustration.setImageResource(R.drawable.flipped_illustration)
                settings_bottomsheet.setBackgroundResource(R.drawable.settings_bottomsheet)
                closeSettingsIcon.setImageResource(R.drawable.close)

            } else {
                //night stuff
                messageTypeCard.setCardBackgroundColor(Color.parseColor("#6B6B6B"))
                settingIcon.setImageResource(R.drawable.setting_night)
                bottomsheet.setBackgroundResource(R.drawable.bottomsheet_night)
                parent.setBackgroundColor(Color.parseColor("#000000"))
                textStart.setTextColor(Color.parseColor("#FFFFFF"))
                iconStart.setImageResource(R.drawable.arrow_night)
                mainIllustration.setImageResource(R.drawable.flipped_night)
                settings_bottomsheet.setBackgroundResource(R.drawable.settingsbottomsheet_night)
                closeSettingsIcon.setImageResource(R.drawable.close_night)

            }

            val handler = Handler(Looper.getMainLooper())

            val right_to_left = AnimationUtils.loadAnimation(this, R.anim.right_to_left)
            val text_animation = AnimationUtils.loadAnimation(this, R.anim.edittext1_anim)
            val text_animation2 = AnimationUtils.loadAnimation(this, R.anim.edittext2_anim)
            val text_animation3 = AnimationUtils.loadAnimation(this, R.anim.edittext3_anim)
            val text_animation4 = AnimationUtils.loadAnimation(this, R.anim.edittext4_anim)
            val remove_items = AnimationUtils.loadAnimation(this, R.anim.remove_items)
            val main_illustration = AnimationUtils.loadAnimation(this, R.anim.main_illustration)
            val fade_out = AnimationUtils.loadAnimation(this, R.anim.fade_out)
            val fade_in = AnimationUtils.loadAnimation(this, R.anim.fade_in)

            right_to_left.duration = 1200
            text_animation.duration = 1200
            text_animation2.duration = 1200
            text_animation3.duration = 1200
            text_animation4.duration = 1200
            remove_items.duration = 1200
            main_illustration.duration = 1200
            fade_out.duration = 500

            mainIllustration.startAnimation(right_to_left)
            unleashText.startAnimation(text_animation2)
            powertext.startAnimation(text_animation3)
            gptText.startAnimation(text_animation4)
            click.startAnimation(text_animation3)

            click.setOnClickListener {

                if(maAuth.currentUser?.isEmailVerified == true) {
                    BottomSheetBehavior.from(bottomsheet).apply {
                        peekHeight = 400
                        this.state = BottomSheetBehavior.STATE_EXPANDED
                    }
                    mainIllustration.startAnimation(main_illustration)
                    unleashText.startAnimation(remove_items)
                    powertext.startAnimation(remove_items)
                    gptText.startAnimation(remove_items)
                    click.startAnimation(remove_items)
                    click.isEnabled = false
                    handler.postDelayed({
                        unleashText.startAnimation(fade_out)
                        powertext.startAnimation(fade_out)
                        gptText.startAnimation(fade_out)
                        click.startAnimation(fade_out)
                    }, 1200)
                }
                else{
                    val builder = MaterialAlertDialogBuilder(this)
                    builder.setTitle("Alert!")
                    builder.setMessage("Verify your email before you can use this application.\n\nIf already verified, restart application!")
                    builder.setPositiveButton("Okay") { dialog, which ->
                        dialog.dismiss()
                    }
                    builder.setNegativeButton("Send again"){ dialog, which ->
                        maAuth.currentUser?.sendEmailVerification()?.addOnCompleteListener {
                            Toast.makeText(applicationContext, "Verification mail sent", Toast.LENGTH_SHORT).show()
                        }
                    }
                    builder.show()
                }
            }

            settingCard.setOnClickListener {
                BottomSheetBehavior.from(settings_bottomsheet).apply {
                    peekHeight = 300
                    this.state = BottomSheetBehavior.STATE_EXPANDED
                }
                BottomSheetBehavior.from(bottomsheet).apply {
                    peekHeight = 170
                    this.state = BottomSheetBehavior.STATE_COLLAPSED
                }
            }
            closeSettings.setOnClickListener {
                BottomSheetBehavior.from(settings_bottomsheet).apply {
                    peekHeight = 0
                    this.state = BottomSheetBehavior.STATE_COLLAPSED
                    this.state = BottomSheetBehavior.STATE_HIDDEN
                }
                BottomSheetBehavior.from(bottomsheet).apply {
                    peekHeight = 170
                    this.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }


            addToChat(
                "Hi there! welcome to AskGPT-Ai, a fast and accurate " +
                        "response based application, " + "start by asking a question. " +
                        "Each time you refresh or reload the application, " +
                        "a new chat will be created and previous chat data will be wiped for " +
                        "better end-to-end encryption, AskGPT now!\n\nLong press this message for more options",
                "bot",
                false
            )


            val cardShareToFriends = findViewById<CardView>(R.id.cardShareToFriends)
            val nameText = findViewById<TextView>(R.id.nameText)
            val emailText = findViewById<TextView>(R.id.emailText)
            val profileImage = findViewById<ImageView>(R.id.profileImage)

            val uid = maAuth.currentUser?.uid



            val infoCard = findViewById<FrameLayout>(R.id.infoCard)
            val infoCardIcon = findViewById<ImageView>(R.id.infoCardIcon)
            val infoCardText = findViewById<TextView>(R.id.infoCardText)
            val closeInfoCard = findViewById<CardView>(R.id.closeInfoCard)

            BottomSheetBehavior.from(infoCard).apply {
                peekHeight = 0
                this.state = BottomSheetBehavior.STATE_HIDDEN
            }

            val appearance = findViewById<CardView>(R.id.appearance)


            val privacyCard = findViewById<CardView>(R.id.privacyCard)





            val helpCard = findViewById<CardView>(R.id.helpCard)

            val profileInfoCard = findViewById<CardView>(R.id.profileInfoCard)

            val profileInfoSheet = findViewById<FrameLayout>(R.id.profileInfoSheet)
            val closeProfileInfoSheet = findViewById<CardView>(R.id.closeProfileInfoSheet)
            profileInfoImg = findViewById<ImageView>(R.id.profileImg)
            val firstNameEditText = findViewById<EditText>(R.id.firstNameEditText)
            val lastNameEditText = findViewById<EditText>(R.id.lastNameEditText)
            val emailEditText = findViewById<EditText>(R.id.emailEditText)
            val saveButton = findViewById<MaterialButton>(R.id.saveButton)
            val logoutButton = findViewById<MaterialButton>(R.id.logoutButton)

            val accountSettings = findViewById<CardView>(R.id.accountSettings)
            val accountSettingsCard = findViewById<FrameLayout>(R.id.accountSettingsCard)
            val closeAccountSettingsCard = findViewById<MaterialCardView>(R.id.closeAccountSettingsCard)

            BottomSheetBehavior.from(accountSettingsCard).apply {
                peekHeight = 0
                this.state = BottomSheetBehavior.STATE_HIDDEN
            }

            if(resources.getString(R.string.mode) == "Day"){
                infoCard.setBackgroundResource(R.drawable.settings_bottomsheet)
                profileInfoSheet.setBackgroundResource(R.drawable.settings_bottomsheet)
                firstNameEditText.setBackgroundResource(R.drawable.edittext)
                lastNameEditText.setBackgroundResource(R.drawable.edittext)
                emailEditText.setBackgroundResource(R.drawable.edittext)
                accountSettingsCard.setBackgroundResource(R.drawable.settings_bottomsheet)
            }else{
                infoCard.setBackgroundResource((R.drawable.settingsbottomsheet_night))
                profileInfoSheet.setBackgroundResource(R.drawable.settingsbottomsheet_night)
                firstNameEditText.setBackgroundResource(R.drawable.edittext_night)
                lastNameEditText.setBackgroundResource(R.drawable.edittext_night)
                emailEditText.setBackgroundResource(R.drawable.edittext_night)
                accountSettingsCard.setBackgroundResource(R.drawable.settingsbottomsheet_night)
            }

            BottomSheetBehavior.from(profileInfoSheet).apply {
                peekHeight = 0
                this.state = BottomSheetBehavior.STATE_HIDDEN
            }

            saveButton.setOnClickListener{
                val firstName = firstNameEditText.text.toString()
                val lastName = lastNameEditText.text.toString()

                val uid = maAuth.currentUser?.uid
                dbRef.child("Users").child(uid.toString()).child("FirstName").setValue(firstName)
                dbRef.child("Users").child(uid.toString()).child("LastName").setValue(lastName)
            }

            logoutButton.setOnClickListener{
                val builder = MaterialAlertDialogBuilder(this)
                builder.setTitle("Alert!")
                builder.setMessage("Are you sure to logout?")

                builder.setPositiveButton("YES") { dialog, which ->
                    maAuth.signOut()
                    googleSignInClient.signOut()
                    Paper.book().write(DatabaseModule().emailKey, "emailKey")
                    Paper.book().write(DatabaseModule().emailKey, "passKey")
                    val dir = applicationContext.cacheDir
                    try {
                        deleteDir(dir)
                    }
                    catch (e: Exception){
                        Toast.makeText(applicationContext, "Error logging out!", Toast.LENGTH_SHORT).show()
                    }
                    val intent: Intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                }


                builder.setNegativeButton("CANCEL") { dialog, which ->
                    dialog.dismiss()
                }


                builder.show()



            }

            dbRef.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    nameText.setText(snapshot.child("Users").child(uid.toString()).child("FirstName").value.toString()
                            +" "+ snapshot.child("Users").child(uid.toString()).child("LastName").value.toString() )


                    emailText.setText(snapshot.child("Users").child(uid.toString()).child("email").value.toString())
                    firstNameEditText.setText(snapshot.child("Users").child(uid.toString()).child("FirstName").value.toString())
                    lastNameEditText.setText(snapshot.child("Users").child(uid.toString()).child("LastName").value.toString())
                    emailEditText.setText(snapshot.child("Users").child(uid.toString()).child("email").value.toString())
                    val imgUrl = snapshot.child("Users").child(uid.toString()).child("profile").value.toString()
                    if(imgUrl == null || imgUrl.isEmpty() == true){

                    }
                    else{
                        Picasso.get().load(imgUrl).into(profileImage)
                        Picasso.get().load(imgUrl).into(profileInfoImg)
                    }

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })


            profileInfoCard.setOnClickListener{
                BottomSheetBehavior.from(profileInfoSheet).apply {
                    peekHeight = 400
                    this.state = BottomSheetBehavior.STATE_EXPANDED
                }
                BottomSheetBehavior.from(settings_bottomsheet).apply {
                    peekHeight = 0
                    this.state = BottomSheetBehavior.STATE_COLLAPSED
                    this.state = BottomSheetBehavior.STATE_HIDDEN
                }
            }
            closeProfileInfoSheet.setOnClickListener{
                BottomSheetBehavior.from(profileInfoSheet).apply {
                    peekHeight = 0
                    this.state = BottomSheetBehavior.STATE_COLLAPSED
                    this.state = BottomSheetBehavior.STATE_HIDDEN
                }
                BottomSheetBehavior.from(settings_bottomsheet).apply {
                    peekHeight = 300
                    this.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }






            helpCard.setOnClickListener{
                BottomSheetBehavior.from(infoCard).apply {
                    peekHeight = 250
                    this.state = BottomSheetBehavior.STATE_EXPANDED
                }
                infoCardIcon.setImageResource(R.drawable.help)
                infoCardText.setText("For more information and queries, contact the developer on following address:\n\n" +
                        "mail: pankajisrani17@gmail.com\n")
                BottomSheetBehavior.from(settings_bottomsheet).apply {
                    peekHeight = 0
                    this.state = BottomSheetBehavior.STATE_COLLAPSED
                    this.state = BottomSheetBehavior.STATE_HIDDEN
                }
            }
            val privacyPolicyCard = findViewById<CardView>(R.id.privacyPolicyCard)
            val closePrivacyPolicyCard = findViewById<CardView>(R.id.closePrivacyPolicyCard)

            privacyCard.setOnClickListener{
                privacyPolicyCard.visibility = View.VISIBLE
                BottomSheetBehavior.from(settings_bottomsheet).apply {
                    peekHeight = 0
                    this.state = BottomSheetBehavior.STATE_COLLAPSED
                    this.state = BottomSheetBehavior.STATE_HIDDEN
                }
            }
            closePrivacyPolicyCard.setOnClickListener{
                privacyPolicyCard.visibility = View.GONE
                BottomSheetBehavior.from(settings_bottomsheet).apply {
                    peekHeight = 300
                    this.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }
            appearance.setOnClickListener{
                BottomSheetBehavior.from(infoCard).apply {
                    peekHeight = 250
                    this.state = BottomSheetBehavior.STATE_EXPANDED
                }
                infoCardIcon.setImageResource(R.drawable.appearance)
                infoCardText.setText("Appearance themes are changed automatically accordingly. " +
                        "Change your system theme to set application theme to" +
                        " either dark mode or light mode.")
                BottomSheetBehavior.from(settings_bottomsheet).apply {
                    peekHeight = 0
                    this.state = BottomSheetBehavior.STATE_COLLAPSED
                    this.state = BottomSheetBehavior.STATE_HIDDEN
                }
            }

            closeInfoCard.setOnClickListener{
                BottomSheetBehavior.from(infoCard).apply {
                    peekHeight = 0
                    this.state = BottomSheetBehavior.STATE_COLLAPSED
                    this.state = BottomSheetBehavior.STATE_HIDDEN
                }
                BottomSheetBehavior.from(settings_bottomsheet).apply {
                    peekHeight = 300
                    this.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }

            val savedMessages = findViewById<CardView>(R.id.savedMessages)
            savedMessages.setOnClickListener{
                val intent = Intent(applicationContext, SavedMessages::class.java)
                startActivity(intent)
            }

            cardShareToFriends.setOnClickListener{
                //https://github.com/1-Pankaj/AskGPT-Ai

                val intent = Intent(Intent.ACTION_SEND)

                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_SUBJECT, "Ai Response")

                intent.putExtra(Intent.EXTRA_TEXT, "Hi there! Checkout this awesome" +
                        " Ai response application i'm using\n\nhttps://github.com/1-Pankaj/AskGPT-Ai")
                startActivity(Intent.createChooser(intent, "Share Via"))
            }

            sendButton.setOnClickListener {
                if (messageBox.text.toString().trim().isEmpty()) {

                } else {
                    val messageText = messageBox.text.toString().trim()
                    messageBox.setText("")
                    addToChat(messageText, "me", false)
                    try {
                        if (mode == "image") {
                            CallImageAPI(messageText)
                        } else {
                            callAPI(messageText);
                        }

                    } catch (e: Exception) {
                        Log.d("dax", e.message.toString())
                    }
                }

            }


            profileInfoImgCard = findViewById<CardView>(R.id.profileImgCard)

            profileInfoImgCard.setOnClickListener{
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(Intent.createChooser(intent, "Pick your image to upload"), 22)
            }



            //code here

            accountSettings.setOnClickListener{
                BottomSheetBehavior.from(settings_bottomsheet).apply {
                    peekHeight = 0
                    this.state = BottomSheetBehavior.STATE_COLLAPSED
                    this.state = BottomSheetBehavior.STATE_HIDDEN
                }
                BottomSheetBehavior.from(accountSettingsCard).apply {
                    peekHeight = 400
                    this.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }

            closeAccountSettingsCard.setOnClickListener{
                BottomSheetBehavior.from(accountSettingsCard).apply {
                    peekHeight = 0
                    this.state = BottomSheetBehavior.STATE_COLLAPSED
                    this.state = BottomSheetBehavior.STATE_HIDDEN
                }
                BottomSheetBehavior.from(settings_bottomsheet).apply {
                    peekHeight = 300
                    this.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }
            val clearCacheCard = findViewById<CardView>(R.id.cacheClearCard)
            val accountDeleteCard = findViewById<CardView>(R.id.accountDeleteCard)



            accountDeleteCard.setOnClickListener{
                val builder = MaterialAlertDialogBuilder(this)
                builder.setTitle("Alert!")
                builder.setMessage("Deleting account will delete all your saved messages and your current data too, proceed with caution!" +
                        "\n\nAre you sure to delete your account?")
                builder.setOnDismissListener {

                }
                builder.setPositiveButton("Cancel") { dialog, which ->
                    dialog.dismiss()
                }
                builder.setNegativeButton("Delete"){ dialog, which ->
                    val builder2 = MaterialAlertDialogBuilder(this)
                    builder2.setTitle("Final Warning!")
                    builder2.setMessage("Proceed to delete your account?")
                    builder2.setPositiveButton("Cancel"){ dialog, which ->
                        dialog.dismiss()
                    }
                    builder2.setNegativeButton("Delete"){ dialog, which ->
                        val uid = maAuth.currentUser?.uid
                        maAuth.currentUser?.delete()?.addOnCompleteListener { task ->
                            if (task.isSuccessful){
                                maAuth.signOut()
                                googleSignInClient.signOut()
                                Paper.book().write(DatabaseModule().emailKey, "emailKey")
                                Paper.book().write(DatabaseModule().emailKey, "passKey")
                                dbRef.child("Users").child(uid.toString()).removeValue()
                                val ref: StorageReference = FirebaseStorage.getInstance().getReference()
                                if(ref.child(uid.toString()) != null){
                                    ref.child(uid.toString()).delete()
                                }
                                val dir = applicationContext.cacheDir
                                try {
                                    deleteDir(dir)
                                }
                                catch (e: Exception){
                                    Toast.makeText(applicationContext, "Error deleting cache, remove manually if still exists!", Toast.LENGTH_SHORT).show()
                                }
                                val intent = Intent(applicationContext, HomePage::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }
                    }
                    builder2.show()
                }
                builder.show()
            }

            clearCacheCard.setOnClickListener{
                val builder = MaterialAlertDialogBuilder(this)
                builder.setTitle("Alert!")
                builder.setMessage("Clearing cache will log you out of the app, Are you sure?")

                builder.setPositiveButton("YES") { dialog, which ->
                    maAuth.signOut()
                    googleSignInClient.signOut()
                    Paper.book().write(DatabaseModule().emailKey, "emailKey")
                    Paper.book().write(DatabaseModule().emailKey, "passKey")
                    val dir = applicationContext.cacheDir
                    try {
                        deleteDir(dir)
                    }
                    catch (e: Exception){
                        Toast.makeText(applicationContext, "Error deleting cache", Toast.LENGTH_SHORT).show()
                    }
                    finish()
                    finishAffinity()
                }


                builder.setNegativeButton("CANCEL") { dialog, which ->
                    dialog.dismiss()
                }


                builder.show()
            }



        } catch (e: Exception) {
            Log.d("dax", e.message.toString())
        }
    }


    fun deleteDir(dir: File?): Boolean {
        return if (dir != null && dir.isDirectory) {
            val children = dir.list()
            for (i in children.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
            dir.delete()
        } else if (dir != null && dir.isFile) {
            dir.delete()
        } else {
            false
        }
    }


    //uploadImage
    fun uploadImage() {

        if (fileUri != null) {

            val progressDialog = ProgressDialog(this)

            progressDialog.setTitle("Uploading")
            progressDialog.setMessage("Please wait...")
            progressDialog.show()


            val mAuth = FirebaseAuth.getInstance()
            val uid = mAuth.currentUser?.uid
            val dbRef = FirebaseDatabase.getInstance().reference
            val ref: StorageReference = FirebaseStorage.getInstance().getReference()
                .child(uid.toString()).child("image")

            val uploadTask : UploadTask = ref.putFile(fileUri!!)


            val urlTask = uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                ref.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    progressDialog.dismiss()
                    val downloadUri = task.result
                    dbRef.child("Users").child(uid.toString()).child("profile").setValue(downloadUri.toString())
                    Toast.makeText(applicationContext, "Successful", Toast.LENGTH_SHORT).show()
                } else {
                    progressDialog.dismiss()
                    Toast.makeText(applicationContext, "Failure", Toast.LENGTH_SHORT)
                        .show()
                }
            }

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 22 && resultCode == RESULT_OK && data != null && data.data != null) {

            fileUri = data.data
            try {
                uploadImage()
            } catch (e: Exception) {

                e.printStackTrace()
            }
        }
    }




    fun addToChat(message: String?, sentBy: String?, image: Boolean?) {
        runOnUiThread(Runnable {
            kotlin.run {
                (messageList as ArrayList<Message>).add(Message(message, sentBy, image))
                messageAdapter?.notifyDataSetChanged()
                messageRecycleView.smoothScrollToPosition(messageAdapter!!.getItemCount());
            }
        })

    }

    fun addResponse(response: String?) {

        try {
            if (response != null) {
                (messageList as ArrayList<Message>).removeAt((messageList as ArrayList<Message>).size - 1)
                addToChat(response, "bot", false)
            };
        } catch (e: Exception) {
            Log.d("dax", e.message.toString())
        }
    }


    fun CallImageAPI(text: String?) {
        (messageList as ArrayList<Message>).add(Message("Typing...", "bot", false))
        //API CALL
        val jsonBody = JSONObject()
        try {
            jsonBody.put("prompt", text)
            jsonBody.put("size", "256x256")
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

//        val dbRef = FirebaseDatabase.getInstance().reference
//        val requestBody = RequestBody.create(JSON, jsonBody.toString())
//        dbRef.addListenerForSingleValueEvent(object : ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val apiKey = snapshot.child("Admin").child("API-KEY").value.toString()
//                val request: Request = Request.Builder()
//                    .url("https://api.openai.com/v1/images/generations")
//                    .header("Authorization", apiKey.toString()) //API KEYS GO HERE
//                    .post(requestBody)
//                    .build()
//                client.newCall(request).enqueue(object : Callback {
//                    override fun onFailure(call: Call, e: IOException) {
//                        addResponse("Failure to load response")
//                    }
//
//                    override fun onResponse(call: Call, response: Response) {
//                        val jsonObject = JSONObject(response.body?.string())
//                        val imageUrl = jsonObject.getJSONArray("data").getJSONObject(0).getString("url")
////
//                        addToChat(imageUrl, "bot", true)
//                    }
//
//                })
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(applicationContext, "Error retrieving data, reload application!", Toast.LENGTH_SHORT).show()
//            }
//
//        })


        addToChat("Image generation is currently under hold.\n\nAs Image generation by Ai is a resource consuming task, the developer " +
                "of this application needs to generate a bit of revenue before starting image generation again. Stay tuned and support the developer!", "bot", false)

    }













    fun callAPI(question : String){
        (messageList as ArrayList<Message>).add(Message("Typing...", "bot", false))
        //okhttp
        val jsonBody = JSONObject()
        try {

            jsonBody.put("model", "text-davinci-003")
            jsonBody.put("prompt", question)
            jsonBody.put("max_tokens", 4000);
            jsonBody.put("temperature", 0);
        }
        catch (e : Exception){
            addResponse("Failure to load response due to "+e.message.toString())
        }


        val body = RequestBody.create(JSON, jsonBody.toString())
        val dbRef = FirebaseDatabase.getInstance().reference
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val apiKey = snapshot.child("Admin").child("API-KEY").value.toString()
                val request: Request = Request.Builder()
                    .url("https://api.openai.com/v1/completions")
                    .header("Authorization", apiKey.toString()) //API KEYS GO HERE
                    .post(body)
                    .build()

                client.newCall(request).enqueue(object : Callback{
                    override fun onFailure(call: Call, e: IOException) {
                        addResponse("Failure to load response due to "+e.message.toString())
                    }

                    override fun onResponse(call: Call, response: Response) {
                        if(response.isSuccessful){

                            try {
                                var jsonObject = JSONObject(response.body?.string())
                                val jsonArray = jsonObject.getJSONArray("choices")
                                val result = jsonArray.getJSONObject(0).getString("text")
                                Log.d("dax", result.toString())
                                addResponse(result.toString().trim())

                            } catch (e: JSONException) {
                                Log.d("dax", e.message.toString())
                            }
                        }
                        else{
                            addResponse("Failure to load response, try again!")
                        }
                    }

                })
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Error retrieving data, reload application!", Toast.LENGTH_SHORT).show()
            }

        })

    }
    class MessageAdapter(var messageList: List<Message>, var context: Context) : RecyclerView.Adapter<MessageAdapter.MyViewHolder>() {
        var lastPosition = messageList.size
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_items, parent, false)
            return MyViewHolder(view)
        }
        fun copyToClipboard(text: CharSequence){
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("label",text)
            clipboard.setPrimaryClip(clip)
        }
        fun setAnimation(viewToAnimate : View,position: Int)
        {
            if (position > lastPosition)
            {
                val animation = AnimationUtils.loadAnimation(context, R.anim.bottom_to_top);
                animation.duration = 400
                viewToAnimate.startAnimation(animation);
                lastPosition = position;
            }
        }
        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val message = messageList[position]
            val bottom_to_top = AnimationUtils.loadAnimation(context, R.anim.bottom_to_top)
            val top_to_bottom = AnimationUtils.loadAnimation(context, R.anim.top_to_bottom)
            bottom_to_top.duration = 400
            top_to_bottom.duration = 400
            val handler = Handler(Looper.getMainLooper())

            val dbRef = FirebaseDatabase.getInstance().reference
            val mAuth = FirebaseAuth.getInstance()

            if(context.resources.getString(R.string.mode) == "Day"){
                holder.titleText.setBackgroundResource(R.drawable.edittext)
            }else{
                holder.titleText.setBackgroundResource(R.drawable.edittext_night)
            }

            if (message.getsentby() == message.SENT_BY_ME) {
                holder.leftChatView.visibility = View.GONE
                holder.rightChatView.visibility = View.VISIBLE
                holder.rightTextView.text = message.getmessage()
                setAnimation(holder.rightChatView, position)
            }
            else if(message.getimage() == true){
                holder.leftChatView.visibility = View.VISIBLE
                holder.rightChatView.visibility = View.GONE
                holder.leftTextView.visibility = View.GONE
                holder.imageCard.visibility = View.VISIBLE
                Picasso.get().load(message.getmessage()).into(holder.imageView)
                setAnimation(holder.leftChatView, position)
            }
            else {
                holder.rightChatView.visibility = View.GONE
                holder.leftChatView.visibility = View.VISIBLE
                holder.imageCard.visibility = View.GONE
                holder.leftTextView.text = message.getmessage()
                setAnimation(holder.leftChatView, position)
            }

            var saveMessageText = ""
            holder.saveMessage.setOnClickListener{
                if(messageList.get(position).getimage() == true){

                }else{

                    holder.saveMessageCard.visibility = View.VISIBLE
                    holder.saveMessageCard.startAnimation(bottom_to_top)

                    saveMessageText = messageList.get(position).getmessage().toString()
                }
            }

            holder.saveButton.setOnClickListener{


                val titleText = holder.titleText.text.toString()
                if(titleText.isEmpty()){
                    holder.titleText.setError("Title can't be empty!")
                }else{

                    dbRef.child("Users").child(mAuth.currentUser?.uid.toString()).child("messages").child(titleText.toString()).setValue(saveMessageText)
                    Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
                    holder.saveMessageCard.startAnimation(top_to_bottom)
                    handler.postDelayed({
                        holder.saveMessageCard.visibility = View.GONE
                    },400)
                }

            }

            holder.closeSaveMessageCard.setOnClickListener{


                holder.saveMessageCard.startAnimation(top_to_bottom)
                handler.postDelayed({
                    holder.saveMessageCard.visibility = View.GONE
                },400)
            }

            holder.leftChatView.setOnLongClickListener(){

                if(messageList.get(position).getimage() == true){
                    holder.saveMessage.visibility = View.GONE
                }
                else{
                    holder.saveMessage.visibility = View.VISIBLE
                }

                holder.popupCard.visibility = View.VISIBLE
                holder.popupCard.startAnimation(bottom_to_top)
                true
            }
            holder.copyCard.setOnClickListener{
                copyToClipboard(messageList.get(position).getmessage().toString())
                holder.popupCard.startAnimation(top_to_bottom)
                handler.postDelayed({
                    holder.popupCard.visibility = View.GONE
                },400)
                Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
            }
            holder.closeCard.setOnClickListener{
                holder.popupCard.startAnimation(top_to_bottom)
                handler.postDelayed({
                    holder.popupCard.visibility = View.GONE
                },400)
            }
            holder.readingMode.setOnClickListener{
                val intent = Intent(context.applicationContext, ReadingMode::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if(messageList.get(position).getimage() == true){
                    intent.putExtra("mode", "image")
                    intent.putExtra("text", messageList.get(position).getmessage().toString())
                    context.applicationContext.startActivity(intent)
                }
                else{
                    intent.putExtra("mode", "text")
                    intent.putExtra("text", messageList.get(position).getmessage().toString())
                    context.applicationContext.startActivity(intent)
                }

            }
        }

        override fun getItemCount(): Int {
            return messageList.size
        }

        inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var leftChatView: LinearLayout
            var rightChatView: LinearLayout
            var leftTextView: TextView
            var rightTextView: TextView
            var popupCard: CardView
            var copyCard: CardView
            var readingMode: CardView
            var closeCard: CardView
            var imageCard: CardView
            var imageView: ImageView
            var titleText: TextView
            var saveMessageCard: CardView
            var saveButton: MaterialButton
            var saveMessage: CardView
            var closeSaveMessageCard: CardView
            init {
                leftChatView = itemView.findViewById<LinearLayout>(R.id.left_chat)
                rightChatView = itemView.findViewById<LinearLayout>(R.id.right_chat)
                leftTextView = itemView.findViewById<TextView>(R.id.left_chat_text)
                rightTextView = itemView.findViewById<TextView>(R.id.right_chat_text)
                popupCard = itemView.findViewById<CardView>(R.id.popupCard)
                copyCard = itemView.findViewById<CardView>(R.id.copyCard)
                readingMode = itemView.findViewById<CardView>(R.id.readingMode)
                closeCard = itemView.findViewById<CardView>(R.id.closeCard)
                imageCard = itemView.findViewById<CardView>(R.id.imageCard)
                imageView = itemView.findViewById<ImageView>(R.id.imageView)
                titleText = itemView.findViewById<TextView>(R.id.titleText)
                saveMessageCard = itemView.findViewById<CardView>(R.id.saveMessageCard)
                saveButton = itemView.findViewById<MaterialButton>(R.id.saveButton)
                saveMessage = itemView.findViewById<CardView>(R.id.saveMessage)
                closeSaveMessageCard = itemView.findViewById<CardView>(R.id.closeSaveMessageCard)

            }
        }
    }


}
