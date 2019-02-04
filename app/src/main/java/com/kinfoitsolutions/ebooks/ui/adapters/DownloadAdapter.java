package com.kinfoitsolutions.ebooks.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.drivingschool.android.customviews.CustomTextView;
import com.kinfoitsolutions.ebooks.R;
import com.kinfoitsolutions.ebooks.ui.model.DownloadModelClass;
import com.kinfoitsolutions.ebooks.ui.model.GetAllBooksResponse.BookPayload;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.ViewHolder> {

    private List<BookPayload> downloadModelClassList;
    private Context context;
    private mDownloadListener mDownloadListener;

    public interface mDownloadListener{
        public void mPdfDownload(View v, int position,String pdfLink);
    }


    public DownloadAdapter(List<BookPayload> downloadModelClassList, Context context,mDownloadListener downloadListener) {
        this.downloadModelClassList = downloadModelClassList;
        this.context = context;
        this.mDownloadListener = downloadListener;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.download_row,parent,false);

        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        final BookPayload lists = downloadModelClassList.get(position);

        holder.title.setText(lists.getName());
        holder.author_name.setText(lists.getAuthorName());


        try {
            Picasso.get().load(lists.getBookImage()).fit()
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.no_image)
                    .into(holder.imageView);
        } catch (Exception e) {
            e.printStackTrace();
            holder.imageView.setImageResource(R.drawable.no_image);
        }



        holder.btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDownloadListener.mPdfDownload(view,position,lists.getBookFile());
            }
        });

    }

    @Override
    public int getItemCount() {
        return downloadModelClassList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title,author_name;
        ImageView imageView;
        CustomTextView btnDownload;



        public ViewHolder(@NonNull View view) {

            super(view);

            imageView = view.findViewById(R.id.image_down);


            title =  view.findViewById(R.id.book_title);
            author_name =  view.findViewById(R.id.author_name_down);
            btnDownload =  view.findViewById(R.id.btnDownload);


        }
    }
}
