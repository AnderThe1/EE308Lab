package com.example.bobing;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.util.concurrent.TimeUnit;

public class game extends AppCompatActivity {

    private View prebutton;
    private int prize[] = {32, 16, 8, 4, 2};
    private int Dice[] = {R.drawable.value1, R.drawable.value2, R.drawable.value3, R.drawable.value4, R.drawable.value5, R.drawable.value6};
    private int txv[] = {R.id.txv1, R.id.txv2, R.id.txv3, R.id.txv4, R.id.txv5, R.id.txv6};
    private int img[] = {R.id.img1, R.id.img2, R.id.img3, R.id.img4, R.id.img5, R.id.img6};
    private String award[] = {"一秀", "二举", "四进", "三红", "对堂", "四点红", "五子登科", "五红", "黑六勃", "遍地锦", "六杯红", "金花"};
    private int[] rollFace = {R.drawable.roll1, R.drawable.roll1, R.drawable.roll2, R.drawable.roll3, R.drawable.roll4, R.drawable.roll5, R.drawable.roll6, R.drawable.roll7, R.drawable.roll8, R.drawable.roll9};
    private String re = "";
    private int turn = 0, round = -1;
    private TextView txt;
    public MediaPlayer mp;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        prebutton = findViewById(R.id.back);
        prebutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });

    }

    public int num(){
        String t = "";
        try {
            FileInputStream f=openFileInput("data.txt");
            byte [] buffer=new  byte[f.available()];
            f.read(buffer);
            t = new String(buffer);
            f.close();
            return Integer.parseInt(t);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void Roll(View view) {

        int dices[] = new int[6];
        String val[] = new String[6];
        int record[] = new int[6];

        round++;

        mp = MediaPlayer.create(this, R.raw.sound_dice);
        sp = this.getSharedPreferences("config",MODE_PRIVATE);
        if(sp.getBoolean("sound",true))
            mp.start();

        for (int i = 0; i < dices.length; i++) {
            dices[i] = (int)(Math.random()*6);
            val[i] = "    " + (dices[i]+1) + "   ";
            record[dices[i]]++;
        }

        TextView textView;
        ImageView imageView;
        for (int i = 0; i < 6; i++){
            textView = findViewById(txv[i]);
            textView.setText(val[i]);
            imageView = findViewById(img[i]);
            imageView.setImageResource(Dice[dices[i]]);
        }

        int cnt = getAward(record);
        if (cnt != -1)
            prize[cnt] -= 1;

        Toast.makeText(getApplicationContext(), "NEXT Player " + ((turn%num())+1), Toast.LENGTH_SHORT).show();
    }

    public void Record(View view) {
        String rest = "一秀饼：" + prize[0] + "; 二举饼："+ prize[1] + "; 四进饼：" + prize[2] + "; 三红饼：" + prize[3] + "; 对堂饼：" + prize[4];
        if (re.isEmpty())
            Toast.makeText(getApplicationContext(), "No Record\n"+rest, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(), re+rest, Toast.LENGTH_SHORT).show();
    }

    public int getAward(int[] ar) {
        txt = findViewById(R.id.show);
        String r = "Round " + (round/num() + 1) + ": ";
        int m;

        re += r + "Player " + ((turn % num())+1);
        if (++turn == 2*(num()+1))
            turn -= num();
        if (turn > num()) {
            int ind = re.indexOf("\n");
            re = re.substring(ind+1);
        }

        if (ar[3] == 6)
            m = 10;
        else if (ar[0] == 6)
            m = 9;
        else if (ar[1] == 6)
            m = 8;
        else if (ar[3] == 5 && ar[0] == 1)
            m = 7;
        else if (ar[1] == 5)
            m = 6;
        else if (ar[3] == 4)
            if (ar[0] == 2)
                m = 11;
            else
                m = 5;
        else if ((ar[0]==1 && ar[1]==1 && ar[2]==1 && ar[3]==1 && ar[4]==1 && ar[5]==1) ||
                ((ar[0]==3? 3:0) + (ar[1]==3? 3:0) + (ar[2]==3? 3:0) + (ar[4]==3? 3:0) + (ar[5]==3? 3:0) == 6))
            m = 4;
        else if (ar[3] == 3)
            m = 3;
        else if (ar[0]==4 || ar[1]==4 || ar[2]==4 || ar[4]==4 || ar[5]==4)
            m = 2;
        else if (ar[3] == 2)
            m = 1;
        else if (ar[3] == 1)
            m = 0;
        else
            m = -1;

        if (m >= 0) {
            r += award[m];
            txt.setText(r);
            re +=  " won " + award[m] + "\n";
            if (m > 4)
                return 4;
            else
                return m;
        } else {
            txt.setText(r + "未中");
            re += " got Nothing\n";
        }
        return m;
    }

}