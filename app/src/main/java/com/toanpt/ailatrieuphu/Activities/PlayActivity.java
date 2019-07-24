package com.toanpt.ailatrieuphu.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.toanpt.ailatrieuphu.Adapter.LeftAdapter;
import com.toanpt.ailatrieuphu.Database.DatabaseHelper;
import com.toanpt.ailatrieuphu.Model.Question;
import com.toanpt.ailatrieuphu.R;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.models.BarModel;

import java.util.ArrayList;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlayActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton btnHelp5050, btnHelpAudience, btnHelpCall, btnHelpStop, btnSound;
    private TextView tvCoin, tvTimer, tvQuestionNum, tvQuestion;
    private Button btnCaseA, btnCaseB, btnCaseC, btnCaseD;
    private ListView lvLeft;
    private LeftAdapter adapter;
    private ProgressBar progressBar;
    private DrawerLayout dlPlay;
    private Animation animation;
    private DatabaseHelper database;
    private Question question;
    private Handler handler;
    private CountDown countDown;
    private int time = 30, level = 1, truecase;
    private boolean isHelp5050, isRunning, isPlaySound;
    private int highScore = 0;
    private MediaPlayer mediaPlayer;
    private ArrayList<String> list;
    private LinearLayout linearLayout, linearLayout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initWidgets();
        setupListView();
        setupStart();
    }

    private void initWidgets() {
        btnHelp5050 = findViewById(R.id.btnHelp5050);
        btnHelpAudience = findViewById(R.id.btnHelpAudience);
        btnHelpCall = findViewById(R.id.btnHelpCall);
        btnHelpStop = findViewById(R.id.btnHelpStop);
        btnSound = findViewById(R.id.btnSound);
        tvCoin = findViewById(R.id.tvCoin);
        tvTimer = findViewById(R.id.tvTimer);
        tvQuestionNum = findViewById(R.id.tvQuestionNum);
        tvQuestion = findViewById(R.id.tvQuestion);
        btnCaseA = findViewById(R.id.btnCaseA);
        btnCaseB = findViewById(R.id.btnCaseB);
        btnCaseC = findViewById(R.id.btnCaseC);
        btnCaseD = findViewById(R.id.btnCaseD);
        dlPlay = findViewById(R.id.dlPlay);
        progressBar = findViewById(R.id.progressBar);
        lvLeft = findViewById(R.id.lvLeft);

        btnHelp5050.setOnClickListener(this);
        btnHelpAudience.setOnClickListener(this);
        btnHelpCall.setOnClickListener(this);
        btnHelpStop.setOnClickListener(this);
        btnSound.setOnClickListener(this);
        btnCaseA.setOnClickListener(this);
        btnCaseB.setOnClickListener(this);
        btnCaseC.setOnClickListener(this);
        btnCaseD.setOnClickListener(this);

        adapter = new LeftAdapter(this);
        animation = AnimationUtils.loadAnimation(this, R.anim.anim_answer_true);

        database = new DatabaseHelper(this);
        countDown = new CountDown();
        handler = new Handler();
    }


    private void setupListView() {
        list = new ArrayList<>();
        list.add("15.  150.000.000");
        list.add("14.  85.000.000");
        list.add("13.  60.000.000");
        list.add("12.  40.000.000");
        list.add("11.  30.000.000");
        list.add("10.  22.000.000");
        list.add("9.  14.000.000");
        list.add("8.  10.000.000");
        list.add("7.  6.000.000");
        list.add("6.  3.000.000");
        list.add("5.  2.000.000");
        list.add("4.  1.000.000");
        list.add("3.  600.000");
        list.add("2.  400.000");
        list.add("1.  200.000");

        adapter.setList(list);
        lvLeft.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }


    private void setupStart() {
        isPlaySound = getSoundRef();
        if (isPlaySound) {
            btnSound.setImageResource(R.drawable.ic_sound);
            playSound(R.raw.ready, false);
        } else {
            btnSound.setImageResource(R.drawable.ic_mute);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
                tvTimer.setVisibility(View.VISIBLE);
                tvQuestionNum.setVisibility(View.VISIBLE);
                tvQuestion.setVisibility(View.VISIBLE);
                btnCaseA.setVisibility(View.VISIBLE);
                btnCaseB.setVisibility(View.VISIBLE);
                btnCaseC.setVisibility(View.VISIBLE);
                btnCaseD.setVisibility(View.VISIBLE);
                setQuestion(level);
            }
        }, 6000);
    }

    private boolean getSoundRef() {
        SharedPreferences soundRef = getSharedPreferences("soundRef", Context.MODE_PRIVATE);
        boolean b = soundRef.getBoolean("sound", true);
        return b;
    }

    private void setSoundRef(boolean isPlaySound) {
        SharedPreferences soundRef = getSharedPreferences("soundRef", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = soundRef.edit();
        editor.putBoolean("sound", isPlaySound);
        editor.commit();
    }

    @Override
    public void onClick(final View view) {
        if (view.getId() == R.id.btnCaseA || view.getId() == R.id.btnCaseB || view.getId() == R.id.btnCaseC || view.getId() == R.id.btnCaseD) {
            view.setBackgroundResource(R.drawable.bg_button_selected);
            progressBar.setVisibility(View.INVISIBLE);
            setEnableButton(false);
            handler.removeCallbacks(countDown);

            switch (view.getId()) {
                case R.id.btnCaseA:
                    if (isPlaySound) {
                        playSound(R.raw.ans_a, false);
                    }
                    break;
                case R.id.btnCaseB:
                    if (isPlaySound) {
                        playSound(R.raw.ans_b, false);
                    }
                    break;
                case R.id.btnCaseC:
                    if (isPlaySound) {
                        playSound(R.raw.ans_c, false);
                    }
                    break;
                case R.id.btnCaseD:
                    if (isPlaySound) {
                        playSound(R.raw.ans_d, false);
                    }
                    break;
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (view.getId() == truecase) {
                        switch (truecase) {
                            case R.id.btnCaseA:
                                btnCaseA.startAnimation(animation);
                                btnCaseA.setBackgroundResource(R.drawable.bg_button_answer_true);
                                if (isPlaySound) {
                                    playSound(R.raw.true_a, false);
                                }
                                break;
                            case R.id.btnCaseB:
                                btnCaseB.startAnimation(animation);
                                btnCaseB.setBackgroundResource(R.drawable.bg_button_answer_true);
                                if (isPlaySound) {
                                    playSound(R.raw.true_b, false);
                                }
                                break;
                            case R.id.btnCaseC:
                                btnCaseC.startAnimation(animation);
                                btnCaseC.setBackgroundResource(R.drawable.bg_button_answer_true);
                                if (isPlaySound) {
                                    playSound(R.raw.true_c, false);
                                }
                                break;
                            case R.id.btnCaseD:
                                btnCaseD.startAnimation(animation);
                                btnCaseD.setBackgroundResource(R.drawable.bg_button_answer_true);
                                if (isPlaySound) {
                                    playSound(R.raw.true_d, false);
                                }
                                break;
                        }
                        setCoin(level);
                        level++;

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (level < 16) {
                                    setQuestion(level);
                                } else {
                                    finishGame();
                                }
                            }
                        }, 4000);
                    } else {
                        switch (truecase) {
                            case R.id.btnCaseA:
                                btnCaseA.startAnimation(animation);
                                btnCaseA.setBackgroundResource(R.drawable.bg_button_answer_false);
                                if (isPlaySound) {
                                    playSound(R.raw.false_a, false);
                                }
                                break;
                            case R.id.btnCaseB:
                                btnCaseB.startAnimation(animation);
                                btnCaseB.setBackgroundResource(R.drawable.bg_button_answer_false);
                                if (isPlaySound) {
                                    playSound(R.raw.false_b, false);
                                }
                                break;
                            case R.id.btnCaseC:
                                btnCaseC.startAnimation(animation);
                                btnCaseC.setBackgroundResource(R.drawable.bg_button_answer_false);
                                if (isPlaySound) {
                                    playSound(R.raw.false_c, false);
                                }
                                break;
                            case R.id.btnCaseD:
                                btnCaseD.startAnimation(animation);
                                btnCaseD.setBackgroundResource(R.drawable.bg_button_answer_false);
                                if (isPlaySound) {
                                    playSound(R.raw.false_d, false);
                                }
                                break;
                        }
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (isPlaySound) {
                                    playSound(R.raw.lose, false);
                                }
                                finishGame();
                            }
                        }, 4000);
                    }
                }
            }, 5000);

        }

        if (view.getId() == R.id.btnHelp5050) {
            handler.removeCallbacks(countDown);
            progressBar.setVisibility(View.INVISIBLE);
            btnHelp5050.setEnabled(false);
            isHelp5050 = true;
            btnHelp5050.setImageResource(R.drawable.ic_help_5050_x);

            if (isPlaySound) {
                playSound(R.raw.help_5050, false);
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    help5050();
                    countDown.run();
                }
            }, 3000);
        }

        if (view.getId() == R.id.btnHelpAudience) {
            handler.removeCallbacks(countDown);
            progressBar.setVisibility(View.INVISIBLE);
            btnHelpAudience.setImageResource(R.drawable.ic_help_audience_x);
            btnHelpAudience.setEnabled(false);

            if (isPlaySound) {
                playSound(R.raw.help_audience, false);
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    helpAudience();
                }
            }, 5000);
        }

        if (view.getId() == R.id.btnHelpCall) {
            handler.removeCallbacks(countDown);
            progressBar.setVisibility(View.INVISIBLE);
            btnHelpCall.setImageResource(R.drawable.ic_help_call_x);
            btnHelpCall.setEnabled(false);

            if (isPlaySound) {
                playSound(R.raw.help_call, false);
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    helpCall();

                }
            }, 5000);
        }

        if (view.getId() == R.id.btnHelpStop) {
            handler.removeCallbacks(countDown);
            progressBar.setVisibility(View.INVISIBLE);
            btnHelpStop.setEnabled(false);

            if (isPlaySound) {
                playSound(R.raw.help_stop, false);
            }

            helpStop();
        }

        if (view.getId() == R.id.btnSound) {
            if (isPlaySound) {
                btnSound.setImageResource(R.drawable.ic_mute);
                isPlaySound = false;
                setSoundRef(isPlaySound);
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
            } else {
                btnSound.setImageResource(R.drawable.ic_sound);
                isPlaySound = true;
                setSoundRef(isPlaySound);
            }
        }
    }

    private void setEnableButton(boolean b) {
        btnCaseA.setEnabled(b);
        btnCaseB.setEnabled(b);
        btnCaseC.setEnabled(b);
        btnCaseD.setEnabled(b);
    }

    private void setButtonDefaultBackground() {
        btnCaseA.setBackgroundResource(R.drawable.bg_button_play);
        btnCaseB.setBackgroundResource(R.drawable.bg_button_play);
        btnCaseC.setBackgroundResource(R.drawable.bg_button_play);
        btnCaseD.setBackgroundResource(R.drawable.bg_button_play);
    }

    private void setQuestion(final int level) {
        question = database.getQuestion(level);
        Log.d("AAA", "setQuestion: " + question.getTrueCase());
        tvQuestionNum.setText("Câu hỏi " + level);
        tvQuestion.setText(question.getQuestion());
        btnCaseA.setText("A. " + question.getCaseA());
        btnCaseB.setText("B. " + question.getCaseB());
        btnCaseC.setText("C. " + question.getCaseC());
        btnCaseD.setText("D. " + question.getCaseD());
        setupTrueCase(question);
        setEnableButton(true);
        isHelp5050 = false;
        isRunning = true;
        setButtonDefaultBackground();
        progressBar.setVisibility(View.VISIBLE);

        linearLayout = (LinearLayout) lvLeft.getChildAt(15 - level);
        linearLayout.setBackgroundResource(R.drawable.bg_highlight);
        if (level > 1) {
            linearLayout2 = (LinearLayout) lvLeft.getChildAt(15 - level + 1);
            linearLayout2.setBackgroundResource(R.drawable.bg_left);
        }


        if (isPlaySound) {
            playSound(soundQuestion[level - 1], false);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (level == 5 || level == 10 || level == 15) {
                        playSound(R.raw.important, false);
                    }
                }
            }, 2000);
        }

        time = 30;
        handler.postDelayed(countDown, 1000);
    }

    private void setCoin(int level) {
        switch (level) {
            case 1:
                tvCoin.setText("200.000");
                break;
            case 2:
                tvCoin.setText("400.000");
                break;
            case 3:
                tvCoin.setText("600.000");
                break;
            case 4:
                tvCoin.setText("1000.000");
                break;
            case 5:
                tvCoin.setText("2000.000");
                break;
            case 6:
                tvCoin.setText("3000.000");
                break;
            case 7:
                tvCoin.setText("6000.000");
                break;
            case 8:
                tvCoin.setText("10.000.000");
                break;
            case 9:
                tvCoin.setText("14.000.000");
                break;
            case 10:
                tvCoin.setText("22.000.000");
                break;
            case 11:
                tvCoin.setText("30.000.000");
                break;
            case 12:
                tvCoin.setText("40.000.000");
                break;
            case 13:
                tvCoin.setText("60.000.000");
                break;
            case 14:
                tvCoin.setText("85.000.000");
                break;
            case 15:
                tvCoin.setText("150.000.000");
                break;
        }
    }

    private void setupTrueCase(Question question) {
        switch (question.getTrueCase()) {
            case 1:
                truecase = R.id.btnCaseA;
                break;
            case 2:
                truecase = R.id.btnCaseB;
                break;
            case 3:
                truecase = R.id.btnCaseC;
                break;
            case 4:
                truecase = R.id.btnCaseD;
                break;
        }
    }

    private void help5050() {
        if (isPlaySound) {
            playSound(R.raw.s5050, false);
        }
        switch (truecase) {
            case R.id.btnCaseA:
                btnCaseB.setEnabled(false);
                btnCaseB.setText("");
                btnCaseC.setEnabled(false);
                btnCaseC.setText("");
                break;
            case R.id.btnCaseB:
                btnCaseA.setEnabled(false);
                btnCaseA.setText("");
                btnCaseD.setEnabled(false);
                btnCaseD.setText("");
                break;
            case R.id.btnCaseC:
                btnCaseD.setEnabled(false);
                btnCaseD.setText("");
                btnCaseA.setEnabled(false);
                btnCaseA.setText("");
                break;
            case R.id.btnCaseD:
                btnCaseB.setEnabled(false);
                btnCaseB.setText("");
                btnCaseC.setEnabled(false);
                btnCaseC.setText("");
                break;
        }
    }

    private void helpAudience() {
        final Dialog dialog = new Dialog(this);
        dialog.setTitle("Hỏi ý kiến khán giả");
        dialog.setContentView(R.layout.dialog_help_audience);

        Button btnOk = dialog.findViewById(R.id.btnOk);
        final BarChart bcAudience = dialog.findViewById(R.id.bcAudience);

        if (isPlaySound) {
            playSound(R.raw.bg_audience, false);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Random random = new Random();
                int rdTrue = random.nextInt(10) + 50;
                int rdFalse1 = random.nextInt(10) + 5;
                final int rdFalse2 = random.nextInt(10) + 5;
                final int rdFalse3 = 100 - rdTrue - rdFalse1 - rdFalse2;
                if (!btnHelp5050.isEnabled() && isHelp5050) {
                    rdTrue = random.nextInt(10) + 60;
                    rdFalse1 = 100 - rdTrue;
                    switch (truecase) {
                        case R.id.btnCaseA:
                            bcAudience.addBar(new BarModel("A", rdTrue, 0xFF563456));
                            bcAudience.addBar(new BarModel("B", 0, 0xFF563456));
                            bcAudience.addBar(new BarModel("C", 0, 0xFF563456));
                            bcAudience.addBar(new BarModel("D", rdFalse1, 0xFF563456));
                            bcAudience.startAnimation();
                            break;
                        case R.id.btnCaseB:
                            bcAudience.addBar(new BarModel("A", 0, 0xFF563456));
                            bcAudience.addBar(new BarModel("B", rdTrue, 0xFF563456));
                            bcAudience.addBar(new BarModel("C", rdFalse1, 0xFF563456));
                            bcAudience.addBar(new BarModel("D", 0, 0xFF563456));
                            bcAudience.startAnimation();
                            break;
                        case R.id.btnCaseC:
                            bcAudience.addBar(new BarModel("A", 0, 0xFF563456));
                            bcAudience.addBar(new BarModel("B", rdFalse1, 0xFF563456));
                            bcAudience.addBar(new BarModel("C", rdTrue, 0xFF563456));
                            bcAudience.addBar(new BarModel("D", 0, 0xFF563456));
                            bcAudience.startAnimation();
                            break;
                        case R.id.btnCaseD:
                            bcAudience.addBar(new BarModel("A", rdFalse1, 0xFF563456));
                            bcAudience.addBar(new BarModel("B", 0, 0xFF563456));
                            bcAudience.addBar(new BarModel("C", 0, 0xFF563456));
                            bcAudience.addBar(new BarModel("D", rdTrue, 0xFF563456));
                            bcAudience.startAnimation();
                            break;
                    }
                } else {
                    switch (truecase) {
                        case R.id.btnCaseA:
                            bcAudience.addBar(new BarModel("A", rdTrue, 0xFF563456));
                            bcAudience.addBar(new BarModel("B", rdFalse1, 0xFF563456));
                            bcAudience.addBar(new BarModel("C", rdFalse2, 0xFF563456));
                            bcAudience.addBar(new BarModel("D", rdFalse3, 0xFF563456));
                            bcAudience.startAnimation();
                            break;
                        case R.id.btnCaseB:
                            bcAudience.addBar(new BarModel("A", rdFalse1, 0xFF563456));
                            bcAudience.addBar(new BarModel("B", rdTrue, 0xFF563456));
                            bcAudience.addBar(new BarModel("C", rdFalse2, 0xFF563456));
                            bcAudience.addBar(new BarModel("D", rdFalse3, 0xFF563456));
                            bcAudience.startAnimation();
                            break;
                        case R.id.btnCaseC:
                            bcAudience.addBar(new BarModel("A", rdFalse1, 0xFF563456));
                            bcAudience.addBar(new BarModel("B", rdFalse2, 0xFF563456));
                            bcAudience.addBar(new BarModel("C", rdTrue, 0xFF563456));
                            bcAudience.addBar(new BarModel("D", rdFalse3, 0xFF563456));
                            bcAudience.startAnimation();
                            break;
                        case R.id.btnCaseD:
                            bcAudience.addBar(new BarModel("A", rdFalse1, 0xFF563456));
                            bcAudience.addBar(new BarModel("B", rdFalse2, 0xFF563456));
                            bcAudience.addBar(new BarModel("C", rdFalse3, 0xFF563456));
                            bcAudience.addBar(new BarModel("D", rdTrue, 0xFF563456));
                            bcAudience.startAnimation();
                            break;
                    }
                }
            }
        }, 6000);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                countDown.run();
                progressBar.setVisibility(View.VISIBLE);
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    private void helpCall() {
        final Dialog dialog = new Dialog(this);
        dialog.setTitle("Gọi điện thoại cho người thân");
        dialog.setContentView(R.layout.dialog_help_call);

        CircleImageView imgHelpCall1 = dialog.findViewById(R.id.imgHelpCall1);
        CircleImageView imgHelpCall2 = dialog.findViewById(R.id.imgHelpCall2);
        CircleImageView imgHelpCall3 = dialog.findViewById(R.id.imgHelpCall3);
        CircleImageView imgHelpCall4 = dialog.findViewById(R.id.imgHelpCall4);

        imgHelpCall1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAnswerDialog();
                dialog.dismiss();
            }
        });

        imgHelpCall2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAnswerDialog();
                dialog.dismiss();
            }
        });

        imgHelpCall3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAnswerDialog();
                dialog.dismiss();
            }
        });

        imgHelpCall4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAnswerDialog();
                dialog.dismiss();
            }
        });

        dialog.setCancelable(false);
        dialog.show();
    }

    private void showAnswerDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setTitle("Gọi điện thoại cho người thân");
        dialog.setContentView(R.layout.dialog_help_call_answer);

        TextView tvAnswer = dialog.findViewById(R.id.tvAnswer);
        Button btnOk = dialog.findViewById(R.id.btnOk);

        switch (truecase) {
            case R.id.btnCaseA:
                if (isPlaySound) {
                    playSound(R.raw.help_a, false);
                }
                tvAnswer.setText("Đáp án A");
                break;
            case R.id.btnCaseB:
                if (isPlaySound) {
                    playSound(R.raw.help_b, false);
                }
                tvAnswer.setText("Đáp án B");
                break;
            case R.id.btnCaseC:
                if (isPlaySound) {
                    playSound(R.raw.help_c, false);
                }
                tvAnswer.setText("Đáp án C");
                break;
            case R.id.btnCaseD:
                if (isPlaySound) {
                    playSound(R.raw.help_d, false);
                }
                tvAnswer.setText("Đáp án D");
                break;
        }

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                countDown.run();
            }
        });

        dialog.setCancelable(false);
        dialog.show();
    }

    private void helpStop() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Dừng cuộc chơi");
        builder.setMessage("Bạn chắc chắn muốn dừng cuộc chơi tại đây?");
        builder.setPositiveButton("Chắc chắn", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finishGame();
                    }
                }, 4000);

            }
        });

        builder.setNegativeButton("Nghĩ lại", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                countDown.run();
                progressBar.setVisibility(View.VISIBLE);
                btnHelpStop.setEnabled(true);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    private void finishGame() {
        setEnableButton(false);
        isRunning = false;
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_finish_game);

        TextView tvTitle = dialog.findViewById(R.id.tvTitle);
        TextView tvFinishCoin = dialog.findViewById(R.id.tvFinishCoin);
        Button btnOk = dialog.findViewById(R.id.btnOk);
        Button btnSave = dialog.findViewById(R.id.btnSave);

        if (level == 16) {
            tvTitle.setText("Chúc mừng bạn đã xuất sắc vượt qua 15 câu hỏi của chương trình.");
            tvFinishCoin.setText("150.000.000 VNĐ");
            highScore = 150000000;
        } else {
            if (!btnHelpStop.isEnabled()) {
                tvTitle.setText("Bạn đã lựa chọn dừng cuộc chơi tại câu hỏi " + level);
                switch (level) {
                    case 1:
                        highScore = 0;
                        tvFinishCoin.setText("0 VNĐ");
                        break;
                    case 2:
                        highScore = 200000;
                        tvFinishCoin.setText("200.000 VNĐ");
                        break;
                    case 3:
                        highScore = 400000;
                        tvFinishCoin.setText("400.000 VNĐ");
                        break;
                    case 4:
                        highScore = 600000;
                        tvFinishCoin.setText("600.000 VNĐ");
                        break;
                    case 5:
                        highScore = 1000000;
                        tvFinishCoin.setText("1.000.000 VNĐ");
                        break;
                    case 6:
                        highScore = 2000000;
                        tvFinishCoin.setText("2.000.000 VNĐ");
                        break;
                    case 7:
                        highScore = 3000000;
                        tvFinishCoin.setText("3.000.000 VNĐ");
                        break;
                    case 8:
                        highScore = 6000000;
                        tvFinishCoin.setText("6.000.000 VNĐ");
                        break;
                    case 9:
                        highScore = 10000000;
                        tvFinishCoin.setText("10.000.000 VNĐ");
                        break;
                    case 10:
                        highScore = 14000000;
                        tvFinishCoin.setText("14.000.000 VNĐ");
                        break;
                    case 11:
                        highScore = 22000000;
                        tvFinishCoin.setText("22.000.000 VNĐ");
                        break;
                    case 12:
                        highScore = 30000000;
                        tvFinishCoin.setText("30.000.000 VNĐ");
                        break;
                    case 13:
                        highScore = 40000000;
                        tvFinishCoin.setText("40.000.000 VNĐ");
                        break;
                    case 14:
                        highScore = 60000000;
                        tvFinishCoin.setText("60.000.000 VNĐ");
                        break;
                    case 15:
                        highScore = 85000000;
                        tvFinishCoin.setText("85.000.000 VNĐ");
                        break;
                }
            } else {
                tvTitle.setText("Bạn đã trả lời sai câu hỏi " + level);
                if (level <= 5) {
                    highScore = 0;
                    tvFinishCoin.setText("0 VNĐ");
                } else if (level > 5 && level <= 10) {
                    highScore = 2000000;
                    tvFinishCoin.setText("2.000.000 VNĐ");
                } else if (level > 10 && level < 16) {
                    highScore = 22000000;
                    tvFinishCoin.setText("22.000.000 VNĐ");
                }
            }
            if (isPlaySound) {
                playSound(R.raw.finish, false);
            }
        }

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                saveHighScore();
            }
        });

        dialog.setCancelable(false);
        dialog.show();
    }

    private void saveHighScore() {
        final Dialog dialog = new Dialog(this);
        dialog.setTitle("Lưu điểm");
        dialog.setContentView(R.layout.dialog_save);

        final TextInputEditText edtName = dialog.findViewById(R.id.edtName);
        Button btnOk = dialog.findViewById(R.id.btnOk);


        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edtName.getText().toString();
                if (name.equals("")) {
                    Toast.makeText(PlayActivity.this, "Vui lòng nhập tên người chơi!", Toast.LENGTH_SHORT).show();
                } else {
                    database.saveHighScore(name, highScore);
                    dialog.dismiss();
                    finish();
                }
            }
        });

        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        if (dlPlay.isDrawerOpen(GravityCompat.START)) {
            dlPlay.closeDrawer(GravityCompat.START);
        } else {
            if (isRunning) {
                handler.removeCallbacks(countDown);
                progressBar.setVisibility(View.INVISIBLE);
                helpStop();
            } else {
                finish();
            }
        }

    }

    @Override
    protected void onPause() {
        handler.removeCallbacks(countDown);
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (isRunning) {
            countDown.run();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRunning = false;
        handler.removeCallbacks(countDown);
    }

    private class CountDown implements Runnable {
        @Override
        public void run() {
            handleCountDown();
        }
    }

    private void handleCountDown() {
        time--;
        tvTimer.setText(String.valueOf(time));
        handler.postDelayed(countDown, 1000);
        if (time == 0) {
            handler.removeCallbacks(countDown);
            progressBar.setVisibility(View.INVISIBLE);
            finishGame();
        }
    }

    private void playSound(int source, boolean looping) {
        mediaPlayer = MediaPlayer.create(this, source);
        mediaPlayer.setLooping(looping);
        mediaPlayer.start();
    }

    private static final int[] soundQuestion = {R.raw.ques01, R.raw.ques02, R.raw.ques03, R.raw.ques04, R.raw.ques05, R.raw.ques06, R.raw.ques07, R.raw.ques08, R.raw.ques09, R.raw.ques10, R.raw.ques11, R.raw.ques12, R.raw.ques13, R.raw.ques14, R.raw.ques15};
}
