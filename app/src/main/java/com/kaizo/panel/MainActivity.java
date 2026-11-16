package com.kaizo.panel;

import android.app.Activity;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

public class MainActivity extends Activity {
    private final int BG = Color.rgb(10, 8, 18);
    private final int PURPLE = Color.rgb(168, 70, 255);
    private SharedPreferences prefs;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        prefs = getSharedPreferences("kaizo_settings", MODE_PRIVATE);
        getWindow().setStatusBarColor(BG);
        getWindow().setNavigationBarColor(BG);

        ScrollView scroll = new ScrollView(this);
        scroll.setBackgroundColor(BG);
        LinearLayout page = new LinearLayout(this);
        page.setOrientation(LinearLayout.VERTICAL);
        page.setPadding(dp(20), dp(26), dp(20), dp(28));
        scroll.addView(page);

        TextView brand = new TextView(this);
        brand.setText("KAIZO");
        brand.setTextColor(Color.WHITE);
        brand.setTextSize(34);
        brand.setTypeface(Typeface.DEFAULT, Typeface.BOLD_ITALIC);
        brand.setGravity(Gravity.CENTER);
        page.addView(brand);

        TextView sub = new TextView(this);
        sub.setText("CUSTOM ROOM • PLAYER SETUP");
        sub.setTextColor(PURPLE);
        sub.setTextSize(12);
        sub.setGravity(Gravity.CENTER);
        sub.setPadding(0, 0, 0, dp(20));
        page.addView(sub);

        addSection(page, "PROFIL ROOM");
        EditText room = new EditText(this);
        room.setSingleLine(true);
        room.setHint("Nama custom room");
        room.setHintTextColor(0xff8f879b);
        room.setTextColor(Color.WHITE);
        room.setText(prefs.getString("room", ""));
        styleField(room);
        page.addView(room, matchWrap());

        addSection(page, "PENGATURAN SENSITIVITAS");
        addSlider(page, "General", "general", 0, 200, 100);
        addSlider(page, "Red Dot", "reddot", 0, 200, 95);
        addSlider(page, "2x Scope", "scope2", 0, 200, 90);
        addSlider(page, "4x Scope", "scope4", 0, 200, 85);
        addSlider(page, "Sniper Scope", "sniper", 0, 200, 60);

        addSection(page, "MENU KAIZO • DEMO VISUAL");
        addToggle(page, "ESP Line (simulasi)", "esp_line_demo", false);
        addToggle(page, "ESP Box (simulasi)", "esp_box_demo", false);
        addToggle(page, "Map Radar (simulasi)", "map_radar_demo", false);
        addToggle(page, "Penanda target (simulasi)", "target_marker_demo", false);
        addToggle(page, "Info jarak (simulasi)", "distance_demo", false);

        addSection(page, "PREFERENSI HUD");
        addToggle(page, "Tombol tembak kiri", "leftfire", true);
        addToggle(page, "Quick weapon switch", "quickswitch", true);
        addToggle(page, "Getar perangkat", "vibration", false);

        Button save = new Button(this);
        save.setText("SIMPAN PENGATURAN");
        save.setTextColor(Color.WHITE);
        save.setAllCaps(false);
        save.setTextSize(15);
        GradientDrawable buttonBg = new GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            new int[]{0xff7624db, 0xffc044ff});
        buttonBg.setCornerRadius(dp(14));
        save.setBackground(buttonBg);
        LinearLayout.LayoutParams bp = matchWrap();
        bp.topMargin = dp(24);
        page.addView(save, bp);
        save.setOnClickListener(v -> {
            prefs.edit().putString("room", room.getText().toString()).apply();
            Toast.makeText(this, "Pengaturan KAIZO tersimpan", Toast.LENGTH_SHORT).show();
        });

        TextView note = new TextView(this);
        note.setText("Menu ESP Line, ESP Box, Radar, penanda target, dan jarak hanya sakelar demo untuk tampilan aplikasi. Tidak membaca layar/game, tidak menampilkan posisi pemain, dan tidak mengubah Free Fire.");
        note.setTextColor(0xffaaa2b7);
        note.setTextSize(11);
        note.setGravity(Gravity.CENTER);
        note.setPadding(dp(8), dp(18), dp(8), 0);
        page.addView(note);

        setContentView(scroll);
    }

    private void addSection(LinearLayout parent, String title) {
        TextView t = new TextView(this);
        t.setText(title);
        t.setTextColor(PURPLE);
        t.setTextSize(13);
        t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        t.setPadding(0, dp(18), 0, dp(9));
        parent.addView(t);
    }

    private void addSlider(LinearLayout parent, String label, String key, int min, int max, int def) {
        LinearLayout card = card(parent);
        TextView title = new TextView(this);
        title.setText(label + "  •  " + prefs.getInt(key, def));
        title.setTextColor(Color.WHITE);
        title.setTextSize(14);
        card.addView(title);
        SeekBar bar = new SeekBar(this);
        bar.setMax(max - min);
        bar.setProgress(prefs.getInt(key, def) - min);
        bar.setProgressTintList(android.content.res.ColorStateList.valueOf(PURPLE));
        bar.setThumbTintList(android.content.res.ColorStateList.valueOf(0xffe6c5ff));
        card.addView(bar);
        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar b, int p, boolean user) {
                title.setText(label + "  •  " + (p + min));
                if (user) prefs.edit().putInt(key, p + min).apply();
            }
            public void onStartTrackingTouch(SeekBar b) {}
            public void onStopTrackingTouch(SeekBar b) {}
        });
    }

    private void addToggle(LinearLayout parent, String label, String key, boolean def) {
        LinearLayout card = card(parent);
        Switch sw = new Switch(this);
        sw.setText(label);
        sw.setTextColor(Color.WHITE);
        sw.setTextSize(14);
        sw.setChecked(prefs.getBoolean(key, def));
        sw.setButtonTintList(android.content.res.ColorStateList.valueOf(PURPLE));
        sw.setOnCheckedChangeListener((button, checked) ->
            prefs.edit().putBoolean(key, checked).apply());
        card.addView(sw);
    }

    private LinearLayout card(LinearLayout parent) {
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(dp(14), dp(10), dp(14), dp(8));
        GradientDrawable bg = new GradientDrawable();
        bg.setColor(0xff191323);
        bg.setCornerRadius(dp(13));
        bg.setStroke(dp(1), 0xff54227e);
        box.setBackground(bg);
        LinearLayout.LayoutParams lp = matchWrap();
        lp.bottomMargin = dp(8);
        parent.addView(box, lp);
        return box;
    }

    private void styleField(EditText e) {
        GradientDrawable bg = new GradientDrawable();
        bg.setColor(0xff191323);
        bg.setCornerRadius(dp(12));
        bg.setStroke(dp(1), 0xff54227e);
        e.setBackground(bg);
        e.setPadding(dp(14), dp(8), dp(14), dp(8));
    }

    private LinearLayout.LayoutParams matchWrap() {
        return new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT);
    }
    private int dp(int v) { return (int)(v * getResources().getDisplayMetrics().density + 0.5f); }
}
