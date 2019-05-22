package com.afedaxo.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.afedaxo.R
import com.afedaxo.helper.MvpAppCompatActivity
import com.afedaxo.model.room.Dish
import com.afedaxo.presentation.presenter.SelectRegionPresenter
import com.afedaxo.presentation.view.SelectRegionView
import com.afedaxo.ui.activity.FoodListActivity.Companion.SESSION_ID
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.activity_select_region.*
import kotlinx.android.synthetic.main.dialog_dish_detected.view.*
import java.text.DecimalFormat



class SelectRegionActivity : MvpAppCompatActivity(), SelectRegionView {
    companion object {
        const val TAG = "SelectRegionActivity"
        const val FULL_FILENAME = "FULL_FILENAME"
        fun getIntent(context: Context, fullFilename: String, sessionId: Int): Intent  {
            val intent = Intent(context, SelectRegionActivity::class.java)
            intent.putExtra(FULL_FILENAME, fullFilename)
            intent.putExtra(SESSION_ID, sessionId)
            return intent
        }
    }

    @InjectPresenter
    lateinit var mSelectRegionPresenter: SelectRegionPresenter

    override fun navigateToFoodListActivity() {
        startActivity(FoodListActivity.getIntent(this, intent.extras.getInt(SESSION_ID)))
    }

    override fun initViewWithUri(fromFile: Uri) {
        cropImageView.setImageUriAsync(fromFile)
    }

    override fun showDishNotDetectedToast() {
        runOnUiThread {
            Toast.makeText(this, "Dish is not detected! Please select dish with price", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun showRetakePhotoButton() {
        runOnUiThread {
            ac_sr_retake_btn.visibility = View.VISIBLE
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_region)

        mSelectRegionPresenter.initCropView(
            intent.extras.getString(FULL_FILENAME))

        ac_sr_retake_btn.setOnClickListener {
            mSelectRegionPresenter.onRetakePhoto(intent.extras.getString(FULL_FILENAME))
        }

        ac_sr_select_btn.setOnClickListener {
            mSelectRegionPresenter.onSelectRegion(cropImageView.getCroppedImage(),
                intent.extras.getString(FULL_FILENAME), intent.extras.getInt(SESSION_ID))
        }
    }

    override fun dishDetected(dish: Dish) {
        val regex = "[0-9]+[.,]?[0-9]?[0-9]?".toRegex()
        runOnUiThread {
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_dish_detected, null)
            // Initialize a new instance of
            val builder = AlertDialog.Builder(this)
                .setView(mDialogView)

            val regexVal = regex.find(dish.priceVal)?.value

            var dialog: AlertDialog? = null

            if (regexVal != null) {
                val priceFloat = regexVal
                    .replace(',', '.').toFloat()

                val df = DecimalFormat("###,###,##0.00")
                val formatted = df.format(priceFloat)

                // Display a message on alert dialog
                mDialogView.dialog_dishdtc_val.setText(
                    getString(R.string.dish_detect_dialog_with_price, formatted)

                )

                // Set a positive button and its click listener on alert dialog
                mDialogView.dialog_dishdtc_confirm_btn.setOnClickListener {
                    mSelectRegionPresenter.dishConfirmed(dish)
                }


                // Display a negative button on alert dialog
                mDialogView.dialog_dishdtc_selectag_btn.setOnClickListener  {
                    mSelectRegionPresenter.disposeDish(dish);
                    dialog?.dismiss()
                }

                dialog = builder.show()
                dialog?.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
        }
    }

    override fun pressBack() {
        onBackPressed()
    }

    override fun onBackPressed() {
        startActivity(PhotoTakingActivity.getIntent(this, intent.extras.getInt(SESSION_ID)))
    }
}
