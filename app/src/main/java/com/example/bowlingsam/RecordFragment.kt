package com.example.bowlingsam

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.*
import androidx.camera.video.VideoCapture
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_record.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class RecordFragment : Fragment() {
    //영상 촬영 관련 변수들 선언
    private var videoCapture: VideoCapture<Recorder>? = null
    private var recording: Recording? = null
    private lateinit var cameraExecutor: ExecutorService

    private lateinit var safeContext: Context
    private lateinit var recordButton: ImageView
    private lateinit var closeButton: ImageView

    //객체 생성 시 로그 태그, 파일 이름, 권한 코드 정의
    companion object {
        private const val TAG = "CameraXApp"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()

        fun newInstance() : RecordFragment {
            return RecordFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        safeContext = context
    }

    //뷰가 생성되었을 때
    //프레그먼트와 레이아웃을 연결해주는 파트
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_record, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //카메라 및 저장 권한 획득
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this.requireActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        recordButton = view.findViewById(R.id.record_button)
        closeButton = view.findViewById(R.id.close_button)


        //영상 촬영 버튼 클릭 리스너 설정
        record_button.setOnClickListener { captureVideo(view) }
        //닫기 버튼 클릭 리스너 설정
        closeButton.setOnClickListener {
            //홈 프레그먼트로 이동
            val navigation: BottomNavigationView = view.rootView.findViewById(R.id.bottom_nav)
            navigation.selectedItemId = R.id.menu_home
        }

        //카메라 익세큐터 설정
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    override fun onDestroy() {
        super.onDestroy()
        //에러 처리 -> 안 먹힘
        /*
        if(recording != null) {
            recording?.close()
            recording = null
        }
         */
        cameraExecutor.shutdown()
    }

    //영상 촬영과 영상 촬영 중단을 정의하는 함수
    private fun captureVideo(view: View) {
        val videoCapture = this.videoCapture ?: return

        recordButton = view.findViewById(R.id.record_button)

        //촬영이 진행되고 있는 경우, 영상 촬영을 중단
        val curRecording = recording
        if (curRecording != null) {
            // Stop the current recording session.
            curRecording.stop()
            recording = null
            return
        }

        //새로운 영상 촬영 세션 생성 및 시작
        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/CameraX-Video")
            }
        }

        val mediaStoreOutputOptions = MediaStoreOutputOptions
            .Builder(requireActivity().contentResolver, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
            .setContentValues(contentValues)
            .build()

        //영상 촬영 시작 및 에러 처리
        recording = videoCapture.output
            .prepareRecording(safeContext, mediaStoreOutputOptions)
            .apply {
                if (PermissionChecker.checkSelfPermission(requireActivity(),
                        Manifest.permission.RECORD_AUDIO) ==
                    PermissionChecker.PERMISSION_GRANTED)
                {
                    withAudioEnabled()
                }
            }
            .start(ContextCompat.getMainExecutor(safeContext)) { recordEvent ->
                when(recordEvent) {
                    is VideoRecordEvent.Start -> {
                        recordButton.setImageResource(R.drawable.ic_record_btn_red)
                    }
                    is VideoRecordEvent.Finalize -> {
                        if (!recordEvent.hasError()) {
                            val msg = "Video capture succeeded: " +
                                    "${recordEvent.outputResults.outputUri}"
                            Toast.makeText(requireActivity().baseContext, msg, Toast.LENGTH_SHORT)
                                .show()
                            Log.d(TAG, msg)
                        } else {
                            recording?.close()
                            recording = null
                            Log.e(TAG, "Video capture ends with error: " +
                                    "${recordEvent.error}")
                        }
                        recordButton.setImageResource(R.drawable.ic_record_btn)
                    }
                }
            }

    }

    //카메라 설정
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(safeContext)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            //카메라 프리뷰 설정
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(camera_preview.surfaceProvider)
                }

            //영상 촬영 객체 선언 및 촬영 화질 설정
            val recorder = Recorder.Builder()
                .setQualitySelector(QualitySelector.from(Quality.HIGHEST))
                .build()
            videoCapture = VideoCapture.withOutput(recorder)

            //후면 카메라를 기본 카메라로 설정
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                //바인딩 되어 있는 카메라들을 해제
                cameraProvider.unbindAll()

                //유스케이스와 카메라를 바인딩
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, videoCapture)

            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(safeContext))

    }

    //요구한 모든 권한을 획득 했는지 확인
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            safeContext, it) == PackageManager.PERMISSION_GRANTED
    }

    //카메라 및 저장 권한 획득 여부에 따른 결과 처리 함수
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(this.requireContext(),
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }
}