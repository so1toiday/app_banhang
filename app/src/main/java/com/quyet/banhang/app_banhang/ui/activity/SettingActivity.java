package com.quyet.banhang.app_banhang.ui.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.quyet.banhang.app_banhang.R;
import com.quyet.banhang.app_banhang.model.User;
import com.squareup.picasso.Picasso;

public class SettingActivity extends AppCompatActivity {
    private ImageView mImage;
    private DatabaseReference mReference;
    private static final int GALLERY_CODE = 999;
    private StorageReference mImageStore;
    FirebaseUser firebaseUser;
    EditText mName, mAddress, mPhone, mEmail, mBrithday;
    RadioButton mNam, mNu;
    String urlImage="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Anhxa();
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Chi tiết thông báo");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Picasso.with(SettingActivity.this).load(user.getImage()).into(mImage);
                urlImage=user.getImage();
                mName.setText(user.getName());
                mAddress.setText(user.getAddress());
                mPhone.setText(user.getPhone());
                mEmail.setText(firebaseUser.getEmail());
                mBrithday.setText(user.getBirthday());
                if(user.isSex()){
                    mNam.setChecked(true);
                }else {
                    mNu.setChecked(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void Anhxa() {
        mImage = findViewById(R.id.imUser);
        mName = findViewById(R.id.edName);
        mAddress = findViewById(R.id.edAddress);
        mPhone = findViewById(R.id.edPhone);
        mEmail = findViewById(R.id.edEmail);
        mBrithday = findViewById(R.id.edBirthday);
        mNam = findViewById(R.id.rdoNam);
        mNu = findViewById(R.id.rdoNu);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mReference = FirebaseDatabase.getInstance().getReference("user").child(firebaseUser.getUid());
        mReference.keepSynced(true);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK && data != null) {
            final Uri uri = data.getData();
            mImageStore = FirebaseStorage.getInstance().getReference().child("image_profile").child(firebaseUser.getUid());
            mImageStore.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mImageStore.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                mReference.child("image").setValue(task.getResult().toString());
                                mImage.setImageURI(uri);
                                Toast.makeText(SettingActivity.this, "Đã thay ảnh", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        }
    }

    public void clickEdit(View view) {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_CODE);
    }

    public void clickUpdate(View view) {
        User user=new User();
        user.setSex(mNam.isChecked());
        user.setAddress(mAddress.getText().toString());
        user.setBirthday(mBrithday.getText().toString());
        user.setPhone(mPhone.getText().toString());
        user.setName(mName.getText().toString());
        user.setEmail(mEmail.getText().toString());
        user.setImage(urlImage);
        try {
            mReference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(SettingActivity.this, "Lưu thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SettingActivity.this, "Lưu thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }catch(Exception e){
        }
    }
}
