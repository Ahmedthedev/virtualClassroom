package com.esgi.virtualclassroom.modules.classrooms;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.esgi.virtualclassroom.R;
import com.esgi.virtualclassroom.data.AuthenticationProvider;
import com.esgi.virtualclassroom.data.models.Classroom;
import com.esgi.virtualclassroom.data.models.User;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ClassroomsRecyclerViewAdapter extends RecyclerView.Adapter<ClassroomsRecyclerViewAdapter.ViewHolder> {
    private ArrayList<Classroom> classrooms;
    private Listener listener;

    ClassroomsRecyclerViewAdapter(ArrayList<Classroom> classrooms) {
        this.classrooms = classrooms;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ClassroomsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_home_classroom_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassroomsRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.bind(classrooms.get(position));
    }

    @Override
    public int getItemCount() {
        return classrooms.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.classroom_title_text_view) TextView titleTextView;
        @BindView(R.id.classroom_description_text_view) TextView descriptionTextView;
        @BindView(R.id.imageView) CircleImageView circleImageView;
        @BindView(R.id.attachment_favorite_full) ImageView favoriteButtonFull;
        @BindView(R.id.attachment_favorite_empty) ImageView favoriteButtonEmpty;

        @BindView(R.id.classroom_viewers_count_text_view) TextView viewersCountTextView;
        @BindView(R.id.classroom_subscriptions_count_text_view) TextView subscriptionsCountTextView;
        @BindView(R.id.classroom_attachments_count_text_view) TextView attachmentsCountTextView;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(final Classroom classroom) {
            User user = AuthenticationProvider.getCurrentUser();

            titleTextView.setText(classroom.getTitle());
            descriptionTextView.setText(classroom.getDescription());
            viewersCountTextView.setText(String.valueOf(classroom.getViewersCount()));
            subscriptionsCountTextView.setText(String.valueOf(classroom.getSubscriptionsCount()));
            attachmentsCountTextView.setText(String.valueOf(classroom.getAttachmentsCount()));

            if (classroom.getTeacher().getPictureUrl() != null) {
                listener.loadImage(classroom.getTeacher().getPictureUrl(), circleImageView);
            }

            itemView.setOnClickListener(view -> listener.onClassroomClick(classroom));

            favoriteButtonEmpty.setVisibility(user.hasSubscribed(classroom) ? View.GONE : View.VISIBLE);
            favoriteButtonEmpty.setOnClickListener(view -> listener.postSubscription(classroom));

            favoriteButtonFull.setVisibility(user.hasSubscribed(classroom) ? View.VISIBLE : View.GONE);
            favoriteButtonFull.setOnClickListener(view -> listener.deleteSubscription(classroom));
        }
    }

    public interface Listener {
        void onClassroomClick(Classroom classroom);
        void loadImage(String url, ImageView imageView);
        void postSubscription(Classroom classroom);
        void deleteSubscription(Classroom classroom);
    }
}
