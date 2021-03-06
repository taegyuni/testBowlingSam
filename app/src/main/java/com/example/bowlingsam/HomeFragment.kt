package com.example.bowlingsam

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_history.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_settings.*


class HomeFragment : Fragment() {

    private lateinit var practiceListFragment: PracticeListFragment
    private lateinit var gripFragment: GripFragment
    private lateinit var postureVideoFragment: PostureVideoFragment

    //firebase Auth
    private lateinit var firebaseAuth: FirebaseAuth
    //firebase firestore
    private lateinit var firestore: FirebaseFirestore

    companion object {
        fun newInstance() : HomeFragment {
            return HomeFragment()
        }
    }

    private var carouselList = ArrayList<CarouselItem>()
    private lateinit var carouselAdapter: CarouselAdapter

    //뷰가 생성되었을 때
    //프레그먼트와 레이아웃을 연결해주는 파트
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var a : String = ""
        var c : Int
        firestore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        val docRef = firestore.collection("users").document(firebaseAuth?.uid.toString())
        docRef.get()
            .addOnSuccessListener { document ->
                if(document != null){

                    a = document["userNickName"].toString()
                    intro_text.setText(a + "님, \n안녕하세요")
                    a = document["ballsize"].toString()
                    c = a.toInt()
                    ballsize_value.setText(""+c + " lb")
                    a = document["avg"].toString()
                    avg_value.setText(a)
                    a = document["recentScore"].toString()
                    recent_value.setText(a)
                } else {

                }
            }
            .addOnFailureListener { exception ->

            }

        val view = inflater.inflate(R.layout.fragment_home, container, false)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //캐러셀 데이터 배열을 준비
        carouselList.add(CarouselItem(R.raw.sample1, R.drawable.bowling1,"볼링 기초 1", "입문 과정 / 홍길동"))
        carouselList.add(CarouselItem(R.raw.sample2, R.drawable.bowling2,"볼링 기초 2", "입문 과정 / 밤톨이"))
        carouselList.add(CarouselItem(R.raw.sample3, R.drawable.bowling3,"볼링 중급", "심화 과정 / 손흥민"))
        carouselList.add(CarouselItem(R.raw.sample4, R.drawable.bowling4,"볼링 고급", "심화 과정 / 홍길동"))
        carouselList.add(CarouselItem(R.raw.sample5, R.drawable.bowling1,"볼링 고급", "심화 과정 / 홍길동"))
        carouselList.add(CarouselItem(R.raw.sample6, R.drawable.bowling3,"볼링 고급", "심화 과정 / 홍길동"))

        //캐러셀 어뎁터 인스턴스 설정 및 클릭 리스너 설정
        carouselAdapter = CarouselAdapter(carouselList) {
            var videoID: Int = it.videopath
            var title: String = it.text

            postureVideoFragment = PostureVideoFragment.newInstance(videoID, title, "설명 없음.\n")
            parentFragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left,R.anim.exit_to_right).add(R.id.fragments_frame, postureVideoFragment).commit()
        }

        //캐러셀 아이템 여백 및 크기 설정
        val pageMarginPx = resources.getDimensionPixelOffset(R.dimen.pageMargin)
        val pagerWidth = resources.getDimensionPixelOffset(R.dimen.pageWidth)
        val screenWidth = resources.displayMetrics.widthPixels
        val offsetPx = screenWidth - pageMarginPx - pagerWidth

        viewpager.setPageTransformer { page, position ->
            page.translationX = position * -offsetPx
        }

        //캐러셀 아이템 하나를 미리 로드
        viewpager.offscreenPageLimit = 1

        viewpager.apply {
            adapter = carouselAdapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
        }

        //두번째 아이템이 가장 먼저 나오도록 설정
        viewpager.currentItem = 1

        //그립 버튼 클릭 리스너 설정
        home_grip_button.setOnClickListener {
            onHomeGripButtonClicked()
        }

        //자세 버튼 클릭 리스너 설정
        home_posture_button.setOnClickListener {
            onHomePostureButtonClicked()
        }


    }

    //그립 버튼 클릭 리스너 정의
    private fun onHomeGripButtonClicked() {
        gripFragment = GripFragment.newInstance()
        parentFragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left,R.anim.exit_to_right).add(R.id.fragments_frame, gripFragment).commit()

    }

    //자세 버튼 클릭 리스너 정의
    private fun onHomePostureButtonClicked() {
        practiceListFragment = PracticeListFragment.newInstance()
        parentFragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left,R.anim.exit_to_right).add(R.id.fragments_frame, practiceListFragment).addToBackStack(null).commit()
    }

}

