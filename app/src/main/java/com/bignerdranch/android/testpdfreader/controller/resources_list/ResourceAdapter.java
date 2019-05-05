package com.bignerdranch.android.testpdfreader.controller.resources_list;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bignerdranch.android.testpdfreader.R;
import com.bignerdranch.android.testpdfreader.databinding.ListItemBookBinding;
import com.bignerdranch.android.testpdfreader.db.entry.Resource;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class ResourceAdapter extends RecyclerView.Adapter<ResourceAdapter.ResourceHolder> {
    private static String TAG = "ResourceAdapter";

    @Nullable
    private List<? extends Resource> mResourceList;
    private ResourceClickCallback mRsourceCallback;

    public ResourceAdapter(ResourceClickCallback resourceCallback) {
        mRsourceCallback = resourceCallback;
    }

    public void setResourceList(final List<? extends Resource> resourceList) {
        if(mResourceList == null) {
            mResourceList = resourceList;
            notifyItemRangeInserted(0, resourceList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mResourceList.size();
                }

                @Override
                public int getNewListSize() {
                    return resourceList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mResourceList.get(oldItemPosition).uri.equals(
                            resourceList.get(newItemPosition).uri
                    );
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Resource newResource = resourceList.get(newItemPosition);
                    Resource olpResource = mResourceList.get(oldItemPosition);
                    return newResource.uri.equals(olpResource.uri)
                            && Objects.equals(newResource.name, olpResource.name)
                            && Objects.equals(newResource.type, olpResource.type);
                }
            });
            mResourceList = resourceList;
            result.dispatchUpdatesTo(this);
        }
    }

    @Nullable
    public List<? extends Resource> getResourceList() {
        return mResourceList;
    }

    @NonNull
    @Override
    public ResourceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemBookBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_book,
                        parent, false);
        binding.setCallback(mRsourceCallback);
        return new ResourceHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ResourceHolder holder, int position) {
        holder.binding.setResource(mResourceList.get(position));
        holder.binding.executePendingBindings();

    }

    @Override
    public int getItemCount() {
        return mResourceList == null ? 0 : mResourceList.size();
    }


    class ResourceHolder extends RecyclerView.ViewHolder {

        final ListItemBookBinding binding;


        public ResourceHolder(ListItemBookBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}