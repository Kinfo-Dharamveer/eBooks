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
import com.kinfoitsolutions.ebooks.ui.model.RecommandedModelClass;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RecommandedRecycleAdapter extends RecyclerView.Adapter<RecommandedRecycleAdapter.MyViewHolder> {

    private List<RecommandedModelClass> OfferList;
    private Context context;
    private mClickListener mClickListener;



    public interface mClickListener{
        public void mClick(View v, int position);
    }

    public RecommandedRecycleAdapter(Context context, List<RecommandedModelClass> offerList,mClickListener listener) {
        this.OfferList = offerList;
        this.context = context;
        this.mClickListener = listener;
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


    @NotNull
    @Override
    public RecommandedRecycleAdapter.MyViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recommanded_list, parent, false);


        return new RecommandedRecycleAdapter.MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final RecommandedModelClass lists = OfferList.get(position);
        holder.title.setText(lists.getTitle());
        holder.rating.setText(lists.getRating());
        holder.author_name.setText(lists.getAuthor_name());
        holder.imageView.setImageResource(lists.getImage());

        holder.bookRowClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.mClick(view,position);
            }
        });

    }


    @Override
    public int getItemCount() {
        return OfferList.size();

    }

}
