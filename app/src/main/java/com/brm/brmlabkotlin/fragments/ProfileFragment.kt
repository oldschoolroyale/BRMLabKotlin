package com.brm.brmlabkotlin.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.agrawalsuneet.dotsloader.loaders.LazyLoader
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.brm.brmlabkotlin.R
import com.brm.brmlabkotlin.activities.RegistrationActivity
import com.brm.brmlabkotlin.presenter.ProfilePresenter
import com.brm.brmlabkotlin.view.ProfileView
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.fragment_profile.view.*

class ProfileFragment : MvpAppCompatFragment(), ProfileView, View.OnClickListener {

    private lateinit var avatarLoader: ProgressBar
    private lateinit var loader: LazyLoader
    private lateinit var avatar: ImageView
    private lateinit var nameText: TextView
    private var user: String? = null

    companion object{
        private val GALLERY_PICK = 1
    }


    @InjectPresenter
    lateinit var presenter: ProfilePresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_profile, container, false)
        (activity as AppCompatActivity).supportActionBar?.hide()
        avatarLoader = view.findViewById(R.id.fragment_profile_avatar_progress_bar)
        loader = view.findViewById(R.id.fragment_profile_dots_loader)
        avatar = view.findViewById(R.id.fragment_profile_avatar)
        nameText = view.findViewById(R.id.fragment_profile_name)
        user = FirebaseAuth.getInstance().currentUser?.uid

        //OnClick
        view.fragment_profile_start_work.setOnClickListener(this)

        view.fragment_profile_log_out.setOnClickListener {
            logOut()
        }
        avatar.setOnClickListener {
            newPhoto()
        }
        presenter.load(user!!)
        return view
    }

    private fun newPhoto() {
        val intent1 = Intent()
        intent1.type = "image/*"
        intent1.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent1, "SELECT_IMAGE"),
        GALLERY_PICK)
    }

    private fun logOut() {
        FirebaseAuth.getInstance().signOut()
        activity?.finish()
        startActivity(Intent(context, RegistrationActivity::class.java))
    }

    override fun startLoading() {
        loader.visibility = View.VISIBLE
    }

    override fun endLoading() {
        loader.visibility = View.INVISIBLE
    }

    override fun setUpProfile(image: String, name: String) {
        image.let { url -> Picasso.with(context).load(url).networkPolicy(
            NetworkPolicy.OFFLINE).into(avatar, object : Callback {
            override fun onSuccess() {
                Log.d("MyLog", "Success!")
            }

            override fun onError() {
                image.let { url -> Picasso.with(context).load(url).into(avatar) }
            }
        })  }
        nameText.text = name
    }

    override fun sendPhoto() {
        avatarLoader.visibility = View.VISIBLE
    }

    override fun putPhoto() {
        avatarLoader.visibility = View.INVISIBLE
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK){
            val imageUri: Uri = data?.data!!
            CropImage.activity(imageUri)
                .setAspectRatio(1,1)
                .start(requireContext(), this)
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            val result: CropImage.ActivityResult = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK){
                val resultUri: Uri = result.uri
                presenter.newPhoto(resultUri, user!!)
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                val error: Exception = result.error
                showError(error = error.toString())
            }
        }
    }

    override fun onClick(v: View?) {

    }

}