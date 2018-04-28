package com.esgi.virtualclassroom.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.esgi.virtualclassroom.R;
import com.esgi.virtualclassroom.models.Document;
import com.esgi.virtualclassroom.models.Message;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class DocumentRecyclerViewAdapter extends RecyclerView.Adapter<DocumentRecyclerViewAdapter.ViewHolder>{
    private Map<String,Document> documents;

    public DocumentRecyclerViewAdapter(Map<String,Document> documents) {
        this.documents = documents;
    }

    @NonNull
    @Override
    public DocumentRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_document_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentRecyclerViewAdapter.ViewHolder holder, int position) {
        final Document document = documents.get(position);
        holder.documentName.setText(document.name);
        holder.documentUrl.setText(document.url);
    }

    @Override
    public int getItemCount() {
        return documents.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView documentName;
        TextView documentUrl;

        ViewHolder(View itemView) {
            super(itemView);
            documentName = itemView.findViewById(R.id.document_name);
            documentUrl = itemView.findViewById(R.id.document_url);
        }
    }
}
