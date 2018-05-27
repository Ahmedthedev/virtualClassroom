package com.esgi.virtualclassroom.modules.home.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.esgi.virtualclassroom.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ClassroomRecyclerViewAdapter extends RecyclerView.Adapter<ClassroomRecyclerViewAdapter.ViewHolder> {
    private ArrayList<String> classrooms;
    private Listener listener;

    public ClassroomRecyclerViewAdapter(ArrayList<String> classrooms) {
        this.classrooms = classrooms;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ClassroomRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_home_classroom_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassroomRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.bind(classrooms.get(position));
    }

    @Override
    public int getItemCount() {
        return classrooms.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.classroom_title_text_view) TextView titleTextView;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(final String classroom) {
            titleTextView.setText(classroom);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onClassroomClick(classroom);
                    }
                }
            });
        }
    }

    public interface Listener {
        void onClassroomClick(String classroom);
    }
}
