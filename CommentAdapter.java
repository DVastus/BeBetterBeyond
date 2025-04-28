package com.firstapp.bbb;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<Comment> comments;

    public CommentAdapter(List<Comment> comments) {
        this.comments = comments;
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timestampText;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.commentMessageText);
            timestampText = itemView.findViewById(R.id.commentTimestampText);
        }
    }

@NonNull
@Override
public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
}

@Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position){
        Comment comment = comments.get(position);
    Log.d("CommentAdapter", "Binding comment: " + comment.getMessage());
    holder.messageText.setText(comment.getMessage());

        // Format timestamp
       String time = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
               .format(new Date(comment.getTimestamp()));
       holder.timestampText.setText(time);

       Log.d("CommentAdapter", "Comment loaded: " + comment.getMessage());
}

@Override
    public int getItemCount() {
        return comments.size();
}





}
