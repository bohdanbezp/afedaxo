package com.afedaxo.presentation.ui.start

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.afedaxo.R
import com.afedaxo.databinding.FragmentStartBinding
import com.afedaxo.presentation.ui.base.BaseFragment
import org.koin.android.viewmodel.ext.android.viewModel


open class StartFragment : BaseFragment() {
    lateinit var binding: FragmentStartBinding

    val viewModel : StartViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_start, container, false
        )

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        viewModel.newSessionCreated.observe(this, Observer<Int> { sessionId ->
            navigateToFoodList(sessionId)
        })

        return binding.root
    }

    fun navigateToFoodList(sessionId: Int) {
        // TODO
    }
}