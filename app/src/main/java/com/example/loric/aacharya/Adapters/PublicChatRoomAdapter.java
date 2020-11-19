package com.example.loric.aacharya.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.loric.aacharya.EventGallery.PhotoFullScreenActivity;
import com.example.loric.aacharya.Models.ChatRoomModel;
import com.example.loric.aacharya.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.loric.aacharya.Models.FChatMessageModel.INCOMING_IMAGE_LAYOUT;
import static com.example.loric.aacharya.Models.FChatMessageModel.INCOMING_MSG_LAYOUT;
import static com.example.loric.aacharya.Models.FChatMessageModel.OUTGOING_IMAGE_LAYOUT;
import static com.example.loric.aacharya.Models.FChatMessageModel.OUTGOING_MSG_LAYOUT;

public class PublicChatRoomAdapter extends RecyclerView.Adapter {

    List<ChatRoomModel> chatRoomModelList;

    public PublicChatRoomAdapter(List<ChatRoomModel> chatRoomModelList) {
        this.chatRoomModelList = chatRoomModelList;
    }

    @Override
    public int getItemViewType(int position) {
        switch (chatRoomModelList.get(position).getViewType()) {
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
                View incoming_msg = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_room_incoming_message_item_layout, parent, false);
                return new IncomingMessageLayout(incoming_msg);
            case OUTGOING_MSG_LAYOUT:
                View outgoing_msg = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_room_outgoing_message_item_layout, parent, false);
                return new OutgoingMessageLayout(outgoing_msg);
            case INCOMING_IMAGE_LAYOUT:
                View incoming_image = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_room_incoming_image_item_layout, parent, false);
                return new IncomingImageLayout(incoming_image);
            case OUTGOING_IMAGE_LAYOUT:
                View outgoing_image = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_room_outgoing_image_item_layout, parent, false);
                return new OutgoingImageLayout(outgoing_image);
            default:
                return null;

        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (chatRoomModelList.get(position).getViewType()) {
            case INCOMING_MSG_LAYOUT:
                ((IncomingMessageLayout) holder).setIncomingData(chatRoomModelList.get(position).getMessageText(),
                        chatRoomModelList.get(position).getDateTime(),
                        chatRoomModelList.get(position).getSenderName(),
                        chatRoomModelList.get(position).getSenderImage());
                break;
            case OUTGOING_MSG_LAYOUT:
                ((OutgoingMessageLayout) holder).setOutgoingData(chatRoomModelList.get(position).getMessageText(),
                        chatRoomModelList.get(position).getDateTime(),
                        chatRoomModelList.get(position).getSenderName(),
                        chatRoomModelList.get(position).getSenderImage()
                );
                break;
            case INCOMING_IMAGE_LAYOUT:
                ((IncomingImageLayout) holder).setIncomingImageData(chatRoomModelList.get(position).getMessageText(),
                        chatRoomModelList.get(position).getDateTime(),
                        chatRoomModelList.get(position).getSenderName(),
                        chatRoomModelList.get(position).getSenderImage()
                );
                break;
            case OUTGOING_IMAGE_LAYOUT:
                ((OutgoingImageLayout) holder).setOutgoingImageData(chatRoomModelList.get(position).getMessageText(),
                        chatRoomModelList.get(position).getDateTime(),
                        chatRoomModelList.get(position).getSenderName(),
                        chatRoomModelList.get(position).getSenderImage()
                );
                break;
            default:
                return;
        }

    }

    @Override
    public int getItemCount() {
        return chatRoomModelList.size();
    }

    class IncomingMessageLayout extends RecyclerView.ViewHolder {
        TextView messageIncoming, timestamp_incoming, senderName;
        CircleImageView circleImageView;

        public IncomingMessageLayout(@NonNull View itemView) {
            super(itemView);
            messageIncoming = itemView.findViewById(R.id.incoming_message_item);
            timestamp_incoming = itemView.findViewById(R.id.incoming_time_tv);
            senderName = itemView.findViewById(R.id.sender_name);
            circleImageView = itemView.findViewById(R.id.sender_image);
        }

        private void setIncomingData(String message, Date timestamp, String name, String image) {
            messageIncoming.setText(message);
            SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
            Glide.with(itemView.getContext()).load(image).into(circleImageView);
            senderName.setText(name);

            if (timestamp != null) {
                timestamp_incoming.setText(df.format(timestamp));
            }
        }
    }

    class OutgoingMessageLayout extends RecyclerView.ViewHolder {
        TextView messageOutgoing, timestamp_outgoing, senderName;
        CircleImageView circleImageView;

        public OutgoingMessageLayout(@NonNull View itemView) {
            super(itemView);
            messageOutgoing = itemView.findViewById(R.id.outgoing_message_item);
            timestamp_outgoing = itemView.findViewById(R.id.outgoing_message_time);
            senderName = itemView.findViewById(R.id.sender_name);
            circleImageView = itemView.findViewById(R.id.sender_image);
        }

        private void setOutgoingData(String message, Date timestamp, String name, String image) {
            messageOutgoing.setText(message);
            SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
            Glide.with(itemView.getContext()).load(image).into(circleImageView);
            senderName.setText(name);

            if (timestamp_outgoing != null && timestamp != null) {

                timestamp_outgoing.setText(df.format(timestamp));
            }
        }
    }


    class IncomingImageLayout extends RecyclerView.ViewHolder {

        ImageView image;
        TextView time, senderName;
        CircleImageView circleImageView;


        public IncomingImageLayout(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.incoming_image_item);
            time = itemView.findViewById(R.id.incoming_time_tv);
            senderName = itemView.findViewById(R.id.sender_name);
            circleImageView = itemView.findViewById(R.id.sender_image);

        }

        private void setIncomingImageData(String imageUrl, Date date, String name, String imageSender) {
            Glide.with(itemView.getContext()).load(imageUrl).apply(new RequestOptions().placeholder(R.drawable.image_placeholder)).centerCrop().into(image);
            SimpleDateFormat df = new SimpleDateFormat("hh:mm a");

            senderName.setText(name);
            Glide.with(itemView.getContext()).load(imageSender).into(circleImageView);

            if (date != null) {
                time.setText(df.format(date));
            }
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), PhotoFullScreenActivity.class);

                    intent.putExtra("IMAGE_URL", imageUrl);
                    itemView.getContext().startActivity(intent);
                }
            });


        }
    }

    class OutgoingImageLayout extends RecyclerView.ViewHolder {
        ImageView image;
        TextView time, senderName;
        CircleImageView circleImageView;

        public OutgoingImageLayout(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.outgoing_image_item);
            time = itemView.findViewById(R.id.outgoing_image_time);
            senderName = itemView.findViewById(R.id.sender_name);
            circleImageView = itemView.findViewById(R.id.sender_image);
        }

        private void setOutgoingImageData(String imageUrl, Date dateTime, String name, String imageSender) {
            Glide.with(itemView.getContext()).load(imageUrl).apply(new RequestOptions().placeholder(R.drawable.image_placeholder)).centerCrop().into(image);

            SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
            if (dateTime != null) {
                time.setText(df.format(dateTime));
            }

            senderName.setText(name);

            Glide.with(itemView.getContext()).load(imageSender).into(circleImageView);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), PhotoFullScreenActivity.class);

                    intent.putExtra("IMAGE_URL", imageUrl);
                    itemView.getContext().startActivity(intent);
                }
            });
        }

    }
}
