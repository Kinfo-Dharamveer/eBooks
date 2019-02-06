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
import com.kinfoitsolutions.ebooks.ui.responsemodel.GetAllBooksResponse.Top50BooksPayload;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Top50BookListAdapter extends RecyclerView.Adapter<Top50BookListAdapter.MyViewHolder> {


    Context context;
    private List<Top50BooksPayload> top50BooksPayloadList;
    private mAllTop50BooksClickListener mAllTop50BooksClickListener;


    public interface mAllTop50BooksClickListener{
        void mClickTop50List(View v, int pos, String bookFile);
    }

    public Top50BookListAdapter(Context context, List<Top50BooksPayload> top50BooksPayloadList, Top50BookListAdapter.mAllTop50BooksClickListener mAllTop50BooksClickListener) {
        this.context = context;
        this.top50BooksPayloadList = top50BooksPayloadList;
        this.mAllTop50BooksClickListener = mAllTop50BooksClickListener;
    }

    @NonNull
    @Override
    public Top50BookListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book_list, parent, false);


        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull Top50BookListAdapter.MyViewHolder holder, final int position) {

        final Top50BooksPayload top50BooksPayload = top50BooksPayloadList.get(position);

        holder.title.setText(top50BooksPayload.getName());
        holder.rating.setText("3");
        holder.author_name.setText(top50BooksPayload.getAuthorName());


        try {
            Picasso.get().load(top50BooksPayload.getBookImage()).fit()
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
                mAllTop50BooksClickListener.mClickTop50List(view,position,top50BooksPayload.getBookFile());
            }
        });

    }

    @Override
    public int getItemCount() {
        return top50BooksPayloadList.size();
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
}
