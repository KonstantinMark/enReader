package com.bignerdranch.android.testpdfreader.controller.resources_list;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.android.testpdfreader.R;
import com.bignerdranch.android.testpdfreader.databinding.FragmentMainBinding;
import com.bignerdranch.android.testpdfreader.db.AppDatabase;
import com.bignerdranch.android.testpdfreader.db.entry.Resource;
import com.bignerdranch.android.testpdfreader.controller.resource.ResourceViewerActivity;
import com.bignerdranch.android.testpdfreader.viewmodal.ResourcesListViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class ResourceListFragment extends Fragment {

    private String TAG = "ResourceListFragment";

    private FragmentMainBinding mBinding;

    private ResourceAdapter mAdapter;

    private AppDatabase db;

    public static ResourceListFragment newInstance(){
        return new ResourceListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);

        mBinding.itemsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        mAdapter = new ResourceAdapter(mCallback);
        mBinding.itemsRecyclerView.setAdapter(mAdapter);

        db = AppDatabase.getDatabase(getContext());

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final ResourcesListViewModel viewModel =
                ViewModelProviders.of(this).get(ResourcesListViewModel.class);

        // remove resource
        ItemTouchHelper helper = new ItemTouchHelper(new SwipeToDeleteCallback(){
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Runnable runnable = () -> {
                    int position = viewHolder.getAdapterPosition();
                    Resource resource = Objects.requireNonNull(mAdapter.getResourceList()).get(position);
                    AppDatabase db = AppDatabase.getDatabase(getContext());
                    db.resourceDao().delete(resource);

                    Snackbar.make(Objects.requireNonNull(getView()), R.string.delete_undo_title, Snackbar.LENGTH_LONG)
                            .setAction(R.string.delete_undo_btn, v ->
                                    AsyncTask.execute(() -> db.resourceDao().insert(resource)))
                            .show();
                };
                AsyncTask.execute(runnable);
            }
        });
        helper.attachToRecyclerView(mBinding.itemsRecyclerView);

        subscribeUi(viewModel.getResources());
    }

    private void subscribeUi(LiveData<List<Resource>> liveData){
        liveData.observe(this, resources -> {
            if(resources != null){
                mAdapter.setResourceList(resources);
                mBinding.setIsLoading(false);
            } else {
                mBinding.setIsLoading(true);
            }
        });
    }

    private ResourceClickCallback mCallback = resource -> {
        Intent intent = ResourceViewerActivity.newIntent(getContext(), resource.uri);
        // set last date access
        AsyncTask.execute(()->{
            db.metaDataDao().updateTime(resource.uri, new Timestamp(new java.util.Date().getTime()));
        });
        startActivity(intent);
    };
}
