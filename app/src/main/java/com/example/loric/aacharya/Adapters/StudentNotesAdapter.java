package com.example.loric.aacharya.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.loric.aacharya.Models.StudentNotesModel;
import com.example.loric.aacharya.PDFViewerActivity;
import com.example.loric.aacharya.R;
import com.example.loric.aacharya.StudentsDashboard.RecievePaymentActivity;

import java.util.List;

public class StudentNotesAdapter extends RecyclerView.Adapter<StudentNotesAdapter.ViewHolder> {

    Context context;
    List<StudentNotesModel> studentNotesModelList;

    public StudentNotesAdapter(Context context, List<StudentNotesModel> studentNotesModelList) {
        this.context = context;
        this.studentNotesModelList = studentNotesModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.students_notes_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.setData(studentNotesModelList.get(position).getBookTitle(),
                studentNotesModelList.get(position).getBookDesc(),
                studentNotesModelList.get(position).getBookPrice(),
                studentNotesModelList.get(position).isPurchases(),
                studentNotesModelList.get(position).getBookImage(),
                studentNotesModelList.get(position).getDocId(),
                studentNotesModelList.get(position).getBookUrl());
    }

    @Override
    public int getItemCount() {
        return studentNotesModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView bookTitle, bookDesc, bookPrice;
        Button butNow, readBtn;
        ImageView bookImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bookDesc = itemView.findViewById(R.id.book_des);
            bookTitle = itemView.findViewById(R.id.book_title);
            bookPrice = itemView.findViewById(R.id.book_price);
            bookImage = itemView.findViewById(R.id.book_image);
            butNow = itemView.findViewById(R.id.buy_btn);
            readBtn = itemView.findViewById(R.id.read_btn);
        }

        private void setData(String title, String des, String price, boolean isPurchased, String image,String docId,String book) {
            bookDesc.setText(des);
            String rs = context.getResources().getString(R.string.Rs);
            bookPrice.setText(rs + " " + price + "/-");
            bookTitle.setText(title);
            Glide.with(itemView.getContext()).load(image).into(bookImage);

            if (isPurchased) {
                readBtn.setVisibility(View.VISIBLE);
                butNow.setVisibility(View.GONE);
            } else {
                readBtn.setVisibility(View.GONE);
                butNow.setVisibility(View.VISIBLE);
            }

            butNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, RecievePaymentActivity.class);
                    intent.putExtra("BOOK_PRICE", price);
                    intent.putExtra("BOOK_TITLE", title);
                    intent.putExtra("DOC_ID", docId);
                    context.startActivity(intent);
                }
            });

            readBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PDFViewerActivity.class);
                    intent.putExtra("BOOK_URL", book);
                    context.startActivity(intent);
                }
            });
        }
    }
}
