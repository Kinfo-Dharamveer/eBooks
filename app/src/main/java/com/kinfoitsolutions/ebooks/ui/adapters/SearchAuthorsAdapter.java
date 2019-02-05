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
import com.kinfoitsolutions.ebooks.ui.responsemodel.SearchAuthors.SearchAuthorsPayload;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchAuthorsAdapter extends RecyclerView.Adapter<SearchAuthorsAdapter.MyViewHolder> {


    List<SearchAuthorsPayload> searchAuthorsPayloadList;
    Context context;
    private mSearchAuthorInterface mSearchAuthorInterface;

    public interface mSearchAuthorInterface{
         void mClickAuthorRow(View v, int pos,Integer id);
    }

    public SearchAuthorsAdapter(List<SearchAuthorsPayload> searchAuthorsPayloadList, Context context, SearchAuthorsAdapter.mSearchAuthorInterface mSearchAuthorInterface) {
        this.searchAuthorsPayloadList = searchAuthorsPayloadList;
        this.context = context;
        this.mSearchAuthorInterface = mSearchAuthorInterface;
    }

    @NonNull
    @Override
    public SearchAuthorsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.authors_row,parent,false);
        return new MyViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAuthorsAdapter.MyViewHolder holder, final int position) {

        final SearchAuthorsPayload searchAuthorsPayload = searchAuthorsPayloadList.get(position);

        holder.textAuthorName.setText(searchAuthorsPayload.getName());

        try {
            Picasso.get().load(searchAuthorsPayload.getImageUrl()).fit()
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

                mSearchAuthorInterface.mClickAuthorRow(view, position,searchAuthorsPayload.getId());
            }
        });
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
