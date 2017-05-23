package com.philong.foody.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.philong.foody.R;

public class DangKyActivity extends AppCompatActivity implements View.OnClickListener {


    private Button mDangKyButton;
    private EditText mEmailEditText, mMatKhauEditText, mNhapLaiMatKhauEditText;
    private FirebaseAuth mFirebaseAuth;
    private ProgressDialog mProgressDialog;

    public static Intent newIntent(Context context){
        Intent intent = new Intent(context, DangKyActivity.class);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mFirebaseAuth = FirebaseAuth.getInstance();

        mDangKyButton = (Button) findViewById(R.id.btnDangKy);
        mEmailEditText = (EditText) findViewById(R.id.edtEmailDK);
        mMatKhauEditText = (EditText) findViewById(R.id.edtMatKhauDK);
        mNhapLaiMatKhauEditText = (EditText) findViewById(R.id.edtNhapLaiMatKhauDK);
        mProgressDialog = new ProgressDialog(this);

        mDangKyButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mProgressDialog.setMessage(getString(R.string.xu_ly_dang_ky_dialog));
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        String email = mEmailEditText.getText().toString();
        String matKhau = mMatKhauEditText.getText().toString();
        String nhapLaiMK = mNhapLaiMatKhauEditText.getText().toString();
        if(email.trim().length() == 0) {
            Toast.makeText(this, R.string.nhap_email, Toast.LENGTH_SHORT).show();
        }else if(matKhau.trim().length() == 0){
            Toast.makeText(this, R.string.nhap_mat_khau, Toast.LENGTH_SHORT).show();
        }else if(!nhapLaiMK.equals(matKhau)){
            Toast.makeText(this, R.string.vui_long_nhap_lai_mat_khau, Toast.LENGTH_SHORT).show();
        }else{
            mFirebaseAuth.createUserWithEmailAndPassword(email, matKhau).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        mProgressDialog.dismiss();
                        Toast.makeText(DangKyActivity.this, R.string.dang_ky_thanh_cong, Toast.LENGTH_SHORT).show();
                    }else{
                        mProgressDialog.dismiss();
                        Toast.makeText(DangKyActivity.this, R.string.dang_ky_that_bai, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
