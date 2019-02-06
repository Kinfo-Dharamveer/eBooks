package com.kinfoitsolutions.ebooks.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.kinfoitsolutions.ebooks.R;
import com.kinfoitsolutions.ebooks.ui.responsemodel.GetAllBooksResponse.RecomendedBooksPayload;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BookListRecycleAdapter extends RecyclerView.Adapter<BookListRecycleAdapter.MyViewHolder> {

    Context context;
    private List<RecomendedBooksPayload> OfferList;
    private mAllBooksClickListener mAllBooksClickListener;

    public interface mAllBooksClickListener{
        void mClick(View v, int pos, String bookFile);
    }

    public BookListRecycleAdapter(Context context, List<RecomendedBooksPayload> offerList, BookListRecycleAdapter.mAllBooksClickListener mAllBooksClickListener) {
        this.context = context;
        OfferList = offerList;
        this.mAllBooksClickListener = mAllBooksClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView title, rating,author_name;
        ImageView imageView;
        LinearLayout item_click;


        public MyViewHolder(View view) {
            super(view);

            title =  view.findViewById(R.id.title);
            author_name = (TextView) view.findViewById(R.id.author_name);
            rating = (TextView) view.findViewById(R.id.rating);
            imageView = (ImageView)view.findViewById(R.id.image);
            item_click = view.findViewById(R.id.item_click);


        }

    }



    @Override
    public BookListRecycleAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book_list, parent, false);


        return new BookListRecycleAdapter.MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder( final MyViewHolder holder, final int position) {

        final RecomendedBooksPayload book = OfferList.get(position);

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

        holder.item_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAllBooksClickListener.mClick(view,position,book.getBookFile());
            }
        });


    }


    @Override
    public int getItemCount() {
        return OfferList.size();

    }

}



