package com.esgi.virtualclassroom.Adapters;

import android.app.LauncherActivity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.esgi.virtualclassroom.ListItem;
import com.esgi.virtualclassroom.R;

import org.w3c.dom.Text;

import java.util.List;


public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewAdapter.ViewHolder>{

    private List<ListItem> listItem;
    private Context context;

    public HomeRecyclerViewAdapter(Context context, List<ListItem> listItem) {
        this.listItem = listItem;
        this.context = context;
    }

    @Override
    public HomeRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(HomeRecyclerViewAdapter.ViewHolder holder, int position) {
        ListItem listItem1 = listItem.get(position);

        holder.moduleName.setText(listItem1.getModuleName());
        holder.teacherName.setText(listItem1.getTeacherName());
        holder.dateStart.setText(listItem1.getDateStart());
        holder.dateEnd.setText(listItem1.getDateEnd());

    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView moduleName;
        public TextView teacherName;
        public TextView dateStart;
        public TextView dateEnd;

        public ViewHolder(View itemView) {
            super(itemView);
            moduleName = (TextView) itemView.findViewById(R.id.moduleName);
            teacherName = (TextView) itemView.findViewById(R.id.teacherName);
            dateStart = (TextView) itemView.findViewById(R.id.dateStart);
            dateEnd = (TextView) itemView.findViewById(R.id.dateEnd);
        }
    }
}
