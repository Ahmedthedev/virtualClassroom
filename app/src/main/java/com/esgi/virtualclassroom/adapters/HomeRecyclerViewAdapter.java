package com.esgi.virtualclassroom.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.esgi.virtualclassroom.ClassroomActivity;
import com.esgi.virtualclassroom.R;
import com.esgi.virtualclassroom.models.Module;

import java.util.List;


public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewAdapter.ViewHolder>{
    private List<Module> listModule;
    private Activity activity;

    public HomeRecyclerViewAdapter(Activity activity, List<Module> listItem) {
        this.listModule = listItem;
        this.activity = activity;
    }

    @Override
    public HomeRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_list_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(HomeRecyclerViewAdapter.ViewHolder holder, int position) {
        final Module module = listModule.get(position);

        holder.moduleName.setText(module.getTitle());
        holder.teacherName.setText(module.getTeacher().getName());
        holder.dateStart.setText(module.getStart().toString());
        holder.dateEnd.setText(module.getEnd().toString());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ClassroomActivity.class);
                intent.putExtra(ClassroomActivity.EXTRA_MODULE_ID, module.id);
                activity.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listModule.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView moduleName;
        TextView teacherName;
        TextView dateStart;
        TextView dateEnd;
        LinearLayout linearLayout;

        ViewHolder(View itemView) {
            super(itemView);
            moduleName = itemView.findViewById(R.id.moduleName);
            teacherName = itemView.findViewById(R.id.teacherName);
            dateStart = itemView.findViewById(R.id.dateStart);
            dateEnd = itemView.findViewById(R.id.dateEnd);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }
}
