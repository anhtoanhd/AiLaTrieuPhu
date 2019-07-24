package com.toanpt.ailatrieuphu.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.toanpt.ailatrieuphu.Adapter.HighScoreAdapter;
import com.toanpt.ailatrieuphu.Database.DatabaseHelper;
import com.toanpt.ailatrieuphu.Model.HighScore;
import com.toanpt.ailatrieuphu.R;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnBatDau;
    private Button btnXepHang;
    private Button btnHuongDan;
    private Button btnThoat;
    private ImageButton btnSound;
    private DatabaseHelper db;
    private List<HighScore> listHighScore;
    private HighScoreAdapter adapter;
    private boolean isPlaySound;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        initWidget();
        setupSound();
    }

    private void setupSound() {
        isPlaySound = getSoundRef();
        if(isPlaySound){
            btnSound.setImageResource(R.drawable.ic_sound);
            playSound(R.raw.background, true);
        } else {
            btnSound.setImageResource(R.drawable.ic_mute);
        }
    }

    private void initWidget() {
        btnBatDau = findViewById(R.id.btnBatDau);
        btnXepHang = findViewById(R.id.btnXepHang);
        btnHuongDan = findViewById(R.id.btnHuongDan);
        btnThoat = findViewById(R.id.btnThoat);
        btnSound = findViewById(R.id.btnSound);

        db = new DatabaseHelper(this);

        btnBatDau.setOnClickListener(this);
        btnXepHang.setOnClickListener(this);
        btnHuongDan.setOnClickListener(this);
        btnThoat.setOnClickListener(this);
        btnSound.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnBatDau:
                startActivity(new Intent(MainActivity.this, PlayActivity.class));
                break;
            case R.id.btnXepHang:
                showHighScoreDialog();
                break;
            case R.id.btnHuongDan:
                showHuongDanDialog();
                break;
            case R.id.btnThoat:
                finish();
                break;
            case R.id.btnSound:
                if(isPlaySound){
                    isPlaySound = false;
                    setSoundRef(isPlaySound);
                    if(mediaPlayer.isPlaying()){
                        mediaPlayer.stop();
                    }
                    btnSound.setImageResource(R.drawable.ic_mute);
                } else {
                    isPlaySound = true;
                    setSoundRef(isPlaySound);
                    playSound(R.raw.background, true);
                    btnSound.setImageResource(R.drawable.ic_sound);
                }
                break;
        }
    }

    private void showHighScoreDialog() {
        listHighScore = db.getHighScore();
        adapter = new HighScoreAdapter(this);
        adapter.setListHighScore(listHighScore);
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_high_score);
        RecyclerView rvHighScore = dialog.findViewById(R.id.rvHighScore);
        Button btnOk = dialog.findViewById(R.id.btnOk);

        rvHighScore.setHasFixedSize(true);
        rvHighScore.setLayoutManager(new LinearLayoutManager(this));
        rvHighScore.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.setCancelable(false);
        dialog.show();
    }

    private void showHuongDanDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_huongdan);
        Button btnOk = dialog.findViewById(R.id.btnOk);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.setCancelable(false);
        dialog.show();
    }

    private void playSound(int source, boolean looping){
        mediaPlayer = MediaPlayer.create(this, source);
        mediaPlayer.setLooping(looping);
        mediaPlayer.start();
    }

    private void setSoundRef(boolean isPlaySound){
        SharedPreferences soundRef = getSharedPreferences("soundRef", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = soundRef.edit();
        editor.putBoolean("sound", isPlaySound);
        editor.commit();
    }

    private boolean getSoundRef(){
        SharedPreferences soundRef = getSharedPreferences("soundRef", Context.MODE_PRIVATE);
        boolean b = soundRef.getBoolean("sound", true);
        return b;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mediaPlayer != null && mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupSound();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
    }
}
