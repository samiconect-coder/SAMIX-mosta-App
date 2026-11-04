
package com.samix.mosta;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

import java.util.Locale;

public class MainActivity extends Activity {

    private final int BG = Color.rgb(3, 20, 16);
    private final int GREEN = Color.rgb(7, 65, 43);
    private final int GOLD = Color.rgb(239, 190, 70);
    private final int WHITE = Color.WHITE;
    private final int MUTED = Color.rgb(190, 210, 194);

    private LinearLayout root;
    private LinearLayout currentRow;
    private ToneGenerator tone;
    private final Handler handler = new Handler();

    private boolean soundEnabled = true;
    private boolean playing = false;
    private int volume = 65;

    private final String[] letters = {
        "A .-", "B -...", "C -.-.", "D -..",
        "E .", "F ..-.", "G --.", "H ....",
        "I ..", "J .---", "K -.-", "L .-..",
        "M --", "N -.", "O ---", "P .--.",
        "Q --.-", "R .-.", "S ...", "T -",
        "U ..-", "V ...-", "W .--", "X -..-",
        "Y -.--", "Z --.."
    };

    private final String[] numbers = {
        "0 -----", "1 .----", "2 ..---",
        "3 ...--", "4 ....-", "5 .....",
        "6 -....", "7 --...", "8 ---..", "9 ----."
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(BG);
        getWindow().setNavigationBarColor(BG);

        createTone();
        home();
    }

    private void createTone() {
        try {
            if (tone != null) tone.release();
            tone = new ToneGenerator(
                AudioManager.STREAM_MUSIC, volume
            );
        } catch (Exception e) {
            tone = null;
        }
    }

