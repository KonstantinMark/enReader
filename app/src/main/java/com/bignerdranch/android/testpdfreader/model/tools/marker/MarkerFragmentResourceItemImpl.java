package com.bignerdranch.android.testpdfreader.model.tools.marker;

import com.bignerdranch.android.testpdfreader.databinding.FragmentMobilePageItemBinding;
import com.bignerdranch.android.testpdfreader.controller.resource.resource_view.service.impl.PageParagraphWrapper;

public class MarkerFragmentResourceItemImpl implements Marker {

    public boolean valid = true;
    private FragmentMobilePageItemBinding mBinding;
    private int mPosition;
    private int mCurrentPage;
    private PageParagraphWrapper mContent;

    public MarkerFragmentResourceItemImpl(
            FragmentMobilePageItemBinding binding,
            int currentPage, int position, PageParagraphWrapper content) {
        mBinding = binding;
        mPosition = position;
        mCurrentPage = currentPage;
        mContent = content;
    }

    public void setInvalid() {
        valid = false;
    }

    @Override
    public void setVisibility(boolean visibility) {
//        if (valid) {
//            mBinding.getViewModel().setLabelVisibility(visibility);
//        }
//        mContent.setCurrent(visibility);
    }

    @Override
    public int getPageIndex() {
        return mCurrentPage;
    }

    @Override
    public int getPageElementIndex() {
        return mPosition;
    }

}
