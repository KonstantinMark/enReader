package com.bignerdranch.android.testpdfreader.ui.resource.resource_view.page;

import android.text.Spannable;
import android.text.SpannableString;
import android.widget.TextView;

import com.bignerdranch.android.testpdfreader.R;
import com.bignerdranch.android.testpdfreader.model.text.UnderLineSpan;
import com.bignerdranch.android.testpdfreader.ui.resource.text_selection.TextSelector;

public class TextSelectorImpl implements TextSelector {
        private int mStart;
        private int mEnd;

        private TextView target;


        public TextSelectorImpl(TextView target) {
            this.target = target;
        }

        @Override
        public void set(int start, int end) {
            mStart = start;
            mEnd = end;
        }

        @Override
        public void unSelect() {
            target.setText(
                    target.getText().toString()
            );
        }

        @Override
        public void select() {
            Spannable spannable = SpannableString.valueOf(
                    target.getText().toString());
            spannable.setSpan(
                    new UnderLineSpan(R.color.selectedTextUnderline, R.color.selectedText),
                    mStart, mEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            target.setText(spannable);
        }
}