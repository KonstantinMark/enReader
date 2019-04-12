package com.bignerdranch.android.testpdfreader.control.main_fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.android.testpdfreader.R;
import com.bignerdranch.android.testpdfreader.control.BookViewerActivity;
import com.bignerdranch.android.testpdfreader.control.MainActivity;
import com.bignerdranch.android.testpdfreader.databinding.FragmentMainBinding;
import com.bignerdranch.android.testpdfreader.databinding.ListItemBookBinding;
import com.bignerdranch.android.testpdfreader.model.ResourceDescriptor;
import com.bignerdranch.android.testpdfreader.model.storage.BookStorage;
import com.bignerdranch.android.testpdfreader.model.storage.resource.IResource;
import com.bignerdranch.android.testpdfreader.view.FragmentMainViewModel;
import com.bignerdranch.android.testpdfreader.view.ItemResourceViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainFragment extends Fragment implements MainActivity.ResourceItemAddedListener {
    private String TAG = "MainFragment";
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
                new GridLayoutManager(getContext(), 1)
        );


        mBinding.itemsRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mBinding.itemsRecyclerView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return false;
                    }
                });
                return false;
            }
        });

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

    public class ResourceHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ListItemBookBinding mBinding;
        Uri mUri;
        BookItemTouchListener mBookItemTouchListener;

        public ResourceHolder(ListItemBookBinding binding){
            super(binding.getRoot());
            mBinding = binding;
            mBookItemTouchListener = new BookItemTouchListener(this, MainFragment.this.mAdapter, getContext());
            mBinding.listItemBookForeground.setOnTouchListener(mBookItemTouchListener);
            mBinding.listItemBookForeground.setOnClickListener(this);
            mBinding.listItemBookBackground.setOnClickListener(new OnDeleteListener());
        }

        public void bind(IResource resource){
            mBinding.setViewModel(new ItemResourceViewModel(getContext()));
            mBinding.getViewModel().setResource(resource);
            mUri = resource.getUri();

            mBookItemTouchListener.refresh();

        }
        @Override
        public void onClick(View v) {
            mAdapter.changCurrentInDeleteMod(null);
            Intent i = BookViewerActivity.newIntent(
                    getContext(),
                    new ResourceDescriptor(mUri.toString(),
                            ResourceDescriptor.PDF_MOBILE_TYPE)
            );
            startActivity(i);
        }

        public class OnDeleteListener implements View.OnClickListener {
            @Override
            public void onClick(View v) {
                mBookItemTouchListener.setDefault();

                mAdapter.removeItem(ResourceHolder.this.getPosition());
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        final BookStorage bookStorage = BookStorage.instance(getContext());
                        final IResource resource = bookStorage.get(mUri);
                        bookStorage.remove(mUri);
                        Snackbar.make(getView(), "UNDO?", Snackbar.LENGTH_LONG)
                                .setAction("YES", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Runnable runnable1 = new Runnable() {
                                            @Override
                                            public void run() {
                                                bookStorage.addPdfUri(mUri);
                                            }
                                        };
                                        AsyncTask.execute(runnable1);

                                        Handler handler = new Handler(Looper.getMainLooper());
                                        Runnable runnable = new Runnable() {
                                            @Override
                                            public void run() {
                                                mAdapter.addItem(resource);
                                            }
                                        };
                                        handler.postDelayed(runnable, 0);
                                    }

                                }).show();

                    }
                };
                AsyncTask.execute(runnable);
            }
        }

    }

    public class ResourceAdapter extends RecyclerView.Adapter<ResourceHolder> {
        private List<IResource> mIResources;

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

        public ResourceAdapter(List<IResource> resourceList) {
            mIResources = resourceList;
            mBinding.itemsRecyclerView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN)
                        changCurrentInDeleteMod(null);
                    return false;
                }
            });
        }

        @Override
        public void onBindViewHolder(@NonNull ResourceHolder resourceHolder, int i) {
            resourceHolder.bind(mIResources.get(i));
            mBinding.itemsRecyclerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changCurrentInDeleteMod(null);
                }
            });
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

        public void setResources(List<IResource> resources){
            mIResources = resources;
        }

        public void addItem(IResource resource) {
            changCurrentInDeleteMod(null);
            mIResources.add(resource);
            notifyItemInserted(mIResources.size());
        }

        public void changCurrentInDeleteMod(ResourceHolder holder) {
            if (mCurrentInDeleteMod != null && mCurrentInDeleteMod != holder)
                mCurrentInDeleteMod.mBookItemTouchListener.setDefault();
            mCurrentInDeleteMod = holder;
        }

        public void forgetMe(ResourceHolder holder) {
            if (mCurrentInDeleteMod == holder) mCurrentInDeleteMod = null;
        }
    }
}
