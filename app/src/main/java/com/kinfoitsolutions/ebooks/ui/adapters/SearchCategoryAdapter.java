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
import com.kinfoitsolutions.ebooks.ui.customviews.RegularTextView;
import com.kinfoitsolutions.ebooks.ui.responsemodel.SearchCategoryResponse.SearchCategoryPayload;
import com.kinfoitsolutions.ebooks.ui.responsemodel.SearchDataResponse.SearchDataPayload;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchCategoryAdapter extends RecyclerView.Adapter<SearchCategoryAdapter.MyHolder> {


    private List<SearchCategoryPayload> searchCategoryPayloadList;
    private Context context;
    private mSearchCatClickListener mSearchCatClickListener;

    public interface mSearchCatClickListener{
        public void searchBookCat(View v, int position);
    }


    public SearchCategoryAdapter(List<SearchCategoryPayload> searchBookPayloadList, Context context, mSearchCatClickListener mSearchCatClickListener) {
        this.searchCategoryPayloadList = searchBookPayloadList;
        this.context = context;
        this.mSearchCatClickListener = mSearchCatClickListener;
    }

    @NonNull
    @Override
    public SearchCategoryAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_row,parent,false);
        return new MyHolder(mView);

    }

    @Override
    public void onBindViewHolder(@NonNull SearchCategoryAdapter.MyHolder holder, final int position) {

        final SearchCategoryPayload searchCategoryPayload = searchCategoryPayloadList.get(position);

        try {
            Picasso.get().load(searchCategoryPayload.getImageUrl()).fit()
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.no_image)
                    .into(holder.image);
        } catch (Exception e) {
            e.printStackTrace();
            holder.image.setImageResource(R.drawable.no_image);
        }

        holder.titleCat.setText(searchCategoryPayload.getName());

        holder.qty.setText("( "+searchCategoryPayloadList.size()+" )" +" items");

        holder.categorybookClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSearchCatClickListener.searchBookCat(view,position);

            }
        });

    }

    @Override
    public int getItemCount() {
        return searchCategoryPayloadList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        private ImageView image;
        private BoldTextView titleCat;
        private RegularTextView qty;
        private CardView categorybookClick;

        public MyHolder(@NonNull View view) {
            super(view);


            image = view.findViewById(R.id.imageCate);
            titleCat = view.findViewById(R.id.titleCat);
            qty = view.findViewById(R.id.qtye);
            categorybookClick = view.findViewById(R.id.categorybookClick);



        }
    }
}
