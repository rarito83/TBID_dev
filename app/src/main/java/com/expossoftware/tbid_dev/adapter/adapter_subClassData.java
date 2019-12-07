package com.expossoftware.tbid_dev.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.expossoftware.tbid_dev.R;
import com.expossoftware.tbid_dev.model.ItemSubClassData;

import java.util.ArrayList;

public class adapter_subClassData extends RecyclerView.Adapter<adapter_subClassData.subClassViewHolder>{
    private ArrayList<ItemSubClassData> dataSubKelasList;

    public adapter_subClassData(ArrayList<ItemSubClassData> dataSubKelas) {
        this.dataSubKelasList = dataSubKelas;
    }

    @NonNull
    @Override
    public subClassViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.cv_programkelas,viewGroup,false);
        return new subClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull subClassViewHolder holder, int position) {
        holder.txtNamaSubClassData.setText(dataSubKelasList.get(position).getNamaSubKelas());
    }

    @Override
    public int getItemCount() {
        return (dataSubKelasList != null) ? dataSubKelasList.size() : 0;
    }

    public class subClassViewHolder  extends RecyclerView.ViewHolder {
        private TextView txtNamaSubClassData;

        public subClassViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNamaSubClassData = (TextView) itemView.findViewById(R.id.lvTbxKelas);
        }
    }
}
