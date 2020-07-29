package com.quyet.banhang.app_banhang.ui.fragment;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.quyet.banhang.app_banhang.R;
import com.quyet.banhang.app_banhang.adapter.CommentAdapter;
import com.quyet.banhang.app_banhang.model.CommentModel;
import com.quyet.banhang.app_banhang.model.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewsProductFragment extends Fragment {
    RecyclerView mListComment;
    Button mButtonCommnent;
    DatabaseReference reference;
    String pid;
    FirebaseUser firebaseUser;
    LinearLayout layout;
    CommentAdapter adapter;

    public ReviewsProductFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle b = getArguments();
        pid = b.getString("id");
        return inflater.inflate(R.layout.fragment_reviews_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findView(view);
        try {


            reference.child("comments").child(pid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<CommentModel> comments = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        comments.add(snapshot.getValue(CommentModel.class));
                    }
                    if (comments.size() > 0) {
                        layout.setVisibility(View.GONE);
                        mListComment.setVisibility(View.VISIBLE);
                    } else {
                        layout.setVisibility(View.VISIBLE);
                        mListComment.setVisibility(View.GONE);
                    }
                    if (adapter == null) {
                        adapter = new CommentAdapter(getContext(), comments);
                        mListComment.setAdapter(adapter);
                        mListComment.setLayoutManager(new LinearLayoutManager(getContext()));
                    } else {
                        adapter.setList(comments);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            mButtonCommnent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (firebaseUser == null) {
                        Toast.makeText(getContext(), "Vui lòng đăng nhập để sử dụng chức năng này", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    final AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
                    View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_comment, null);
                    dialog.show();
                    dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    dialog.setContentView(view);
                    Button mButton = view.findViewById(R.id.btnComment);
                    final EditText mContent = view.findViewById(R.id.edCommnet);
                    mButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!mContent.getText().equals("")) {
                                reference.child("user").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        User user = dataSnapshot.getValue(User.class);
                                        user.setUID(dataSnapshot.getKey());
                                        CommentModel commnet = new CommentModel();
                                        commnet.setComment(mContent.getText().toString());
                                        commnet.setUser(user);
                                        Date d = new Date();
                                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                        String date = dateFormat.format(d);
                                        commnet.setDate(date);
                                        reference.child("comments").child(pid).push().setValue(commnet).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(getContext(), "Bình luận thành công", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(getContext(), "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }
                            dialog.dismiss();
                        }
                    });
                }
            });
        } catch (Exception e) {

        }
    }

    private void findView(View view) {
        mListComment = view.findViewById(R.id.reComment);
        mButtonCommnent = view.findViewById(R.id.btnComment);
        reference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        layout = view.findViewById(R.id.linear);
    }
}
