package com.afedaxo.presentation.ui.foodlist

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
import kotlinx.android.synthetic.main.fragment_food_list.*
import org.koin.android.viewmodel.ext.android.viewModel

open class FoodListFragment : BaseFragment() {
    lateinit var binding: FragmentFoodListBinding

    val viewModel : FoodListViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_food_list, container, false
        )

        binding.viewModel = viewModel

        viewModel.onProcessClick.observe(this, Observer<Any> {
            navigateToChooseParamsActivity()
        })

        viewModel.onAddPhotoClick.observe(this, Observer<Any> {
            navigateToPhotoTakingActivity()
        })

        viewModel.dishesBitmaps.observe(this, Observer<List<Bitmap>> { bitmaps ->
            if (bitmaps.size >= 2) {
                enableProcessButton()
            }
            updateWithBitmaps(bitmaps)
        })

        // Creates a vertical Layout Manager
        ac_fl_recyclerview.layoutManager = LinearLayoutManager(context)

        binding.lifecycleOwner = this

        return binding.root
    }

    @UiThread
    fun updateWithBitmaps(dishesBitmap: List<Bitmap>) {
        ac_fl_recyclerview.adapter = DishAdapter(
            dishesBitmap,
            context!!
        )
        (ac_fl_recyclerview.adapter as DishAdapter).notifyDataSetChanged()
        ac_fl_recyclerview.scheduleLayoutAnimation()
        checkEmpty()
    }

    @UiThread
    fun checkEmpty() {
        val adapter = ac_fl_recyclerview.adapter
        if (adapter != null) {
            ac_fl_empty_view.visibility = (if (adapter.itemCount == 0) View.VISIBLE else View.GONE)
        }
    }

    fun navigateToChooseParamsActivity() {

    }

    fun navigateToPhotoTakingActivity() {

    }

    @UiThread
    fun enableProcessButton() {
        ac_fl_process_btn.visibility = View.VISIBLE
    }

}