package com.bignerdranch.android.testpdfreader.trash;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.android.testpdfreader.R;
import com.bignerdranch.android.testpdfreader.databinding.FragmentParagraphListBinding;
import com.google.android.material.snackbar.Snackbar;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ParagraphListFragment extends Fragment {
    private static final String TAG = "ParagraphListFragment";
    private static final int OPEN_PDF_REQUEST_CODE = 1;

    //private ParagraphViewModel mSelected;
    //private UnSelectionListener mUnSelectionListener;

    private Snackbar mSnackbar;

    public static ParagraphListFragment newInstance(){
        return new ParagraphListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        FragmentParagraphListBinding binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_paragraph_list,
                        container, false);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        binding.recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                //notifyUnselectedListeners();
            }
        });

        //mSelected = null;

        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == OPEN_PDF_REQUEST_CODE){
            if(data != null){
                Uri uriPdf = data.getData();
                getActivity().grantUriPermission(
                        Objects.requireNonNull(getActivity()).getPackageName(),
                        uriPdf,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                );

            }
        }
    }

    private String readFile(){
        Log.i(TAG, "readFile");
        String fileNamePdf = "Tools of Titans.pdf";
        try (InputStream isPdf = Objects.requireNonNull(getActivity()).getAssets().open(fileNamePdf)) {
            PdfReader reader = new PdfReader(isPdf);

            PdfReaderContentParser parser = new PdfReaderContentParser(reader);

            StringBuilder sb = new StringBuilder();
            TextExtractionStrategy strategy;
            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                strategy = parser.processContent(i, new SimpleTextExtractionStrategy());
                sb.append('\n');
                sb.append(strategy.getResultantText());
            }

            Log.i(TAG, sb.toString());

            reader.close();

        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }

        return null;
    }
}
