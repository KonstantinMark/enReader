package com.bignerdranch.android.testpdfreader.model.tools.marker;

import com.bignerdranch.android.testpdfreader.databinding.FragmentPdfMobilePageListItemBinding;

public class MarkerFragmentResourceItemImpl implements Marker {

    public boolean valid = true;
    private FragmentPdfMobilePageListItemBinding mBinding;
    private int mPosition;
    private int mCurrentPage;

    public MarkerFragmentResourceItemImpl(
            FragmentPdfMobilePageListItemBinding binding,
            int currentPage, int position) {
        mBinding = binding;
        mPosition = position;
        mCurrentPage = currentPage;
    }

    public void setInvalid() {
        valid = false;
    }

    @Override
    public void setVisibility(boolean visibility) {
        if (valid)
            mBinding.getViewModel().setLabelVisibility(visibility);
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
