package com.example.bobing;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.bobing.databinding.ActivityMainBinding;

import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    public MediaPlayer mp;
    public boolean sound = true;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 播放背景音乐
        mp = MediaPlayer.create(this, R.raw.bg);
        // 循环播放
        mp.setLooping(true);
        sp = this.getSharedPreferences("config",MODE_PRIVATE);
        if(sp.getBoolean("music",true))
            mp.start();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }



    public void Click(View view) {
        final View loginForm = getLayoutInflater().inflate(R.layout.login, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.roll0)
                .setTitle("Please input the number of Players:")
                .setView(loginForm)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText ed = loginForm.findViewById(R.id.edit);
                        String num = ed.getText().toString().trim();
                        FileOutputStream f;
                        try {
                            f = openFileOutput("data.txt",MODE_PRIVATE);
                            f.write(num.getBytes());
                            f.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            Integer.parseInt(num);
                            Toast.makeText(getApplicationContext(), "Player:"+num, Toast.LENGTH_SHORT).show();
                            Log.e("TAG", "onClick: 点下确定按钮");
                            Intent intent=new Intent();
                            intent.setAction("android.intent.action.GAME");
                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            startActivity(intent);
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Please enter the correct format: integer", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.e("TAG", "onClick: 点下取下按钮");
                    }
                })
                .create().show();
        Log.e("abc", "onClick: ");
    }

}