package com.philong.foody.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.philong.foody.R;

public class TrangChuActivity extends AppCompatActivity {


    public static Intent newIntent(Context context){
        Intent intent = new Intent(context, TrangChuActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_chu);
    }

}
