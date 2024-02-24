package com.example.dethithu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayoutp;
    private AlbumAdapter albumAdapter;
    private List<Album> albumList;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.rvList);
        swipeRefreshLayoutp = findViewById(R.id.sLayout);
        albumList = new ArrayList<>();
        albumAdapter = new AlbumAdapter(this,albumList);
        LinearLayoutManager ln = new LinearLayoutManager(this);
        recyclerView.setAdapter(albumAdapter);
        recyclerView.setLayoutManager(ln);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                albumAdapter.Filter();
            }
        });
        loadData();
        swipeRefreshLayoutp.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RetrofitService rf = RetrofitInstance.getInstance().create(RetrofitService.class);
                Call<List<Album>> call = rf.getPostAlbum();

                call.enqueue(new Callback<List<Album>>() {
                    @Override
                    public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                        if (response.isSuccessful()) {
                            List<Album> initialAlbums = response.body();
                            albumAdapter.clear();
                            albumAdapter.addNewAlbums(initialAlbums);
                            albumAdapter.notifyDataSetChanged();
                            swipeRefreshLayoutp.setRefreshing(false);
                        } else {
                            Toast.makeText(MainActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Album>> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    private void loadData() {
        RetrofitService rf = RetrofitInstance.getInstance().create(RetrofitService.class);
        Call<List<Album>> call = rf.getPostAlbum();

        call.enqueue(new Callback<List<Album>>() {
            @Override
            public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                if (response.isSuccessful()) {
                    List<Album> initialAlbums = response.body();
                    albumAdapter.addNewAlbums(initialAlbums);
                    albumAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Album>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}



