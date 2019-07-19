package com.afedaxo.presentation.ui.result

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.UiThread
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.afedaxo.R
import com.afedaxo.databinding.FragmentFoodListBinding
import com.afedaxo.presentation.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_result.*
import org.koin.android.viewmodel.ext.android.viewModel

class ResultFragment : BaseFragment() {

    lateinit var binding: FragmentFoodListBinding

    val viewModel : ResultViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_result, container, false
        )

        // Creates a vertical Layout Manager
        ac_res_recyclerview.layoutManager = LinearLayoutManager(activity)

//        viewModel.think(intent.extras.getInt(FoodListActivity.SESSION_ID),
//            intent.extras.getInt(ResultActivity.MONEY_WEIGHT_ID), intent.extras.getInt(ResultActivity.PEOPLE_NUM)
//        )

        viewModel.resultAvailable.observe(this, Observer<List<Pair<Int, Bitmap>>> {
            showResult(it)
        })

        return binding.root
    }

    @UiThread
    fun showResult(dishesImgs: List<Pair<Int, Bitmap>>) {
        ac_res_recyclerview.adapter = DishResultAdapter(
            dishesImgs,
            activity!!
        )
        (ac_res_recyclerview.adapter as DishResultAdapter).notifyDataSetChanged()
        ac_res_recyclerview.scheduleLayoutAnimation()

    }

    fun navigateToStart() {
    }
}