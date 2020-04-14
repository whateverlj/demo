package com.example.other;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.annotation.BindPath;

@BindPath("other/other")
public class OtherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);
    }
}
