package dev.atahabaki.phoenixtoolbox.views

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.atahabaki.phoenixtoolbox.services.HouseKeeperService


class SplashActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            val info = JobInfo.Builder(HouseKeeperService.jobId, ComponentName(applicationContext, HouseKeeperService::class.java))
                    .setRequiresCharging(false)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .setRequiresDeviceIdle(false)
                    .setPersisted(true)
                    .setPeriodic(15 * 60 * 1000)
                    .build()
            val scheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
            scheduler.schedule(info)
        }

        Intent(this, HomeActivity::class.java).let {
            startActivity(it)
        }
        finish()
    }
}