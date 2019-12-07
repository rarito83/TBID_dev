package com.expossoftware.tbid_dev.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.expossoftware.tbid_dev.R;
import com.expossoftware.tbid_dev.model.ItemPengajar;

import java.util.ArrayList;

public class adapter_pengajar extends RecyclerView.Adapter<adapter_pengajar.viewHolderPengajar> {
    private ArrayList<ItemPengajar> pengajarDataList;

    private ItemPengajar[] arrItemPengajar;

    public adapter_pengajar(ItemPengajar[] arrItemPengajar) {
        this.arrItemPengajar = arrItemPengajar;
    }

    public adapter_pengajar(ArrayList<ItemPengajar> dataList) {
        this.pengajarDataList = dataList;
    }


    @NonNull
    @Override
    public adapter_pengajar.viewHolderPengajar onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.cv_pengajar, viewGroup, false);
        return new viewHolderPengajar(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapter_pengajar.viewHolderPengajar viewHolderPengajar, int position) {
        viewHolderPengajar.txtNama.setText(pengajarDataList.get(position).getNama());
        viewHolderPengajar.txtJabatan.setText(pengajarDataList.get(position).getJabatan());
        viewHolderPengajar.imgPengajar.setImageResource(pengajarDataList.get(position).getImage());
        //viewHolderPengajar.imgPengajar.setImageResource(arrItemPengajar[position].getImage());

        //Picasso.get().load(pengajarDataList.get(position).getImage()).into(viewHolderPengajar.imgPengajar);
    }

    @Override
    public int getItemCount() {
        return (pengajarDataList != null) ? pengajarDataList.size() : 0;
    }

    public class viewHolderPengajar extends RecyclerView.ViewHolder {
        private TextView txtNama, txtJabatan;
        private ImageView imgPengajar;

        public viewHolderPengajar(View itemView) {
            super(itemView);
            txtNama = (TextView) itemView.findViewById(R.id.lvTbxName);
            txtJabatan = (TextView) itemView.findViewById(R.id.lvTbxDescription);
            imgPengajar = (ImageView) itemView.findViewById(R.id.lvImageView);
        }
    }
}
