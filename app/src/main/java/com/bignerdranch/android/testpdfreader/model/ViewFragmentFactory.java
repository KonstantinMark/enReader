package com.bignerdranch.android.testpdfreader.model;

import com.bignerdranch.android.testpdfreader.control.resource.ResourceViewFragment;
import com.bignerdranch.android.testpdfreader.control.resource.view_fragment.MobileViewFragment;
import com.bignerdranch.android.testpdfreader.model.storage.resource.IResource;

public class ViewFragmentFactory {

    public static ResourceViewFragment getFragment(IResource resource) {
        return MobileViewFragment.config(resource.getUri(), new MobileViewFragment());
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
