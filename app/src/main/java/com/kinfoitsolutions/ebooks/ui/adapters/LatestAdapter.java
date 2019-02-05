package com.kinfoitsolutions.ebooks.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.kinfoitsolutions.ebooks.R;
import com.kinfoitsolutions.ebooks.ui.responsemodel.latestBooks.LatestBooksPayload;
import com.squareup.picasso.Picasso;

import java.util.List;

public class LatestAdapter extends RecyclerView.Adapter<LatestAdapter.MyViewHolder> {

    private List<LatestBooksPayload> latestList;
    private Context context;


    public LatestAdapter(List<LatestBooksPayload> latestList, Context context) {
        this.latestList = latestList;
        this.context = context;
    }

    @NonNull
    @Override
    public LatestAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommanded_list,parent,false);
        return new MyViewHolder(mView);
    }


    @Override
    public void onBindViewHolder(@NonNull LatestAdapter.MyViewHolder holder, int position) {

        final LatestBooksPayload lists = latestList.get(position);

        holder.title.setText(lists.getName());
        holder.rating.setText("5");
        holder.author_name.setText(lists.getAuthorName());


        try {
            Picasso.get().load(lists.getBookImage()).fit()
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.no_image)
                    .into(holder.imageView);
        } catch (Exception e) {
            e.printStackTrace();
            holder.imageView.setImageResource(R.drawable.no_image);
        }




    }

    @Override
    public int getItemCount() {
        return latestList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, rating,author_name;
        ImageView imageView;
        LinearLayout bookRowClick;


        MyViewHolder(View view) {
            super(view);

            title =  view.findViewById(R.id.title);
            author_name =  view.findViewById(R.id.author_name);
            rating =  view.findViewById(R.id.rating);
            imageView = view.findViewById(R.id.image);
            bookRowClick = view.findViewById(R.id.bookRowClick);


        }
    }
}
