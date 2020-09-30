package com.brm.brmlabkotlin.fragments.loginFragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import br.com.simplepass.loadingbutton.customViews.CircularProgressButton
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.brm.brmlabkotlin.R
import com.brm.brmlabkotlin.activities.MainActivity
import com.brm.brmlabkotlin.presenter.CodeSendPresenter
import com.brm.brmlabkotlin.view.CodeSendView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.disposables.Disposable
import java.util.function.Consumer


class CodeSendFragment : MvpAppCompatFragment(), CodeSendView {

    val args: CodeSendFragmentArgs by navArgs()
    private lateinit var animButton: CircularProgressButton
    private lateinit var editText: EditText
    private  var mCurrentUser : FirebaseUser? = null
    private lateinit var observer: Disposable

    @InjectPresenter
    lateinit var presenter: CodeSendPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.code_send_fragment, container, false)
        animButton = view.findViewById(R.id.code_send_fragment_btn)
        editText = view.findViewById(R.id.code_send_fragment_edit_text)

        val mAuth = FirebaseAuth.getInstance()
        mCurrentUser = mAuth.currentUser

        observer = RxTextView.textChanges(editText)
            .subscribe{charSequence: CharSequence ->
                if (charSequence.toString().isNotEmpty()){
                    animButton.setBackgroundResource(R.drawable.button_shape_blue)
                    animButton.setOnClickListener{
                        if (editText.text.isNotEmpty()){
                            Log.d("MyLog", args.code)
                            presenter.prepare(args.code, editText.text.toString())
                        }
                        else{
                            editText.error = "Пункт ввода кода не заполнен"
                        }
                    }
                }
                else{
                    animButton.setBackgroundResource(R.drawable.button_shape_grey)
                    animButton.setOnClickListener { null }
                }
            }


        return view
    }

    override fun startLoading() {
       animButton.startAnimation()
    }

    override fun endLoading() {
        animButton.revertAnimation()
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
    }

    override fun openProfile() {
        startActivity(Intent(context, MainActivity::class.java))
        activity?.finish()
    }

    override fun resendCode() {
        TODO("Not yet implemented")
    }

    override fun onStart() {
        if (mCurrentUser != null){
            openProfile()
        }
        super.onStart()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        observer.dispose()
    }
}