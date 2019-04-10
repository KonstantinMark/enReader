package com.bignerdranch.android.testpdfreader.control.content;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.bignerdranch.android.testpdfreader.R;
import com.bignerdranch.android.testpdfreader.databinding.FragmentPdfJsViewBinding;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

public class PdfJsViewFragment extends ResourceReceiverFragment {
    private static final String TAG = "PdfJsViewFragment";
    private static final String JS_PDF_PATH =
            "file:///android_asset/pdfjs/web/viewer.html?file=";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentPdfJsViewBinding binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_pdf_js_view,
                        container, false);

        configWebView(binding.pdfWebView);
        loadUrl(binding.pdfWebView);

        return binding.getRoot();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void configWebView(WebView view){
        view.getSettings().setJavaScriptEnabled(true);
        view.getSettings().setAllowUniversalAccessFromFileURLs(true);
        view.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        //view.addJavascriptInterface(new WordClickListener(), "clickListener");

        view.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.e(TAG, consoleMessage.message());
                return true;
            }
        });
    }

    private void loadUrl(WebView view){
        try {
            view.loadUrl(JS_PDF_PATH + URLEncoder.encode(getResourceUri().toString(), "UTF-8"));

            Log.i(TAG, URLEncoder.encode(getResourceUri().toString(), "UTF-8"));

        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        };
    }

    /*
    class WordClickListener {
        @JavascriptInterface
        public void onClick(String text){
            notifyTextSelected(text);
        }

        @JavascriptInterface
        public void onTextSelected(String text){
            notifyTextSelected(text);
        }
    }
    */



}
