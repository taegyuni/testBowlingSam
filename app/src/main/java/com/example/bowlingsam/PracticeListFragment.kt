package com.example.bowlingsam

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_practice_list.*


class PracticeListFragment : Fragment() {

    private lateinit var postureVideoFragment: PostureVideoFragment

    companion object {
        fun newInstance() : PracticeListFragment {
            return PracticeListFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_practice_list, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //뒤로가기 버튼 클릭 리스너 설정
        toolbar.setNavigationOnClickListener {
            parentFragmentManager.beginTransaction().remove(this).commit()
        }

        //자세1 버튼 클릭 리스너 설정
        practice1_button.setOnClickListener {
            postureVideoFragment = PostureVideoFragment.newInstance(R.raw.sample1, practice1_name.text.toString())
            parentFragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left,R.anim.exit_to_right).add(R.id.fragments_frame, postureVideoFragment).commit()

        }
        //자세2 버튼 클릭 리스너 설정
        practice2_button.setOnClickListener {
            postureVideoFragment = PostureVideoFragment.newInstance(R.raw.sample1, practice2_name.text.toString())
            parentFragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left,R.anim.exit_to_right).add(R.id.fragments_frame, postureVideoFragment).commit()

        }
        //자세3 버튼 클릭 리스너 설정
        practice3_button.setOnClickListener {
            postureVideoFragment = PostureVideoFragment.newInstance(R.raw.sample1, practice3_name.text.toString())
            parentFragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left,R.anim.exit_to_right).add(R.id.fragments_frame, postureVideoFragment).commit()

        }
    }
}

