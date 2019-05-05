package com.bignerdranch.android.testpdfreader.model.tools;

import android.net.Uri;

import com.bignerdranch.android.testpdfreader.controller.resource.ResourceViewFragment;
import com.bignerdranch.android.testpdfreader.controller.resource.resource_view.MobileViewFragment;

public class ViewFragmentFactory {

    public static ResourceViewFragment getFragment(Uri uri) {
        return MobileViewFragment.config(uri, new MobileViewFragment());
//        switch (descriptor.getType()) {
//
//            case (ResourceDescriptor.PDF_FULL_TYPE):
//                return PdfFullViewFragment.config(descriptor.getDescription());
//            case (ResourceDescriptor.PDF_JS_TYPE):
//                return PdfJsViewFragment.config(descriptor.getDescription());
//            case (ResourceDescriptor.PDF_MOBILE_TYPE):
//                return MobileViewFragment.config(descriptor.getDescription());
//
//            default:
//                return PdfFullViewFragment.config( descriptor.getDescription());
//        }
    }

}
