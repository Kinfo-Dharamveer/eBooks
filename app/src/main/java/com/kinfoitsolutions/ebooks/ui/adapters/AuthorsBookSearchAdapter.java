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
import com.kinfoitsolutions.ebooks.ui.responsemodel.AllSearchDataSuccess.AllSearchDataPayload;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AuthorsBookSearchAdapter extends RecyclerView.Adapter<AuthorsBookSearchAdapter.MyViewHolder> {

    private Context context;
    private List<AllSearchDataPayload> searchAuthorsPayloadList;

    public AuthorsBookSearchAdapter(Context context, List<AllSearchDataPayload> searchAuthorsPayloadList) {
        this.context = context;
        this.searchAuthorsPayloadList = searchAuthorsPayloadList;
    }

    @NonNull
    @Override
    public AuthorsBookSearchAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.authors_row,parent,false);
        return new MyViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull AuthorsBookSearchAdapter.MyViewHolder holder, int position) {


        final AllSearchDataPayload allSearchDataPayload = searchAuthorsPayloadList.get(position);

        holder.textAuthorName.setText(allSearchDataPayload.getName());


        try {
            Picasso.get().load(allSearchDataPayload.getBookImage()).fit()
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.no_image)
                    .into(holder.imageAuthor);
        } catch (Exception e) {
            e.printStackTrace();
            holder.imageAuthor.setImageResource(R.drawable.no_image);
        }

    }

    @Override
    public int getItemCount() {
        return searchAuthorsPayloadList.size();
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
