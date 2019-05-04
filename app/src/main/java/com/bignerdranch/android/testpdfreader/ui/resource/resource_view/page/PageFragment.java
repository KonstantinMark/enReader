package com.bignerdranch.android.testpdfreader.ui.resource.resource_view.page;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.android.testpdfreader.R;
import com.bignerdranch.android.testpdfreader.databinding.FragmentMobilePageBinding;
import com.bignerdranch.android.testpdfreader.ui.resource.resource_view.page.service.TextTranslationActionListener;
import com.bignerdranch.android.testpdfreader.model.resource_loader.OnPageLoadedListener;
import com.bignerdranch.android.testpdfreader.view.PdfMobilePageFragmentViewModel;
import com.bignerdranch.android.testpdfreader.viewmodal.ResourceViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

public class PageFragment extends Fragment implements OnPageLoadedListener {
    private static String TAG = "PageFragment";

    private static String ARG_URI = "com.bignerdranch.android.testpdfreader.ui.resource.view_fragment.mobile_view.uri";
    private static String ARG_PAGE_NUM = "com.bignerdranch.android.testpdfreader.ui.resource.view_fragment.mobile_view.page_num";
    private TextTranslationActionListener mITextSelectionListener;

    private FragmentMobilePageBinding mBinding;
    private PageAdapter mPageAdapter;
    private ResourceViewModel mResourceViewModel;

    private Uri resourceUri;
    private int pageNum;
//    private PdfMobilePageFragmentViewModel mViewModel;
//    private PageFragmentAdapter mAdapter;
//    private ArrayList<PageItemWrapper> mContent = new ArrayList<>();

//    private MarkerIntentListener mMarkerIntentListener;


