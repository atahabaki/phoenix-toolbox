package dev.atahabaki.phoenixtoolbox.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dev.atahabaki.phoenixtoolbox.R
import dev.atahabaki.phoenixtoolbox.databinding.FragmentQuickActionsBinding
import dev.atahabaki.phoenixtoolbox.execRoot
import dev.atahabaki.phoenixtoolbox.viewmodels.FabStateViewModel
import dev.atahabaki.phoenixtoolbox.viewmodels.RecoveryMenuStateViewModel
import dev.atahabaki.phoenixtoolbox.viewmodels.ToggleGcamViewModel
import java.io.BufferedReader
import java.io.InputStreamReader

class QuickActionsFragment : Fragment(R.layout.fragment_quick_actions) {
    private var _binding: FragmentQuickActionsBinding? = null
    private val binding get() = _binding!!
    private val gcamProp = "persist.camera.HAL3.enabled"

    private val viewModel: ToggleGcamViewModel by activityViewModels()
    private val fabViewModel: FabStateViewModel by activityViewModels()
    private val recMenuViewModel: RecoveryMenuStateViewModel by activityViewModels()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuickActionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fabViewModel.setVisibility(false)
        setupSwitch()
        recMenuViewModel.setState(false)
        binding.quickAccessClearCache.setOnClickListener {
            execRoot("rm -rf /cache/*", "${activity?.packageName}.clearCache")
        }
        binding.quickAccessReloadGcamStatus.setOnClickListener {
            setupSwitchStatus()
        }
    }

    private fun setupSwitchStatus() {
        binding.quickAccessToggleGcam.isChecked = getGcamStatus()
    }

    private fun setupSwitch() {
        setupSwitchStatus()
        binding.quickAccessToggleGcam.setOnCheckedChangeListener { _, isChecked -> toggleGcam(isChecked)}
    }

    private fun toggleGcam(isChecked: Boolean) {
        if (isChecked) {
            enableGcam()
        }
        else {
            disableGcam()
        }
    }

    private fun getGcamStatus(): Boolean {
        try {
            val p = java.lang.Runtime.getRuntime().exec("getprop $gcamProp")
            p.waitFor()
            val stdOut = BufferedReader(InputStreamReader(p.inputStream))
            val line = stdOut.readLine().trim()
            return line == "1"
        } catch (e: Exception) {
            Log.d("${activity?.packageName}.toggleGcam", "${e.message}")
        }
        return false
    }

    private fun disableGcam() {
        execRoot("setprop $gcamProp 0", "${activity?.packageName}.setProp")
        val status = getGcamStatus()
        if (!status) viewModel.selectGcamState(false)
        binding.quickAccessToggleGcam.isChecked = status
    }

    private fun enableGcam() {
        execRoot("setprop $gcamProp 1", "${activity?.packageName}.setProp")
        val status = getGcamStatus()
        if (status) viewModel.selectGcamState(true)
        binding.quickAccessToggleGcam.isChecked = status
    }
}