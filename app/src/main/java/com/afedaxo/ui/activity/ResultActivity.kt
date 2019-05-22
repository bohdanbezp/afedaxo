package com.afedaxo.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.afedaxo.R
import com.afedaxo.helper.MvpAppCompatActivity
import com.afedaxo.presentation.presenter.ResultPresenter
import com.afedaxo.presentation.view.ResultView
import com.arellomobile.mvp.presenter.InjectPresenter
import android.graphics.Bitmap
import androidx.recyclerview.widget.LinearLayoutManager
import com.afedaxo.ui.adapter.DishAdapter
import com.afedaxo.ui.adapter.DishResultAdapter
import kotlinx.android.synthetic.main.activity_food_list.*
import kotlinx.android.synthetic.main.activity_result.*


class ResultActivity : MvpAppCompatActivity(), ResultView {
    companion object {
        const val TAG = "ResultActivity"
        const val PEOPLE_NUM = "PEOPLE_NUM"
        const val MONEY_WEIGHT_ID = "MONEY_WEIGHT_ID"
        fun getIntent(context: Context, sessionId: Int,
                      peopleNum: Int, moneyWeightId: Int): Intent {
            val intent = Intent(context, ResultActivity::class.java)
            intent.putExtra(FoodListActivity.SESSION_ID, sessionId)
            intent.putExtra(PEOPLE_NUM, peopleNum)
            intent.putExtra(MONEY_WEIGHT_ID, moneyWeightId)
            return intent
        }
    }

    @InjectPresenter
    lateinit var mResultPresenter: ResultPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        // Creates a vertical Layout Manager
        ac_res_recyclerview.layoutManager = LinearLayoutManager(this)

        mResultPresenter.think(intent.extras.getInt(FoodListActivity.SESSION_ID),
            intent.extras.getInt(MONEY_WEIGHT_ID), intent.extras.getInt(PEOPLE_NUM)
            )

    }

    override fun showResult(dishesImgs: List<Pair<Int, Bitmap>>) {
        runOnUiThread {
            ac_res_recyclerview.adapter = DishResultAdapter(
                dishesImgs,
                this@ResultActivity
            )
            (ac_res_recyclerview.adapter as DishResultAdapter).notifyDataSetChanged()
            ac_res_recyclerview.scheduleLayoutAnimation()
        }

    }

    override fun onDestroy() {
        super.onDestroy()


    }
}
