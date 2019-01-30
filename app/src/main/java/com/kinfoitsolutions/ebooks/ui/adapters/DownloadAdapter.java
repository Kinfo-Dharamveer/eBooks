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

import java.util.List;

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.ViewHolder> {

    private List<DownloadModelClass> downloadModelClassList;
    private Context context;
    private mDownloadListener mDownloadListener;

    public interface mDownloadListener{
        public void mClick(View v, int position);
    }


    public DownloadAdapter(List<DownloadModelClass> downloadModelClassList, Context context,mDownloadListener downloadListener) {
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

        final DownloadModelClass lists = downloadModelClassList.get(position);

        holder.title.setText(lists.getTitle());
        holder.author_name.setText(lists.getAuthor_name());
        holder.imageView.setImageResource(lists.getImage());


        holder.btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDownloadListener.mClick(view,position);
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