    public static PageFragment newInstance(Uri resourceUri, int pageNum) {
        Bundle arg = new Bundle();
        arg.putString(ARG_URI, resourceUri.toString());
        arg.putInt(ARG_PAGE_NUM, pageNum);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(arg);
        return fragment;
    }

//    public void setMarkerIntentListener(MarkerIntentListener listener) {
//        mMarkerIntentListener = listener;
//    }

//    private void newMarkerIntent(Marker marker) {
//        if (mMarkerIntentListener != null)
//            mMarkerIntentListener.newMarkIntent(marker);
//    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_mobile_page,
                        container, false);
        mBinding.setViewModel(new PdfMobilePageFragmentViewModel());

        resourceUri = Uri.parse(getArguments().getString(ARG_URI));
        pageNum = getArguments().getInt(ARG_PAGE_NUM);

        mBinding.fragmentPdfMobilePageRecyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity()));

        mPageAdapter = new PageAdapter(mITextSelectionListener);
        mBinding.fragmentPdfMobilePageRecyclerView.setAdapter(mPageAdapter);

        mResourceViewModel = ViewModelProviders.of(this).get(ResourceViewModel.class);
        mResourceViewModel.load(resourceUri);

        return mBinding.getRoot();
    }

    @Override
    public void onLoaded(List<String> content) {
        setContent(content);
    }

    public void setContent(List<String> content) {
        Log.i(TAG, "setContent" + content.size());
        if(content != null){
            List<PageItemWrapper> list = wrap(content);
            mPageAdapter.setItemList(list);
            closeAnimation();

            startMarkerChangedListen(list);
//        if(mCurrentItem!= -1){
//            PageItemWrapper wrapper = mContent.get(mCurrentItem);
//            if(wrapper!= null) wrapper.setCurrent(true);
//        }

//        updateUI();

//        Handler handler = new Handler(Looper.getMainLooper());

//        handler.postDelayed(this::closeAnimation, 1000);
        }
    }
    private void closeAnimation() {
        mBinding.getViewModel().setAnimationVisibility(false);
    }

    private void startMarkerChangedListen(List<PageItemWrapper> list){
        mResourceViewModel.getMetaDate().observe(this, metaData -> {
            if(metaData != null ) {
                boolean needUpdate = false;
                if (metaData.currentPage == pageNum) {
                    for (int i = 0; i < list.size(); i++){
                        if(i == metaData.currentItem){
                            if(!list.get(i).isCurrent){
                                list.get(i).isCurrent = true;
                                needUpdate = true;
                            }
                        } else {
                            if(list.get(i).isCurrent){
                                list.get(i).isCurrent = false;
                                needUpdate = true;
                            }
                        }
                    }
                } else {
                    for (PageItemWrapper wrapper: list){
                        if(wrapper.isCurrent) {
                            wrapper.isCurrent = false;
                            needUpdate = true;
                        }
                    }
                }
                if(needUpdate) mPageAdapter.setItemList(list);
            }
        });
    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//    }
//
//    private void updateUI() {
//        if (mAdapter == null) {
//            mAdapter = new PageFragmentAdapter(mContent);
//            mBinding.fragmentPdfMobilePageRecyclerView.setAdapter(mAdapter);
//        } else {
//            mAdapter.setContent(mContent);
//            mAdapter.notifyDataSetChanged();
//        }
//    }

    private List<PageItemWrapper> wrap(List<String> list){
        List<PageItemWrapper> wrappers = new ArrayList<>();

        int counter = 0;
        for (String s: list){
            PageItemWrapper wrapper = new PageItemWrapper();
            wrapper.id = counter++;
            wrapper.content = s;
            wrappers.add(wrapper);
        }
        return wrappers;
    }

    private boolean isDataNeeded = true;
    @Override
    public boolean dataStillNeeded() {
        return isDataNeeded;
    }

    @Override
    public void onPause() {
        isDataNeeded = false;
        super.onPause();
    }

    //------------------------------------------------------------------------
    // ######################### Holder and Adapter ##########################
    //------------------------------------------------------------------------

//    private class PageFragmentHolder extends RecyclerView.ViewHolder implements
//            TranslationListener {
//
//        private FragmentPdfMobilePageListItemBinding mBinding;
//        private MarkerFragmentResourceItemImpl mMarker;
//
//        public PageFragmentHolder(FragmentPdfMobilePageListItemBinding binding) {
//            super(binding.getRoot());
//            mBinding = binding;
//            mBinding.fragmentPdfMobilePageListItemContentText
//                    .setOnClickListener(new OnClickedWordSelector(new TextSelectorImpl()));
//            mBinding.fragmentPdfMobilePageListItemContentText
//                    .setOnTouchListener(new OnClickWithoutFocusFixer());
//
//            mBinding.fragmentPdfMobilePageListItemContentText
//                    .setOnSelectionChangListener(new OnParagraphSelectionChangedListener());
//
//            mBinding.fragmentPdfMobilePageListItemTranslateParagraphBtn.setOnClickListener(
//                    new OnTranslateBtnClickListener()
//            );
//        }
//
//        public void bind(PageItemWrapper content) {
//            if (mMarker != null) {
//                mMarker.setInvalid();
//                mCurrentItem = mMarker.getPageElementIndex();
//            }
//            mBinding.getViewModel().setLabelVisibility(false);
//
//            mMarker = new MarkerFragmentResourceItemImpl(mBinding, mCurrentPage,
//                    getAdapterPosition(), content);
//
//            if (content.isCurrent()) {
//                newMarkerIntent(mMarker);
//            }
//
//            mBinding.getViewModel().setContent(content.getContent());
//        }
//
//        @Override
//        public void onTranslated(String paragraph) {
//            mBinding.getViewModel().showTranslation(paragraph);
//        }
//
//        @Override
//        public void translateError(Error error) {
//            mBinding.getViewModel().showTranslation(
//                    getResources().getString(R.string.translation_error)
//                            + "\n" + error.toString());
//        }
//
//        private class OnParagraphSelectionChangedListener
//                implements SelectionAdaptedTextView.OnSelectionChangListener {
//            @Override
//            public void onSelectionChanged(int selStart, int selEnd) {
//                if (selStart != selEnd) {
//                    mITextSelectionListener.paragraphSelected(
//                            mBinding.fragmentPdfMobilePageListItemContentText
//                                    .getText().toString().substring(selStart, selEnd),
//                            PageFragmentHolder.this);
//                    mBinding.getViewModel().setTranslationLoadingAnimationVisibility(true);
//                }
//            }
//        }
//
//        private class OnTranslateBtnClickListener implements View.OnClickListener {
//            @Override
//            public void onClick(View v) {
//                if (!mBinding.getViewModel().isTranslationVisible()) {
//                    mBinding.getViewModel().setTranslationLoadingAnimationVisibility(true);
//                    mITextSelectionListener.paragraphSelected(
//                            mBinding.fragmentPdfMobilePageListItemContentText
//                                    .getText().toString(), PageFragmentHolder.this);
//                } else {
//                    mBinding.getViewModel()
//                            .setTranslationLoadingAnimationVisibility(false);
//                }
//                newMarkerIntent(mMarker);
//            }
//        }
//
//        private class OnClickedWordSelector implements View.OnClickListener {
//
//            private TextSelectorImpl mSelector;
//
//            public OnClickedWordSelector(TextSelectorImpl selector) {
//                mSelector = selector;
//            }
//
//            @Override
//            public void onClick(View v) {
//                TextView textView = (TextView) v;
//                TextManager textManager = new TextManager(textView.getText().toString());
//                int pos[] = textManager.getWordSelectionPositions(textView.getSelectionStart());
//                String selectedWord = textManager.getWordSelection(textView.getSelectionStart());
//
//                mSelector.set(pos[0], pos[1]);
//                mSelector.select();
//
//                newMarkerIntent(mMarker);
//                notifyTextSelected(selectedWord, mSelector);
//            }
//        }
//
//
//        private class TextSelectorImpl implements TextSelector {
//
//            int mStart;
//            int mEnd;
//
//            public void set(int start, int end) {
//                mStart = start;
//                mEnd = end;
//            }
//
//            @Override
//            public void unSelect() {
//                mBinding.fragmentPdfMobilePageListItemContentText.setText(
//                        mBinding.fragmentPdfMobilePageListItemContentText.getText().toString()
//                );
//            }
//
//            @Override
//            public void select() {
//                Spannable spannable = SpannableString.valueOf(
//                        mBinding.fragmentPdfMobilePageListItemContentText.getText().toString());
//                spannable.setSpan(
//                        new UnderLineSpan(R.color.selectedTextUnderline, R.color.selectedText),
//                        mStart, mEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                mBinding.fragmentPdfMobilePageListItemContentText.setText(spannable);
//            }
//        }
//    }
//
//    private class PageFragmentAdapter extends RecyclerView.Adapter<PageFragmentHolder> {
//
//        private List<PageItemWrapper> items;
//        public int currentMarker = -1;
//
//        public PageFragmentAdapter(List<PageItemWrapper> items) {
//            this.items = items;
//        }
//
//        public void setContent(List<PageItemWrapper> items) {
//            this.items = items;
//        }
//
//        @NonNull
//        @Override
//        public PageFragmentHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
//            FragmentPdfMobilePageListItemBinding binding =
//                    DataBindingUtil.inflate(layoutInflater,
//                            R.layout.fragment_mobile_page__item,
//                            viewGroup, false);
//            binding.setViewModel(new PdfMobilePageItemViewModel());
//
//            return new PageFragmentHolder(binding);
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull PageFragmentHolder pageFragmentHolder, int i) {
//            pageFragmentHolder.bind(items.get(i));
//        }
//
//        @Override
//        public int getItemCount() {
//            return items.size();
//        }
//    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(ARG_URI, resourceUri.toString());
        outState.putInt(ARG_PAGE_NUM, pageNum);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null){
            resourceUri = Uri.parse(savedInstanceState.getString(ARG_URI));
            pageNum = savedInstanceState.getInt(ARG_PAGE_NUM);
        }
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        mITextSelectionListener = (TextTranslationActionListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mITextSelectionListener = null;
    }

}
