package com.quyet.banhang.app_banhang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.quyet.banhang.app_banhang.R;
import com.quyet.banhang.app_banhang.model.CommentModel;
import com.squareup.picasso.Picasso;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.viewholder> {
    Context context;
    List<CommentModel> list;
    public  CommentAdapter(Context context,List<CommentModel> list){
        this.context=context;
        this.list=list;
    }
    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewholder holder, final int position) {
        CommentModel c=list.get(position);
        Picasso.with(context).load(c.getUser().getImage()).into(holder.mImage);
        holder.mName.setText(c.getUser().getName());
        holder.mComment.setText(c.getComment());
        holder.mDateComment.setText(c.getDate());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<CommentModel> comments) {
        this.list=comments;
        notifyDataSetChanged();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView mName,mComment;
        CircleImageView mImage;
        TextView mDateComment;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            mName=itemView.findViewById(R.id.tvTitleComment);
            mComment=itemView.findViewById(R.id.tvContentComment);
            mImage=itemView.findViewById(R.id.imComment);
            mDateComment=itemView.findViewById(R.id.tvDateComment);

        }
    }
}
