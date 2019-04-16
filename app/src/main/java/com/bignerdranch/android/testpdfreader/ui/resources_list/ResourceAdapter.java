package com.bignerdranch.android.testpdfreader.ui.resources_list;

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

    public ResourceAdapter(){
       // setHasStableIds(true);
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

    @NonNull
    @Override
    public ResourceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemBookBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_book,
                        parent, false);
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


//    private ResourceHolder mCurrentInDeleteMod;
//    private Context mContext;

//    public ResourceAdapter() {
//        mResourceList = resourceList;
//        mContext = context;
//        binding.itemsRecyclerView.setOnTouchListener(new ResourceOnTouchListener());
//    }

//    @NonNull
//    @Override
//    public ResourceHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
//        ListItemBookBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.list_item_book,
//        viewGroup, false);
//        return new ResourceHolder(binding);
//    }
//
//    private class ResourceOnTouchListener implements View.OnTouchListener{
//
//        @SuppressLint("ClickableViewAccessibility")
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            if (event.getAction() == MotionEvent.ACTION_DOWN)
//                v.setOnTouchListener((v2, event2) -> {
//                    if (event.getAction() == MotionEvent.ACTION_UP) {
//                        changCurrentInDeleteMod(null);
//                    }
//                    return false;
//                });
//            return false;
//        }
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ResourceHolder resourceHolder, int i) {
//        resourceHolder.bind(mResourceList.get(i));
////        binding.itemsRecyclerView.setOnClickListener(v -> changCurrentInDeleteMod(null));
//    }
//
//    public void removeItem(int position) {
//        changCurrentInDeleteMod(null);
//        mResourceList.remove(position);
//        notifyItemRemoved(position);
//    }
//
//    @Override
//    public int getItemCount() {
//        return mResourceList.size();
//    }
//
//    public void setResourceList(List<Resource> resources){
//        mResourceList = resources;
//    }
//
//    public void addItem(Resource resource, int position) {
//        changCurrentInDeleteMod(null);
//        mResourceList.add(position, resource);
//        notifyItemInserted(position);
//    }
//
//    public void changCurrentInDeleteMod(ResourceHolder holder) {
//        if (mCurrentInDeleteMod != null && mCurrentInDeleteMod != holder)
//            mCurrentInDeleteMod.mBookItemTouchListener.setDefault();
//        mCurrentInDeleteMod = holder;
//    }
//
//    public void forgetMe(ResourceHolder holder) {
//        if (mCurrentInDeleteMod == holder) mCurrentInDeleteMod = null;
//    }
//
//    public List<Resource> getResources(){
//        return mResourceList;
//    }
//

    // --------------------------------------------------------------------------------------------------
    class ResourceHolder extends RecyclerView.ViewHolder {

        final ListItemBookBinding binding;

//        Resource mResource;
//        BookItemTouchListener mBookItemTouchListener;


        public ResourceHolder(ListItemBookBinding binding){
            super(binding.getRoot());
            this.binding = binding;
            //        mBookItemTouchListener = new BookItemTouchListener(this, ResourcesFragment.this.mAdapter, getContext());
//            setListeners();
        }
//
//        public void setListeners() {
//            binding.listItemBookForeground.setOnFocusChangeListener((v, hasFocus) -> {
//                Log.i(TAG, mResource.name + " focus: " + hasFocus);
//            });
//            binding.listItemBookForeground.setOnTouchListener(mBookItemTouchListener);
//            binding.listItemBookForeground.setOnClickListener(new OnResourceClickListener(
//                    mContext, mResource));
    //        binding.listItemBookBackground.setOnClickListener(new OnResourceDeleteListener(
    //                mContext, mAdapter, getAdapterPosition(), mResource, getView()));
//        }
//
//        public void bind(Resource resource){
//            ResourceItemViewModel model = new ResourceItemViewModel(mContext);
//            model.setResource(resource);
//            binding.setViewModel(model);
//            mResource = resource;
////            mBookItemTouchListener.refresh();
//        }

    }

}