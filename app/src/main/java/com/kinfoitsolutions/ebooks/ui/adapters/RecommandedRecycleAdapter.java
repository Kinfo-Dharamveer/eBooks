package com.kinfoitsolutions.ebooks.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.kinfoitsolutions.ebooks.R;
import com.kinfoitsolutions.ebooks.ui.responsemodel.GetAllBooksResponse.BookPayload;
import com.squareup.picasso.Picasso;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RecommandedRecycleAdapter extends
        RecyclerView.Adapter<RecommandedRecycleAdapter.MyViewHolder> implements Filterable {

    private List<BookPayload> booksList;
    private Context context;
    private mClickListener mClickListener;
    private List<BookPayload> bookListFiltered;

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    bookListFiltered = booksList;
                }
                else {
                    List<BookPayload> filteredList = new ArrayList<>();

                    for (BookPayload row : booksList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getAuthorName().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    bookListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = bookListFiltered;
                return filterResults;


            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                bookListFiltered = (ArrayList<BookPayload>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public interface mClickListener {
        public void mClick(View v, int position);
    }

    public RecommandedRecycleAdapter(Context context, List<BookPayload> bookList, mClickListener listener) {
        this.booksList = bookList;
        this.context = context;
        this.mClickListener = listener;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {


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


    @NotNull
    @Override
    public RecommandedRecycleAdapter.MyViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recommanded_list, parent, false);

        return new RecommandedRecycleAdapter.MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final BookPayload book = booksList.get(position);

        holder.title.setText(book.getName());
        holder.rating.setText("2");
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


        holder.bookRowClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.mClick(view, position);
            }
        });

    }


    @Override
    public int getItemCount() {
        return booksList.size();

    }

}
