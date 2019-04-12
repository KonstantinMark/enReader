package com.bignerdranch.android.testpdfreader.control.main_fragment;

import android.graphics.Canvas;
import android.util.Log;
import android.view.View;

import java.util.HashMap;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import static androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_SWIPE;
import static androidx.recyclerview.widget.ItemTouchHelper.LEFT;
import static androidx.recyclerview.widget.ItemTouchHelper.RIGHT;

enum ButtonsState {
    GONE,
    VISIBLE,
}

class SwipeController extends ItemTouchHelper.Callback {
    float cc;
    int width;
    private String TAG = "SwipeController";
    private HashMap<RecyclerView.ViewHolder, ItemState> states = new HashMap<>();
    private RecyclerView.ViewHolder last;

    public SwipeController(SwipeControllerActions buttonsActions) {
    }

    private ItemState getState(RecyclerView.ViewHolder viewHolder) {
        if (!states.containsKey(viewHolder)) {
            states.put(viewHolder, new ItemState());
        }
        last = viewHolder;
        return states.get(viewHolder);
    }

    private void setState(RecyclerView.ViewHolder viewHolder, ItemState state) {
        states.put(viewHolder, state);
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, LEFT | RIGHT);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        Log.i(TAG, "onSwiped");
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        getDefaultUIUtil().clearView(((MainFragment.ResourceHolder) viewHolder).mBinding.listItemBookForeground);
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        Log.i(TAG, flags + "  +++++  " + layoutDirection + "   = " + super.convertToAbsoluteDirection(flags, layoutDirection));
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    @Override
    public void onChildDraw(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
        cc = dX;
        width = getDeleteItemWidth(viewHolder);
        final View foregroundView = ((MainFragment.ResourceHolder) viewHolder).mBinding.listItemBookForeground;
        if (actionState == ACTION_STATE_SWIPE) {
            Log.i(TAG, width + " " + dX);
            dX = Math.max(-width, dX);
            getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
                    actionState, isCurrentlyActive);
            if (-width == dX) {

            }
        }
    }

    private int getDeleteItemWidth(RecyclerView.ViewHolder viewHolder) {
        final View foregroundView = ((MainFragment.ResourceHolder) viewHolder).mBinding.listItemBookBackground;
        return foregroundView.getWidth();
    }

    private int getRootWidth(RecyclerView.ViewHolder viewHolder) {
        final View foregroundView = ((MainFragment.ResourceHolder) viewHolder).mBinding.listItemBookRootElement;
        return foregroundView.getWidth();
    }

    class ItemState {
        boolean swipeBack = false;
        ButtonsState buttonShowedState = ButtonsState.GONE;
    }
}