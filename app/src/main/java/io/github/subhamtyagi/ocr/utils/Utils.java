package io.github.subhamtyagi.ocr.utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;

import com.googlecode.leptonica.android.AdaptiveMap;
import com.googlecode.leptonica.android.Binarize;
import com.googlecode.leptonica.android.Convert;
import com.googlecode.leptonica.android.Enhance;
import com.googlecode.leptonica.android.Pix;
import com.googlecode.leptonica.android.ReadFile;
import com.googlecode.leptonica.android.Rotate;
import com.googlecode.leptonica.android.Skew;
import com.googlecode.leptonica.android.WriteFile;

import java.util.Set;

public class Utils {

    private static final String DEFAULT_LANGUAGE = "eng";
    private static final Set<String> DEFAULT_MULTIPLE_LANGUAGE = Set.of("chi_sim", "eng");
    private static final String DEFAULT_ENGINE_MODE = "3";

    @SuppressLint("DefaultLocale")
    public static String getSize(int size) {
        String s = "";
        double kb = size / 1024;
        double mb = kb / 1024;
        if (size < 1024) {
            s = "$size Bytes";
        } else if (size < 1024 * 1024) {
            s = String.format("%.2f", kb) + " KB";
        } else if (size < 1024 * 1024 * 1024) {
            s = String.format("%.2f", mb) + " MB";
        }
        return s;
    }

    public static Bitmap preProcessBitmap(Bitmap bitmap) {
        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Pix pix = ReadFile.readBitmap(bitmap);
        pix = Convert.convertTo8(pix);

        if (SpUtil.getInstance().getBoolean(Constants.KEY_CONTRAST, true)) {
            // pix=AdaptiveMap.backgroundNormMorph(pix);
            pix = AdaptiveMap.pixContrastNorm(pix);
        }

        if (SpUtil.getInstance().getBoolean(Constants.KEY_UN_SHARP_MASKING, true))
            pix = Enhance.unsharpMasking(pix);

        if (SpUtil.getInstance().getBoolean(Constants.KEY_OTSU_THRESHOLD, true))
            pix = Binarize.otsuAdaptiveThreshold(pix);

        if (SpUtil.getInstance().getBoolean(Constants.KEY_FIND_SKEW_AND_DESKEW, true)) {
            float f = Skew.findSkew(pix);
            pix = Rotate.rotate(pix, f);
        }

        return WriteFile.writeBitmap(pix);
    }

    public static boolean isPreProcessImage() {
        return SpUtil.getInstance().getBoolean(Constants.KEY_GRAYSCALE_IMAGE_OCR, true);
    }

    public static boolean isPersistData() {
        return SpUtil.getInstance().getBoolean(Constants.KEY_PERSIST_DATA, true);
    }

    public static String getTesseractStringForMultipleLanguages(Set<String> langs) {
        if (langs == null || langs.size() == 0) return DEFAULT_LANGUAGE;
        StringBuilder rLanguage = new StringBuilder();
        for (String lang : langs) {
            rLanguage.append(lang);
            rLanguage.append("+");
        }
        return rLanguage.subSequence(0, rLanguage.toString().lastIndexOf('+')).toString();
    }

    public static String getTrainingDataType() {
        return SpUtil.getInstance().getString(Constants.KEY_TESS_TRAINING_DATA_SOURCE, "best");
    }

    public static String getTrainingDataLanguage() {
        if (SpUtil.getInstance().getBoolean(Constants.KEY_ENABLE_MULTI_LANG)) {
            return getTesseractStringForMultipleLanguages(SpUtil.getInstance().getStringSet(Constants.KEY_LANGUAGE_FOR_TESSERACT_MULTI, DEFAULT_MULTIPLE_LANGUAGE));
        } else {
            return SpUtil.getInstance().getString(Constants.KEY_LANGUAGE_FOR_TESSERACT, DEFAULT_LANGUAGE);
        }

    }

    public static String setTrainingDataLanguage(String language) {
        if (SpUtil.getInstance().getBoolean(Constants.KEY_ENABLE_MULTI_LANG)) {
            return getTesseractStringForMultipleLanguages(SpUtil.getInstance().getStringSet(Constants.KEY_LANGUAGE_FOR_TESSERACT_MULTI, null));
        } else {
            return SpUtil.getInstance().getString(Constants.KEY_LANGUAGE_FOR_TESSERACT, DEFAULT_LANGUAGE);
        }

    }

    public static int getPageSegMode() {
        return Integer.parseInt(SpUtil.getInstance().getString(Constants.KEY_PAGE_SEG_MODE, "1"));
    }

    public static int getEngineMode(){
        return Integer.parseInt(SpUtil.getInstance().getString(Constants.KEY_ENGINE_MODE, DEFAULT_ENGINE_MODE));
    }

    public static void putLastUsedText(String text) {
        SpUtil.getInstance().putString(Constants.KEY_LAST_USE_IMAGE_TEXT, text);
    }

    public static String getLastUsedText() {
        return SpUtil.getInstance().getString(Constants.KEY_LAST_USE_IMAGE_TEXT, "");
    }

    public static String[] getLast3UsedLanguage() {
        return new String[]{
                SpUtil.getInstance().getString(Constants.KEY_LAST_USED_LANGUAGE_1, "eng"),
                SpUtil.getInstance().getString(Constants.KEY_LAST_USED_LANGUAGE_2, "hin"),
                SpUtil.getInstance().getString(Constants.KEY_LAST_USED_LANGUAGE_3, "deu")
        };
    }

    public static void setLastUsedLanguage(String lastUsedLanguage) {
        String l1 = SpUtil.getInstance().getString(Constants.KEY_LAST_USED_LANGUAGE_1, "eng");
        if (lastUsedLanguage.contentEquals(l1)) {
            return;
        }
        String l2 = SpUtil.getInstance().getString(Constants.KEY_LAST_USED_LANGUAGE_2, "hin");
        if (l2.contentEquals(lastUsedLanguage)) {
            SpUtil.getInstance().putString(Constants.KEY_LAST_USED_LANGUAGE_2, l1);
            SpUtil.getInstance().putString(Constants.KEY_LAST_USED_LANGUAGE_1, lastUsedLanguage);
        } else {
            SpUtil.getInstance().putString(Constants.KEY_LAST_USED_LANGUAGE_3, l2);
            SpUtil.getInstance().putString(Constants.KEY_LAST_USED_LANGUAGE_2, l1);
            SpUtil.getInstance().putString(Constants.KEY_LAST_USED_LANGUAGE_1, lastUsedLanguage);
        }

    }

    public static void putLastUsedImageLocation(String imageURI) {
        SpUtil.getInstance().putString(Constants.KEY_LAST_USE_IMAGE_LOCATION, imageURI);
    }


}
