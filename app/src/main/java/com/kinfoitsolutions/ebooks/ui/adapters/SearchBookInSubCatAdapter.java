package com.kinfoitsolutions.ebooks.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.kinfoitsolutions.ebooks.R;
import com.kinfoitsolutions.ebooks.ui.customviews.BoldTextView;
import com.kinfoitsolutions.ebooks.ui.responsemodel.AllSearchDataSuccess.AllSearchDataPayload;
import com.kinfoitsolutions.ebooks.ui.responsemodel.FilterResponse.FilterBooksPayload;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchBookInSubCatAdapter extends RecyclerView.Adapter<SearchBookInSubCatAdapter.MyViewHolder> {

    private Context context;
    private List<AllSearchDataPayload> searchAuthorsPayloadList;


    public SearchBookInSubCatAdapter(Context context, List<AllSearchDataPayload> searchAuthorsPayloadList) {
        this.context = context;
        this.searchAuthorsPayloadList = searchAuthorsPayloadList;
    }

    @NonNull
    @Override
    public SearchBookInSubCatAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_row,parent,false);
        return new MyViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchBookInSubCatAdapter.MyViewHolder holder, int position) {

        final AllSearchDataPayload allSearchDataPayload = searchAuthorsPayloadList.get(position);

        try {
            Picasso.get().load(allSearchDataPayload.getBookImage()).fit()
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.no_image)
                    .into(holder.image);
        } catch (Exception e) {
            e.printStackTrace();
            holder.image.setImageResource(R.drawable.no_image);
        }



        holder.titleCat.setText(allSearchDataPayload.getName());
    }

    @Override
    public int getItemCount() {
        return searchAuthorsPayloadList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;
        private BoldTextView titleCat;


        public MyViewHolder(@NonNull View view) {
            super(view);

            image = view.findViewById(R.id.imageCate);
            titleCat = view.findViewById(R.id.titleCat);
        }
    }
}
