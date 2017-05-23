package com.philong.foody.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.philong.foody.R;

import java.util.Arrays;
import java.util.List;

public class DangNhapActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, FirebaseAuth.AuthStateListener{

    private Button mDangNhapGoogleButton;
    private Button mDangNhapFacebookButton;
    private Button mDangNhapButton;
    private EditText mEmailEditTextDN, mMatKhauEditTextDN;
    private TextView mQuenMatKhauTextView, mDangKyTextView;
    private FirebaseAuth mFirebaseAuth;
    private GoogleApiClient mGoogleApiClient;
    private CallbackManager mCallbackManager;
    private LoginManager mLoginManager;
    private List<String> mPermisionFacebook;
    public static final int REQUEST_CODE_GOOGLE_SIGN_IN = 0;
    public static int KIEM_TRA_PROVIDER_DANG_NHAP  = 0;

    public static Intent newIntent(Context context){
        Intent intent = new Intent(context, DangNhapActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_dang_nhap);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseAuth.signOut();
        init();
        event();
        TaoClientDangNhapGoogle();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mFirebaseAuth.removeAuthStateListener(this);
    }

    public void init(){
        mDangNhapGoogleButton = (Button) findViewById(R.id.btnDangNhapGoogle);
        mDangNhapFacebookButton = (Button) findViewById(R.id.btnDangNhapFacebook);
        mQuenMatKhauTextView = (TextView) findViewById(R.id.txtQuenMatKhau);
        mDangKyTextView = (TextView) findViewById(R.id.txtDangKy);
        mEmailEditTextDN = (EditText) findViewById(R.id.edtEmailDN);
        mMatKhauEditTextDN = (EditText) findViewById(R.id.edtMatKhauDN);
        mDangNhapButton = (Button) findViewById(R.id.btnDangNhap);
        mPermisionFacebook = Arrays.asList("email", "public_profile");

    }

    public void event(){

        mDangNhapGoogleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DangNhapGoogle(mGoogleApiClient);
            }
        });

        mDangNhapFacebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DangNhapFacebook();
            }
        });

        mDangKyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(DangKyActivity.newIntent(DangNhapActivity.this));
            }
        });

        mQuenMatKhauTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(QuenMatKhauActivity.newIntent(DangNhapActivity.this));
            }
        });

        mDangNhapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DangNhap();
            }
        });

        mCallbackManager = CallbackManager.Factory.create();
        mLoginManager = LoginManager.getInstance();

    }

    private void DangNhap(){
        String email = mEmailEditTextDN.getText().toString();
        String matKhau = mMatKhauEditTextDN.getText().toString();
        mFirebaseAuth.signInWithEmailAndPassword(email, matKhau).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(DangNhapActivity.this, R.string.tai_khoan_khong_hop_le, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void DangNhapFacebook(){
        mLoginManager.logInWithReadPermissions(this, mPermisionFacebook);

        mLoginManager.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                KIEM_TRA_PROVIDER_DANG_NHAP = 2;
                String tokenId = loginResult.getAccessToken().getToken();
                DangNhapFirebase(tokenId);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    private void TaoClientDangNhapGoogle(){
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

    }

    private void DangNhapGoogle(GoogleApiClient googleApiClient){
        KIEM_TRA_PROVIDER_DANG_NHAP = 1;
        Intent i = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(i, REQUEST_CODE_GOOGLE_SIGN_IN);
    }

    private void DangNhapFirebase(String tokenId){
        if(KIEM_TRA_PROVIDER_DANG_NHAP == 1){
            AuthCredential authCredential = GoogleAuthProvider.getCredential(tokenId, null);
            mFirebaseAuth.signInWithCredential(authCredential);
        }else if(KIEM_TRA_PROVIDER_DANG_NHAP == 2){
            AuthCredential authCredential = FacebookAuthProvider.getCredential(tokenId);
            mFirebaseAuth.signInWithCredential(authCredential);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != Activity.RESULT_OK){
            return;
        }
        if(requestCode == REQUEST_CODE_GOOGLE_SIGN_IN){
            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            GoogleSignInAccount googleSignInAccount = googleSignInResult.getSignInAccount();
            String tokenId = googleSignInAccount.getIdToken();
            DangNhapFirebase(tokenId);
        }else{
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null){
            Toast.makeText(this, R.string.dang_nhap_thanh_cong, Toast.LENGTH_SHORT).show();
            startActivity(TrangChuActivity.newIntent(this));
        }
    }
}
