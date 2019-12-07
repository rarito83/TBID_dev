package com.expossoftware.tbid_dev.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.expossoftware.tbid_dev.R;
import com.expossoftware.tbid_dev.model.ItemPresensi;

import java.util.ArrayList;

public class adapter_presensi extends RecyclerView.Adapter<adapter_presensi.PresensiViewHolder> {
    private ArrayList<ItemPresensi> dataPresensiList;

    public adapter_presensi(ArrayList<ItemPresensi> dataPresensi) {
        this.dataPresensiList = dataPresensi;
    }

    @NonNull
    @Override
    public adapter_presensi.PresensiViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.cv_presensi,viewGroup,false);
        return new adapter_presensi.PresensiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapter_presensi.PresensiViewHolder holder, int position) {
        holder.txtPresenceSubClass.setText(dataPresensiList.get(position).getPresenceSubClass());
        holder.txtPresenceTime.setText(dataPresensiList.get(position).getPresenceDay() + ", " +
                dataPresensiList.get(position).getPresenceDate() + " " +
                dataPresensiList.get(position).getPresenceTime());
    }

    @Override
    public int getItemCount() {
        return (dataPresensiList != null) ? dataPresensiList.size() : 0;
    }

    public class PresensiViewHolder  extends RecyclerView.ViewHolder {
        private TextView txtPresenceSubClass, txtPresenceTime;

        public PresensiViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPresenceSubClass = (TextView) itemView.findViewById(R.id.cvTbxPresenceSubClass);
            txtPresenceTime = (TextView) itemView.findViewById(R.id.cvTbxPresenceTime);
        }
    }


}

