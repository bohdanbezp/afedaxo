package com.afedaxo.ui.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.afedaxo.helper.MvpAppCompatActivity
import com.afedaxo.presentation.presenter.PhotoTakingPresenter
import com.afedaxo.presentation.view.PhotoTakingView
import com.afedaxo.ui.activity.FoodListActivity.Companion.SESSION_ID
import com.arellomobile.mvp.presenter.InjectPresenter
import io.fotoapparat.Fotoapparat
import kotlinx.android.synthetic.main.activity_photo_taking.*
import android.view.View
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import io.fotoapparat.parameter.ScaleType
import io.fotoapparat.selector.back
import io.fotoapparat.view.CameraView
import com.afedaxo.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch



class PhotoTakingActivity : MvpAppCompatActivity(), PhotoTakingView {
    companion object {
        const val TAG = "PhotoTakingActivity"
        fun getIntent(context: Context, sessionId: Int): Intent {
            val intent = Intent(context, PhotoTakingActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra(SESSION_ID, sessionId)
            return intent
        }
    }

    @InjectPresenter
    lateinit var mPhotoTakingPresenter: PhotoTakingPresenter

    private lateinit var fotoapparat: Fotoapparat

    override fun navigateToSelectRegionActivity(fullsizeFilename: String) {
        startActivity(SelectRegionActivity.getIntent(this, fullsizeFilename,
            intent.extras.getInt(SESSION_ID)))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_taking)

        ac_pt_snap_btn.setOnClickListener {
            hideReusePhotoButton()
            val photoResult = fotoapparat.takePicture()

            YoYo.with(Techniques.FadeOutDown)
                .onEnd {
                    actionBar?.hide()
                    findViewById<CameraView>(R.id.ac_pt_camera_view)?.visibility = View.INVISIBLE
                    findViewById<View>(R.id.ac_pt_snap_btn)?.visibility = View.INVISIBLE
                    val filenameResult
                            = mPhotoTakingPresenter.onPhotoTaken(photoResult)
                    GlobalScope.launch {
                        navigateToSelectRegionActivity(filenameResult.await())
                    }
                }
                .duration(1000)
                .playOn(findViewById<CameraView>(R.id.ac_pt_camera_view))
        }

        mPhotoTakingPresenter.init(intent.extras.getInt(SESSION_ID))

        ac_pt_reuse_previes_btn.setOnClickListener {
            mPhotoTakingPresenter.onReuseBtnClick()
        }

        fotoapparat = Fotoapparat(
            context = this,
            lensPosition = back(),
            scaleType = ScaleType.CenterCrop,
            view = ac_pt_camera_view
        )
    }

    override fun onStart() {
        super.onStart()
        actionBar?.show()
        ac_pt_camera_view.visibility = View.VISIBLE
        setupPermissions()
    }

    override fun onStop() {
        fotoapparat.stop()
        super.onStop()
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.CAMERA)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to record denied")
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Permission to access the camera is required for this app to record audio.")
                    .setTitle("Permission required")

                builder.setPositiveButton("OK"
                ) { dialog, id ->
                    Log.i(TAG, "Clicked")
                    makeRequest()
                }

                val dialog = builder.create()
                dialog.show()
            } else {
                makeRequest()
            }
        }
        else {
            fotoapparat.start()
        }
    }

    private val CAMERA_REQUEST_CODE = 101

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            fotoapparat.start()
        }
    }

    override fun showReusePhotoButton() {
        runOnUiThread {
            ac_pt_reuse_previes_btn.visibility = View.VISIBLE
        }
    }

    override fun hideReusePhotoButton() {
        runOnUiThread {
            ac_pt_reuse_previes_btn.visibility = View.GONE
        }
    }

}
