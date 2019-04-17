package com.bignerdranch.android.testpdfreader.viewmodal;

import android.app.Application;
import android.net.Uri;

import com.bignerdranch.android.testpdfreader.db.AppDatabase;
import com.bignerdranch.android.testpdfreader.db.entry.Resource;
import com.bignerdranch.android.testpdfreader.model.translator.Translator;
import com.bignerdranch.android.testpdfreader.model.translator.TranslatorFactory;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class TranslationManagerViewModel extends AndroidViewModel {

    private Translator mTranslator;
    private AppDatabase db;

    public TranslationManagerViewModel(@NonNull Application application) {
        super(application);
        db = AppDatabase.getDatabase(application);
        loadTranslator();
    }

    public Translator getTranslator(){
        return mTranslator;
    }

    public LiveData<Resource> getResource(Uri uri){
        return db.resourceDao().loadResource(uri);
    }

    private void loadTranslator(){
        TranslatorFactory translatorFactory = new TranslatorFactory();
        mTranslator = translatorFactory.newInstance();
    }

}
