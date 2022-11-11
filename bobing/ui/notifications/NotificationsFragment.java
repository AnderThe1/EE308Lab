package com.example.bobing.ui.notifications;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.bobing.MainActivity;
import com.example.bobing.R;
import com.example.bobing.databinding.FragmentNotificationsBinding;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private MediaPlayer mp;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final MainActivity mainActivity = (MainActivity) getActivity();
        mp = mainActivity.mp;
        sp = getContext().getSharedPreferences("config", Context.MODE_PRIVATE);
        Switch switch1 = root.findViewById(R.id.switch1);
        switch1.setChecked(sp.getBoolean("music",true));
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor = sp.edit();
                if(buttonView.isPressed()) {
                    if (isChecked) {
                        mp.start();
                        editor.putBoolean("music",true);
                        Toast.makeText(getContext(), "打开音乐", Toast.LENGTH_SHORT).show();
                    } else {
                        mp.pause();
                        mp.seekTo(0);
                        editor.putBoolean("music",false);
                        Toast.makeText(getContext(), "关闭音乐", Toast.LENGTH_SHORT).show();
                    }
                }
                editor.commit();
            }
        });
        Switch switch2 = root.findViewById(R.id.switch2);
        switch2.setChecked(sp.getBoolean("sound",true));
        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor = sp.edit();
                if(buttonView.isPressed()) {
                    if (isChecked) {
                        mainActivity.sound = true;
                        editor.putBoolean("sound",true);
                        Toast.makeText(getContext(), "打开音效", Toast.LENGTH_SHORT).show();
                    } else {
                        mainActivity.sound = false;
                        editor.putBoolean("sound",false);
                        Toast.makeText(getContext(), "关闭音效", Toast.LENGTH_SHORT).show();
                    }
                }
                editor.commit();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}