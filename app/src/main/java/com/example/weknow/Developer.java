package com.example.weknow;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class Developer extends AppCompatActivity {

    TextView link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer);
//
//        link = findViewById(R.id.space);
//        link.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
