package com.afedaxo.presentation.ui.photo

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.afedaxo.R
import com.afedaxo.databinding.FragmentPhotoTakingBinding
import com.afedaxo.presentation.ui.base.BaseFragment
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import io.fotoapparat.Fotoapparat
import io.fotoapparat.parameter.ScaleType
import io.fotoapparat.selector.back
import io.fotoapparat.view.CameraView
import kotlinx.android.synthetic.main.fragment_food_list.*
import kotlinx.android.synthetic.main.fragment_photo_taking.*
import org.koin.android.viewmodel.ext.android.viewModel

open class PhotoTakingFragment : BaseFragment() {
    lateinit var binding: FragmentPhotoTakingBinding

    private lateinit var fotoapparat: Fotoapparat

    val viewModel : PhotoTakingViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_photo_taking, container, false
        )

        binding.viewModel = viewModel

        ac_pt_snap_btn.setOnClickListener {
            val photoResult = fotoapparat.takePicture()

            YoYo.with(Techniques.FadeOutDown)
                .onEnd {
                    binding.root.findViewById<CameraView>(R.id.ac_pt_camera_view)?.visibility = View.INVISIBLE
                    binding.root.findViewById<View>(R.id.ac_pt_snap_btn)?.visibility = View.INVISIBLE
                    viewModel.onPhotoTaken(photoResult)
                }
                .duration(1000)
                .playOn(binding.root.findViewById<CameraView>(R.id.ac_pt_camera_view))
        }

        // TODO
        //viewModel.init(intent.extras.getInt(SESSION_ID))

        viewModel.lastDishRetrieved.observe(this, Observer<String> {
            navigateToSelectRegion(it)
        })

        viewModel.photoProcessed.observe(this, Observer<String> {
            navigateToSelectRegion(it)
        })

        fotoapparat = Fotoapparat(
            context = activity!!,
            lensPosition = back(),
            scaleType = ScaleType.CenterCrop,
            view = ac_pt_camera_view
        )

        // Creates a vertical Layout Manager
        ac_fl_recyclerview.layoutManager = LinearLayoutManager(context)

        binding.lifecycleOwner = this

        return binding.root
    }

    private fun navigateToSelectRegion(it: String) {

    }

    override fun onStart() {
        super.onStart()
        setupPermissions()
    }

    override fun onStop() {
        fotoapparat.stop()
        super.onStop()
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(activity!!,
            Manifest.permission.CAMERA)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity!!,
                    Manifest.permission.CAMERA)) {
                val builder = AlertDialog.Builder(activity!!)
                builder.setMessage("Permission to access the camera is required for this app to record audio.")
                    .setTitle("Permission required")

                builder.setPositiveButton("OK"
                ) { dialog, id ->
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
        ActivityCompat.requestPermissions(activity!!,
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
    
}