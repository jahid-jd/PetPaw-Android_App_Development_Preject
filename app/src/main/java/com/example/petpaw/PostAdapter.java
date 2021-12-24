package com.example.petpaw;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private Context context;
    private List<CreatePost> postList;

    public PostAdapter(Context context, List<CreatePost> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.post_layout, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        CreatePost createPost = postList.get(position);
        holder.nameTextView.setText(createPost.getUsername());
        holder.dateTextView.setText(createPost.getTime());
        holder.descriptionTextView.setText(createPost.getDescription());
        Picasso.with(context).load(createPost.getImageUri()).placeholder(R.drawable.default_animal).fit().centerInside().into(holder.photoImageView);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, dateTextView, descriptionTextView;
        ImageView photoImageView;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            photoImageView = itemView.findViewById(R.id.photoImageView);

        }
    }
}
