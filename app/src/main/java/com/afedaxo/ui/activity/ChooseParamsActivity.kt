package com.afedaxo.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.NumberPicker

import com.arellomobile.mvp.presenter.InjectPresenter
import com.afedaxo.R
import com.afedaxo.helper.MvpAppCompatActivity
import com.afedaxo.presentation.view.ChooseParamsView
import com.afedaxo.presentation.presenter.ChooseParamsPresenter
import com.afedaxo.util.GeneralUtils
import kotlinx.android.synthetic.main.activity_choose_params.*



class ChooseParamsActivity : MvpAppCompatActivity(), ChooseParamsView {
    companion object {
        const val TAG = "ChooseParamsActivity"
        fun getIntent(context: Context, sessionId: Int): Intent {
            val intent = Intent(context, ChooseParamsActivity::class.java)
            intent.putExtra(FoodListActivity.SESSION_ID, sessionId)
            return intent
        }
    }

    @InjectPresenter
    lateinit var mChooseParamsPresenter: ChooseParamsPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_params)

        mChooseParamsPresenter.putMinMaxVals(
            intent.extras.getInt(FoodListActivity.SESSION_ID))

        ac_choo_proceed_btn.setOnClickListener {
            val radioButtonID = GeneralUtils.getRadioGroupId(radioMoneyWeight)
            mChooseParamsPresenter.onProceedBtnClick(
                ac_cp_number_picker.value,
                radioButtonID
            )
        }

        ac_cp_number_picker.setDividerColor(Color.TRANSPARENT)
    }

    private fun NumberPicker.setDividerColor(color: Int) {
        val dividerField = NumberPicker::class.java.declaredFields.firstOrNull { it.name == "mSelectionDivider" } ?: return
        try {
            dividerField.isAccessible = true
            dividerField.set(this, ColorDrawable(Color.parseColor("#00ffffff")))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun setMinMaxPeople(min: Int, max: Int) {
        ac_cp_number_picker.minValue = min
        ac_cp_number_picker.maxValue = if (max == -1) 1 else max
    }

    override fun navigateToResultActivity(peopleNum: Int, moneyWeightId: Int) {
        startActivity(ResultActivity.getIntent(this,
            intent.extras.getInt(FoodListActivity.SESSION_ID),
            peopleNum, moneyWeightId))
    }
}
