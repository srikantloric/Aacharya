package com.example.loric.aacharya.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loric.aacharya.Models.VacancyModel;
import com.example.loric.aacharya.PDFViewerActivity;
import com.example.loric.aacharya.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class VacancyAdapter extends RecyclerView.Adapter<VacancyAdapter.ViewHolder> {

    List<VacancyModel> vacancyModelList;
    Context context;

    public VacancyAdapter(List<VacancyModel> vacancyModelList, Context context) {
        this.vacancyModelList = vacancyModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vacancies_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(vacancyModelList.get(position).getVacancyTitle(),
                vacancyModelList.get(position).getVacancyDes(),
                vacancyModelList.get(position).getVacancyPostDate(),
                vacancyModelList.get(position).getVacancyPdf(),
                vacancyModelList.get(position).isOver());

    }

    @Override
    public int getItemCount() {
        return vacancyModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView vacancyTitle, vacancyDes, vacancyPostDate;
        ImageButton pdfBtn;
        CardView hider;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            vacancyTitle = itemView.findViewById(R.id.vacancy_title);
            vacancyDes = itemView.findViewById(R.id.vacancy_des);
            vacancyPostDate = itemView.findViewById(R.id.posted_date);
            pdfBtn = itemView.findViewById(R.id.pdf_btn);
            hider = itemView.findViewById(R.id.hider_layout);
        }

        private void setData(String title, String des, Date post, String pdfUrl, boolean isOver) {
            if (isOver) {
                hider.setVisibility(View.VISIBLE);
            } else {
                hider.setVisibility(View.GONE);
            }
            vacancyTitle.setText(title);
            vacancyDes.setText(des);


            SimpleDateFormat fmtOut = new SimpleDateFormat("dd-MM-yyyy");
            vacancyPostDate.setText("Posted Date : "+fmtOut.format(post));

            pdfBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PDFViewerActivity.class);
                    intent.putExtra("HEADER_TITLE", "Vacancy Details");
                    intent.putExtra("BOOK_URL", pdfUrl);
                    context.startActivity(intent);

                }
            });
        }
    }
}
