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
import com.kinfoitsolutions.ebooks.ui.responsemodel.CategoryResponse.CategoryPayload;
import com.kinfoitsolutions.ebooks.ui.responsemodel.SearchCategoryResponse.SearchCategoryPayload;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List<CategoryPayload> categoryPayloadList;
    private Context context;
    private mBookCatgoryClickListner mBookCatgoryClickListner;

    public interface mBookCatgoryClickListner{
        void mCatBookClick(View v, int position);

    }




    public CategoryAdapter(List<CategoryPayload> categoryModelList, Context context, CategoryAdapter.mBookCatgoryClickListner mBookCatgoryClickListner) {
        this.categoryPayloadList = categoryModelList;
        this.context = context;
        this.mBookCatgoryClickListner = mBookCatgoryClickListner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_row,parent,false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        final CategoryPayload categoryModel = categoryPayloadList.get(position);

        try {
            Picasso.get().load(categoryModel.getImageUrl()).fit()
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.no_image)
                    .into(holder.image);
        } catch (Exception e) {
            e.printStackTrace();
            holder.image.setImageResource(R.drawable.no_image);
        }



        holder.titleCat.setText(categoryModel.getName());

        holder.qty.setText("( "+categoryModel.getBookCount()+" )" +" items");


        holder.categorybookClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBookCatgoryClickListner.mCatBookClick(view,position);

            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryPayloadList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;
        private BoldTextView titleCat;
        private RegularTextView qty;
        private CardView categorybookClick;

        public ViewHolder(@NonNull View view) {
            super(view);

            image = view.findViewById(R.id.imageCate);
            titleCat = view.findViewById(R.id.titleCat);
            qty = view.findViewById(R.id.qtye);
            categorybookClick = view.findViewById(R.id.categorybookClick);
        }
    }
}
