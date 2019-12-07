package com.expossoftware.tbid_dev.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.expossoftware.tbid_dev.R;
import com.expossoftware.tbid_dev.model.ItemProgramKelas;

import java.util.ArrayList;

public class adapter_programkelas extends RecyclerView.Adapter<adapter_programkelas.ProgramKelasViewHolder>{
    private ArrayList<ItemProgramKelas> dataProgramKelasList;

    public adapter_programkelas(ArrayList<ItemProgramKelas> dataProgramKelas) {
        this.dataProgramKelasList = dataProgramKelas;
    }

    @NonNull
    @Override
    public ProgramKelasViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.cv_programkelas,viewGroup,false);
        return new ProgramKelasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProgramKelasViewHolder holder, int position) {
        holder.txtNamaKelas.setText(dataProgramKelasList.get(position).getNamaKelas());
        holder.txtHargaKelas.setText(dataProgramKelasList.get(position).getHargaKelas());
    }

    @Override
    public int getItemCount() {
        return (dataProgramKelasList != null) ? dataProgramKelasList.size() : 0;
    }

    public class ProgramKelasViewHolder  extends RecyclerView.ViewHolder {
        private TextView txtNamaKelas, txtHargaKelas;

        public ProgramKelasViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNamaKelas = (TextView) itemView.findViewById(R.id.lvTbxKelas);
            txtHargaKelas = (TextView) itemView.findViewById(R.id.lvTbxHarga);
        }
    }
    public void filterList(ArrayList<ItemProgramKelas> filteredList) {
        dataProgramKelasList = filteredList;
        notifyDataSetChanged();
    }
}
