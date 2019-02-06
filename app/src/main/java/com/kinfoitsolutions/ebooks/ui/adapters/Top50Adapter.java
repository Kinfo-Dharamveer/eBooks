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
import com.kinfoitsolutions.ebooks.ui.responsemodel.GetAllBooksResponse.RecomendedBooksPayload;
import com.kinfoitsolutions.ebooks.ui.responsemodel.GetAllBooksResponse.Top50BooksPayload;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Top50Adapter extends RecyclerView.Adapter<Top50Adapter.MyViewHolder> {

    private List<Top50BooksPayload> top50BooksPayloadList;
    private Context context;
    private mTop50ClickListener mTop50ClickListener;

    public interface mTop50ClickListener{
        void mTop50Click(View c, int pos,String bookFile);
    }

    public Top50Adapter(List<Top50BooksPayload> top50BooksPayloadList, Context context, Top50Adapter.mTop50ClickListener mTop50ClickListener) {
        this.top50BooksPayloadList = top50BooksPayloadList;
        this.context = context;
        this.mTop50ClickListener = mTop50ClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recommanded_list, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        final Top50BooksPayload top50BooksPayload = top50BooksPayloadList.get(position);

        holder.title.setText(top50BooksPayload.getName());
        holder.rating.setText("2");
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


        holder.bookRowClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTop50ClickListener.mTop50Click(view, position,top50BooksPayload.getBookFile());
            }
        });

    }

    @Override
    public int getItemCount() {
        return top50BooksPayloadList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView title, rating, author_name;
        ImageView imageView;
        LinearLayout bookRowClick;


        MyViewHolder(View view) {
            super(view);

            title = view.findViewById(R.id.title);
            author_name = view.findViewById(R.id.author_name);
            rating = view.findViewById(R.id.rating);
            imageView = view.findViewById(R.id.image);
            bookRowClick = view.findViewById(R.id.bookRowClick);


        }
    }
}
