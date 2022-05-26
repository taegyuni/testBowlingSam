package com.example.bowlingsam

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.custom_dialog_age.*
import kotlinx.android.synthetic.main.custom_dialog_ballsize.*
import kotlinx.android.synthetic.main.custom_dialog_ballsize.btnCancle
import kotlinx.android.synthetic.main.custom_dialog_ballsize.btnSave
import kotlinx.android.synthetic.main.custom_dialog_nickname.*
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {

    //firebase Auth
    private lateinit var firebaseAuth: FirebaseAuth
    //google client
    private lateinit var googleSignInClient: GoogleSignInClient

    companion object {
        fun newInstance() : SettingsFragment {
            return SettingsFragment()
        }
    }

    //메모리에 올라갔을 때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    //메인 액티비티에 붙었을 때
    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    //뷰가 생성되었을 때
    //프레그먼트와 레이아웃을 연결해주는 파트
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        myNickName_btn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val dialog = CustomDialogSetting(requireContext())
                dialog.selectDialog(1)
            }
        })
        myAge_btn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val dialog = CustomDialogSetting(requireContext())
                dialog.selectDialog(2)
            }
        })
        myBallSize_btn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val dialog = CustomDialogSetting(requireContext())
                dialog.selectDialog(3)
            }
        })


        myLogOut_btn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                var builder = AlertDialog.Builder(context)
                builder.setMessage("로그아웃 하시겠습니까?")
                    .setPositiveButton("확인",
                        DialogInterface.OnClickListener{ dialog, which ->
                        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build();
                        googleSignInClient = GoogleSignIn.getClient(context!!, gso)
                        googleSignInClient.revokeAccess()
                        signOut()
                        activity?.let {
                            val i = Intent(context, LoginActivity::class.java)
                            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(i)
                        }
                        }
                    )
                    .setNegativeButton("취소",
                        DialogInterface.OnClickListener { dialog, which ->

                        }
                    )
                builder.show()

            }
        })
    }
    private fun signOut() { // 로그아웃

        firebaseAuth = FirebaseAuth.getInstance()

        // Firebase sign out
        firebaseAuth.signOut()
        // Google sign out

        Toast.makeText(context, "Logout!", Toast.LENGTH_SHORT).show()

    }
}

class CustomDialogSetting(context: Context) {
    private val dialog = Dialog(context)
    val c = context

    fun selectDialog(select : Int){
        when (select){
            1 -> NicknameDialog()
            2 -> AgeDialog()
            3 -> BallsizeDialog()
        }
    }

    fun NicknameDialog(){
        dialog.setContentView(R.layout.custom_dialog_nickname)
        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()
        dialog.btnCancle.setOnClickListener{
            dialog.dismiss()
        }
        dialog.btnSave.setOnClickListener {
            if(dialog.edit_text_nickname.text.toString().length == 0){
                Toast.makeText(c, "입력해!!!", Toast.LENGTH_SHORT).show()
            } else {
                val nickname = dialog.edit_text_nickname.text.toString()
                Toast.makeText(c, "잘했어!!!", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }
    }

    fun AgeDialog(){
        dialog.setContentView(R.layout.custom_dialog_age)
        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()
        dialog.btnCancle.setOnClickListener{
            dialog.dismiss()
        }
        dialog.btnSave.setOnClickListener {
            if(dialog.edit_text_age.text.toString().length == 0){
                Toast.makeText(c, "입력해!!!", Toast.LENGTH_SHORT).show()
            } else {
                val age = dialog.edit_text_age.text.toString()
                Toast.makeText(c, age, Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }
    }

    fun BallsizeDialog(){
        dialog.setContentView(R.layout.custom_dialog_ballsize)
        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()
        dialog.btnCancle.setOnClickListener{
            dialog.dismiss()
        }
        dialog.btnSave.setOnClickListener {
            if(dialog.edit_text_ballsize.text.toString().length == 0){
                Toast.makeText(c, "입력해!!!", Toast.LENGTH_SHORT).show()
            } else {
                val ballsize = dialog.edit_text_ballsize.text.toString()
                Toast.makeText(c, ballsize, Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }
    }

}