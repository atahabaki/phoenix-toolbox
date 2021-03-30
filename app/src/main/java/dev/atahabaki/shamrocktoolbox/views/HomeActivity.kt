package dev.atahabaki.shamrocktoolbox.views

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import dev.atahabaki.shamrocktoolbox.R
import dev.atahabaki.shamrocktoolbox.databinding.ActivityHomeBinding
import dev.atahabaki.shamrocktoolbox.exec
import dev.atahabaki.shamrocktoolbox.execRoot
import dev.atahabaki.shamrocktoolbox.models.Command
import dev.atahabaki.shamrocktoolbox.viewmodels.FabStateViewModel
import dev.atahabaki.shamrocktoolbox.viewmodels.RecoveryCommandViewModel
import dev.atahabaki.shamrocktoolbox.viewmodels.RecoveryMenuStateViewModel
import dev.atahabaki.shamrocktoolbox.viewmodels.ToggleGcamViewModel

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
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                //add<QuickActionsFragment>(R.id.main_fragment_container)
                add<OpenRecoveryScriptingFragment>(R.id.main_fragment_container)
            }
        }
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
        viewModel.selectedGcamState.observe(this, Observer {
            if (it) {
                notify(R.string.gcam_status_enabled)
            }
            else {
                notify(R.string.gcam_status_disabled)
            }
        })
        recMenuViewModel.isMenuActive.observe(this, Observer {
            if (it) {
                binding.mainBottomAppbar.replaceMenu(R.menu.recovery_cmd_actions)
            }
            else {
                binding.mainBottomAppbar.replaceMenu(R.menu.empty)
            }
        })
        binding.mainNavigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.main_menu_home -> {
                    dismissMainNavView()
                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        replace(R.id.main_fragment_container, QuickActionsFragment())
                    }
                    return@setNavigationItemSelectedListener true
                }
                R.id.main_menu_recovery -> {
                    dismissMainNavView()
                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        replace(R.id.main_fragment_container, OpenRecoveryScriptingFragment())
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
        binding.mainBottomAppbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.menu_rec_cmd_apply -> {
                    val commands = recViewModel.commands.value!!
                    execRoot("echo \"boot-recovery\" > /cache/recovery/command", "${packageName}.apply_rec")
                    for (command in commands.iterator()) {
                        if (command.command == "install" && android.os.Build.DEVICE == "shamrock") {
                            execRoot("echo --update_package=${command.parameters?.joinToString(" ")}>> /cache/recovery/command", "${packageName}.apply_rec")
                        }
                        else execRoot("echo \"$command\" >> /cache/recovery/command", "${packageName}.apply_rec")
                    }
                    execRoot("chmod 666 /cache/recovery/command", "${packageName}.apply_rec");
                    true
                }
                R.id.menu_rec_cmd_clear -> {
                    recViewModel.delAllCommands()
                    recViewModel.setDataChanged(true)
                    true
                }
                else -> false
            }
        }
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