package com.sangmee.fashionpeople.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferNetworkLossHandler
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.ui.fragment.AlarmFragment
import com.sangmee.fashionpeople.ui.fragment.HomeFragment
import com.sangmee.fashionpeople.ui.fragment.InfoFragment
import com.sangmee.fashionpeople.ui.fragment.SearchFragment
import com.sangmee.fashionpeople.kakaologin.GlobalApplication
import com.sangmee.fashionpeople.retrofit.RetrofitClient
import com.sangmee.fashionpeople.retrofit.model.FUser
import kotlinx.android.synthetic.main.activity_main.*
import com.sangmee.fashionpeople.retrofit.model.FeedImage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var mCurrentPhotoPath: String
    private lateinit var mCurrentVideoPath: String

    private val permissionListener = object : PermissionListener {
        override fun onPermissionGranted() {
            Toast.makeText(
                applicationContext,
                R.string.check_permission_completed,
                Toast.LENGTH_SHORT
            ).show()
        }

        override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
            Toast.makeText(applicationContext, R.string.check_permission_denied, Toast.LENGTH_SHORT)
                .show()
        }
    }

    companion object {
        private const val CAMERA_START = 1111
        private const val REQUEST_VIDEO_CAPTURE = 1
        private const val BUCKET_NAME = "fashionprofile-images"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        navigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeItem -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, HomeFragment()).commit()
                }
                R.id.searchItem -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, SearchFragment()).commit()
                }
                R.id.addItem -> {
                    //사진 촬영, 비디오 촬영 선택 다이얼로그 출력
                    CameraDialog.newInstance({
                        startCameraApp()//사진 촬영 클릭시
                    }, {
                        recordVideoCamera()//비디오 촬영 클릭시
                    }).show(supportFragmentManager, CameraDialog.TAG)
                }

                R.id.alarmItem -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, AlarmFragment()).commit()
                }
                R.id.infoItem -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, InfoFragment()).commit()
                }
            }
            return@setOnNavigationItemSelectedListener true

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //카메라 앱 실행 후 결과
        if (requestCode == CAMERA_START) {
            Log.i("REQUEST_TAKE_PHOTO", "${Activity.RESULT_OK}" + " " + "${resultCode}")
            if (resultCode == RESULT_OK) {
                try {
                    galleryAddPic()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, HomeFragment()).commit()
                } catch (e: Exception) {
                    Log.e("REQUEST_TAKE_PHOTO", e.toString())
                }

            } else {
                Toast.makeText(this@MainActivity, "사진찍기를 취소하였습니다.", Toast.LENGTH_SHORT).show()
            }
        }
        if (requestCode == REQUEST_VIDEO_CAPTURE) {
            if (resultCode == RESULT_OK) {
                try {
                    galleryAddVideo()
                } catch (e: Exception) {
                    Log.d("REQUEST_VIDEO_CAPTURE", e.toString())
                }
            } else {
                Log.d("resultcode", resultCode.toString())
                Toast.makeText(this@MainActivity, R.string.cancel_video_record, Toast.LENGTH_SHORT)
                    .show()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    //카메라 앱 실행
    private fun startCameraApp() {
        TedPermission.with(this)
            .setPermissionListener(permissionListener)
            .setDeniedMessage(R.string.check_permission)
            .setPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .check()

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            try {
                captureCamera()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                100
            )
        }
    }


    //임시 파일 생성
    @Throws(IOException::class)
    private fun createImageFile(): File? { // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_$timeStamp.jpg"
        var imageFile: File? = null
        val storageDir = File(
            Environment.getExternalStorageDirectory().toString() + "/Pictures",
            "FashionPeople"
        )
        if (!storageDir.exists()) {
            Log.i("mCurrentPhotoPath1", storageDir.toString())
            storageDir.mkdirs()
        }
        imageFile = File(storageDir, imageFileName)
        mCurrentPhotoPath = imageFile.absolutePath
        Log.d("sangmin", mCurrentPhotoPath)
        return imageFile
    }


    //카메라 실행
    private fun captureCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (takePictureIntent.resolveActivity(packageManager) != null) {

            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (ex: Exception) {
                Log.e("captureCamera Error", ex.toString())
                return
            }
            if (photoFile != null) { // getUriForFile의 두 번째 인자는 Manifest provier의 authorites와 일치해야 함
                val providerURI =
                    FileProvider.getUriForFile(this, "$packageName.provider", photoFile)
                Log.d("sangmin_photoFile", photoFile.absolutePath)
                // 인텐트에 전달할 때는 FileProvier의 Return값인 content://로만!!, providerURI의 값에 카메라 데이터를 넣어 보냄
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, providerURI)
                startActivityForResult(takePictureIntent, CAMERA_START)
            }
        }
    }


    //이미지 로컬 폴더에 저장
    private fun galleryAddPic() {
        Log.i("galleryAddPic", "Call");
        val mediaScanIntent: Intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        // 해당 경로에 있는 파일을 객체화(새로 파일을 만든다는 것으로 이해하면 안 됨)
        val f: File = File(mCurrentPhotoPath)
        val contentUri: Uri = Uri.fromFile(f)
        mediaScanIntent.setData(contentUri)
        sendBroadcast(mediaScanIntent)
        val customId = GlobalApplication.prefs.getString("custom_id", "empty")
        uploadWithTransferUtility(customId, f.name, f)
        postFeedImage(customId, f.name)

        Toast.makeText(this, "사진이 앨범에 저장되었습니다.", Toast.LENGTH_SHORT).show()
    }


    //동영상 녹화 실행
    private fun recordVideoCamera() {

        TedPermission.with(this)
            .setPermissionListener(permissionListener)
            .setDeniedMessage(R.string.check_permission)
            .setPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .check()

        val videoIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        if (intent.resolveActivity(packageManager) != null) { //인텐트 설정에 맞는 액티비티를 찾아줌

            var videoFile: File? = null
            try {
                videoFile = createVideoFile()
            } catch (ex: IOException) {
                Log.e("videoRecord Error", ex.toString())
                return
            }
            videoFile?.let {
                val provideURI =
                    FileProvider.getUriForFile(this, "$packageName.provider", videoFile)
                videoIntent.putExtra(MediaStore.EXTRA_OUTPUT, provideURI)
                startActivityForResult(videoIntent, REQUEST_VIDEO_CAPTURE)
                Log.d("aa", "aa")
            }
        }


    }

    // 비디오 폴더 있는지 확인 후 없으면 생성 + 파일 생성(파일 이름은 겹칠일 없으니 존재하는지 체크 X)
    @Throws(IOException::class)
    private fun createVideoFile(): File? {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val videoFileName = "VID_$timeStamp.mp4"
        var videoFile: File? = null
        val storageDir = File(
            Environment.getExternalStorageDirectory().toString() + "/Pictures",
            "FashionPeople"
        )
        if (!storageDir.exists()) { //자체적으로 폴더 존재하는지 판단하긴 하지만 한번더 확인
            storageDir.mkdirs()
        }
        videoFile = File(storageDir, videoFileName)
        mCurrentVideoPath = videoFile.absolutePath
        return videoFile
    }

    //동영상 로컬 폴더에 저장
    private fun galleryAddVideo() {
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        // 해당 경로에 있는 파일을 객체화(새로 파일을 만든다는 것으로 이해하면 안 됨)
        val f: File = File(mCurrentVideoPath)
        val contentUri: Uri = Uri.fromFile(f)
        mediaScanIntent.data = contentUri
        sendBroadcast(mediaScanIntent)
        val customId = GlobalApplication.prefs.getString("custom_id", "empty")
        uploadWithTransferUtility(customId, f.name, f)
        postFeedImage(customId, f.name)

        Toast.makeText(this, "동영상이 앨범에 저장되었습니다.", Toast.LENGTH_SHORT).show()
    }

    //aws s3에 이미지 업로드
    private fun uploadWithTransferUtility(customId: String, fileName: String, file: File?) {

        val credentialsProvider = CognitoCachingCredentialsProvider(
            applicationContext,
            "ap-northeast-2:04a21c16-627a-49a9-8229-f1c412ddebfa",  // 자격 증명 풀 ID
            Regions.AP_NORTHEAST_2 // 리전
        )

        TransferNetworkLossHandler.getInstance(applicationContext)

        val transferUtility = TransferUtility.builder()
            .context(applicationContext)
            .defaultBucket(BUCKET_NAME)
            .s3Client(AmazonS3Client(credentialsProvider, Region.getRegion(Regions.AP_NORTHEAST_2)))
            .build()

        /* Store the new created Image file path */

        val uploadObserver = transferUtility.upload(
            "users/${customId}/feed/${fileName}",
            file,
            CannedAccessControlList.PublicRead
        )

        //CannedAccessControlList.PublicRead 읽기 권한 추가

        // Attach a listener to the observer
        uploadObserver.setTransferListener(object : TransferListener {
            override fun onStateChanged(id: Int, state: TransferState) {
                if (state == TransferState.COMPLETED) {
                    Log.d("MYTAG", "fileupload")
                }
            }

            override fun onProgressChanged(id: Int, current: Long, total: Long) {
                val done = (((current.toDouble() / total) * 100.0).toInt())
                Log.d("MYTAG", "UPLOAD - - ID: $id, percent done = $done")
            }

            override fun onError(id: Int, ex: Exception) {
                Log.d("MYTAG", "UPLOAD ERROR - - ID: $id - - EX: ${ex.message.toString()}")
            }
        })

        // If you prefer to long-poll for updates
        if (uploadObserver.state == TransferState.COMPLETED) {
            /* Handle completion */

        }
    }

    private fun postFeedImage(customId: String, fileName: String) {
        val feedImage = FeedImage(
            listOf(),
            0,
            fileName,
            listOf(),
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()),
            customId
        )
        RetrofitClient().getFeedImageService().putFeedImage(customId, feedImage).enqueue(object :
            Callback<FeedImage> {
            override fun onResponse(call: Call<FeedImage>, response: Response<FeedImage>) {
            }

            override fun onFailure(call: Call<FeedImage>, t: Throwable) {
            }
        })
    }
}
