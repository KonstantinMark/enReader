package com.bignerdranch.android.testpdfreader.model;

import com.bignerdranch.android.testpdfreader.control.content.PdfFullViewFragment;
import com.bignerdranch.android.testpdfreader.control.content.PdfJsViewFragment;
import com.bignerdranch.android.testpdfreader.control.content.PdfMobileViewFragment;
import com.bignerdranch.android.testpdfreader.control.content.TextSelectorFragment;

public class ViewFragmentFactory {

    public static TextSelectorFragment getFragment(ResourceDescriptor descriptor){
        switch (descriptor.getType()) {

            case (ResourceDescriptor.PDF_FULL_TYPE):
                return PdfFullViewFragment.newInstance(descriptor.getDescription());
            case (ResourceDescriptor.PDF_JS_TYPE):
                return PdfJsViewFragment.newInstance(descriptor.getDescription());
            case (ResourceDescriptor.PDF_MOBILE_TYPE):
                return PdfMobileViewFragment.newInstance(descriptor.getDescription());

            default:
                return PdfFullViewFragment.newInstance( descriptor.getDescription());
        }
    }

}
