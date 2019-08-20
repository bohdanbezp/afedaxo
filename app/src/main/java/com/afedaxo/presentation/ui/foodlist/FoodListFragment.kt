package com.afedaxo.presentation.ui.foodlist

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.UiThread
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.afedaxo.R
import com.afedaxo.databinding.FragmentFoodListBinding
import com.afedaxo.presentation.ui.base.BaseFragment
import com.afedaxo.util.reObserve
import kotlinx.android.synthetic.main.fragment_food_list.view.*
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

open class FoodListFragment : BaseFragment() {
    private lateinit var dishAdapter: DishAdapter

    var sessionId: Int = 0
    lateinit var binding: FragmentFoodListBinding

    val viewModel : FoodListViewModel by viewModel()

    var mDishesBitmap: List<Bitmap>? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.d("onActivityCreated")

        viewModel.onProcessClick.reObserve(this, Observer<Any> {
            navigateToChooseParamsActivity()
        })

        viewModel.onAddPhotoClick.reObserve(this, Observer<Any> {
            navigateToPhotoTakingActivity()
        })

        viewModel.dishesBitmaps.reObserve(this, Observer<List<Bitmap>> { bitmaps ->
            Timber.d("Bitmap updated! " + bitmaps.size)
            if (bitmaps.size >= 2) {
                enableProcessButton()
            }
            updateWithBitmaps(bitmaps)
        })
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_food_list, container, false
        )

        binding.viewModel = viewModel

        dishAdapter = DishAdapter(
                ArrayList(),
        context!!
        )

        // Creates a vertical Layout Manager
        binding.root.ac_fl_recyclerview.layoutManager = LinearLayoutManager(context)
        binding.root.ac_fl_recyclerview.adapter = dishAdapter

        binding.lifecycleOwner = this

        return binding.root
    }

    @UiThread
    fun updateWithBitmaps(dishesBitmap: List<Bitmap>) {
        checkEmpty()

        val bitmapDiffUtilCallback = DishBitmapDiffUtilCallback(dishAdapter.items, dishesBitmap)
        val bitmapDiffResult = DiffUtil.calculateDiff(bitmapDiffUtilCallback)
        dishAdapter.items = dishesBitmap
        bitmapDiffResult.dispatchUpdatesTo(dishAdapter)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionId = FoodListFragmentArgs.fromBundle(arguments!!).sessionId
    }

    @UiThread
    fun checkEmpty() {
        val adapter = binding.root.ac_fl_recyclerview.adapter
        if (adapter != null) {
            binding.root.ac_fl_empty_view.visibility = (if (adapter.itemCount == 0) View.VISIBLE else View.GONE)
        }
    }

    fun navigateToChooseParamsActivity() {
        val action
                = FoodListFragmentDirections.actionFoodListFragmentToChooseParamsFragment(sessionId)
        findNavController().navigate(action)
    }

    fun navigateToPhotoTakingActivity() {
        val action = FoodListFragmentDirections.actionFoodListFragmentToPhotoTakingFragment(sessionId)
        findNavController().navigate(action)
    }

    @UiThread
    fun enableProcessButton() {
        binding.root.ac_fl_process_btn.visibility = View.VISIBLE
    }

}

class DishBitmapDiffUtilCallback(private val oldList: List<Bitmap>, private val newList: List<Bitmap>): DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition] == newList[newItemPosition]
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition].sameAs(newList[newItemPosition])
}