package com.bignerdranch.android.testpdfreader.trash.textViewMod;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.bignerdranch.android.testpdfreader.R;


public class TextViewCustomSelectionMod extends androidx.appcompat.widget.AppCompatTextView
                                        implements UnSelectionListener{
    private static final String TAG = "TextViewCustomSelectionMod";

    private SelectionInfo mSelectionInfo;
    private SelectionListener mSelectionListener;

    private SelectActionCalback mSelectActionCalback;

    public TextViewCustomSelectionMod(Context context) {
        super(context);
        init();
    }

    public TextViewCustomSelectionMod(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextViewCustomSelectionMod(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mSelectActionCalback = new SelectActionCalback();
            setCustomSelectionActionModeCallback(mSelectActionCalback);
        }
    }

    public void setSelectionListener(SelectionListener listener){
        mSelectionListener = listener;
    }

    private void notifySelectionListener(String text){
        if(mSelectionListener != null
                && text != null){
            mSelectionListener.onTextSelected(text, this);
        }
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(new SpannableString(text), type);
        mSelectionInfo = new SelectionInfo((Spannable) getText());

        mSelectionInfo.setColorSelected(
                getContext().getResources().getColor(R.color.selectedTextBackground),
                getContext().getResources().getColor(R.color.selectedText));
        mSelectionInfo.setColorUnSelected(
                getContext().getResources().getColor(R.color.unSelectedTextBackground),
                getContext().getResources().getColor(R.color.unSelectedText));
    }

    @SuppressLint("LongLogTag")
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.i(TAG, "On det");
        removeSelection();
    }

    private static int CLICK_ACTION_THRESHOLD_DISTANCE = 200;
    private static int CLICK_ACTION_THRESHOLD_DURATION = 200;
    private static int DOUBLE_CLICK_DURATION = 200;
    private float startX;
    private float startY;
    private long lastTouchDownTime = -1;
    private long lastTouchUpTime = -1;
    private boolean lastTouchIsClick;

    @SuppressLint({"LongLogTag", "ClickableViewAccessibility"})
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // if in selection mod (user try to select text) do nothing
        if(hasSelection()) return false;

        float xTouch =  event.getX();
        float yTouch =  event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastTouchDownTime = System.currentTimeMillis();
                startX = xTouch;
                startY = yTouch;
                break;

            case MotionEvent.ACTION_UP:
                if(isALongClick(startX, xTouch, startY, yTouch)) {
                    removeSelection();
                    event.setAction(MotionEvent.ACTION_DOWN);
                    return super.onTouchEvent(event);
                }

                if (isAClick(startX, xTouch, startY, yTouch)) {
                    int offset = getPreciseOffset((int) xTouch, (int) yTouch);
                    lastTouchIsClick = true;

                    addSelectionToWordWithOffset(offset);
                } else {
                    lastTouchIsClick = false;
                }

                if(isDoubleClick(lastTouchUpTime)) {
                    int offset = getPreciseOffset((int) xTouch, (int) yTouch);
                    lastTouchIsClick = true;

                    addSelectionToSentenceWithOffset(offset);
                }
                lastTouchUpTime = System.currentTimeMillis();
        }

        return true;
    }

    private boolean isAClick(float startX, float endX, float startY, float endY) {
        float differenceX = Math.abs(startX - endX);
        float differenceY = Math.abs(startY - endY);
        return !(differenceX > CLICK_ACTION_THRESHOLD_DISTANCE
                || differenceY > CLICK_ACTION_THRESHOLD_DISTANCE);
    }

    private boolean isALongClick(float startX, float endX, float startY, float endY) {
       return isAClick(startX, endX, startY, endY) &&
               System.currentTimeMillis() - lastTouchDownTime >= CLICK_ACTION_THRESHOLD_DURATION;
    }

    private boolean isDoubleClick(long lastTouchDownTime){
        return lastTouchDownTime != -1
                && System.currentTimeMillis() - lastTouchDownTime < DOUBLE_CLICK_DURATION;
    }

    private int getPreciseOffset(int x, int y) {
        Layout layout = getLayout();

        if (layout != null) {
            int topVisibleLine = layout.getLineForVertical(y);
            int offset = layout.getOffsetForHorizontal(topVisibleLine, x);

            int offset_x = (int) layout.getPrimaryHorizontal(offset);
            if (offset_x > x) {
                return layout.getOffsetToLeftOf(offset);
            }
        }
        return getOffset(x, y);
    }

    private int getOffset(int x, int y) {
        Layout layout = getLayout();
        int offset = -1;

        if (layout != null) {
            int topVisibleLine = layout.getLineForVertical(y);
            offset = layout.getOffsetForHorizontal(topVisibleLine, x);
        }

        return offset;
    }

    @SuppressLint("LongLogTag")
    private void addSelectionToWordWithOffset(int offset){
        mSelectionInfo.changSelection(offset);
        notifySelectionListener(mSelectionInfo.getSelectedText());
    }

    @SuppressLint("LongLogTag")
    private void addSelectionToSentenceWithOffset(int offset){
        mSelectionInfo.changSelectionSentence(offset);
        Log.i(TAG, mSelectionInfo.getSelectedText());
        notifySelectionListener(mSelectionInfo.getSelectedText());
    }

    @SuppressLint("LongLogTag")
    @Override
    public void removeSelection() {
        mSelectionInfo.removeSelection();
        notifySelectionListener("");
        Log.i(TAG, "onRemuve");
    }

}
