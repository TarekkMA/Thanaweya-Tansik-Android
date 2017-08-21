package com.tmaprojects.tansik.utils;

import com.tmaprojects.tansik.model.FilterItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by tarekkma on 8/21/17.
 */

public class TansikFilter {

    public static String[][] specifiersIN_3lme;
    public static String[][] specifiersIN_adbe;
    public static String[][] specifiersOUT_3lme;
    public static String[][] specifiersOUT_adbe;

    public static List<FilterItem> filterItems = new ArrayList<>();

    static {
        TansikFilter.specifiersIN_3lme = new String[][] { { "" }, { "طب " }, { "هندسة " }, { "اسنان " }, { "صيدلة" }, { "علاج", "طبيعي" }, { "بيطري" }, { "سياسية", "علوم" }, { "علوم " }, { "تمريض" }, { "ألسن " }, { "فنون" }, { "حاسبات", "معلومات" }, { "اثار" }, { "اعلام" }, { "تربية" }, { "تجارة" }, { "زراعه" }, { "نوعية" }, { "اداب" }, { "سياحة" }, { "حقوق" }, { "اقتصاد", "منزلي" }, { "خدمة", "اجتماعية" }, { "عمالية" }, { "فنية" } };
        TansikFilter.specifiersOUT_3lme = new String[][] { { "" }, { "أسنان", "بيطري" }, { "" }, { "" }, { "" }, { "" }, { "" }, { "" }, { "معهد", "سياسية", "حاسب", "اكادمية", "دار", "اداب", "رياضة" }, { "" }, { "" }, { "" }, { "" }, { "" }, { "" }, { "" }, { "" }, { "" }, { "" }, { "" }, { "" }, { "" }, { "" }, { "" }, { "" }, { "" } };
        TansikFilter.specifiersIN_adbe = new String[][] { { "" }, { "علوم", "سياسية" }, { "اعلام" }, { "اثار" }, { "تربية" }, { "فنون" }, { "اداب" }, { "نوعية" }, { "تجارة" }, { "رياض", "اطفال" }, { "دار ", "علوم" }, { "حقوق" }, { "اقتصاد", "منزلي" }, { "سياحة" }, { "خدمة", "اجتماعية" }, { "فنية" }, { "عمالية" }, { "تسويق" } };
        TansikFilter.specifiersOUT_adbe = new String[][] { { "" }, { "أسنان", "بيطري" }, { "" }, { "" }, { "" }, { "" }, { "" }, { "" }, { "معهد", "سياسية", "حاسب", "اكادمية", "دار", "اداب", "رياضة" }, { "" }, { "" }, { "" }, { "" }, { "" }, { "" }, { "" }, { "" }, { "" } };


    }

    public static String arabicIgnoreSpelling(final String s) {
        return s.replace('ة', 'ه').replace('أ', 'ا').replace('إ', 'ا').replace('ى', 'ي').replace('آ', 'ا');
    }



}
