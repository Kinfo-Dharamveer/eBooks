package com.kinfoitsolutions.ebooks.ui.fragments


import android.app.ProgressDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment

import com.kinfoitsolutions.ebooks.R
import com.kinfoitsolutions.ebooks.ui.BaseFragment
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.fragment_web_view.view.*
import android.webkit.WebView
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.widget.Toast
import com.drivingschool.android.AppConstants
import com.orhanobut.hawk.Hawk


class WebViewFragment : BaseFragment() {

    private lateinit var viewOfLayout: View
    private lateinit var progressDialog: ProgressDialog

    override fun provideYourFragmentView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        viewOfLayout = inflater.inflate(R.layout.fragment_web_view, parent, false)
        setHasOptionsMenu(true)

        val url = Hawk.get(AppConstants.PDF_FILE,"")

        startWebView(url)


        return viewOfLayout
    }

    private fun startWebView(url: String) {


        val settings = viewOfLayout.webView.getSettings()

        settings.setJavaScriptEnabled(true);
        viewOfLayout.webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);

        viewOfLayout.webView.getSettings().setBuiltInZoomControls(true);
        viewOfLayout.webView.getSettings().setUseWideViewPort(true);
        viewOfLayout.webView.getSettings().setLoadWithOverviewMode(true);

        progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Loading...")
        progressDialog.show();


        viewOfLayout.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }

            override fun onPageFinished(view: WebView, url: String) {
                if (progressDialog.isShowing) {
                    progressDialog.dismiss()
                }
            }

            override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
                Toast.makeText(context, "Error:$description", Toast.LENGTH_SHORT).show()

            }
        }
        viewOfLayout.webView.loadUrl("https://docs.google.com/viewer?url=" + url)



    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.action_search).setVisible(false)
        menu.findItem(R.id.action_edit).setVisible(false)
        menu.findItem(R.id.action_filter).setVisible(false)
        super.onPrepareOptionsMenu(menu)

    }


}
