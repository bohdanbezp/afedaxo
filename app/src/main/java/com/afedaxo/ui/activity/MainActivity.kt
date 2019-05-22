package com.afedaxo.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.afedaxo.R
import com.afedaxo.helper.MvpAppCompatActivity
import com.afedaxo.presentation.presenter.MainPresenter
import com.afedaxo.presentation.view.MainView
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : MvpAppCompatActivity(), MainView {
    companion object {
        const val TAG = "MainActivity"
        fun getIntent(context: Context): Intent = Intent(context, MainActivity::class.java)
    }

    @InjectPresenter
    lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ac_m_start_btn.setOnClickListener {
            presenter.onStartProcessClick()
        }
    }

    override fun navigateToFoodListActivity(sessionId: Int) {
        startActivity(FoodListActivity.getIntent(this, sessionId))
    }

    override fun navigateToChooseParamsActivity(sessionId: Int) {
        startActivity(ChooseParamsActivity.getIntent(this, sessionId))
    }
}
