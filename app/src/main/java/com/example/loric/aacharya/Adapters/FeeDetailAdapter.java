package com.example.loric.aacharya.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loric.aacharya.Models.FeeDetailModel;
import com.example.loric.aacharya.R;

import java.util.Date;
import java.util.List;

public class FeeDetailAdapter extends RecyclerView.Adapter<FeeDetailAdapter.ViewHolder> {

    List<FeeDetailModel> feeDetailModelList;

    public FeeDetailAdapter(List<FeeDetailModel> feeDetailModelList) {
        this.feeDetailModelList = feeDetailModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fee_detail_rv_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(feeDetailModelList.get(position).getPaidAmount(), feeDetailModelList.get(position).getDueAmount(), feeDetailModelList.get(position).getPaymentDate());

    }

    @Override
    public int getItemCount() {
        return feeDetailModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView paidAmount, dueAmount, paymentDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            paidAmount = itemView.findViewById(R.id.paid_amount);
            dueAmount = itemView.findViewById(R.id.due_amount);
            paymentDate = itemView.findViewById(R.id.payment_date);
        }

        private void setData(String paid, String due, Date dte) {
            paidAmount.setText(paid);
            dueAmount.setText(due);

        }

    }
}
