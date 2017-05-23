package com.philong.foody.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.philong.foody.R;

public class QuenMatKhauActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText mEmailEditTextQMK;
    private Button mGuiButton;
    private TextView mChuaLaThanhVienTextView;
    private FirebaseAuth mFirebaseAuth;

    public static Intent newIntent(Context context){
        Intent intent = new Intent(context, QuenMatKhauActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quen_mat_khau);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mFirebaseAuth = FirebaseAuth.getInstance();

        mEmailEditTextQMK = (EditText) findViewById(R.id.edtEmailQMK);
        mGuiButton = (Button) findViewById(R.id.btnGui);
        mChuaLaThanhVienTextView = (TextView) findViewById(R.id.txtThanhVien);

        mGuiButton.setOnClickListener(this);
        mChuaLaThanhVienTextView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id){
            case R.id.btnGui:
                String email = mEmailEditTextQMK.getText().toString();
                if(kiemTraEmail(email)){
                    mFirebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(QuenMatKhauActivity.this, R.string.khoi_phuc_email_thanh_cong, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(this, R.string.email_khong_hop_le, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.txtThanhVien:
                startActivity(DangKyActivity.newIntent(QuenMatKhauActivity.this));
                break;

        }
    }

    private boolean kiemTraEmail(String email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
