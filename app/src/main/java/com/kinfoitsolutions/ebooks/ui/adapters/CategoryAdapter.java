package com.kinfoitsolutions.ebooks.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.kinfoitsolutions.ebooks.R;
import com.kinfoitsolutions.ebooks.ui.customviews.BoldTextView;
import com.kinfoitsolutions.ebooks.ui.customviews.RegularTextView;
import com.kinfoitsolutions.ebooks.ui.model.CategoryModel;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List<CategoryModel> categoryModelList;
    private Context context;

    public CategoryAdapter(List<CategoryModel> categoryModelList, Context context) {
        this.categoryModelList = categoryModelList;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_row,parent,false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final CategoryModel categoryModel = categoryModelList.get(position);

        holder.image.setImageResource(categoryModel.getImage());
        holder.titleCat.setText(categoryModel.getTitle());
        holder.qty.setText("( "+categoryModel.getQty()+" )" +" items");
    }

    @Override
    public int getItemCount() {
        return categoryModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;
        private BoldTextView titleCat;
        private RegularTextView qty;

        public ViewHolder(@NonNull View view) {
            super(view);

            image = view.findViewById(R.id.imageCate);
            titleCat = view.findViewById(R.id.titleCat);
            qty = view.findViewById(R.id.qtye);
        }
    }
}
