package com.kinfoitsolutions.ebooks.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.kinfoitsolutions.ebooks.R;
import com.kinfoitsolutions.ebooks.ui.model.RecommandedModelClass;

import java.util.List;

public class BookListRecycleAdapter extends RecyclerView.Adapter<BookListRecycleAdapter.MyViewHolder> {

    Context context;
    private List<RecommandedModelClass> OfferList;


    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView title, rating,author_name;
        ImageView imageView;


        public MyViewHolder(View view) {
            super(view);

            title =  view.findViewById(R.id.title);
            author_name = (TextView) view.findViewById(R.id.author_name);
            rating = (TextView) view.findViewById(R.id.rating);
            imageView = (ImageView)view.findViewById(R.id.image);




        }

    }


    public BookListRecycleAdapter(Context context, List<RecommandedModelClass> offerList) {
        this.OfferList = offerList;
        this.context = context;
    }

    @Override
    public BookListRecycleAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book_list, parent, false);


        return new BookListRecycleAdapter.MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder( final MyViewHolder holder, final int position) {
        final RecommandedModelClass lists = OfferList.get(position);
        holder.title.setText(lists.getTitle());
        holder.rating.setText(lists.getRating());
        holder.author_name.setText(lists.getAuthor_name());
        holder.imageView.setImageResource(lists.getImage());



    }


    @Override
    public int getItemCount() {
        return OfferList.size();

    }

}



