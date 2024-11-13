package com.nauproject.myproject;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class reminderAdapter extends RecyclerView.Adapter<reminderAdapter.reminderViewHolder>{

    Context context;
    ArrayList<item_reminder> reminderList;

    public reminderAdapter(Context context, ArrayList<item_reminder> reminderList) {
        this.context = context;
        this.reminderList = reminderList;
    }

    @NonNull
    @Override
    public reminderAdapter.reminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycle_row, parent, false);

        return new reminderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull reminderAdapter.reminderViewHolder holder, int position) {
        item_reminder reminder = reminderList.get(position);

        Glide.with(context)
                .load(reminder.getImage()   )
                .into(holder.imageView);

        holder.tvStatus.setText(reminder.getStatus());
        holder.tvJam.setText(reminder.getJam());

        holder.imageButton2.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("")
                        .setItems(new CharSequence[]{"Edit", "Delete"}, (dialog, which) -> {
                            if (which == 0) {
                                Toast.makeText(context, "Edit Tersimpan", Toast.LENGTH_SHORT).show();
                            } else if (which == 1) {
                                AlertDialog deleteConfirmDialog = new AlertDialog.Builder(context)
                                        .setTitle("Konfirmasi Hapus")
                                        .setMessage("Apakah Anda yakin ingin menghapus pengingat ini?")
                                        .setPositiveButton("Ya", (dialog1, which1) -> {
                                            reminderList.remove(adapterPosition);
                                            Toast.makeText(context, "Pengingat Dihapus", Toast.LENGTH_SHORT).show();
                                            notifyItemRemoved(adapterPosition);
                                            notifyItemRangeChanged(adapterPosition, reminderList.size());
                                        })
                                        .setNegativeButton("Tidak", null)
                                        .create();
                                deleteConfirmDialog.show();
                            }
                        });
                builder.create().show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return reminderList.size();
    }

    public class reminderViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView tvStatus, tvJam;
        ImageButton imageButton2;

        public reminderViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView4);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvJam = itemView.findViewById(R.id.tvJam);
            imageButton2 = itemView.findViewById(R.id.imageButton2);
        }
    }
}
