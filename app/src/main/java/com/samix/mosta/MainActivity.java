
package com.samix.mosta;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.graphics.*;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.view.*;
import android.widget.*;
import java.util.*;

public class MainActivity extends Activity {

    final int BG = Color.rgb(3, 20, 16);
    final int GREEN = Color.rgb(5, 65, 43);
    final int GOLD = Color.rgb(239, 190, 70);
    final int WHITE = Color.WHITE;
    final int MUTED = Color.rgb(190, 210, 194);

    LinearLayout root;
    ToneGenerator tone;
    Handler handler = new Handler();

    boolean soundEnabled = true;
    boolean playing = false;
    int volume = 65;

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
        "6 -....", "7 --...", "8 ---..", "9 ----."
    };

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);

        getWindow().setStatusBarColor(BG);
        getWindow().setNavigationBarColor(BG);

        try {
            tone = new ToneGenerator(AudioManager.STREAM_MUSIC, volume);
        } catch (Exception e) {}

        home();
    }

    GradientDrawable panel(int color, int border) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(28);
        d.setStroke(2, border);
        return d;
    }

    TextView text(String value, int size, int color) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextColor(color);
        t.setTextSize(size);
        t.setGravity(Gravity.CENTER);
        t.setPadding(10, 10, 10, 10);
        return t;
    }

    void setup(String title) {
        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);

        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(16, 16, 16, 24);
        root.setGravity(Gravity.CENTER_HORIZONTAL);
        root.setBackground(new PatternBackground());

        scroll.addView(root);
        setContentView(scroll);

        TextView header = text("♛ SAMIX ♛\nmosta App", 29, GOLD);
        header.setTypeface(null, 1);
        header.setPadding(10, 22, 10, 8);
        root.addView(header);

        root.addView(text(title, 17, MUTED));
        View line = new View(this);
        line.setBackgroundColor(GOLD);
        LinearLayout.LayoutParams lp =
            new LinearLayout.LayoutParams(120, 2);
        lp.setMargins(0, 10, 0, 14);
        root.addView(line, lp);
    }

    class PatternBackground extends View {
        Paint p = new Paint(3);

        PatternBackground() {
            super(MainActivity.this);
        }

        @Override
        protected void onDraw(Canvas c) {
            super.onDraw(c);
            c.drawColor(BG);

            p.setColor(Color.rgb(8, 45, 31));
            p.setStrokeWidth(1);
            p.setStyle(Paint.Style.STROKE);

            for (int y = 0; y < getHeight(); y += 65) {
                for (int x = 0; x < getWidth(); x += 65) {
                    c.drawCircle(x, y, 23, p);
                    c.drawRect(x - 16, y - 16, x + 16, y + 16, p);
                }
            }

            p.setColor(Color.rgb(17, 75, 48));
            p.setStrokeWidth(2);
            c.drawLine(0, 0, getWidth(), getHeight(), p);
            c.drawLine(getWidth(), 0, 0, getHeight(), p);
        }
    }

    void addButton(String label, String icon, Runnable action) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setGravity(Gravity.CENTER);
        card.setPadding(10, 14, 10, 14);
        card.setBackground(panel(GREEN, GOLD));

        TextView symbol = text(icon, 34, GOLD);
        TextView name = text(label, 17, WHITE);
        name.setTypeface(null, 1);

        card.addView(symbol);
        card.addView(name);

        LinearLayout.LayoutParams lp =
            new LinearLayout.LayoutParams(0, 145, 1);
        lp.setMargins(5, 6, 5, 6);

        card.setOnClickListener(v -> {
            clickSound();
            action.run();
        });

        card.setElevation(7);
        currentRow.addView(card, lp);
    }

    LinearLayout currentRow;

    void addRow() {
        currentRow = new LinearLayout(this);
        currentRow.setOrientation(LinearLayout.HORIZONTAL);
        currentRow.setGravity(Gravity.CENTER);
        root.addView(currentRow);
    }

    void addText(String value, int size) {
        root.addView(text(value, size, WHITE));
    }

    void addAction(String label, Runnable action) {
        Button b = new Button(this);
        b.setText(label);
        b.setTextColor(WHITE);
        b.setAllCaps(false);
        b.setTextSize(15);
        b.setBackground(panel(GREEN, GOLD));

        LinearLayout.LayoutParams lp =
            new LinearLayout.LayoutParams(-1, -2);
        lp.setMargins(0, 7, 0, 7);
        root.addView(b, lp);

        b.setOnClickListener(v -> {
            clickSound();
            action.run();
        });
    }

    void home() {
        setup("تعلم شفرة مورس بكل سهولة");

        TextView welcome = text(
            "🇩🇿  مرحباً بك في عالم مورس  🇩🇿",
            19, GOLD
        );
        root.addView(welcome);

        addRow();
        addButton("الدروس", "📚", () -> lessons());
        addButton("مترجم مورس", "🔤", () -> translator());

        addRow();
        addButton("تدريب الصوت", "🎧", () -> practice());
        addButton("الاختبارات", "📝", () -> quiz());

        addRow();
        addButton("الإعدادات", "⚙️", () -> settings());
        addButton("حول التطبيق", "ⓘ", () -> about());

        root.addView(text("• • •  — — —  • • •", 23, GOLD));
        root.addView(text("SAMIX mosta App", 19, GOLD));
        root.addView(text("Algeria • Mostaganem 🇩🇿", 14, MUTED));
    }

    String findCode(char c) {
        for (String s : letters) {
            String[] a = s.split(" ");
            if (a[0].charAt(0) == c) return a[1];
        }

        for (String s : numbers) {
            String[] a = s.split(" ");
            if (a[0].charAt(0) == c) return a[1];
        }

        return null;
    }

    String encode(String input) {
        StringBuilder out = new StringBuilder();

        for (char c : input.toUpperCase().toCharArray()) {
            if (c == ' ') {
                out.append("/ ");
            } else {
                String code = findCode(c);
                if (code != null) out.append(code).append(" ");
            }
        }

        return out.toString().trim();
    }

    String decode(String input) {
        StringBuilder out = new StringBuilder();
        String[] words = input.trim().split("\\s*/\\s*");

        for (int w = 0; w < words.length; w++) {
            for (String code : words[w].trim().split("\\s+")) {
                char found = '?';

                for (String s : letters) {
                    String[] a = s.split(" ");
                    if (a[1].equals(code)) found = a[0].charAt(0);
                }

                for (String s : numbers) {
                    String[] a = s.split(" ");
                    if (a[1].equals(code)) found = a[0].charAt(0);
                }

                out.append(found);
            }

            if (w < words.length - 1) out.append(" ");
        }

        return out.toString();
    }

    EditText makeInput(String hint) {
        EditText e = new EditText(this);
        e.setHint(hint);
        e.setHintTextColor(MUTED);
        e.setTextColor(WHITE);
        e.setTextSize(17);
        e.setPadding(14, 12, 14, 12);
        e.setBackground(panel(GREEN, GOLD));
        root.addView(e);
        return e;
    }

    void lessons() {
        setup("مكتبة دروس مورس");

        addText("الحروف الإنجليزية", 21);
        for (String s : letters) addText(s, 19);

        addText("الأرقام", 21);
        for (String s : numbers) addText(s, 19);

        addAction("العودة للرئيسية", () -> home());
    }

    void translator() {
        setup("مترجم مورس");

        EditText input = makeInput("اكتب نصاً إنجليزياً أو شفرة مورس");
        TextView output = text("ستظهر النتيجة هنا", 20, GOLD);
        root.addView(output);

        addAction("تحويل النص إلى مورس", () -> {
            output.setText(encode(input.getText().toString()));
        });

        addAction("تحويل مورس إلى نص", () -> {
            output.setText(decode(input.getText().toString()));
        });

        addAction("تشغيل صوت النتيجة", () -> {
            playMorse(output.getText().toString());
        });

        addAction("إيقاف الصوت", () -> stopMorse());
        addAction("الرئيسية", () -> home());
    }

    void practice() {
        setup("مختبر صوت مورس");

        addText("النقطة قصيرة والشرطة طويلة", 20);

        addAction("تشغيل نقطة  •", () -> beep(130));
        addAction("تشغيل شرطة  —", () -> beep(390));
        addAction("تشغيل نداء SOS", () -> playMorse("... --- ..."));
        addAction("إيقاف الصوت", () -> stopMorse());
        addAction("الرئيسية", () -> home());
    }

    void beep(int duration) {
        if (soundEnabled && tone != null) {
            tone.startTone(ToneGenerator.TONE_DTMF_0, duration);
        }
    }

    void clickSound() {
        if (soundEnabled && tone != null) {
            tone.startTone(ToneGenerator.TONE_PROP_BEEP, 55);
        }
    }

    void playMorse(String code) {
        stopMorse();

        if (!soundEnabled || tone == null) return;

        playing = true;
        playStep(code.replace("/", " / "), 0);
    }

    void playStep(String code, int index) {
        if (!playing || index >= code.length()) {
            playing = false;
            return;
        }

        char c = code.charAt(index);
        int delay = 130;

        if (c == '.') {
            beep(120);
            delay = 250;
        } else if (c == '-') {
            beep(360);
            delay = 490;
        } else if (c == '/') {
            delay = 500;
        }

        handler.postDelayed(() -> playStep(code, index + 1), delay);
    }

    void stopMorse() {
        playing = false;
        handler.removeCallbacksAndMessages(null);

        if (tone != null) tone.stopTone();
    }

    void settings() {
        setup("إعدادات التطبيق");

        addText("التحكم في الصوت", 20);
        addText(soundEnabled ? "الصوت مفعل" : "الصوت متوقف", 17);

        addAction("تشغيل الصوت", () -> {
            soundEnabled = true;
            settings();
        });

        addAction("إيقاف الصوت", () -> {
            soundEnabled = false;
            stopMorse();
            settings();
        });

        addText("مستوى الصوت: " + volume + "%", 18);

        SeekBar bar = new SeekBar(this);
        bar.setMax(100);
        bar.setProgress(volume);
        root.addView(bar);

        bar.setOnSeekBarChangeListener(
            new SeekBar.OnSeekBarChangeListener() {
                public void onProgressChanged(
                    SeekBar b, int value, boolean user
                ) {
                    volume = value;
                    if (tone != null) tone.release();

                    try {
                        tone = new ToneGenerator(
                            AudioManager.STREAM_MUSIC, volume
                        );
                    } catch (Exception e) {
                        tone = null;
                    }
                }

                public void onStartTrackingTouch(SeekBar b) {}
                public void onStopTrackingTouch(SeekBar b) {}
            }
        );

        addAction("الرئيسية", () -> home());
    }

    void quiz() {
        setup("اختبار مورس");

        addText("ماذا تعني الشفرة؟", 22);
        addText("... --- ...", 30);

        addAction("SOS", () -> result(true));
        addAction("ABC", () -> result(false));
        addAction("XYZ", () -> result(false));
        addAction("الرئيسية", () -> home());
    }

    void result(boolean correct) {
        Toast.makeText(
            this,
            correct ? "إجابة صحيحة! أحسنت" : "حاول مرة أخرى",
            Toast.LENGTH_SHORT
        ).show();
    }

    void about() {
        setup("حول SAMIX");

        addText("SAMIX mosta App", 24);
        addText("تطبيق جزائري لتعلم شفرة مورس", 18);
        addText("الإصدار 3.0", 17);
        addText("صنع بحب في الجزائر 🇩🇿", 17);

        addAction("الرئيسية", () -> home());
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
