package com.bignerdranch.android.testpdfreader.ui.resources_list;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.android.testpdfreader.R;
import com.bignerdranch.android.testpdfreader.databinding.FragmentMainBinding;
import com.bignerdranch.android.testpdfreader.db.AppDatabase;
import com.bignerdranch.android.testpdfreader.db.entry.Resource;
import com.bignerdranch.android.testpdfreader.viewmodal.ResourcesListViewModel;
import com.google.android.material.snackbar.Snackbar;

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

    public static ResourceListFragment newInstance(){
        return new ResourceListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);

        mBinding.itemsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        mAdapter = new ResourceAdapter();
        mBinding.itemsRecyclerView.setAdapter(mAdapter);

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final ResourcesListViewModel viewModel =
                ViewModelProviders.of(this).get(ResourcesListViewModel.class);

        //TODO set click listener
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

    //    private void updateUI(){
//        setAnimationVisibility(true);
//        Runnable runnable = () -> {
//            Runnable runnable1 = () -> {
//                updateResourcesList();
//                setAnimationVisibility(false);
//            };
//            new Handler(Looper.getMainLooper()).postDelayed(runnable1, 0);
//        };
//        AsyncTask.execute(runnable);
//    }
//
//    private void setAnimationVisibility(boolean visibility) {
//        binding.getViewModel().setIsLoading(visibility);
//    }
//
//    private void updateResourcesList() {
//        AppDatabase db = AppDatabase.getDatabase(getContext());
//        LiveData<List<Resource>> liveResources = db.resourceDao().loadAllResources();
//
//        liveResources.observe(this, resources -> {
//            if (mAdapter == null) {
//                mAdapter = new ResourceAdapter(resources, getContext());
//                binding.itemsRecyclerView.setAdapter(mAdapter);
//            } else {
//                List<Resource> current = mAdapter.getResources();
//                for (Resource r: resources){
//                    if(!current.contains(r))
//                        mAdapter.addItem(r, resources.indexOf(r));
//                }
//                for(int i = 0; i < current.size(); i++){
//                    if(!resources.contains(current.get(i))){
//                        mAdapter.removeItem(i);
//                    }
//                }
//            }
//        });
//    }
}
