package dev.atahabaki.shamrocktoolbox.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import dev.atahabaki.shamrocktoolbox.R
import dev.atahabaki.shamrocktoolbox.databinding.FragmentQuickActionsBinding
import dev.atahabaki.shamrocktoolbox.exec
import dev.atahabaki.shamrocktoolbox.execRoot
import java.io.BufferedReader
import java.io.InputStreamReader

class QuickActionsFragment : Fragment(R.layout.fragment_quick_actions) {
    private var _binding: FragmentQuickActionsBinding? = null
    private val binding get() = _binding!!

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
        binding.quickAccessToggleGcam.setOnClickListener {
            toggleGcam()
        }
    }

    fun toggleGcam() {
        val prop = "persist.camera.HAL3.enabled"
        try {
            val p = java.lang.Runtime.getRuntime().exec("getprop $prop")
            p.waitFor()
            val stdOut = BufferedReader(InputStreamReader(p.inputStream))
            val line = stdOut.readLine()
            if (line.trim() == "1") {
                disableGcam(prop)
            } else {
                enableGcam(prop)
            }
        } catch (e: Exception) {
            Log.d("${activity?.packageName}.toggleGcam", "${e.message}")
        }
    }

    fun disableGcam(prop: String) {
        execRoot("setprop $prop 0", "${activity?.packageName}.setProp")
        notify(R.string.gcam_status_disabled,R.string.reboot)
    }

    fun enableGcam(prop: String) {
        execRoot("setprop $prop 1", "${activity?.packageName}.setProp")
        notify(R.string.gcam_status_enabled,R.string.reboot)
    }

    fun notify(@StringRes resId: Int, @StringRes actionId: Int) {
        val contextView = binding.root
        Snackbar.make(binding.root,resId,Snackbar.LENGTH_SHORT).setAction(actionId) {
            exec("reboot", "${activity?.packageName}.notify.reboot")
        }.setAnchorView(R.id.main_bottom_appbar).show()
    }
}