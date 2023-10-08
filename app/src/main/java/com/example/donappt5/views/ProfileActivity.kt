package com.example.donappt5.views
//import com.squareup.picasso.Picasso;
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.donappt5.R
import com.example.donappt5.data.services.FirestoreService
import com.example.donappt5.data.util.Status
import com.example.donappt5.databinding.LayoutProfileBinding
import com.example.donappt5.util.MyGlobals
import com.example.donappt5.util.Util
import com.example.donappt5.viewmodels.ProfileViewModel
import com.example.donappt5.views.charitylist.CharityListActivity
import com.example.donappt5.views.onboarding.OnBoardingActivity
import com.facebook.login.LoginManager
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso

class ProfileActivity : AppCompatActivity() {
    lateinit var ctx: Context
    lateinit var viewModel: ProfileViewModel
    var SELECT_PICTURE = 12341
    private lateinit var binding: LayoutProfileBinding

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LayoutProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        ctx = this
        binding.btnChangeName.setOnClickListener { requestNameChange() }

        with(binding.btnLogOut) {
            setOnClickListener { v: View? ->
                LoginManager.getInstance().logOut()
                AuthUI.getInstance()
                    .signOut(ctx)
                    .addOnCompleteListener { task: Task<Void?>? ->
                        val intent = Intent(
                            ctx,
                            AuthenticationActivity::class.java
                        )
                        startActivity(intent)
                    }
            }
        }
        val user = FirebaseAuth.getInstance().currentUser

        viewModel.photourl.observe(this) {
            if (it.status == Status.SUCCESS) {
                if (it.data != null) {
                    Picasso.with(ctx).load(it.data).fit().into(binding.ivProfilePhoto)
                } else {
                    if (user!!.photoUrl != null) {
                        Picasso.with(ctx).load(user.photoUrl.toString()).fit().into(binding.ivProfilePhoto)
                    }
                }
            }
        }

        val llwithimage = findViewById<LinearLayout>(R.id.llImage)
        llwithimage.setOnClickListener { loadImage() }

        binding.btnLoadProfile.setOnClickListener { FirestoreService.uploadImage(viewModel.loadedUri) }
        binding.btnFavs.setOnClickListener { onFavsClick() }
        binding.btnSettings.setOnClickListener {
            val intent = Intent(ctx, SettingsActivity::class.java)
            startActivity(intent)
        }

        MyGlobals(ctx).setupBottomNavigation(ctx, this, binding.bottomNavigation)

        val btnGoToOnboarding = findViewById<Button>(R.id.btnGoToOnboarding)
        btnGoToOnboarding.setOnClickListener {
            val intent = Intent(ctx, OnBoardingActivity::class.java)
            startActivity(intent)
        }
    }

    fun onFavsClick() {
        val intent = Intent(ctx, CharityListActivity::class.java)
        intent.putExtra("fillingmode", Util.FILLING_FAVORITES)
        startActivity(intent)
    }

    fun loadImage() {
        openImageChooser()
    }

    fun openImageChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE)
    }

    fun requestNameChange() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Title")
        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)
        builder.setTitle("Enter your username")
        builder.setPositiveButton("OK") { _: DialogInterface?, _: Int ->
            binding.tvUserName.text = viewModel.updateUserName(input.text.toString())
        }
        builder.setNegativeButton(
            "Cancel"
        ) { dialog: DialogInterface, _: Int -> dialog.cancel() }
        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) {
            return
        }
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                val selectedImageUri = data.data
                if (null != selectedImageUri) {
                    binding.ivProfilePhoto.post {
                        binding.ivProfilePhoto.setImageURI(selectedImageUri)
                        viewModel.loadedUri = selectedImageUri
                    }
                }
            }
        }
    }
}