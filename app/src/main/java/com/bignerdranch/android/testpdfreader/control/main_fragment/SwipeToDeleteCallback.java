package com.bignerdranch.android.testpdfreader.control.main_fragment;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

enum ButtonsState {
    GONE,
    VISIBLE
}

public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {
    private String TAG = "SwipeToDeleteCallback";
    private HashMap<RecyclerView.ViewHolder, ButtonsState> viewHoldersStated = new HashMap<>();

    private ButtonsState buttonShowedState = ButtonsState.GONE;
    private RectF buttonInstance = null;
    private boolean swipeBack = false;

    public SwipeToDeleteCallback(int dragDirs, int swipeDirs) {
        super(dragDirs, swipeDirs);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        final View foregroundView = ((MainFragment.ResourceHolder) viewHolder).mBinding.listItemBookForeground;
        getDefaultUIUtil().clearView(foregroundView);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {
        final View foregroundView = ((MainFragment.ResourceHolder) viewHolder).mBinding.listItemBookForeground;
        int width = getDeleteItemWidth(viewHolder);
        dX = Math.max(dX, -width);

        getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive);
        setTouchListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    private int getDeleteItemWidth(RecyclerView.ViewHolder viewHolder) {
        final View foregroundView = ((MainFragment.ResourceHolder) viewHolder).mBinding.listItemBookBackground;
        return foregroundView.getWidth();
    }

    private int getRootWidth(RecyclerView.ViewHolder viewHolder) {
        final View foregroundView = ((MainFragment.ResourceHolder) viewHolder).mBinding.listItemBookRootElement;
        return foregroundView.getWidth();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {


        //listener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
        Log.i(TAG, "onSwiped " + direction);

    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    private void setTouchListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
        final int width = getDeleteItemWidth(viewHolder);
        final int rootWidth = getRootWidth(viewHolder);


        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i(TAG, String.valueOf(dX) + "   ");

                swipeBack = event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP;
                if (swipeBack) {
                    Log.i(TAG, String.valueOf(event.getX()));
                }
                return false;
            }
        });
    }

    private void setItemsClickable(RecyclerView recyclerView, boolean isClickable) {
        for (int i = 0; i < recyclerView.getChildCount(); ++i) {
            recyclerView.getChildAt(i).setClickable(isClickable);
        }
    }

    public interface RecyclerItemTouchHelperListener {
        void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);

    }
}