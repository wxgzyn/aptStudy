package com.luck.wxg.testapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import com.luck.wxg.annotation.DIActivity;
import com.luck.wxg.annotation.DIView;

@DIActivity
public class TestActivity extends AppCompatActivity {

    @DIView(R.id.text)
    public TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        if (textView != null) {
            textView.setText("映射成功");
        }
    }
}
