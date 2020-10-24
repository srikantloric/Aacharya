package com.example.loric.aacharya.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loric.aacharya.Models.FChatMessageModel;
import com.example.loric.aacharya.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.example.loric.aacharya.Models.FChatMessageModel.INCOMING_MSG_LAYOUT;
import static com.example.loric.aacharya.Models.FChatMessageModel.OUTGOING_MSG_LAYOUT;

public class FChatMessagingAdapter extends RecyclerView.Adapter {

    List<FChatMessageModel> fChatMessageModelList;

    public FChatMessagingAdapter(List<FChatMessageModel> fChatMessageModelList) {
        this.fChatMessageModelList = fChatMessageModelList;
    }

    @Override
    public int getItemViewType(int position) {
        switch (fChatMessageModelList.get(position).getViewType()) {
            case 0:
                return INCOMING_MSG_LAYOUT;
            case 1:
                return OUTGOING_MSG_LAYOUT;
            default:
                return -1;
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case INCOMING_MSG_LAYOUT:
                View incoming_msg = LayoutInflater.from(parent.getContext()).inflate(R.layout.fchat_incoming_msg_item_layout, parent, false);
                return new IncomingMessageLayout(incoming_msg);
            case OUTGOING_MSG_LAYOUT:
                View outgoing_msg = LayoutInflater.from(parent.getContext()).inflate(R.layout.fchat_outgoing_msg_item_layout, parent, false);
                return new OutgoingMessageLayout(outgoing_msg);
            default:
                return null;

        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (fChatMessageModelList.get(position).getViewType()) {
            case INCOMING_MSG_LAYOUT:
                ((IncomingMessageLayout) holder).setIncomingData(fChatMessageModelList.get(position).getMessageText(), fChatMessageModelList.get(position).getDateTime());
                break;
            case OUTGOING_MSG_LAYOUT:
                ((OutgoingMessageLayout) holder).setOutgoingData(fChatMessageModelList.get(position).getMessageText(), fChatMessageModelList.get(position).getDateTime());
                break;
            default:
                return;
        }

    }

    @Override
    public int getItemCount() {
        return fChatMessageModelList.size();
    }

    class IncomingMessageLayout extends RecyclerView.ViewHolder {
        TextView messageIncoming, timestamp_incoming;

        public IncomingMessageLayout(@NonNull View itemView) {
            super(itemView);
            messageIncoming = itemView.findViewById(R.id.incoming_msg_tv);
            timestamp_incoming = itemView.findViewById(R.id.time_tv);
        }

        private void setIncomingData(String message, Date timestamp) {
            messageIncoming.setText(message);
            SimpleDateFormat df = new SimpleDateFormat("hh:mm a");

            if (timestamp != null) {
                timestamp_incoming.setText(df.format(timestamp));
            }
        }
    }

    class OutgoingMessageLayout extends RecyclerView.ViewHolder {
        TextView messageOutgoing, timestamp_outgoing;

        public OutgoingMessageLayout(@NonNull View itemView) {
            super(itemView);
            messageOutgoing = itemView.findViewById(R.id.outgoing_msg_tv);
            timestamp_outgoing = itemView.findViewById(R.id.time_tv_outgoing);
        }

        private void setOutgoingData(String message, Date timestamp) {
            messageOutgoing.setText(message);
            SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
            if (timestamp_outgoing != null && timestamp != null) {

                timestamp_outgoing.setText(df.format(timestamp));
            }
        }
    }
}
