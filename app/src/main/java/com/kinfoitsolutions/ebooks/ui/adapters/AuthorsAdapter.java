package com.kinfoitsolutions.ebooks.ui.adapters;

import android.app.SearchManager;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;
import com.drivingschool.android.customviews.CustomTextView;
import com.kinfoitsolutions.ebooks.R;
import com.kinfoitsolutions.ebooks.ui.customviews.BoldTextView;
import com.kinfoitsolutions.ebooks.ui.model.AuthorsModel;

import java.util.List;

public class AuthorsAdapter extends RecyclerView.Adapter<AuthorsAdapter.MyViewHolder> {

    private List<AuthorsModel> authorsModelList;
    private Context context;



    public AuthorsAdapter(List<AuthorsModel> authorsModelList, Context context) {
        this.authorsModelList = authorsModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public AuthorsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.authors_row,parent,false);
        return new MyViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull AuthorsAdapter.MyViewHolder holder, int position) {

        final AuthorsModel authorsModel = authorsModelList.get(position);

        holder.imageAuthor.setImageResource(authorsModel.getAuhtorimage());
        holder.textAuthorName.setText(authorsModel.getAuthorName());
    }

    @Override
    public int getItemCount() {
        return authorsModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {





        ImageView imageAuthor;
        BoldTextView textAuthorName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageAuthor = itemView.findViewById(R.id.imageAuthor);
            textAuthorName = itemView.findViewById(R.id.textAuthorName);
        }
    }
}
