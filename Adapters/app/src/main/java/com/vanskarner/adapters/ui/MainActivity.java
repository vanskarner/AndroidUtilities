package com.vanskarner.adapters.ui;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Filter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vanskarner.adapters.MovieModel;
import com.vanskarner.adapters.R;
import com.vanskarner.adapters.adapters.MoviesNew;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SearchView searchView;
    MoviesNew moviesAdapter;
    ArrayList<MovieModel> rowsArrayList = new ArrayList<>();
    private int pageNumber = 1;
    boolean isLoading = false;
    boolean isFiltering = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adapter);
        recyclerView = findViewById(R.id.recyclerMovies);
        searchView = findViewById(R.id.searchView);
        searchView.setQueryHint("Buscar");
        initAdapter();
        initScrollListener();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                isFiltering = true;
                Filter myFilterB = moviesAdapter.getFilter();
                myFilterB.filter(newText);
                return false;
            }
        });
    }

    private List<MovieModel> populateData() {
        List<MovieModel> movieModels = new ArrayList<>();
        movieModels.add(new MovieModel(0, "Pedro", "IMAGE 1"));
        movieModels.add(new MovieModel(1, "Juan", "IMAGE 2"));
        movieModels.add(new MovieModel(2, "Diego", "IMAGE 3"));
        movieModels.add(new MovieModel(3, "Ramirez", "IMAGE 4"));
        movieModels.add(new MovieModel(4, "Pablo", "IMAGE 5"));
        movieModels.add(new MovieModel(5, "Luis", "IMAGE 6"));//-
        movieModels.add(new MovieModel(6, "Daniel", "IMAGE 7"));
        movieModels.add(new MovieModel(7, "Fabian", "IMAGE 8"));
        movieModels.add(new MovieModel(8, "Carlos", "IMAGE 9"));//-
        movieModels.add(new MovieModel(9, "Echina", "IMAGE 10"));
        return movieModels;
    }

    private List<MovieModel> populateDataExtra2() {
        List<MovieModel> movieModels = new ArrayList<>();
        for (int i = rowsArrayList.size(); i < rowsArrayList.size() + 10; i++) {
            movieModels.add(new MovieModel(i, "Add N°" + i, "IMG" + i));
        }
        return movieModels;
    }

    private void initAdapter() {
        moviesAdapter = new MoviesNew(rowsArrayList);
        recyclerView.setAdapter(moviesAdapter);
        moviesAdapter.addList(populateData());
        moviesAdapter.setOnItemClickListener(view -> {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            Toast.makeText(MainActivity.this, "You Clicked: " +
                            rowsArrayList.get(viewHolder.getAdapterPosition()).toString(),
                    Toast.LENGTH_SHORT).show();
        });
    }

    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading && !isFiltering) {
                    if (linearLayoutManager != null &&
                            linearLayoutManager.findLastCompletelyVisibleItemPosition() == rowsArrayList.size() - 1) {
                        pageNumber++;
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });


    }

    private void loadMore() {
        moviesAdapter.showProgress();
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            moviesAdapter.hideProgress();
            moviesAdapter.addList(populateDataExtra2());
            isLoading = false;
        }, 2000);
    }

}