package com.samix.mosta;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import android.graphics.Typeface;
import java.util.*;

public class MainActivity extends Activity {

    LinearLayout layout;
    EditText input;
    TextView result;

    String[] letters = {
        "A .-", "B -...", "C -.-.", "D -..",
        "E .", "F ..-.", "G --.", "H ....",
        "I ..", "J .---", "K -.-", "L .-..",
        "M --", "N -.", "O ---", "P .--.",
        "Q --.-", "R .-.", "S ...", "T -",
        "U ..-", "V ...-", "W .--", "X -..-",
        "Y -.--", "Z --.."
    };

    String[] numbers = {
        "0 -----", "1 .----", "2 ..---", "3 ...--",
        "4 ....-", "5 .....", "6 -....", "7 --...",
        "8 ---..", "9 ----."
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        home();
    }

    void setup(String title) {
        ScrollView scroll = new ScrollView(this);
        layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(22, 25, 22, 25);
        layout.setBackgroundColor(Color.rgb(9, 35, 31));
        layout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        scroll.addView(layout);
        setContentView(scroll);

        TextView heading = new TextView(this);
        heading.setText(title);
        heading.setTextSize(27);
        heading.setTypeface(null, Typeface.BOLD);
        heading.setTextColor(Color.rgb(226, 190, 91));
        heading.setGravity(Gravity.CENTER);
        heading.setPadding(5, 20, 5, 25);
        layout.addView(heading);
    }

    void addText(String text, int size) {
        TextView t = new TextView(this);
        t.setText(text);
        t.setTextSize(size);
        t.setTextColor(Color.WHITE);
        t.setPadding(10, 12, 10, 12);
        layout.addView(t);
    }

    void addButton(String text, Runnable action) {
        Button b = new Button(this);
        b.setText(text);
        b.setTextColor(Color.WHITE);
        b.setBackgroundTintList(
            android.content.res.ColorStateList.valueOf(
                Color.rgb(24, 105, 83)
            )
        );
        LinearLayout.LayoutParams p =
            new LinearLayout.LayoutParams(
                -1, -2
            );
        p.setMargins(0, 8, 0, 8);
        layout.addView(b, p);
        b.setOnClickListener(v -> action.run());
    }

    void home() {
        setup("SAMIX mosta App");

        addText("مرحباً بك في عالم شفرة مورس", 20);
        addText("تعلم • ترجم • اختبر معلوماتك", 16);

        addButton("📚 دروس مورس", () -> lessons());
        addButton("🔤 مترجم مورس", () -> translator());
        addButton("🧠 اختبار مورس", () -> quiz());

        addText("SAMIX • MOSTAGANEM 🇩🇿", 14);
    }

    void lessons() {
        setup("دروس شفرة مورس");

        addText("شفرة مورس نظام يستخدم النقاط والشرطات لتمثيل الحروف والأرقام.", 17);

        addText("النقطة: .", 20);
        addText("الشرطة: -", 20);

        addText("الحروف الإنجليزية:", 19);

        for (String letter : letters) {
            addText(letter, 17);
        }

        addText("الأرقام:", 19);

        for (String number : numbers) {
            addText(number, 17);
        }

        addButton("العودة للرئيسية", () -> home());
    }

    String encode(String text) {
        StringBuilder output = new StringBuilder();

        for (char c : text.toUpperCase().toCharArray()) {
            if (c == ' ') {
                output.append("/ ");
                continue;
            }

            String code = findCode(c);

            if (code != null) {
                output.append(code).append(" ");
            }
        }

        return output.toString().trim();
    }

    String findCode(char c) {
        for (String item : letters) {
            String[] parts = item.split(" ");
            if (parts[0].charAt(0) == c) return parts[1];
        }

        for (String item : numbers) {
            String[] parts = item.split(" ");
            if (parts[0].charAt(0) == c) return parts[1];
        }

        return null;
    }

    String decode(String text) {
        StringBuilder output = new StringBuilder();

        String[] words = text.trim().split("\\s*/\\s*");

        for (int w = 0; w < words.length; w++) {
            String[] codes = words[w].trim().split("\\s+");

            for (String code : codes) {
                if (code.isEmpty()) continue;

                char found = '?';

                for (String item : letters) {
                    String[] p = item.split(" ");
                    if (p[1].equals(code)) found = p[0].charAt(0);
                }

                for (String item : numbers) {
                    String[] p = item.split(" ");
                    if (p[1].equals(code)) found = p[0].charAt(0);
                }

                output.append(found);
            }

            if (w < words.length - 1) output.append(" ");
        }

        return output.toString();
    }

    void translator() {
        setup("مترجم شفرة مورس");

        addText("اكتب نصاً إنجليزياً أو شفرة مورس:", 17);

        input = new EditText(this);
        input.setTextColor(Color.WHITE);
        input.setHintTextColor(Color.LTGRAY);
        input.setHint("اكتب هنا...");
        input.setSingleLine(false);
        layout.addView(input);

        addButton("تحويل النص إلى مورس", () -> {
            String value = input.getText().toString();
            result.setText(encode(value));
        });

        addButton("تحويل مورس إلى نص", () -> {
            String value = input.getText().toString();
            result.setText(decode(value));
        });

        result = new TextView(this);
        result.setTextColor(Color.rgb(226, 190, 91));
        result.setTextSize(20);
        result.setPadding(10, 25, 10, 25);
        layout.addView(result);

        addButton("العودة للرئيسية", () -> home());
    }

    void quiz() {
        setup("اختبار مورس");

        addText("ماذا يعني الرمز التالي؟", 19);
        addText("... --- ...", 30);

        addButton("SOS", () -> answer(true));
        addButton("ABC", () -> answer(false));
        addButton("XYZ", () -> answer(false));

        addButton("العودة للرئيسية", () -> home());
    }

    void answer(boolean correct) {
        if (correct) {
            Toast.makeText(this, "إجابة صحيحة! أحسنت", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "حاول مرة أخرى", Toast.LENGTH_SHORT).show();
        }
    }
}