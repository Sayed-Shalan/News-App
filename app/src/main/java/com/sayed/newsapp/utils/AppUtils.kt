package com.sayed.newsapp.utils

import android.app.Activity
import android.app.AlertDialog
import android.app.TaskStackBuilder
import android.content.Context
import android.net.ConnectivityManager
import android.os.Environment
import android.support.v4.app.NavUtils
import com.sayed.newsapp.other.OkCancelCallback
import com.sayed.newsapp.ui.base.BaseActivity
import java.text.SimpleDateFormat
import java.util.*
import android.os.Environment.getExternalStorageDirectory
import java.io.File.separator
import android.os.Environment.MEDIA_MOUNTED
import java.io.File


public class AppUtils {

    //Static data
    companion object{
        //method for check if the device is connected to internet or no
        public fun isNetworkAvailable(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }

        @JvmStatic
        fun navigateUp(activity: BaseActivity) {
            val upIntent = NavUtils.getParentActivityIntent(activity)
            if (upIntent == null) {
                activity.finish()
                //            if (activity.getResources().getBoolean(R.bool.is_right_to_left)) activity.overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                //            else activity.overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            } else if (NavUtils.shouldUpRecreateTask(activity, upIntent)) {
                // This activity is NOT part of this app's task, so create a new task
                // when navigating up, with a synthesized back stack.
                TaskStackBuilder.create(activity)
                    // Add all of this activity's parents to the back stack
                    .addNextIntentWithParentStack(upIntent)
                    // Navigate up to the closest parent
                    .startActivities()
            } else {
                // This activity is part of this app's task, so simply
                // navigate up to the logical parent activity.
                NavUtils.navigateUpTo(activity, upIntent)
            }
        }


        //open ok cancel Dialog
        fun buildOkCancelDialog(activity: Activity, title: String, okStr: String, cancelStr: String, okCancelCallback: OkCancelCallback) {
            val builder = AlertDialog.Builder(activity)
            builder.setMessage(title)
                .setCancelable(false)
                .setPositiveButton(okStr) { dialog, which -> okCancelCallback.onOkClick() }
                .setNegativeButton(cancelStr) { dialog, which ->
                    okCancelCallback.onCancelClick()
                    dialog.cancel()
                }
            val alertDialog = builder.create()
            alertDialog.show()
        }

        //convert date to client timezone
        fun convertDateToClientTimeZone(mDate: String): String{
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z", Locale.getDefault())
            formatter.timeZone = TimeZone.getTimeZone("UTC")
            return formatter.parse(mDate).toString()
        }

        //get cache dir
        fun getDiskCacheDir(context: Context, uniqueName: String="responses"): File {
            val cachePath = if (Environment.MEDIA_MOUNTED
                    .equals(Environment.getExternalStorageState())
            )
                getExternalCacheDir(
                    context
                ).getPath()/* w w  w.ja  v  a  2 s .  c  om*/
            else
                context.cacheDir.path

            return File(cachePath + File.separator + uniqueName)
        }

        fun getExternalCacheDir(context: Context): File {
            val cacheDir = ("/Android/data/" + context.packageName
                    + "/cache/")
            return File(Environment.getExternalStorageDirectory().getPath() + cacheDir)
        }
    }



}