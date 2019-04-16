package com.bignerdranch.android.testpdfreader.ui.main_fragment;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.android.testpdfreader.R;
import com.bignerdranch.android.testpdfreader.db.AppDatabase;
import com.bignerdranch.android.testpdfreader.databinding.FragmentMainBinding;
import com.bignerdranch.android.testpdfreader.databinding.ListItemBookBinding;
import com.bignerdranch.android.testpdfreader.db.entry.Resource;
import com.bignerdranch.android.testpdfreader.view.FragmentMainViewModel;
import com.bignerdranch.android.testpdfreader.view.item.ResourceItemViewModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainFragment extends Fragment {
    private String TAG = "MainFragment";
    private FragmentMainBinding mBinding;
    private ResourceAdapter mAdapter;

    public static MainFragment newInstance(){
        return new MainFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil
                .inflate(inflater, R.layout.
                                fragment_main,
                        container, false);

        mBinding.setViewModel(new FragmentMainViewModel());
        mBinding.itemsRecyclerView.setLayoutManager(
                new GridLayoutManager(getContext(), 1)
        );

        updateUI();

        return mBinding.getRoot();
    }

    private void updateUI(){
        setAnimationVisibility(true);
        Runnable runnable = () -> {
            Runnable runnable1 = () -> {
                updateResourcesList();
                setAnimationVisibility(false);
            };
            new Handler(Looper.getMainLooper()).postDelayed(runnable1, 0);
        };
        AsyncTask.execute(runnable);
    }

    private void setAnimationVisibility(boolean visibility) {
        mBinding.getViewModel().setProgressBarVisibility(visibility);
    }

    private void updateResourcesList() {
        AppDatabase db = AppDatabase.getDatabase(getContext());
        LiveData<List<Resource>> liveResources = db.resourceDao().loadAllResources();

        liveResources.observe(this, resources -> {
            if (mAdapter == null) {
                mAdapter = new ResourceAdapter(resources);
                mBinding.itemsRecyclerView.setAdapter(mAdapter);
            } else {
                List<Resource> current = mAdapter.getResources();
                for (Resource r: resources){
                    if(!current.contains(r))
                        mAdapter.addItem(r, resources.indexOf(r));
                }
                for(int i = 0; i < current.size(); i++){
                    if(!resources.contains(current.get(i))){
                        mAdapter.removeItem(i);
                    }
                }
            }
        });
    }

    public class ResourceHolder extends RecyclerView.ViewHolder {
        public ListItemBookBinding mBinding;
        Resource mResource;
        BookItemTouchListener mBookItemTouchListener;


        public ResourceHolder(ListItemBookBinding binding){
            super(binding.getRoot());
            mBinding = binding;
            mBookItemTouchListener = new BookItemTouchListener(this, MainFragment.this.mAdapter, getContext());
            setListeners();
        }

        public void setListeners() {
            mBinding.listItemBookForeground.setOnTouchListener(mBookItemTouchListener);
            mBinding.listItemBookForeground.setOnClickListener(new OnResourceClickListener(
                    getContext(), mResource));
            mBinding.listItemBookBackground.setOnClickListener(new OnResourceDeleteListener(
                    getContext(), mAdapter, getAdapterPosition(), mResource, getView()));
        }

        public void bind(Resource resource){
            ResourceItemViewModel model = new ResourceItemViewModel(getContext());
            model.setResource(resource);
            mBinding.setViewModel(model);
            mResource = resource;
            mBookItemTouchListener.refresh();
        }

    }

    public class ResourceAdapter extends RecyclerView.Adapter<ResourceHolder> {
        private List<Resource> mIResources;

        private ResourceHolder mCurrentInDeleteMod;

        @NonNull
        @Override
        public ResourceHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            ListItemBookBinding binding = DataBindingUtil
                    .inflate(layoutInflater, R.layout.list_item_book,
                            viewGroup, false);
            return new ResourceHolder(binding);
        }

        @SuppressLint("ClickableViewAccessibility")
        public ResourceAdapter(List<Resource> resourceList) {
            mIResources = resourceList;
            mBinding.itemsRecyclerView.setOnTouchListener(new ResourceOnTouchListener());
        }

        private class ResourceOnTouchListener implements View.OnTouchListener{

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                v.setOnTouchListener((v2, event2) -> {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        changCurrentInDeleteMod(null);
                    }
                    return false;
                });
                return false;
            }
        }

        @Override
        public void onBindViewHolder(@NonNull ResourceHolder resourceHolder, int i) {
            resourceHolder.bind(mIResources.get(i));
            mBinding.itemsRecyclerView.setOnClickListener(v -> changCurrentInDeleteMod(null));
        }

        public void removeItem(int position) {
            changCurrentInDeleteMod(null);
            mIResources.remove(position);
            notifyItemRemoved(position);
        }

        @Override
        public int getItemCount() {
            return mIResources.size();
        }

        public void setResources(List<Resource> resources){
            mIResources = resources;
        }

        public void addItem(Resource resource, int position) {
            changCurrentInDeleteMod(null);
            mIResources.add(position, resource);
            notifyItemInserted(position);
        }

        public void changCurrentInDeleteMod(ResourceHolder holder) {
            if (mCurrentInDeleteMod != null && mCurrentInDeleteMod != holder)
                mCurrentInDeleteMod.mBookItemTouchListener.setDefault();
            mCurrentInDeleteMod = holder;
        }

        public void forgetMe(ResourceHolder holder) {
            if (mCurrentInDeleteMod == holder) mCurrentInDeleteMod = null;
        }

        private List<Resource> getResources(){
            return mIResources;
        }
    }
}
