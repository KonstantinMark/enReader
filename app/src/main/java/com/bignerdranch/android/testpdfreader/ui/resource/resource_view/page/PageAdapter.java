package com.bignerdranch.android.testpdfreader.ui.resource.resource_view.page;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bignerdranch.android.testpdfreader.R;
import com.bignerdranch.android.testpdfreader.databinding.FragmentMobilePageItemBinding;
import com.bignerdranch.android.testpdfreader.model.tools.OnClickWithoutFocusFixer;
import com.bignerdranch.android.testpdfreader.ui.resource.resource_view.page.service.TextTranslationActionListener;
import com.bignerdranch.android.testpdfreader.ui.resource.resource_view.page.service.impl.OnClickedWordSelector;
import com.bignerdranch.android.testpdfreader.ui.resource.resource_view.page.service.impl.OnParagraphSelectedListenerImpl;
import com.bignerdranch.android.testpdfreader.ui.resource.resource_view.page.service.impl.OnParagraphSelectionChangedListener;
import com.bignerdranch.android.testpdfreader.ui.resource.resource_view.page.service.impl.OnTranslateBtnClickListener;
import com.bignerdranch.android.testpdfreader.ui.resource.resource_view.page.service.impl.OnClickWordListenerImpl;
import com.bignerdranch.android.testpdfreader.view.item.PdfMobilePageItemViewModel;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class PageAdapter extends RecyclerView.Adapter<PageAdapter.PageHolder> {

    private List<? extends PageItemWrapper> mItemList;
    private TextTranslationActionListener mTextTranslationActionListener;

    public PageAdapter(TextTranslationActionListener textTranslationActionListener) {
        mTextTranslationActionListener = textTranslationActionListener;
    }

    public void setItemList(List<? extends PageItemWrapper> itemList) {
        if(mItemList == null){
            mItemList = itemList;
            notifyItemRangeChanged(0, itemList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mItemList.size();
                }

                @Override
                public int getNewListSize() {
                    return itemList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mItemList.get(oldItemPosition).id ==
                            itemList.get(newItemPosition).id;
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    PageItemWrapper oldItem = mItemList.get(oldItemPosition);
                    PageItemWrapper newItem = itemList.get(newItemPosition);
                    return oldItem.id == newItem.id
                            && oldItem.isCurrent == newItem.isCurrent
                            && Objects.equals(oldItem.content, newItem.content);
                }
            });
            mItemList = itemList;
            result.dispatchUpdatesTo(this);
        }
    }

    public List<? extends PageItemWrapper> getItemList() {
        return mItemList;
    }


    @NonNull
    @Override
    public PageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FragmentMobilePageItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.fragment_mobile_page__item,
                        parent, false);
        binding.setViewModel(new PdfMobilePageItemViewModel());
        return new PageHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PageHolder holder, int position) {
        holder.binding.getViewModel().setContent(mItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }



    public class PageHolder extends RecyclerView.ViewHolder {
        FragmentMobilePageItemBinding binding;

        public PageHolder(FragmentMobilePageItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            setListeners();
        }

        @SuppressLint("ClickableViewAccessibility")
        public void setListeners(){
            binding.fragmentPdfMobilePageListItemContentText.setOnClickListener(
                    new OnClickedWordSelector(
                            new OnClickWordListenerImpl(
                                new TextSelectorImpl(binding.fragmentPdfMobilePageListItemContentText),
                                    mTextTranslationActionListener)
                    ));
            binding.fragmentPdfMobilePageListItemContentText.setOnTouchListener(new OnClickWithoutFocusFixer());

            binding.fragmentPdfMobilePageListItemContentText.setOnSelectionChangListener(
                    new OnParagraphSelectionChangedListener(
                            binding.fragmentPdfMobilePageListItemContentText,
                            new OnParagraphSelectedListenerImpl(
                                    mTextTranslationActionListener,
                                    binding.getViewModel(),
                                    binding.getViewModel()
                            )));

            binding.fragmentPdfMobilePageListItemTranslateParagraphBtn.setOnClickListener(
                    new OnTranslateBtnClickListener(binding.getViewModel(), mTextTranslationActionListener)
            );
        }
    }


}
