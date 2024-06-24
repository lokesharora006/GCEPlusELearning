package com.phinion.gcepluselearning

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.barteksc.pdfviewer.PDFView
import okhttp3.*
import java.io.IOException

class PdfViewActivity : AppCompatActivity() {

    private lateinit var pdfView: PDFView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_view)



        pdfView = findViewById(R.id.pdfView)

        val pdfUrl = intent.getStringExtra("pdf_url")

        if (pdfUrl != null) {
            downloadAndDisplayPdf(pdfUrl)
        } else {
            Toast.makeText(this, "PDF not found", Toast.LENGTH_SHORT).show()
        }

        val downloadButton = findViewById<Button>(R.id.downloadButton)
        downloadButton.setOnClickListener {
            downloadPdf(pdfUrl)
        }
    }

    private fun downloadAndDisplayPdf(url: String) {
        val request = Request.Builder()
            .url(url)
            .build()

        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(
                        this@PdfViewActivity,
                        "Failed to download PDF",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val inputStream = response.body?.byteStream()
                runOnUiThread {
                    pdfView.fromStream(inputStream)
                        .onError {
                            Toast.makeText(
                                this@PdfViewActivity,
                                "Failed to open PDF",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        .load()
                }
            }
        })
    }

    private fun downloadPdf(url: String?) {
        if (url != null) {
            val request = DownloadManager.Request(Uri.parse(url))
            request.setTitle("Downloading PDF")
            request.setDescription("Please wait...")
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                "filename.pdf"
            )
            val manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            manager.enqueue(request)
        }
    }

}