package com.kinfoitsolutions.ebooks.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.kinfoitsolutions.ebooks.R;
import com.kinfoitsolutions.ebooks.ui.customviews.BoldTextView;
import com.kinfoitsolutions.ebooks.ui.responsemodel.AuthorsBooksResponse.AuthorsPayload;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AuthorsAdapter extends RecyclerView.Adapter<AuthorsAdapter.MyViewHolder> {

    private List<AuthorsPayload> authorsModelList;
    private Context context;
    private mAuthorRowClickInterface mAuthorRowClickInterface;

    public interface mAuthorRowClickInterface{
        void mAuthorRowClick(View c, int Pos, Integer id);
    }

    public AuthorsAdapter(List<AuthorsPayload> authorsModelList, Context context, AuthorsAdapter.mAuthorRowClickInterface mAuthorRowClickInterface) {
        this.authorsModelList = authorsModelList;
        this.context = context;
        this.mAuthorRowClickInterface = mAuthorRowClickInterface;
    }

    @NonNull
    @Override
    public AuthorsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.authors_row,parent,false);
        return new MyViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull AuthorsAdapter.MyViewHolder holder, final int position) {

        final AuthorsPayload authors = authorsModelList.get(position);

        holder.textAuthorName.setText(authors.getName());


        try {
            Picasso.get().load(authors.getStatus()).fit()
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.no_image)
                    .into(holder.imageAuthor);
        } catch (Exception e) {
            e.printStackTrace();
            holder.imageAuthor.setImageResource(R.drawable.no_image);
        }

        holder.cardClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAuthorRowClickInterface.mAuthorRowClick(view, position,authors.getId());
            }
        });


    }

    @Override
    public int getItemCount() {
        return authorsModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        ImageView imageAuthor;
        BoldTextView textAuthorName;
        CardView cardClick;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageAuthor = itemView.findViewById(R.id.authorImage);
            textAuthorName = itemView.findViewById(R.id.authorName);
            cardClick = itemView.findViewById(R.id.authorRowClick);
        }
    }
}
