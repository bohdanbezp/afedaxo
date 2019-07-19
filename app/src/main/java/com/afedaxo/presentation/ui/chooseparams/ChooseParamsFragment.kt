package com.afedaxo.presentation.ui.chooseparams

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.UiThread
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.afedaxo.R
import com.afedaxo.databinding.FragmentChooseParamsBinding
import com.afedaxo.presentation.ui.base.BaseFragment
import com.afedaxo.util.GeneralUtils
import kotlinx.android.synthetic.main.fragment_choose_params.*
import org.koin.android.viewmodel.ext.android.viewModel

class ChooseParamsFragment : BaseFragment() {

    lateinit var binding: FragmentChooseParamsBinding

    val viewModel : ChooseParamsViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_choose_params, container, false
        )

//        viewModel.putMinMaxVals(
//            intent.extras.getInt(FoodListActivity.SESSION_ID))

        ac_choo_proceed_btn.setOnClickListener {
            val radioButtonID = GeneralUtils.getRadioGroupId(radioMoneyWeight)
            viewModel.onProceedBtnClick(
                ac_cp_number_picker.value,
                radioButtonID
            )
        }

        viewModel.proceedClicked.observe(this, Observer<Pair<Int, Int>> {
            navigateToResult(it.first, it.second)
        })

        viewModel.calculatedMinMaxValues.observe(this, Observer<Pair<Int, Int>> {
            setMinMaxPeople(it.first, it.second)
        })

        ac_cp_number_picker.setDividerColor(Color.TRANSPARENT)

        return binding.root
    }

    @UiThread
    fun setMinMaxPeople(min: Int, max: Int) {
        ac_cp_number_picker.minValue = min
        ac_cp_number_picker.maxValue = if (max == -1) 1 else max
    }

    fun navigateToResult(peopleNum: Int, moneyWeightId: Int) {

    }
}