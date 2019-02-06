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
import com.kinfoitsolutions.ebooks.ui.responsemodel.AllSearchDataSuccess.AllSearchDataPayload;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DownloadSearchAdapter extends RecyclerView.Adapter<DownloadSearchAdapter.MyViewHolder> {


    private List<AllSearchDataPayload> allSearchDataPayloadList;
    private Context context;
    private mDownloadSearchListener mDownloadListener;

    public interface mDownloadSearchListener{
         void mPdfSearchDownload(View v, int position, String pdfLink);
    }

    public DownloadSearchAdapter(List<AllSearchDataPayload> allSearchDataPayloadList, Context context, mDownloadSearchListener mDownloadListener) {
        this.allSearchDataPayloadList = allSearchDataPayloadList;
        this.context = context;
        this.mDownloadListener = mDownloadListener;
    }

    @NonNull
    @Override
    public DownloadSearchAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.download_row,parent,false);

        return new MyViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull DownloadSearchAdapter.MyViewHolder holder, final int position) {

        final AllSearchDataPayload lists = allSearchDataPayloadList.get(position);

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
                mDownloadListener.mPdfSearchDownload(view,position,lists.getBookFile());
            }
        });

    }

    @Override
    public int getItemCount() {
        return allSearchDataPayloadList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title,author_name;
        ImageView imageView;
        CustomTextView btnDownload;


        public MyViewHolder(@NonNull View view) {
            super(view);

            imageView = view.findViewById(R.id.image_down);
            title =  view.findViewById(R.id.book_title);
            author_name =  view.findViewById(R.id.author_name_down);
            btnDownload =  view.findViewById(R.id.btnDownload);


        }
    }
}
