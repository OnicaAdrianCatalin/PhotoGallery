package com.example.photogallery.presentation

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.example.photogallery.databinding.FragmentPhotoPageBinding

class PhotoPageFragment : PollNotificationHandlerFragment() {
    private lateinit var uri: Uri
    private lateinit var binding: FragmentPhotoPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        uri = arguments?.getParcelable(ARG_URI) ?: Uri.EMPTY
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhotoPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.webView.settings.javaScriptEnabled = true
        setupProgressBar()
        binding.webView.loadUrl(uri.toString())
    }

    private fun setupProgressBar() {
        binding.webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(webView: WebView?, newProgress: Int) {
                if (newProgress == MAX_PERCENT) {
                    binding.progressBar.visibility = View.GONE
                } else {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.progressBar.progress = newProgress
                }
            }

            override fun onReceivedTitle(view: WebView?, title: String?) {
                (activity as AppCompatActivity).supportActionBar?.subtitle = title
            }
        }
    }

    companion object {
        private const val ARG_URI = "photo_page_uri"
        private const val MAX_PERCENT = 100

        fun newInstance(uri: Uri?): PhotoPageFragment {
            return PhotoPageFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_URI, uri)
                }
            }
        }
    }
}
