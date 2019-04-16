package com.bignerdranch.android.testpdfreader.model.tools.marker;

import com.bignerdranch.android.testpdfreader.ui.resource.view_fragment.mobile_view.PageFragmentItemWrapper;
import com.bignerdranch.android.testpdfreader.databinding.FragmentPdfMobilePageListItemBinding;

public class MarkerFragmentResourceItemImpl implements Marker {

    public boolean valid = true;
    private FragmentPdfMobilePageListItemBinding mBinding;
    private int mPosition;
    private int mCurrentPage;
    private PageFragmentItemWrapper mContent;

    public MarkerFragmentResourceItemImpl(
            FragmentPdfMobilePageListItemBinding binding,
            int currentPage, int position, PageFragmentItemWrapper content) {
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
        if (valid) {
            mBinding.getViewModel().setLabelVisibility(visibility);
        }
        mContent.setMarker(visibility);
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
