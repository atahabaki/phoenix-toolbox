package dev.atahabaki.phoenixtoolbox.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.atahabaki.phoenixtoolbox.models.Command

class RecoveryCommandViewModel: ViewModel() {
    private val mutableCommands = MutableLiveData<MutableList<Command>>()
    val commands: LiveData<MutableList<Command>> get() = mutableCommands

    init {
        mutableCommands.value = mutableListOf()
    }

    fun addCommand(command: Command) {
        mutableCommands.value?.add(command)
    }

    fun delAllCommands() {
        mutableCommands.value?.clear()
    }

    fun removeCommand(index: Int) {
        mutableCommands.value?.removeAt(index)
    }

    private val _isChanged = MutableLiveData<Boolean>()
    val isChanged: LiveData<Boolean> get() = _isChanged

    fun setDataChanged(changed: Boolean) {
        _isChanged.value = changed
    }

    fun setCommands(commands: MutableList<Command>) {
        mutableCommands.value = commands
    }
}