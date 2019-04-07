package com.bignerdranch.android.testpdfreader.control.content;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.bignerdranch.android.testpdfreader.control.content.pdf_mobile_view.PdfMobileViewFragment;

public abstract class TextSelectorFragment extends Fragment {
    private ITextSelectedReceiver mITextSelectedReceiver;
    private static final String ARG_URL = "uri";

    private Uri mUri;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mITextSelectedReceiver = (ITextSelectedReceiver) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUri = Uri.parse(getArguments().getString(ARG_URL));
    }

    protected Uri getResourceUri(){
        return mUri;
    }


    public static TextSelectorFragment newInstance(String uri){
        Bundle args = new Bundle();
        args.putString(ARG_URL, uri);

        PdfMobileViewFragment fragment = new PdfMobileViewFragment();
        fragment.setArguments(args);
        return fragment;
    }


    protected void notifyTextSelected(String text){
        if(mITextSelectedReceiver != null
                && text != null
                && text.length() > 0) {
            mITextSelectedReceiver.textSelected(text);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mITextSelectedReceiver = null;
    }
}
