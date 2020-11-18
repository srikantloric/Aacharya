package com.example.loric.aacharya.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.loric.aacharya.Models.FChatMessageModel;
import com.example.loric.aacharya.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.example.loric.aacharya.Models.FChatMessageModel.INCOMING_IMAGE_LAYOUT;
import static com.example.loric.aacharya.Models.FChatMessageModel.INCOMING_MSG_LAYOUT;
import static com.example.loric.aacharya.Models.FChatMessageModel.OUTGOING_IMAGE_LAYOUT;
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
            case 2:
                return INCOMING_IMAGE_LAYOUT;
            case 3:
                return OUTGOING_IMAGE_LAYOUT;
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
            case INCOMING_IMAGE_LAYOUT:
                View incoming_image = LayoutInflater.from(parent.getContext()).inflate(R.layout.fchat_incoming_image_item_layout, parent, false);
                return new IncomingImageLayout(incoming_image);
            case OUTGOING_IMAGE_LAYOUT:
                View outgoing_image = LayoutInflater.from(parent.getContext()).inflate(R.layout.fchat_outgoing_image_item_layout, parent, false);
                return new OutgoingImageLayout(outgoing_image);
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
            case INCOMING_IMAGE_LAYOUT:
                ((IncomingImageLayout) holder).setIncomingImageData(fChatMessageModelList.get(position).getMessageText(), fChatMessageModelList.get(position).getDateTime());
                break;
            case OUTGOING_IMAGE_LAYOUT:
                ((OutgoingImageLayout) holder).setOutgoingImageData(fChatMessageModelList.get(position).getMessageText(), fChatMessageModelList.get(position).getDateTime());
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
            messageIncoming = itemView.findViewById(R.id.incoming_image_item);
            timestamp_incoming = itemView.findViewById(R.id.incoming_time_tv);
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
            messageOutgoing = itemView.findViewById(R.id.outgoing_image_item);
            timestamp_outgoing = itemView.findViewById(R.id.outgoing_image_time);
        }

        private void setOutgoingData(String message, Date timestamp) {
            messageOutgoing.setText(message);
            SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
            if (timestamp_outgoing != null && timestamp != null) {

                timestamp_outgoing.setText(df.format(timestamp));
            }
        }
    }


    class IncomingImageLayout extends RecyclerView.ViewHolder {

        ImageView image;
        TextView time;

        public IncomingImageLayout(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.incoming_image_item);
            time = itemView.findViewById(R.id.incoming_time_tv);
        }

        private void setIncomingImageData(String imageUrl, Date date) {
            Glide.with(itemView.getContext()).load(imageUrl).into(image);
            SimpleDateFormat df = new SimpleDateFormat("hh:mm a");

            if (date != null) {
                time.setText(df.format(date));
            }


        }
    }

    class OutgoingImageLayout extends RecyclerView.ViewHolder {
        ImageView image;
        TextView time;

        public OutgoingImageLayout(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.outgoing_image_item);
            time = itemView.findViewById(R.id.outgoing_image_time);
        }

        private void setOutgoingImageData(String imageUrl, Date dateTime) {
            Glide.with(itemView.getContext()).load(imageUrl).into(image);

            SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
            if (dateTime != null) {
                time.setText(df.format(dateTime));
            }
        }

    }
}
