package com.bignerdranch.android.testpdfreader.control.content.pdf_mobile_view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bignerdranch.android.testpdfreader.R;
import com.bignerdranch.android.testpdfreader.control.content.TextSelectorFragment;
import com.bignerdranch.android.testpdfreader.control.content.TranslationReceiver;
import com.bignerdranch.android.testpdfreader.databinding.FragmentPdfMobilePageBinding;
import com.bignerdranch.android.testpdfreader.databinding.FragmentPdfMobilePageListItemBinding;
import com.bignerdranch.android.testpdfreader.view.item.PdfMobilePageFragmentViewModel;
import com.bignerdranch.android.testpdfreader.view.item.PdfMobilePageItem;

import java.util.ArrayList;
import java.util.List;

public class PageFragment extends TextSelectorFragment {
     private String TAG = "PageFragment";

    private PdfMobilePageFragmentViewModel mViewModel;

    private FragmentPdfMobilePageBinding mBinding;
    private PageFragmentAdapter mAdapter;
    private List<String> mContent;

    public static PageFragment newInstance(){
        PageFragment fragment = new PageFragment();
        return fragment;
    }

    public PageFragment() {
        mViewModel = new PdfMobilePageFragmentViewModel();
        mContent = new ArrayList<>();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_pdf_mobile_page,
                        container, false);

        mBinding.fragmentPdfMobilePageRecyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity()));

        mBinding.setViewModel(mViewModel);

        updateUI();

        return mBinding.getRoot();
    }

    private void updateUI() {
        if (mAdapter == null) {
            mAdapter = new PageFragmentAdapter(mContent);
            mBinding.fragmentPdfMobilePageRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setContent(mContent);
            //mAdapter.notifyDataSetChanged();
        }
    }

    public void setContent(List<String> content) {
        mViewModel.setContentReceived(true);
        mContent = content;
        updateUI();
    }

    private class PageFragmentHolder extends RecyclerView.ViewHolder implements View.OnClickListener, TranslationReceiver {
        private FragmentPdfMobilePageListItemBinding mBinding;

        public PageFragmentHolder(FragmentPdfMobilePageListItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            mBinding.fragmentPdfMobilePageListItemContentText.setOnClickListener(this);
            mBinding.fragmentPdfMobilePageListItemTranslateParagraphBtn.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!mBinding.getViewModel().isTranslationVisible()) {
                                mBinding.getViewModel().setTranslationLoadingAnimationVisibility(true);
                                notifyParagraphSelected(
                                        mBinding.fragmentPdfMobilePageListItemContentText
                                                .getText().toString(), PageFragmentHolder.this);
                            } else {
                                mBinding.getViewModel()
                                        .setTranslationLoadingAnimationVisibility(false);
                            }
                        }
                    }
            );

        }

        public void bind(String content) {
            mBinding.getViewModel().setContent(content);
        }

        @Override
        public void onClick(View v) {
            TextView textView = (TextView) v;
            TextManager textManager = new TextManager(textView.getText().toString());
            notifyTextSelected(textManager.getWordSelection(textView.getSelectionStart()));
        }

        @Override
        public void receiveTranslation(String string) {
            mBinding.getViewModel().showTranslation(string);
        }
    }

    private class PageFragmentAdapter extends RecyclerView.Adapter<PageFragmentHolder> {

        private List<String> items;

        public PageFragmentAdapter(List<String> items) {
            this.items = items;
        }

        public void setContent(List<String> items) {
            this.items = items;
        }

        @NonNull
        @Override
        public PageFragmentHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            FragmentPdfMobilePageListItemBinding binding =
                    DataBindingUtil.inflate(
                            layoutInflater,
                            R.layout.fragment_pdf_mobile_page__list_item,
                            viewGroup, false);
            binding.setViewModel(new PdfMobilePageItem());
            return new PageFragmentHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull PageFragmentHolder pageFragmentHolder, int i) {
            pageFragmentHolder.bind(items.get(i));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }
}
