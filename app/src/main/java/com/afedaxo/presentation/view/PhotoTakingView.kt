package com.afedaxo.presentation.view

import com.arellomobile.mvp.MvpView

interface PhotoTakingView : MvpView {
    fun navigateToSelectRegionActivity(fullsizeFilename: String)
    fun showReusePhotoButton()
    fun hideReusePhotoButton()
}
