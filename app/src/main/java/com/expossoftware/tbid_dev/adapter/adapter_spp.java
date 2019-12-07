package com.expossoftware.tbid_dev.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.expossoftware.tbid_dev.R;
import com.expossoftware.tbid_dev.model.ItemSpp;

import java.util.ArrayList;

public class adapter_spp extends RecyclerView.Adapter<adapter_spp.sppViewHolder> {
    private String strMonth;

    private ArrayList<ItemSpp> dataSppList;

    public adapter_spp(ArrayList<ItemSpp> dataSpp) {
        this.dataSppList = dataSpp;
    }

    @NonNull
    @Override
    public adapter_spp.sppViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.cv_spp,viewGroup,false);
        return new adapter_spp.sppViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapter_spp.sppViewHolder holder, int position) {
        holder.tbxSppDate.setText(dataSppList.get(position).getSppStrDate() + " " + dataSppList.get(position).getSppStrMonth());
        holder.tbxSppClass.setText(dataSppList.get(position).getSppStrClass() + " - " + dataSppList.get(position).getSppStrSubClass());
        holder.tbxSppPaymentType.setText(dataSppList.get(position).getSppStrNominal() + " " + dataSppList.get(position).getSppStrRefID()
                + " " + dataSppList.get(position).getSppStrType() + " " + dataSppList.get(position).getSppStrAliasName());
    }

    @Override
    public int getItemCount() {
        return (dataSppList != null) ? dataSppList.size() : 0;
    }

    public class sppViewHolder  extends RecyclerView.ViewHolder {
        private TextView tbxSppDate, tbxSppClass, tbxSppPaymentType;

        public sppViewHolder(@NonNull View itemView) {
            super(itemView);
            tbxSppDate = (TextView) itemView.findViewById(R.id.cvSppTbxDate);
            tbxSppClass = (TextView) itemView.findViewById(R.id.cvSppTbxClass);
            tbxSppPaymentType = (TextView) itemView.findViewById(R.id.cvSppTbxPaymentType);
        }
    }

    public void filterList(ArrayList<ItemSpp> filteredList) {
        dataSppList = filteredList;
        notifyDataSetChanged();
    }
}