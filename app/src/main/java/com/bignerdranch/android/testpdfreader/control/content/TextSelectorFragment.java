package com.bignerdranch.android.testpdfreader.control.content;

import android.content.Context;
import android.support.v4.app.Fragment;

public abstract class TextSelectorFragment extends Fragment {
    private ITextSelectedReceiver mITextSelectedReceiver;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mITextSelectedReceiver = (ITextSelectedReceiver) context;
    }

    protected void notifyTextSelected(String text){
        if(mITextSelectedReceiver != null
                && text != null
                && text.length() > 0) {
            mITextSelectedReceiver.textSelected(text);
        }
    }

    protected void notifyParagraphSelected(String paragraph, TranslationReceiver receiver) {
        if (mITextSelectedReceiver != null
                && paragraph != null
                && paragraph.length() > 0) {
            mITextSelectedReceiver.paragraphSelected(paragraph, receiver);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mITextSelectedReceiver = null;
    }
}
