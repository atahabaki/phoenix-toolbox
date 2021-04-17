package dev.atahabaki.phoenixtoolbox.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ToggleGcamViewModel: ViewModel() {
    private val mutableGcamState = MutableLiveData<Boolean>()
    val selectedGcamState: LiveData<Boolean> get() = mutableGcamState

    fun selectGcamState(state: Boolean) {
        mutableGcamState.value = state
    }
}