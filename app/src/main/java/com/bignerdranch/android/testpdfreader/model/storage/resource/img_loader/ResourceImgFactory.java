package com.bignerdranch.android.testpdfreader.model.storage.resource.img_loader;

import android.content.Context;

import com.bignerdranch.android.testpdfreader.model.storage.resource.img_loader.ResourceImgLoader;
import com.bignerdranch.android.testpdfreader.model.storage.resource.img_loader.impl.ResourceImgDefault;
import com.bignerdranch.android.testpdfreader.model.storage.resource.img_loader.impl.ResourceImgDocxLoader;
import com.bignerdranch.android.testpdfreader.model.storage.resource.img_loader.impl.ResourceImgPdf;
import com.bignerdranch.android.testpdfreader.model.storage.resource.img_loader.impl.ResourceImgTxtLoader;
import com.bignerdranch.android.testpdfreader.model.storage.resource.tool.ResourceType;

import java.util.HashMap;
import java.util.List;

public class ResourceImgFactory {

    private Context mContext;
    private HashMap<ResourceType, ResourceImgLoader> cash = new HashMap<>();

    public ResourceImgFactory(Context context) {
        mContext = context;
        cash.put(ResourceType.PDF, new ResourceImgPdf(mContext));
        cash.put(ResourceType.TXT, new ResourceImgTxtLoader(mContext));
        cash.put(ResourceType.DOCX, new ResourceImgDocxLoader(mContext));
        cash.put(ResourceType.UNDEFINE, new ResourceImgDefault(mContext));
    }

    public ResourceImgLoader getResourceImgLoader(ResourceType type) {
        ResourceImgLoader loader = cash.get(type);
        return loader == null ? cash.get(ResourceType.UNDEFINE) : loader;

    }

}
