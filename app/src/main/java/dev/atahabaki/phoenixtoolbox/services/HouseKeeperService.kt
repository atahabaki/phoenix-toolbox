package dev.atahabaki.phoenixtoolbox.services

import android.app.job.JobParameters
import android.app.job.JobService
import android.os.Build
import androidx.annotation.RequiresApi
import dev.atahabaki.phoenixtoolbox.deleteCache

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class HouseKeeperService: JobService() {
    companion object {
        val jobId: Int = 74387
    }
    override fun onStartJob(params: JobParameters?): Boolean {
        deleteCache(packageName)
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
       return true
    }
}