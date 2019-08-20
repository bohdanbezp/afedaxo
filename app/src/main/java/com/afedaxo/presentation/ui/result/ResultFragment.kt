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
import com.afedaxo.databinding.FragmentResultBinding
import com.afedaxo.presentation.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_result.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class ResultFragment : BaseFragment() {

    lateinit var binding: FragmentResultBinding

    val viewModel : ResultViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_result, container, false
        )

        // Creates a vertical Layout Manager
        binding.root.ac_res_recyclerview.layoutManager = LinearLayoutManager(activity)

        val sessionId = ResultFragmentArgs.fromBundle(arguments!!).sessionId
        val peopleNum = ResultFragmentArgs.fromBundle(arguments!!).peopleNum
        val moneyWeightId = ResultFragmentArgs.fromBundle(arguments!!).moneyWeightId

        viewModel.think(sessionId,
            moneyWeightId, peopleNum
        )

        viewModel.resultAvailable.observe(this, Observer<List<Pair<Int, Bitmap>>> {
            showResult(it)
        })

        return binding.root
    }

    @UiThread
    fun showResult(dishesImgs: List<Pair<Int, Bitmap>>) {
        binding.root.ac_res_recyclerview.adapter = DishResultAdapter(
            dishesImgs,
            activity!!
        )
        (binding.root.ac_res_recyclerview.adapter as DishResultAdapter).notifyDataSetChanged()
        binding.root.ac_res_recyclerview.scheduleLayoutAnimation()

    }

    fun navigateToStart() {
    }
}