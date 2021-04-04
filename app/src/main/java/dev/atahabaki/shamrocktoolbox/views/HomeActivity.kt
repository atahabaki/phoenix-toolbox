package dev.atahabaki.shamrocktoolbox.views

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import dev.atahabaki.shamrocktoolbox.R
import dev.atahabaki.shamrocktoolbox.databinding.ActivityHomeBinding
import dev.atahabaki.shamrocktoolbox.exec
import dev.atahabaki.shamrocktoolbox.execRoot
import dev.atahabaki.shamrocktoolbox.viewmodels.FabStateViewModel
import dev.atahabaki.shamrocktoolbox.viewmodels.RecoveryCommandViewModel
import dev.atahabaki.shamrocktoolbox.viewmodels.RecoveryMenuStateViewModel
import dev.atahabaki.shamrocktoolbox.viewmodels.ToggleGcamViewModel
import java.io.*

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>

    private val viewModel: ToggleGcamViewModel by viewModels()
    private val fabViewModel: FabStateViewModel by viewModels()
    private val recViewModel: RecoveryCommandViewModel by viewModels()
    private val recMenuViewModel: RecoveryMenuStateViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initBottomNav()
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace(R.id.main_fragment_container, OpenRecoveryScriptingFragment()).addToBackStack("recovery")
            }
        }
        setupFab()
        setupGcam()
        setupMenu()
        setupBottomAppBar()
        setupNav()
    }

    private fun setupBottomAppBar() {
        binding.mainBottomAppbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.menu_rec_cmd_apply -> {
                    applyCommands()
                    true
                }
                R.id.menu_rec_cmd_clear -> {
                    deleteCommands()
                    true
                }
                else -> false
            }
        }
    }

    private fun setupNav() {
        binding.mainNavigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.main_menu_home -> {
                    dismissMainNavView()
                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        replace(R.id.main_fragment_container, QuickActionsFragment()).addToBackStack("quick_actions")
                    }
                    return@setNavigationItemSelectedListener true
                }
                R.id.main_menu_recovery -> {
                    dismissMainNavView()
                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        replace(R.id.main_fragment_container, OpenRecoveryScriptingFragment()).addToBackStack("recovery")
                    }
                    true
                }
                R.id.main_menu_coffee -> {
                    dismissMainNavView()
                    gotoBuyMeACoffee()
                    return@setNavigationItemSelectedListener true
                }
                R.id.main_menu_send_feedback -> {
                    dismissMainNavView()
                    gotoIssues()
                    return@setNavigationItemSelectedListener true
                }
                else -> false
            }
        }
    }

    private fun setupMenu() {
        recMenuViewModel.isMenuActive.observe(this, Observer {
            if (it) {
                binding.mainBottomAppbar.replaceMenu(R.menu.recovery_cmd_actions)
            }
            else {
                binding.mainBottomAppbar.replaceMenu(R.menu.empty)
            }
        })
    }

    private fun setupGcam() {
        viewModel.selectedGcamState.observe(this, Observer {
            if (it) {
                notify(R.string.gcam_status_enabled)
            }
            else {
                notify(R.string.gcam_status_disabled)
            }
        })
    }

    private fun setupFab() {
        fabViewModel.isVisible.observe(this, Observer {
            if (it)
                binding.mainFab.visibility = View.VISIBLE
            else binding.mainFab.visibility = View.GONE
        })
        fabViewModel.isClicked.observe(this, Observer {
            if (it) {
                val recoveryCommandDialog = RecoveryCommandDialog()
                recoveryCommandDialog.show(supportFragmentManager,"${packageName}.recoveryCommandDialog")
            }
        })
        binding.mainFab.setOnClickListener {
            fabViewModel.setClickState(true)
        }
    }

    private fun deleteCommands() {
        recViewModel.delAllCommands()
        recViewModel.setDataChanged(true)
        execRoot("su -c \"rm /cache/recovery/command\"", "${packageName}.deleteCommands")
    }

    private fun applyCommands() {
        val commands = recViewModel.commands.value!!
        execRoot("echo \"boot-recovery\" > /cache/recovery/command", "${packageName}.applyCommands")
        for (command in commands.iterator()) {
            if (command.command == "install" && needsPatch()) {
                execRoot("echo --update_package=${command.parameters?.joinToString(" ")}>> /cache/recovery/command", "${packageName}.applyCommands")
            }
            else execRoot("echo \"$command\" >> /cache/recovery/command", "${packageName}.applyCommands")
        }
        execRoot("chmod 666 /cache/recovery/command", "${packageName}.applyCommands")
        if (isApplied())
            notify(R.string.commands_applied)
        else
            Snackbar.make(binding.root, R.string.commands_not_applied, Snackbar.LENGTH_SHORT).setAction(R.string.retry, View.OnClickListener {
                applyCommands()
            }).setAnchorView(binding.mainBottomAppbar).show()
    }

    private fun isApplied(): Boolean {
        val from = "/cache/recovery/command"
        execRoot("su -c '[ -e $from ] && cp $from ${cacheDir.absolutePath}/command'", "${packageName}.applyCommands")
        return try {
            FileReader("${cacheDir.absolutePath}/command")
            true
        }
        catch (e: FileNotFoundException) {
            false
        }
    }

    private fun needsPatch(): Boolean {
        listOf<String>("shamrock", "mido").forEach {
            return android.os.Build.DEVICE == it
        }
        return false
    }

    private fun gotoBuyMeACoffee() {
        goto("https://buymeacoffee.com/atahabaki")
    }

    private fun gotoIssues() {
        goto("https://github.com/atahabaki/shamrock-toolbox/issues")
    }

    private fun goto(url: String) {
        val i: Intent = Intent(Intent.ACTION_VIEW)
        i.setData(Uri.parse(url))
        startActivity(i)
    }

    private fun notify(@StringRes resId: Int) {
        Snackbar.make(binding.root,resId, Snackbar.LENGTH_SHORT).setAction(R.string.reboot) {
            exec("reboot", "${packageName}.notify.reboot")
        }.setAnchorView(binding.mainBottomAppbar).show()
    }

    private fun initBottomNav() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.mainFramer)
        bottomSheetBehavior.isHideable = true
        bottomSheetBehavior.peekHeight = binding.mainBottomAppbar.height
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        initBottomAppBarNavigationClick()
        dismissWhenClickToFramerListener()
    }

    private fun initBottomAppBarNavigationClick() {
        binding.mainBottomAppbar.setNavigationOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            fabViewModel.setVisibility(false)
        }
    }

    private fun dismissWhenClickToFramerListener() {
        binding.mainFramer.setOnClickListener {
            dismissMainNavView()
        }
    }

    private fun dismissMainNavView() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        fabViewModel.setVisibility(true)
    }

    private fun initView() {
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}