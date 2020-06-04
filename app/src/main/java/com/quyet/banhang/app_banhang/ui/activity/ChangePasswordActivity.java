package com.quyet.banhang.app_banhang.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.quyet.banhang.app_banhang.R;
import com.quyet.banhang.app_banhang.function.Validate;

public class ChangePasswordActivity extends AppCompatActivity {
    EditText mPassword, mConfirmPassword, mPasswordOld, mEmail;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Đổi mật khẩu");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        initView();

    }

    private void initView() {
        mPassword = findViewById(R.id.edPassword);
        mConfirmPassword = findViewById(R.id.edConfirmPassword);
        user = FirebaseAuth.getInstance().getCurrentUser();
        mPasswordOld = findViewById(R.id.edPasswordOld);
        mEmail = findViewById(R.id.edEmail);
    }

    public void clickChangPassword(View view) {
        Validate v = new Validate();
        if (user == null) {
            Toast.makeText(this, "Vui lòng đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!v.checkNotEmpty(mPassword, mConfirmPassword)) {
            Toast.makeText(this, "Mật khẩu không được để trống", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!v.checkLengthPassword(mPassword.getText().toString())) {
            Toast.makeText(this, "Độ dài mật khẩu phải trên 6 kí tự", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!v.checkEmail(mEmail.getText().toString())) {
            Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        AuthCredential credential= EmailAuthProvider.getCredential(mEmail.getText().toString(),mPasswordOld.getText().toString());

        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    if (mPassword.getText().toString().equals(mConfirmPassword.getText().toString())) {
                        user.updatePassword(mPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ChangePasswordActivity.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ChangePasswordActivity.this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(ChangePasswordActivity.this, "Mật khẩu xác nhận chưa đúng", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }
}
