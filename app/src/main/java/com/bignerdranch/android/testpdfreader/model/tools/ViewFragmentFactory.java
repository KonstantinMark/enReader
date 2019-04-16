package com.bignerdranch.android.testpdfreader.model.tools;

import com.bignerdranch.android.testpdfreader.ui.resource.ResourceViewFragment;
import com.bignerdranch.android.testpdfreader.ui.resource.view_fragment.MobileViewFragment;
import com.bignerdranch.android.testpdfreader.db.entry.Resource;

public class ViewFragmentFactory {

    public static ResourceViewFragment getFragment(Resource resource) {
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
