package com.ymo.ui.base

import androidx.lifecycle.ViewModel
import com.ymo.data.model.error.ErrorManager
import com.ymo.di.NetworkModule

import javax.inject.Inject

abstract class BaseViewModel : ViewModel() {
    /**Inject Singleton ErrorManager
     * Use this errorManager to get the Errors
     */
    @Inject
    lateinit var errorManager: ErrorManager

    @Inject
    lateinit var network: NetworkModule.Network
}