    private GradientDrawable panel(int color, int border) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(28);
        d.setStroke(2, border);
        return d;
    }

    private TextView makeText(
        String value, int size, int color
    ) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextColor(color);
        t.setTextSize(size);
        t.setGravity(Gravity.CENTER);
        t.setPadding(10, 10, 10, 10);
        return t;
    }

    private void setup(String subtitle) {
        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);

        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER_HORIZONTAL);
        root.setPadding(16, 16, 16, 28);
        root.setBackground(new PatternBackground());

        scroll.addView(root);
        setContentView(scroll);

        TextView logo = makeText(
            "♛ SAMIX ♛\nmosta App", 30, GOLD
        );
        logo.setTypeface(null, 1);
        logo.setPadding(10, 22, 10, 8);
        root.addView(logo);

        root.addView(makeText(subtitle, 17, MUTED));

        View line = new View(this);
        line.setBackgroundColor(GOLD);

        LinearLayout.LayoutParams lp =
            new LinearLayout.LayoutParams(120, 2);
        lp.setMargins(0, 12, 0, 16);
        root.addView(line, lp);
    }

    // خلفية هندسية مرسومة
    private class PatternBackground extends Drawable {

        private final Paint p =
            new Paint(Paint.ANTI_ALIAS_FLAG);

        @Override
        public void draw(Canvas canvas) {
            Rect b = getBounds();

            canvas.drawColor(BG);

            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(1);
            p.setColor(Color.rgb(8, 45, 31));

            for (int y = b.top; y < b.bottom; y += 65) {
                for (int x = b.left; x < b.right; x += 65) {
                    canvas.drawCircle(x, y, 23, p);
                    canvas.drawRect(
                        x - 16, y - 16,
                        x + 16, y + 16, p
                    );
                }
            }

            p.setColor(Color.rgb(17, 75, 48));
            p.setStrokeWidth(1.5f);

            canvas.drawLine(
                b.left, b.top, b.right, b.bottom, p
            );

            canvas.drawLine(
                b.right, b.top, b.left, b.bottom, p
            );
        }

        @Override
        public void setAlpha(int alpha) {
            p.setAlpha(alpha);
        }

        @Override
        public void setColorFilter(ColorFilter filter) {
            p.setColorFilter(filter);
        }

        @Override
        public int getOpacity() {
            return PixelFormat.OPAQUE;
        }
    }

    private void addRow() {
        currentRow = new LinearLayout(this);
        currentRow.setOrientation(LinearLayout.HORIZONTAL);
        currentRow.setGravity(Gravity.CENTER);
        root.addView(currentRow);
    }

    private void addButton(
        String label, String symbol, Runnable action
    ) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setGravity(Gravity.CENTER);
        card.setPadding(8, 14, 8, 14);
        card.setBackground(panel(GREEN, GOLD));
        card.setElevation(8);

        TextView icon = makeText(symbol, 34, GOLD);
        TextView name = makeText(label, 17, WHITE);
        name.setTypeface(null, 1);

        card.addView(icon);
        card.addView(name);

        LinearLayout.LayoutParams lp =
            new LinearLayout.LayoutParams(0, 145, 1);

        lp.setMargins(5, 6, 5, 6);

        card.setOnClickListener(v -> {
            clickSound();
            action.run();
        });

        currentRow.addView(card, lp);
    }

    private void addAction(String label, Runnable action) {
        Button button = new Button(this);
        button.setText(label);
        button.setTextColor(WHITE);
        button.setTextSize(15);
        button.setAllCaps(false);
        button.setBackground(panel(GREEN, GOLD));

        LinearLayout.LayoutParams lp =
            new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            );

        lp.setMargins(0, 7, 0, 7);
        root.addView(button, lp);

        button.setOnClickListener(v -> {
            clickSound();
            action.run();
        });
    }

    private void home() {
        stopMorse();
        setup("تعلم شفرة مورس بكل سهولة");

        root.addView(makeText(
            "🇩🇿 مرحباً بك في عالم مورس 🇩🇿",
            19, GOLD
        ));

        addRow();
        addButton("الدروس", "📚", this::lessons);
        addButton("مترجم مورس", "🔤", this::translator);

        addRow();
        addButton("تدريب الصوت", "🎧", this::practice);
        addButton("الاختبارات", "📝", this::quiz);

        addRow();
        addButton("الإعدادات", "⚙️", this::settings);
        addButton("حول التطبيق", "ⓘ", this::about);

        root.addView(makeText(
            "• • •     — — —     • • •", 23, GOLD
        ));

        root.addView(makeText(
            "SAMIX mosta App", 19, GOLD
        ));

        root.addView(makeText(
            "Algeria • Mostaganem 🇩🇿", 14, MUTED
        ));
    }

    private String findCode(char c) {
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

    private String encode(String input) {
        StringBuilder out = new StringBuilder();

        for (char c : input.toUpperCase(Locale.ROOT).toCharArray()) {
            if (c == ' ') {
                out.append("/ ");
            } else {
                String code = findCode(c);
                if (code != null) {
                    out.append(code).append(" ");
                }
            }
        }

        return out.toString().trim();
    }

    private String decode(String input) {
        if (input.trim().isEmpty()) return "";

        StringBuilder out = new StringBuilder();
        String[] words = input.trim().split("\\s*/\\s*");

        for (int w = 0; w < words.length; w++) {
            String word = words[w].trim();

            if (!word.isEmpty()) {
                String[] codes = word.split("\\s+");

                for (String code : codes) {
                    char found = '?';

                    for (String s : letters) {
                        String[] a = s.split(" ");
                        if (a[1].equals(code)) {
                            found = a[0].charAt(0);
                            break;
                        }
                    }

                    for (String s : numbers) {
                        String[] a = s.split(" ");
                        if (a[1].equals(code)) {
                            found = a[0].charAt(0);
                            break;
                        }
                    }

                    out.append(found);
                }
            }

            if (w < words.length - 1) out.append(" ");
        }

        return out.toString();
    }

    private EditText makeInput(String hint) {
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

    private void lessons() {
        stopMorse();
        setup("مكتبة دروس مورس");

        root.addView(makeText("الحروف الإنجليزية", 21, GOLD));

        for (String s : letters) {
            root.addView(makeText(s, 19, WHITE));
        }

        root.addView(makeText("الأرقام", 21, GOLD));

        for (String s : numbers) {
            root.addView(makeText(s, 19, WHITE));
        }

        addAction("العودة للرئيسية", this::home);
    }

    private void translator() {
        stopMorse();
        setup("مترجم مورس");

        EditText input = makeInput(
            "اكتب نصاً إنجليزياً أو شفرة مورس"
        );

        TextView output = makeText(
            "ستظهر النتيجة هنا", 20, GOLD
        );

        root.addView(output);

        addAction("تحويل النص إلى مورس", () -> {
            output.setText(encode(
                input.getText().toString()
            ));
        });

        addAction("تحويل مورس إلى نص", () -> {
            output.setText(decode(
                input.getText().toString()
            ));
        });

        addAction("تشغيل صوت النتيجة", () -> {
            playMorse(output.getText().toString());
        });

        addAction("إيقاف الصوت", this::stopMorse);
        addAction("الرئيسية", this::home);
    }

    private void practice() {
        stopMorse();
        setup("مختبر صوت مورس");

        root.addView(makeText(
            "النقطة قصيرة والشرطة طويلة", 20, WHITE
        ));

        addAction("تشغيل نقطة  •", () -> beep(120));
        addAction("تشغيل شرطة  —", () -> beep(360));
        addAction("تشغيل نداء SOS", () -> playMorse("... --- ..."));
        addAction("إيقاف الصوت", this::stopMorse);
        addAction("الرئيسية", this::home);
    }

    private void beep(int duration) {
        if (soundEnabled && tone != null) {
            tone.startTone(
                ToneGenerator.TONE_DTMF_0, duration
            );
        }
    }

    private void clickSound() {
        if (soundEnabled && tone != null) {
            tone.startTone(
                ToneGenerator.TONE_PROP_BEEP, 55
            );
        }
    }

    private void playMorse(String code) {
        stopMorse();

        if (!soundEnabled || tone == null) return;

        playing = true;
        playStep(code.replace("/", " / "), 0);
    }

    private void playStep(String code, int index) {
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

        handler.postDelayed(
            () -> playStep(code, index + 1), delay
        );
    }

    private void stopMorse() {
        playing = false;
        handler.removeCallbacksAndMessages(null);

        if (tone != null) {
            tone.stopTone();
        }
    }

    private void settings() {
        stopMorse();
        setup("إعدادات التطبيق");

        root.addView(makeText(
            soundEnabled ? "الصوت مفعل" : "الصوت متوقف",
            20, GOLD
        ));

        addAction("تشغيل الصوت", () -> {
            soundEnabled = true;
            settings();
        });

        addAction("إيقاف الصوت", () -> {
            soundEnabled = false;
            settings();
        });

        TextView level = makeText(
            "مستوى الصوت: " + volume + "%",
            18, WHITE
        );
        root.addView(level);

        SeekBar bar = new SeekBar(this);
        bar.setMax(100);
        bar.setProgress(volume);
        root.addView(bar);

        bar.setOnSeekBarChangeListener(
            new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(
                    SeekBar seekBar, int value, boolean user
                ) {
                    volume = value;
                    level.setText("مستوى الصوت: " + volume + "%");
                    createTone();
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {}
            }
        );

        addAction("الرئيسية", this::home);
    }

    private void quiz() {
        stopMorse();
        setup("اختبار مورس");

        root.addView(makeText(
            "ماذا تعني الشفرة التالية؟", 21, WHITE
        ));

        root.addView(makeText(
            "... --- ...", 32, GOLD
        ));

        addAction("SOS", () -> result(true));
        addAction("ABC", () -> result(false));
        addAction("XYZ", () -> result(false));
        addAction("الرئيسية", this::home);
    }

    private void result(boolean correct) {
        Toast.makeText(
            this,
            correct ? "إجابة صحيحة! أحسنت" : "حاول مرة أخرى",
            Toast.LENGTH_SHORT
        ).show();
    }

    private void about() {
        stopMorse();
        setup("حول التطبيق");

        root.addView(makeText(
            "SAMIX mosta App", 25, GOLD
        ));

        root.addView(makeText(
            "تطبيق جزائري لتعلم شفرة مورس",
            18, WHITE
        ));

        root.addView(makeText(
            "الإصدار 3.0", 17, MUTED
        ));

        root.addView(makeText(
            "صنع في الجزائر 🇩🇿", 17, GOLD
        ));

        addAction("الرئيسية", this::home);
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
