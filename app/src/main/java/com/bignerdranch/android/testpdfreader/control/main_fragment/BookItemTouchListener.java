package com.bignerdranch.android.testpdfreader.control.main_fragment;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.bignerdranch.android.testpdfreader.R;

public class BookItemTouchListener implements View.OnTouchListener {
    MainFragment.ResourceAdapter mAdapter;
    boolean movedBeforeUp = false;
    Float moveStart;
    private String TAG = "BookItemTouchListener";
    private MainFragment.ResourceHolder mHolder;
    private float mBackgroundWidth;
    private Context mContext;

    public BookItemTouchListener(MainFragment.ResourceHolder holder, MainFragment.ResourceAdapter adapter, Context context) {
        mHolder = holder;
        mAdapter = adapter;
        mContext = context;
        refresh();
    }

    public void refresh() {
        mBackgroundWidth = mHolder.mBinding.listItemBookBackground.getWidth();
        setDefault();
    }

    private float getBackgroundWidth() {
        if (mBackgroundWidth == 0) refresh();
        return mBackgroundWidth;
    }

    @Override
    public boolean onTouch(View v, final MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            setSelectionBackground();
            return setOnAfterDownListener(v, event);
        }
        return false;
    }

    private boolean setOnAfterDownListener(View v, final MotionEvent event) {
        mHolder.mBinding.listItemBookForeground.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    setSelectionBackground();
                } else {
                    setDefaultBackground();
                }
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    return onMove(v, event);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    return onUp(v, event);
                }
                if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    return onCancel(v, event);
                }
                //setDefaultBackground();
                return false;
            }
        });
        return true;
    }

    private void setSelectionBackground() {
        mHolder.mBinding.listItemBookForeground.setBackgroundColor(mContext.getResources().getColor(R.color.listBookElementBackground_selected));
    }

    private void setDefaultBackground() {
        mHolder.mBinding.listItemBookForeground.setBackgroundColor(mContext.getResources().getColor(R.color.listBookElementBackground));
    }

    private boolean onMove(View v, final MotionEvent event) {
        //setDefaultBackground();
        mAdapter.changCurrentInDeleteMod(mHolder);
        movedBeforeUp = true;
        if (moveStart == null) moveStart = event.getRawX();
        float dX = moveStart - event.getRawX();
        if (-dX < 0)
            mHolder.mBinding.listItemBookForeground.setX(-dX);
        return true;
    }

    private boolean onUp(View v, final MotionEvent event) {
        //setDefaultBackground();
        Log.i(TAG, "onUp");
        if (movedBeforeUp) {
            float dX = moveStart - event.getRawX();
//            setDefault();
            boolean isDeleteBtnVisible = dX > getBackgroundWidth();
            if (isDeleteBtnVisible) {
                setDeleteBtnVisibleState();
            } else {
                setDefault();
            }
            return true;
        } else {
            //setDefault();
            return false;
        }
    }

    private void setDeleteBtnVisibleState() {
        moveStart = null;
        movedBeforeUp = false;
        mHolder.setListeners();
        mHolder.mBinding.listItemBookForeground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDeleteBtnInvisibleState();
            }
        });
        mAdapter.changCurrentInDeleteMod(mHolder);
        mHolder.mBinding.listItemBookForeground.animate().translationX(-getBackgroundWidth());

    }

    private void setDeleteBtnInvisibleState() {
        mHolder.setListeners();
        mAdapter.forgetMe(mHolder);
        mHolder.mBinding.listItemBookForeground.animate().translationX(0);
    }


    private boolean onCancel(View v, final MotionEvent event) {
        //setDefaultBackground();
        if (movedBeforeUp) {
            float dX = moveStart - event.getRawX();
            if (dX > 50) {
                setDeleteBtnVisibleState();
            }
        } else {
            setDefault();
        }
        return true;
    }

    public void setDefault() {
        moveStart = null;
        movedBeforeUp = false;
        setDeleteBtnInvisibleState();
    }


}
