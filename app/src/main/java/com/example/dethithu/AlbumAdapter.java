package com.example.dethithu;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {
    private List<Album> albumList;
    private Context context;

    public AlbumAdapter(Context context, List<Album> albumList) {
        this.context = context;
        this.albumList = albumList;
    }
    public void addNewAlbums(List<Album> newAlbum) {
        albumList.clear();
        albumList.addAll(newAlbum);
        notifyDataSetChanged();
    }

    public void clear() {
        albumList.clear();
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public AlbumAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_album, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Album album = albumList.get(position);
        if(album.getId() % 3 != 0){
            holder.imgstar.setVisibility(View.INVISIBLE);
        } else {
            holder.imgstar.setVisibility(View.VISIBLE);
        }
        holder.tv_title.setText(album.getTitle());
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                RetrofitService rf = RetrofitInstance.getInstance().create(RetrofitService.class);
                Call<Void> call = rf.deleteAlbum(String.valueOf(album.getId()));
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        albumList.remove(position);
                        notifyItemRemoved(position);
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
                return false;
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowAll(albumList.get(position),position);
            }
        });
    }

    public void Filter(){
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_fillter, null);
        // Khởi tạo dialog
        Dialog dialog = new Dialog(context, androidx.appcompat.R.style.Base_V21_Theme_AppCompat_Dialog);
        dialog.setContentView(view);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(params);
        EditText ed_id = dialog.findViewById(R.id.ed_idDialogFillter);
        Button btn_find = dialog.findViewById(R.id.btn_find);
        dialog.show();
        btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RetrofitService rf = RetrofitInstance.getInstance().create(RetrofitService.class);
                Call<Album> call = rf.getAlbum(String.valueOf(ed_id.getText()));

                call.enqueue(new Callback<Album>() {

                    @Override
                    public void onResponse(Call<Album> call, Response<Album> response) {
                        Album initialAlbums = response.body();
                        albumList.clear();
                        albumList.addAll(Collections.singleton(initialAlbums));
                        notifyDataSetChanged();
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<Album> call, Throwable t) {

                    }
                });
            }
        });
    }
    private void ShowAll(Album album,int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_show, null);
        // Khởi tạo dialog
        Dialog dialog = new Dialog(context, androidx.appcompat.R.style.Base_V21_Theme_AppCompat_Dialog);
        dialog.setContentView(view);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(params);
        TextView tv_id = dialog.findViewById(R.id.tv_idDialog);
        TextView tv_title = dialog.findViewById(R.id.tv_titleDialog);
        tv_id.setText(String.valueOf(album.getId()));
        tv_title.setText(album.getTitle());
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgstar;
        private TextView tv_title;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            imgstar = itemView.findViewById(R.id.imgstar);
        }
    }
}
