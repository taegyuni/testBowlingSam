package com.example.bowlingsam

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_practice_list.*


class MainActivity : AppCompatActivity() {

    private lateinit var homeFragment: HomeFragment
    private lateinit var recordFragment: RecordFragment
    private lateinit var historyFragment: HistoryFragment
    private lateinit var settingsFragment: SettingsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //첫 화면을 홈 프레그먼트로 설정
        homeFragment = HomeFragment.newInstance()
        supportFragmentManager.beginTransaction().add(R.id.fragments_frame, homeFragment).commit()

        //bottom nav bar 리스너 설정
        bottom_nav.setOnItemSelectedListener {  item ->
            when(item.itemId) {
                //홈 탭 클릭 시
                R.id.menu_home -> {
                    homeFragment = HomeFragment.newInstance()
                    supportFragmentManager.beginTransaction().replace(R.id.fragments_frame, homeFragment).addToBackStack(null).commit()
                    true
                }
                //촬영 탭 클릭 시
                R.id.menu_record -> {
                    recordFragment = RecordFragment.newInstance()
                    supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_top).replace(R.id.fragments_frame, recordFragment).addToBackStack(null).commit()
                    true
                }
                R.id.menu_history -> {
                    historyFragment = HistoryFragment.newInstance()
                    supportFragmentManager.beginTransaction().replace(R.id.fragments_frame, historyFragment).addToBackStack(null).commit()
                    true
                }
                R.id.menu_settings -> {
                    settingsFragment = SettingsFragment.newInstance()
                    supportFragmentManager.beginTransaction().replace(R.id.fragments_frame, settingsFragment).addToBackStack(null).commit()
                    true
                }
                else -> false
            }

        }
        setSupportActionBar(toolbar)
    }

}