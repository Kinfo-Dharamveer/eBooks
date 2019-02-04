package com.kinfoitsolutions.ebooks.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.kinfoitsolutions.ebooks.R;
import com.kinfoitsolutions.ebooks.ui.responsemodel.GetAllBooksResponse.BookPayload;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BookListRecycleAdapter extends RecyclerView.Adapter<BookListRecycleAdapter.MyViewHolder> {

    Context context;
    private List<BookPayload> OfferList;


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


    public BookListRecycleAdapter(Context context, List<BookPayload> offerList) {
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

        final BookPayload book = OfferList.get(position);

        holder.title.setText(book.getName());
        holder.rating.setText("3");
        holder.author_name.setText(book.getAuthorName());


        try {
            Picasso.get().load(book.getBookImage()).fit()
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
        return OfferList.size();

    }

}



