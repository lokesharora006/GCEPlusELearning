package com.mindorks.example.openpdffile.utils

import android.content.Context
import android.os.Environment
import androidx.core.content.ContextCompat
import java.io.File

object FileUtils {

    fun getPdfNameFromAssets(): String {
        return "MindOrks_Android_Online_Professional_Course-Syllabus.pdf"
    }

    fun getPdfUrl(): String {
      //  return Paper2Fragment.pdfUrl
        //val pdfUrl: String = paperList[position].paper2pdfLink

        return "https://firebasestorage.googleapis.com/v0/b/gcepluselearning.appspot.com/o/GCE-A-LEVEL%2F2017%2FFixes%20word.pdf?alt=media&token=e101730d-7a14-483b-ab37-a52d502c2287"
    }

    fun getRootDirPath(context: Context): String {
        return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            val file: File = ContextCompat.getExternalFilesDirs(
                context.applicationContext,
                null
            )[0]
            file.absolutePath
        } else {
            context.applicationContext.filesDir.absolutePath
        }
    }

}