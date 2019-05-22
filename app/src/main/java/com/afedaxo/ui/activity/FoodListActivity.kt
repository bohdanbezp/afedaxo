package com.afedaxo.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afedaxo.R
import com.afedaxo.helper.MvpAppCompatActivity
import com.afedaxo.presentation.presenter.FoodListPresenter
import com.afedaxo.presentation.view.FoodListView
import com.afedaxo.ui.adapter.DishAdapter
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.activity_food_list.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class FoodListActivity : MvpAppCompatActivity(), FoodListView {
    companion object {
        const val TAG = "FoodListActivity"
        const val SESSION_ID = "SESSION_ID"
        fun getIntent(context: Context, sessionId: Int): Intent {
            val intent = Intent(context, FoodListActivity::class.java)
            intent.putExtra(SESSION_ID, sessionId)
            return intent
        }
    }

    @InjectPresenter
    lateinit var mFoodListPresenter: FoodListPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_list)

        // Creates a vertical Layout Manager
        ac_fl_recyclerview.layoutManager = LinearLayoutManager(this)

        ac_fl_add_btn.setOnClickListener {
            startActivity(PhotoTakingActivity.getIntent(this,
                intent.extras.getInt(SESSION_ID)))
        }

        ac_fl_process_btn.setOnClickListener {
            mFoodListPresenter.onProcessClick()
        }
    }

    override fun onStart() {
        super.onStart()

        GlobalScope.launch(IO) {
            val dishesBitmap = mFoodListPresenter.retrieveDishesBitmaps()
            if (dishesBitmap != null) {
                runOnUiThread({
                    ac_fl_recyclerview.adapter = DishAdapter(
                        dishesBitmap,
                        this@FoodListActivity
                    )
                    (ac_fl_recyclerview.adapter as DishAdapter).notifyDataSetChanged()
                    ac_fl_recyclerview.scheduleLayoutAnimation()
                    checkEmpty()
                })
            }
        }
    }

    fun checkEmpty() {
        val adapter = ac_fl_recyclerview.adapter
        if (adapter != null) {
            ac_fl_empty_view.visibility = (if (adapter.itemCount == 0) View.VISIBLE else View.GONE)
        }
    }

    override fun navigateToChooseParamsActivity() {
        startActivity(ChooseParamsActivity.getIntent(this, intent.extras.getInt(SESSION_ID)))
    }

    override fun enableProcessButton() {
        runOnUiThread {
            ac_fl_process_btn.visibility = View.VISIBLE
        }
    }
}
