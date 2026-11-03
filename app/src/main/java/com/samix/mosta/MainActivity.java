
package com.samix.mosta;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import java.util.*;

public class MainActivity extends Activity {

    LinearLayout root;
    ToneGenerator tone;
    Handler handler = new Handler();
    boolean soundEnabled = true;
    boolean playing = false;
    int volume = 65;

    final int GREEN = Color.rgb(5, 48, 35);
    final int DARK = Color.rgb(3, 25, 20);
    final int GOLD = Color.rgb(226, 184, 75);
    final int WHITE = Color.WHITE;

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
        "0 -----", "1 .----", "2 ..---",
        "3 ...--", "4 ....-", "5 .....",
        "6 -....", "7 --...", "8 ---..",
        "9 ----."
    };

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);

        getWindow().setStatusBarColor(DARK);
        getWindow().setNavigationBarColor(DARK);

        try {
            tone = new ToneGenerator(
                AudioManager.STREAM_MUSIC, volume
            );
        } catch (Exception e) {
            tone = null;
        }

        home();
    }

    GradientDrawable shape(int color, int stroke) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(28);
        d.setStroke(2, stroke);
        return d;
    }

    void setup(String title) {
        ScrollView scroll = new ScrollView(this);

        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(18, 20, 18, 24);
        root.setGravity(Gravity.CENTER_HORIZONTAL);
        root.setBackgroundColor(DARK);
        root.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        scroll.addView(root);
        setContentView(scroll);

        TextView heading = new TextView(this);
        heading.setText("✦ SAMIX ✦\n" + title);
        heading.setTextColor(GOLD);
        heading.setTextSize(25);
        heading.setTypeface(null, Typeface.BOLD);
        heading.setGravity(Gravity.CENTER);
        heading.setPadding(8, 18, 8, 25);
        root.addView(heading);

        TextView subtitle = new TextView(this);
        subtitle.setText("mosta App • تعلم شفرة مورس");
        subtitle.setTextColor(WHITE);
        subtitle.setTextSize(14);
        subtitle.setGravity(Gravity.CENTER);
        root.addView(subtitle);
    }

    void addText(String text, int size) {
        TextView t = new TextView(this);
        t.setText(text);
        t.setTextColor(WHITE);
        t.setTextSize(size);
        t.setGravity(Gravity.CENTER);
        t.setPadding(12, 12, 12, 12);
        root.addView(t);
    }

    void addButton(String text, Runnable action) {
        Button b = new Button(this);
        b.setText(text);
        b.setTextColor(WHITE);
        b.setTextSize(16);
        b.setAllCaps(false);
        b.setBackground(shape(GREEN, GOLD));

        LinearLayout.LayoutParams p =
            new LinearLayout.LayoutParams(
                -1, -2
            );
        p.setMargins(0, 9, 0, 9);
        root.addView(b, p);

        b.setOnClickListener(v -> {
            clickSound();
            action.run();
        });
    }

    void clickSound() {
        if (soundEnabled && tone != null) {
            tone.startTone(ToneGenerator.TONE_PROP_BEEP, 65);
        }
    }

    void home() {
        setup("الرئيسية");

        addText("🇩🇿 مرحباً بك في عالم مورس", 22);
        addText("تعلم • ترجم • استمع • تدرب", 17);

        addButton("📚 الدروس", () -> lessons());
        addButton("🔤 مترجم مورس", () -> translator());
        addButton("📡 تدريب صوت مورس", () -> morsePractice());
        addButton("🔊 النظام الصوتي", () -> soundSettings());
        addButton("🧠 الاختبارات", () -> quiz());

        addText("SAMIX mosta App | الجزائر", 15);
    }

    void lessons() {
        setup("دروس مورس");

        addText("النقطة (.) والشرطة (-) هما أساس شفرة مورس.", 17);

        addText("الحروف الإنجليزية", 20);
        for (String item : letters) {
            addText(item, 18);
        }

        addText("الأرقام", 20);
        for (String item : numbers) {
            addText(item, 18);
        }

        addButton("العودة للرئيسية", () -> home());
    }

    String findCode(char c) {
        for (String item : letters) {
            String[] p = item.split(" ");
            if (p[0].charAt(0) == c) return p[1];
        }

        for (String item : numbers) {
            String[] p = item.split(" ");
            if (p[0].charAt(0) == c) return p[1];
        }

        return null;
    }

    String encode(String text) {
        StringBuilder result = new StringBuilder();

        for (char c : text.toUpperCase().toCharArray()) {
            if (c == ' ') {
                result.append("/ ");
            } else {
                String code = findCode(c);
                if (code != null) {
                    result.append(code).append(" ");
                }
            }
        }

        return result.toString().trim();
    }

    String decode(String text) {
        StringBuilder result = new StringBuilder();
        String[] words = text.trim().split("\\s*/\\s*");

        for (int w = 0; w < words.length; w++) {
            String[] codes = words[w].trim().split("\\s+");

            for (String code : codes) {
                char found = '?';

                for (String item : letters) {
                    String[] p = item.split(" ");
                    if (p[1].equals(code)) found = p[0].charAt(0);
                }

                for (String item : numbers) {
                    String[] p = item.split(" ");
                    if (p[1].equals(code)) found = p[0].charAt(0);
                }

                result.append(found);
            }

            if (w < words.length - 1) result.append(" ");
        }

        return result.toString();
    }

    void translator() {
        setup("مترجم مورس");

        EditText input = new EditText(this);
        input.setHint("اكتب نصاً إنجليزياً أو شفرة مورس");
        input.setTextColor(WHITE);
        input.setHintTextColor(GOLD);
        input.setTextSize(17);
        input.setPadding(15, 15, 15, 15);
        input.setBackground(shape(GREEN, GOLD));
        root.addView(input);

        TextView output = new TextView(this);
        output.setTextColor(GOLD);
        output.setTextSize(21);
        output.setGravity(Gravity.CENTER);
        output.setPadding(10, 25, 10, 25);
        root.addView(output);

        addButton("تحويل النص إلى مورس", () -> {
            output.setText(encode(input.getText().toString()));
        });

        addButton("تحويل مورس إلى نص", () -> {
            output.setText(decode(input.getText().toString()));
        });

        addButton("تشغيل النتيجة صوتياً", () -> {
            playMorse(output.getText().toString());
        });

        addButton("إيقاف الصوت", () -> stopMorse());
        addButton("العودة للرئيسية", () -> home());
    }

    void morsePractice() {
        setup("تدريب صوت مورس");

        addText("استمع إلى النقطة والشرطة", 20);
        addText("النقطة: قصيرة • الشرطة: طويلة", 17);

        addButton("تشغيل نقطة", () -> beep(130));
        addButton("تشغيل شرطة", () -> beep(390));
        addButton("تشغيل SOS", () -> playMorse("... --- ..."));

        addButton("إيقاف التشغيل", () -> stopMorse());
        addButton("العودة للرئيسية", () -> home());
    }

    void beep(int duration) {
        if (!soundEnabled || tone == null) return;
        tone.startTone(ToneGenerator.TONE_DTMF_0, duration);
    }

    void playMorse(String code) {
        stopMorse();

        if (!soundEnabled || tone == null) return;

        playing = true;
        String sequence = code.replace("/", " / ");
        playStep(sequence, 0);
    }

    void playStep(String code, int index) {
        if (!playing || index >= code.length()) {
            playing = false;
            return;
        }

        char c = code.charAt(index);
        int delay = 150;

        if (c == '.') {
            beep(120);
            delay = 250;
        } else if (c == '-') {
            beep(360);
            delay = 490;
        } else if (c == '/') {
            delay = 500;
        }

        handler.postDelayed(
            () -> playStep(code, index + 1), delay
        );
    }

    void stopMorse() {
        playing = false;
        handler.removeCallbacksAndMessages(null);

        if (tone != null) {
            tone.stopTone();
        }
    }

    void soundSettings() {
        setup("النظام الصوتي");

        addText(
            "حالة صوت التطبيق: " +
            (soundEnabled ? "مفعّل" : "متوقف"), 19
        );

        addButton("تشغيل الصوت", () -> {
            soundEnabled = true;
            soundSettings();
        });

        addButton("إيقاف الصوت", () -> {
            soundEnabled = false;
            stopMorse();
            soundSettings();
        });

        addText("مستوى الصوت: " + volume + "%", 18);

        SeekBar bar = new SeekBar(this);
        bar.setMax(100);
        bar.setProgress(volume);
        root.addView(bar);

        bar.setOnSeekBarChangeListener(
            new SeekBar.OnSeekBarChangeListener() {
                public void onProgressChanged(
                    SeekBar s, int value, boolean user
                ) {
                    volume = value;
                    if (tone != null) tone.release();

                    try {
                        tone = new ToneGenerator(
                            AudioManager.STREAM_MUSIC,
                            volume
                        );
                    } catch (Exception e) {
                        tone = null;
                    }
                }

                public void onStartTrackingTouch(SeekBar s) {}
                public void onStopTrackingTouch(SeekBar s) {}
            }
        );

        addButton("العودة للرئيسية", () -> home());
    }

    void quiz() {
        setup("اختبار مورس");

        addText("ماذا تعني الشفرة التالية؟", 20);
        addText("... --- ...", 30);

        addButton("SOS", () -> answer(true));
        addButton("ABC", () -> answer(false));
        addButton("XYZ", () -> answer(false));

        addButton("العودة للرئيسية", () -> home());
    }

    void answer(boolean correct) {
        Toast.makeText(
            this,
            correct ? "إجابة صحيحة! أحسنت" : "حاول مرة أخرى",
            Toast.LENGTH_SHORT
        ).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopMorse();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopMorse();

        if (tone != null) {
            tone.release();
            tone = null;
        }
    }
}
