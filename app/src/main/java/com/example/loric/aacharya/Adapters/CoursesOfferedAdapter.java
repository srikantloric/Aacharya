package com.example.loric.aacharya.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loric.aacharya.Models.CoursesOfferedModel;
import com.example.loric.aacharya.R;

import java.util.List;

public class CoursesOfferedAdapter extends RecyclerView.Adapter<CoursesOfferedAdapter.ViewHolder> {
    List<CoursesOfferedModel> coursesOfferedModelList;

    public CoursesOfferedAdapter(List<CoursesOfferedModel> coursesOfferedModelList) {
        this.coursesOfferedModelList = coursesOfferedModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.courses_offered_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setDetail(coursesOfferedModelList.get(position).getCourseName(), coursesOfferedModelList.get(position).getCourseDetail(), coursesOfferedModelList.get(position).getCourseDetailExtra());
    }

    @Override
    public int getItemCount() {
        return coursesOfferedModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView courseName, courseDetail, courseDetailExtra;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            courseName = itemView.findViewById(R.id.course_name);
            courseDetail = itemView.findViewById(R.id.course_detail);
            courseDetailExtra = itemView.findViewById(R.id.course_detail_extra);
        }

        private void setDetail(String Name, String Detail, String DetailExtra) {
            courseName.setText(Name);
            courseDetail.setText(Detail);
            courseDetailExtra.setText(DetailExtra);

            if (Detail.toUpperCase().trim().equals("NULL")){
                courseDetail.setVisibility(View.GONE);
            }
            if (DetailExtra.toUpperCase().trim().equals("NULL")){
                courseDetailExtra.setVisibility(View.GONE);
            }
        }
    }
}
