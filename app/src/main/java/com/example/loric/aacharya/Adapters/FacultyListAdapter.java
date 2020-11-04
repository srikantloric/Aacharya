package com.example.loric.aacharya.Adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.loric.aacharya.Models.FacultyListModel;
import com.example.loric.aacharya.R;

import java.util.List;

import static com.example.loric.aacharya.Models.FacultyListModel.ITEM_LAYOUT;
import static com.example.loric.aacharya.Models.FacultyListModel.TITLE_LAYOUT_FACULTY;

public class FacultyListAdapter extends RecyclerView.Adapter {


    List<FacultyListModel> facultyListModelList;

    public FacultyListAdapter(List<FacultyListModel> facultyListModelList) {
        this.facultyListModelList = facultyListModelList;
    }

    @Override
    public int getItemViewType(int position) {

        switch (facultyListModelList.get(position).getViewType()) {
            case 0:
                return TITLE_LAYOUT_FACULTY;
            case 1:
                return ITEM_LAYOUT;
            default:
                return -1;
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TITLE_LAYOUT_FACULTY:
                View Title = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_gallary_title_item_layout, parent, false);
                return new TitleLayout(Title);
            case ITEM_LAYOUT:
                View Item = LayoutInflater.from(parent.getContext()).inflate(R.layout.faculties_list_item_layout, parent, false);
                return new ImageItemLayout(Item);
            default:
                return null;
        }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (facultyListModelList.get(position).getViewType()) {
            case TITLE_LAYOUT_FACULTY:
                ((TitleLayout) holder).setTitle(facultyListModelList.get(position).getTitle());
                break;
            case ITEM_LAYOUT:
                ((ImageItemLayout) holder).setFacultyDetail(facultyListModelList.get(position).getImageUrl(), facultyListModelList.get(position).getImageTitle());
                break;
            default:
                return;

        }


    }

    @Override
    public int getItemCount() {
        return facultyListModelList.size();
    }

    class TitleLayout extends RecyclerView.ViewHolder {
        TextView title;

        public TitleLayout(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.event_title);
        }

        private void setTitle(String titleText) {
            title.setText(titleText);
        }

    }

    class ImageItemLayout extends RecyclerView.ViewHolder {


        ImageView facultyImage;
        TextView facultyName;

        public ImageItemLayout(@NonNull View itemView) {
            super(itemView);
            facultyImage = itemView.findViewById(R.id.faculty_image);
            facultyName = itemView.findViewById(R.id.faculty_detail_tv);
        }

        private void setFacultyDetail(String imageUrl, String name) {
            facultyName.setText(name);
            Glide.with(itemView.getContext()).load(imageUrl).into(facultyImage);
        }
    }

}

