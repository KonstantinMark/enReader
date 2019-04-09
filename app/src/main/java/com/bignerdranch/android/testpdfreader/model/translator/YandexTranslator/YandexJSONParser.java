package com.bignerdranch.android.testpdfreader.model.translator.YandexTranslator;

import com.bignerdranch.android.testpdfreader.model.word.PartOfSpeech;
import com.bignerdranch.android.testpdfreader.model.word.Translation;
import com.bignerdranch.android.testpdfreader.model.word.Word;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class YandexJSONParser {
    private static final String TAG = "YandexJSONParser";


    public static Word get(JSONObject jso) {

        Word wt = new Word();

        try {
            JSONArray def = jso.getJSONArray("def");

            for (int i = 0; i < def.length(); i++) {

                JSONObject defAttr = def.getJSONObject(i);

                if (wt.getTranscription() == null) {
                    wt.setTranscription(defAttr.optString("ts"));
                }

                if (wt.getWord() == null) {
                    wt.setWord(defAttr.optString("text"));
                }

                String posStr = defAttr.optString("pos");
                PartOfSpeech pos = PartOfSpeech.definePartOfSpeech(posStr);

                JSONArray tr = defAttr.getJSONArray("tr");
                for (int j = 0; j < tr.length(); j++) {
                    JSONObject trAttr = tr.getJSONObject(j);

                    Translation translation = new Translation();

                    translation.addTranslation(trAttr.optString("text"));
                    translation.setPartOfSpeech(pos);

                    wt.addTranslation(translation);

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return wt;
    }
}
