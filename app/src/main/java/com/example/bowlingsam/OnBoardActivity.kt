package com.example.bowlingsam

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator

class OnBoardActivity : AppCompatActivity() {

    var onBoardingViewPagerAdapter : OnBoardViewPagerAdapter? = null
    //    var tabLayout : TabLayout? = null
    var tabLayout : DotsIndicator? = null
    var onBoardingViewPager : ViewPager? = null
    var next: TextView? = null
    var position = 0
    var sharedPreferences : SharedPreferences? = null
    var startOnboardBtn : Button? = null
    var loginOnboard : LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (restorePrefData()) {
            val i = Intent(this, LoginActivity::class.java)  //login화면으로 이동
            startActivity(i)
            finish()
        }

        setContentView(R.layout.activity_onboarding)

        tabLayout = findViewById(R.id.dots_indicator)
        next = findViewById(R.id.tv_next_onboard)
        startOnboardBtn = findViewById(R.id.btn_startOnboard)
        loginOnboard = findViewById(R.id.onboard_login)

        val onBoardingData : MutableList<OnBoardingData> = ArrayList()
        onBoardingData.add(OnBoardingData("볼링 연습해볼까?", "집에서 무료하게 보내고있는 당신.. \n 볼링이 치고싶으신가요? \n ", R.drawable.ic_launcher_foreground, "볼링!"))
        onBoardingData.add(OnBoardingData("어디서든 편하게!", "집에서 편하게 연습해봐요! \n 자세별로 코치해주고 \n 자신의 모습을 확인할 수 있어요!", R.drawable.ic_launcher_foreground, "볼링쌤을 통해"))
        onBoardingData.add(OnBoardingData("볼링 연습해보세요!", "영상을 통해 배우고 \n 자세 분석으로 확인하고! \n 나의 등급과 점수를 알수 있어요!", R.drawable.ic_launcher_foreground, "볼링쌤과 함께"))
        onBoardingData.add(OnBoardingData("퍼펙트 게임을 향하여", "완벽한 스트라이크를 위해 \n 볼링쌤으로 \n 볼링 연습해봐요!", R.drawable.ic_launcher_foreground, "터키를 목표로"))

        setOnBoardingViewPagerAdapter(onBoardingData)

        position = onBoardingViewPager!!.currentItem

        startOnboardBtn?.setOnClickListener {
            savePrefDate()
            val i = Intent(applicationContext, SignUpActivity::class.java)  //회원가입 화면으로 이동
            startActivity(i)
            finish()
        }

        loginOnboard?.setOnClickListener {
            savePrefDate()
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
            finish()
        }


//        next?.setOnClickListener {
//            if (position < onBoardingData.size) {
//                position++
//                onBoardingViewPager!!.currentItem = position
//            }
//            if (position == onBoardingData.size) {
//                savePrefDate()
//                val i = Intent(applicationContext, LoginActivity::class.java)
//                startActivity(i)
//                finish()
//            }
//        }

//        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
//            override fun onTabSelected(tab: TabLayout.Tab?) {
////                position = tab!!.position
////                if (tab.position == onBoardingData.size - 1) {
////                    next!!.text = "Get Started"
////                } else {
////                    next!!.text = "Next"
////                }
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab?) {
//
//            }
//
//            override fun onTabReselected(tab: TabLayout.Tab?) {
//
//            }
//        })
    }

    private fun setOnBoardingViewPagerAdapter(onBoardingData: List<OnBoardingData>){

        onBoardingViewPager = findViewById(R.id.screenPager)
        onBoardingViewPagerAdapter = OnBoardViewPagerAdapter(this,onBoardingData)
        onBoardingViewPager!!.adapter = onBoardingViewPagerAdapter
        tabLayout?.attachTo(onBoardingViewPager!!)
    }

    private fun savePrefDate() {
        sharedPreferences = applicationContext.getSharedPreferences("pref", Context.MODE_PRIVATE)
        val editor = sharedPreferences!!.edit()
        editor.putBoolean("isFirstTimeRun", true)
        editor.apply()
    }

    private fun restorePrefData(): Boolean {
        sharedPreferences = applicationContext.getSharedPreferences("pref", Context.MODE_PRIVATE)
        return sharedPreferences!!.getBoolean("isFirstTimeRun", false)
    }
}