package com.esgi.virtualclassroom.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.esgi.virtualclassroom.R;
import com.esgi.virtualclassroom.models.Module;

import java.util.List;


public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewAdapter.ViewHolder>{

    private List<Module> listModule;
    private Context context;

    public HomeRecyclerViewAdapter(Context context, List<Module> listItem) {
        this.listModule = listItem;
        this.context = context;
    }

    @Override
    public HomeRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_list_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(HomeRecyclerViewAdapter.ViewHolder holder, int position) {
        Module listModule1 = listModule.get(position);

        holder.moduleName.setText(listModule1.getTitle());
        holder.teacherName.setText(listModule1.getTeacher().getName());
        holder.dateStart.setText(listModule1.getStart());
        holder.dateEnd.setText(listModule1.getEnd());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "you clicked on ",Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return listModule.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView moduleName;
        public TextView teacherName;
        public TextView dateStart;
        public TextView dateEnd;
        public LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            moduleName = (TextView) itemView.findViewById(R.id.moduleName);
            teacherName = (TextView) itemView.findViewById(R.id.teacherName);
            dateStart = (TextView) itemView.findViewById(R.id.dateStart);
            dateEnd = (TextView) itemView.findViewById(R.id.dateEnd);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
        }
    }
}
