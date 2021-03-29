package dev.atahabaki.shamrocktoolbox.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.atahabaki.shamrocktoolbox.models.Command

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
}