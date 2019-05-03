package com.bignerdranch.android.testpdfreader.ui.resource.resource_view.page;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.testpdfreader.model.text.TextManager;
import com.bignerdranch.android.testpdfreader.ui.resource.text_selection.ITextSelectionListener;
import com.bignerdranch.android.testpdfreader.ui.resource.text_selection.TextSelector;

public class OnWordClickedListener implements View.OnClickListener {
    private TextSelector mSelector;
    private TextSelectionManager mTextSelectionManager;

    public OnWordClickedListener(TextSelector selector, TextSelectionManager textSelectionManager) {
        mSelector = selector;
        mTextSelectionManager = textSelectionManager;
    }

    @Override
    public void onClick(View v) {
        TextView textView = (TextView) v;
        TextManager textManager = new TextManager(textView.getText().toString());
        int pos[] = textManager.getWordSelectionPositions(textView.getSelectionStart());
        String selectedWord = textManager.getWordSelection(textView.getSelectionStart());

        mSelector.set(pos[0], pos[1]);
        mSelector.select();

//                newMarkerIntent(mMarker);
//                notifyTextSelected(selectedWord, mSelector);
        mTextSelectionManager.notifyTextSelected(selectedWord, mSelector);
    }
}
