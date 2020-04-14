package com.example.login;

import android.os.Bundle;
import android.view.View;

import com.example.annotation.BindPath;
import com.example.router.Router;

import androidx.appcompat.app.AppCompatActivity;
@BindPath("login/login")
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void other(View view) {
        Router.getInstance().jnmpActivity("other/other",null);
    }
}
