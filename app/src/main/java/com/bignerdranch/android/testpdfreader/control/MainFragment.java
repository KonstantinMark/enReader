package com.bignerdranch.android.testpdfreader.control;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.android.testpdfreader.R;
import com.bignerdranch.android.testpdfreader.databinding.FragmentMainBinding;
import com.bignerdranch.android.testpdfreader.databinding.ListItemBookBinding;
import com.bignerdranch.android.testpdfreader.model.ResourceDescriptor;
import com.bignerdranch.android.testpdfreader.model.storage.BookStorage;
import com.bignerdranch.android.testpdfreader.model.storage.resource.IResource;
import com.bignerdranch.android.testpdfreader.view.FragmentMainViewModel;
import com.bignerdranch.android.testpdfreader.view.ItemResourceViewModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainFragment extends Fragment implements MainActivity.ResourceItemAddedListener{

    private FragmentMainBinding mBinding;
    private ResourceAdapter mAdapter;

    public static MainFragment newInstance(){
        return new MainFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentMainBinding binding = DataBindingUtil
                .inflate(inflater, R.layout.
                                fragment_main,
                        container, false);
        mBinding = binding;

        mBinding.setViewModel(new FragmentMainViewModel());

        mBinding.itemsRecyclerView.setLayoutManager(
                new GridLayoutManager(getContext(), 2)
        );

        updateUI();

        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI(){
        mBinding.getViewModel().setProgressBarVisibility(true);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                BookStorage storage = BookStorage.instance(getContext());
                final List<IResource> resources = storage.getAll();

                Handler handler = new Handler(Looper.getMainLooper());
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if (mAdapter == null) {
                            mAdapter = new ResourceAdapter(resources);
                            mBinding.itemsRecyclerView.setAdapter(mAdapter);
                        } else {
                            mAdapter.setResources(resources);
                            mAdapter.notifyDataSetChanged();
                        }
                        mBinding.getViewModel().setProgressBarVisibility(false);
                    }
                };
                handler.postDelayed(runnable, 0);
            }
        };
        AsyncTask.execute(runnable);

    }

    @Override
    public void notifyItemAdded(Uri uri) {
        BookStorage storage = BookStorage.instance(getContext());
        IResource item = storage.get(uri);
        if (item != null)
            mAdapter.addItem(item);
    }

    private class ResourceHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener,
            View.OnLongClickListener {
        private ListItemBookBinding mBinding;
        Uri mUri;

        public ResourceHolder(ListItemBookBinding binding){
            super(binding.getRoot());
            mBinding = binding;
            mBinding.setViewModel(new ItemResourceViewModel(getContext()));
        }

        public void bind(IResource resource){
            mBinding.getViewModel().setResource(resource);
            mUri = resource.getUri();
            mBinding.getRoot().setOnClickListener(this);
            mBinding.getRoot().setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent i = BookViewerActivity.newIntent(
                    getContext(),
                    new ResourceDescriptor(mUri.toString(),
                            ResourceDescriptor.PDF_MOBILE_TYPE)
            );
            startActivity(i);
        }

        @Override
        public boolean onLongClick(View v) {
            showFilterPopup(mBinding.imageView);
            return false;
        }

        private void showFilterPopup(View v) {
            PopupMenu popup = new PopupMenu(getContext(), v);
            popup.inflate(R.menu.list_item_book__context_menu);
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.list_item_book__context_menu__delete:
                            BookStorage bookStorage = BookStorage.instance(getContext());
                            bookStorage.remove(mUri);
                            mAdapter.removeItem(ResourceHolder.this.getPosition());
                            return true;
                        default:
                            return false;
                    }
                }
            });
            popup.show();
        }
    }

    private class ResourceAdapter extends RecyclerView.Adapter<ResourceHolder> {

        private List<IResource> mIResources;

        public ResourceAdapter(List<IResource> resourceList){
            mIResources = resourceList;
        }

        @NonNull
        @Override
        public ResourceHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            ListItemBookBinding binding = DataBindingUtil
                    .inflate(layoutInflater, R.layout.list_item_book,
                            viewGroup, false);
            return new ResourceHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ResourceHolder resourceHolder, int i) {
            resourceHolder.bind(mIResources.get(i));
        }

        public void removeItem(int position) {
            mIResources.remove(position);
            notifyItemRemoved(position);
        }

        public void addItem(IResource resource) {
            mIResources.add(resource);
            notifyItemInserted(getItemCount());
        }

        @Override
        public int getItemCount() {
            return mIResources.size();
        }

        public void setResources(List<IResource> resources){
            mIResources = resources;
        }
    }
}
