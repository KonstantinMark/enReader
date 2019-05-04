package com.bignerdranch.android.testpdfreader.model.translator.YandexTranslator;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bignerdranch.android.testpdfreader.model.errors.translateerrors.ServerError;
import com.bignerdranch.android.testpdfreader.model.translator.TranslationListener;
import com.bignerdranch.android.testpdfreader.model.translator.Translator;
import com.bignerdranch.android.testpdfreader.model.translator.WordTranslateListener;
import com.bignerdranch.android.testpdfreader.model.word.Translation;
import com.bignerdranch.android.testpdfreader.model.word.Word;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class YandexTranslator implements Translator {
    private static final String TAG = "YandexTranslator";

    private static final String DICTIONARY_KEY = "dict.1.1.20181003T024204Z.c651e0466d30bdd2.5bfb8d5409aacb33b7507b27cf2f35fac4df482f";
    private static final String TRANSLATE_KEY = "trnsl.1.1.20181002T155013Z.40c6bf588b17f805.b643a190d0cb17dd67918799decbdafd65274a54";
    private static final String DICTIONARY_HOST = "https://dictionary.yandex.net/api/v1/dicservice.json/lookup";
    private static final String TRANSLATE_HOST = "https://translate.yandex.net/api/v1.5/tr.json/translate";
    private static final String LANG_EN = "en";
    private static final String LANG_RU = "ru";

    private static String dictionaryRequest(String word) {
        return DICTIONARY_HOST
                + "?key=" + DICTIONARY_KEY
                + "&lang=" + LANG_EN + "-" + LANG_RU
                + "&text=" + word;
    }

    private static String tarnslateRequest(String phrase) {
        return TRANSLATE_HOST
                + "?key=" + TRANSLATE_KEY
                + "&lang=" + LANG_EN + "-" + LANG_RU
                + "&text=" + phrase;
    }

    @Override
    public void translateWord(final String word, final WordTranslateListener listener, final Context context) {
        String url = dictionaryRequest(word);
        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(context).getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, response.toString());
                        Word wt = YandexJSONParser.get(response);

                        if (wt.getWord() == null) {
                            translatePhrase(
                                    word,
                                    new WordTranslatedListenerWrapperOn(listener, word),
                                    context);
                            return;
                        }
                        listener.translateIsDone(wt);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.translateError(new ServerError(error.toString()));
                    }
                }
        );

        queue.add(jsonObjectRequest);

    }

    @Override
    public void translatePhrase(final String paragraph, final TranslationListener listener, final Context context) {
        String temp = paragraph.replaceAll(" ", "%20");
        String url = tarnslateRequest(temp);
        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(context).getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, response.toString());

                        String translation = null;
                        try {
                            JSONArray def = response.getJSONArray("text");
                            translation = (String) def.get(0);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        listener.onTranslated(translation);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.translateError(new ServerError(error.toString()));
                    }
                }
        );

        queue.add(jsonObjectRequest);
    }

    private class WordTranslatedListenerWrapperOn implements TranslationListener {

        private WordTranslateListener mListener;
        private String mWord;

        public WordTranslatedListenerWrapperOn(WordTranslateListener listener, String word) {
            mListener = listener;
            mWord = word;
        }

        @Override
        public void onTranslated(String paragraph) {
            Word word = new Word();
            word.setWord(mWord);

            Translation translation = new Translation();
            translation.addTranslation(paragraph);
            word.addTranslation(translation);

            mListener.translateIsDone(word);
        }

        @Override
        public void translateError(Error error) {
            mListener.translateError(error);
        }
    }

}
