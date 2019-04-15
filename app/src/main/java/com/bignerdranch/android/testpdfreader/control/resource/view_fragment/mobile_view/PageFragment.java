package com.bignerdranch.android.testpdfreader.control.resource.view_fragment.mobile_view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bignerdranch.android.testpdfreader.R;
import com.bignerdranch.android.testpdfreader.control.resource.text_selection.ITextSelectionListener;
import com.bignerdranch.android.testpdfreader.control.resource.text_selection.TextSelector;
import com.bignerdranch.android.testpdfreader.databinding.FragmentPdfMobilePageBinding;
import com.bignerdranch.android.testpdfreader.databinding.FragmentPdfMobilePageListItemBinding;
import com.bignerdranch.android.testpdfreader.model.components.SelectionAdaptedTextView;
import com.bignerdranch.android.testpdfreader.model.resource_loader.OnPageLoadedListener;
import com.bignerdranch.android.testpdfreader.model.text.TextManager;
import com.bignerdranch.android.testpdfreader.model.text.UnderLineSpan;
import com.bignerdranch.android.testpdfreader.model.tools.OnClickWithoutFocusFixer;
import com.bignerdranch.android.testpdfreader.model.tools.marker.Marker;
import com.bignerdranch.android.testpdfreader.model.tools.marker.MarkerFragmentResourceItemImpl;
import com.bignerdranch.android.testpdfreader.model.tools.marker.MarkerIntentListener;
import com.bignerdranch.android.testpdfreader.model.translator.OnParagraphTranslatedListener;
import com.bignerdranch.android.testpdfreader.view.PdfMobilePageFragmentViewModel;
import com.bignerdranch.android.testpdfreader.view.item.PdfMobilePageItemViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PageFragment extends Fragment implements OnPageLoadedListener {
    private static String TAG = "PageFragment";

    private static String ARG_CURRENT_ITEM = "com.bignerdranch.android.testpdfreader.control.resource.view_fragment.mobile_view.current_item";
    private static String ARG_CURRENT_PAGE = "com.bignerdranch.android.testpdfreader.control.resource.view_fragment.mobile_view.current_page";
    private static String ARG_CONTENT = "com.bignerdranch.android.testpdfreader.control.resource.view_fragment.mobile_view.content";

    private ITextSelectionListener mITextSelectionListener;

    private FragmentPdfMobilePageBinding mBinding;
    private PdfMobilePageFragmentViewModel mViewModel;
    private PageFragmentAdapter mAdapter;
    private ArrayList<PageFragmentItemWrapper> mContent = new ArrayList<>();
    private int mCurrentItem;
    private int mCurrentPage;

    private MarkerIntentListener mMarkerIntentListener;
    private TextSelector currentSelected;

    public static PageFragment newInstance(int currentPage, int currentItem) {
        Bundle arg = new Bundle();
        arg.putInt(ARG_CURRENT_PAGE, currentPage);
        arg.putInt(ARG_CURRENT_ITEM, currentItem);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(arg);
        return fragment;
    }

    public void setMarkerIntentListener(MarkerIntentListener listener) {
        mMarkerIntentListener = listener;
    }

    private void newMarkerIntent(Marker marker) {
        if (mMarkerIntentListener != null)
            mMarkerIntentListener.newMarkIntent(marker);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(ARG_CURRENT_ITEM, mCurrentItem);
        outState.putInt(ARG_CURRENT_PAGE, mCurrentPage);
        outState.putSerializable(ARG_CONTENT, mContent);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mCurrentItem = savedInstanceState.getInt(ARG_CURRENT_ITEM);
            mCurrentPage = savedInstanceState.getInt(ARG_CURRENT_PAGE);
            mContent = (ArrayList<PageFragmentItemWrapper>)
                    savedInstanceState.getSerializable(ARG_CONTENT);
            updateUI();
            closeAnimation();
        }
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        mITextSelectionListener = (ITextSelectionListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mITextSelectionListener = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_pdf_mobile_page,
                        container, false);
        mViewModel = new PdfMobilePageFragmentViewModel();
        mBinding.setViewModel(mViewModel);

        assert getArguments() != null;
        mCurrentItem = getArguments().getInt(ARG_CURRENT_ITEM);
        mCurrentPage = getArguments().getInt(ARG_CURRENT_PAGE);

        mBinding.fragmentPdfMobilePageRecyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity()));
        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void updateUI() {
        if (mAdapter == null) {
            mAdapter = new PageFragmentAdapter(mContent);
            mBinding.fragmentPdfMobilePageRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setContent(mContent);
            mAdapter.notifyDataSetChanged();
        }
    }

    private List<PageFragmentItemWrapper> wrap(List<String> list){
        List<PageFragmentItemWrapper> wrappers = new ArrayList<>();
        for (String s: list){
            wrappers.add(new PageFragmentItemWrapper(s));
        }
        return wrappers;
    }

    private void notifyTextSelected(String text, TextSelector listener) {
        mITextSelectionListener.wordSelected(text, listener);

        if (currentSelected != listener) removeCurrentSelection();

        currentSelected = listener;
    }

    public void removeCurrentSelection() {
        if (currentSelected != null)
            currentSelected.unSelect();
    }

    @Override
    public void onLoaded(List<String> content) {
        setContent(content);
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


    public void setContent(List<String> content) {
        mContent = new ArrayList<>();
        if (content != null) mContent.addAll(wrap(content));

        if(mCurrentItem!= -1){
            PageFragmentItemWrapper wrapper = mContent.get(mCurrentItem);
            if(wrapper!= null) wrapper.setMarker(true);
        }

        updateUI();

        Handler handler = new Handler(Looper.getMainLooper());

        handler.postDelayed(this::closeAnimation, 1000);
    }

    private void closeAnimation() {
        mViewModel.setAnimationVisibility(false);
    }

    //------------------------------------------------------------------------
    // ######################### Holder and Adapter ##########################
    //------------------------------------------------------------------------

    private class PageFragmentHolder extends RecyclerView.ViewHolder implements
            OnParagraphTranslatedListener {

        private FragmentPdfMobilePageListItemBinding mBinding;
        private MarkerFragmentResourceItemImpl mMarker;

        public PageFragmentHolder(FragmentPdfMobilePageListItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            mBinding.fragmentPdfMobilePageListItemContentText
                    .setOnClickListener(new OnWordClickedListener(new TextSelectorImpl()));
            mBinding.fragmentPdfMobilePageListItemContentText
                    .setOnTouchListener(new OnClickWithoutFocusFixer());

            mBinding.fragmentPdfMobilePageListItemContentText
                    .setOnSelectionChangListener(new OnSelectionChangListenerImpl());

            mBinding.fragmentPdfMobilePageListItemTranslateParagraphBtn.setOnClickListener(
                    new OnTranslateBtnClickListener()
            );
        }

        public void bind(PageFragmentItemWrapper content) {
            if (mMarker != null) {
                mMarker.setInvalid();
                mCurrentItem = mMarker.getPageElementIndex();
            }
            mBinding.getViewModel().setLabelVisibility(false);

            mMarker = new MarkerFragmentResourceItemImpl(mBinding, mCurrentPage,
                    getAdapterPosition(), content);

            if (content.isMarker()) {
                newMarkerIntent(mMarker);
            }

            mBinding.getViewModel().setContent(content.getContent());
        }

        @Override
        public void onParagraphTranslated(String paragraph) {
            mBinding.getViewModel().showTranslation(paragraph);
        }

        @Override
        public void translateError(Error error) {
            mBinding.getViewModel().showTranslation(
                    getResources().getString(R.string.translation_error)
                            + "\n" + error.toString());
        }

        private class OnSelectionChangListenerImpl
                implements SelectionAdaptedTextView.OnSelectionChangListener {
            @Override
            public void onSelectionChanged(int selStart, int selEnd) {
                if (selStart != selEnd) {
                    mITextSelectionListener.paragraphSelected(
                            mBinding.fragmentPdfMobilePageListItemContentText
                                    .getText().toString().substring(selStart, selEnd),
                            PageFragmentHolder.this);
                    mBinding.getViewModel().setTranslationLoadingAnimationVisibility(true);
                }
            }
        }

        private class OnTranslateBtnClickListener implements View.OnClickListener {
            @Override
            public void onClick(View v) {
                if (!mBinding.getViewModel().isTranslationVisible()) {
                    mBinding.getViewModel().setTranslationLoadingAnimationVisibility(true);
                    mITextSelectionListener.paragraphSelected(
                            mBinding.fragmentPdfMobilePageListItemContentText
                                    .getText().toString(), PageFragmentHolder.this);
                } else {
                    mBinding.getViewModel()
                            .setTranslationLoadingAnimationVisibility(false);
                }
                newMarkerIntent(mMarker);
            }
        }

        private class OnWordClickedListener implements View.OnClickListener {

            private TextSelectorImpl mSelector;

            public OnWordClickedListener(TextSelectorImpl selector) {
                mSelector = selector;
            }

            @Override
            public void onClick(View v) {
                TextView textView = (TextView) v;
                TextManager textManager = new TextManager(textView.getText().toString());
                int pos[] = textManager.getWordSelectionPositions(textView.getSelectionStart());
                String selectedWord = textManager.getWordSelection(textView.getSelectionStart());

                mSelector.set(pos[0], pos[1]);
                mSelector.select();

                newMarkerIntent(mMarker);
                notifyTextSelected(selectedWord, mSelector);
            }
        }


        private class TextSelectorImpl implements TextSelector {

            int mStart;
            int mEnd;

            public void set(int start, int end) {
                mStart = start;
                mEnd = end;
            }

            @Override
            public void unSelect() {
                mBinding.fragmentPdfMobilePageListItemContentText.setText(
                        mBinding.fragmentPdfMobilePageListItemContentText.getText().toString()
                );
            }

            @Override
            public void select() {
                Spannable spannable = SpannableString.valueOf(
                        mBinding.fragmentPdfMobilePageListItemContentText.getText().toString());
                spannable.setSpan(
                        new UnderLineSpan(R.color.selectedTextUnderline, R.color.selectedText),
                        mStart, mEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                mBinding.fragmentPdfMobilePageListItemContentText.setText(spannable);
            }
        }


    }

    private class PageFragmentAdapter extends RecyclerView.Adapter<PageFragmentHolder> {

        private List<PageFragmentItemWrapper> items;
        public int currentMarker = -1;

        public PageFragmentAdapter(List<PageFragmentItemWrapper> items) {
            this.items = items;
        }

        public void setContent(List<PageFragmentItemWrapper> items) {
            this.items = items;
        }

        @NonNull
        @Override
        public PageFragmentHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            FragmentPdfMobilePageListItemBinding binding =
                    DataBindingUtil.inflate(layoutInflater,
                            R.layout.fragment_pdf_mobile_page__list_item,
                            viewGroup, false);
            binding.setViewModel(new PdfMobilePageItemViewModel());

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
