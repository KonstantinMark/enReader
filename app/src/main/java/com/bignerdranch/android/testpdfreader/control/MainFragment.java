package com.bignerdranch.android.testpdfreader.control;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.android.testpdfreader.R;
import com.bignerdranch.android.testpdfreader.databinding.FragmentMainBinding;
import com.bignerdranch.android.testpdfreader.databinding.ListItemBookBinding;
import com.bignerdranch.android.testpdfreader.model.ResourceDescriptor;
import com.bignerdranch.android.testpdfreader.model.storage.BookStorage;
import com.bignerdranch.android.testpdfreader.model.storage.resource.IResource;
import com.bignerdranch.android.testpdfreader.view.ItemResourceViewModel;

import java.util.List;

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


        binding.itemsRecyclerView.setLayoutManager(
                new GridLayoutManager(getContext(), 2)
        );

        mBinding = binding;
        updateUI();

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI(){
        BookStorage storage = BookStorage.instance(getContext());
        List<IResource> resources = storage.getAll();

        if(mAdapter == null){
            mAdapter = new ResourceAdapter(resources);
            mBinding.itemsRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setResources(resources);
            notifyDataChanged();
        }
    }

    private void notifyDataChanged(){
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void resourceItemAdded() {
        notifyDataChanged();
    }

    private class ResourceHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
        }

        @Override
        public void onClick(View v) {
            Intent i = BookViewerActivity.newIntent(
                    getContext(),
                    new ResourceDescriptor(mUri.toString(), ResourceDescriptor.PDF_MOBILE_TYPE)
            );
            startActivity(i);
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

        @Override
        public int getItemCount() {
            return mIResources.size();
        }

        public void setResources(List<IResource> resources){
            mIResources = resources;
        }
    }
}
