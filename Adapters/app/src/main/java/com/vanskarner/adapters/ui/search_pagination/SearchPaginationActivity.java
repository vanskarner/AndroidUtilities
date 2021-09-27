package com.vanskarner.adapters.ui.search_pagination;

import android.os.Bundle;
import android.view.View;
import android.widget.Filter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.vanskarner.adapters.common.bases.BaseActivity;
import com.vanskarner.adapters.common.listener.Pagination;
import com.vanskarner.adapters.common.reactive_views.RxSearchObservable;
import com.vanskarner.adapters.models.PersonModel;
import com.vanskarner.adapters.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SearchPaginationActivity extends BaseActivity
        implements SearchPaginationContract.view, Pagination.OnLoadMoreListener {

    RecyclerView recyclerView;
    SearchView searchView;
    List<PersonModel> list = new ArrayList<>();
    SearchPaginationFilterAdapter adapter = new SearchPaginationFilterAdapter(list);
    Pagination pagination = Pagination
            .createWithLinear(this, Pagination.LAST_POSITION_COMPLETE);
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    SearchPaginationContract.presenter presenter;


    @Override
    protected void injectPresenter() {
        presenter = new SearchPaginationPresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_pagination_activity);
        setupView();
    }

    protected void setupView() {
        recyclerView = findViewById(R.id.recyclerPersons);
        searchView = findViewById(R.id.searchView);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(pagination);
        adapter.setOnItemClickListener(view -> {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            PersonModel model = list.get(viewHolder.getAdapterPosition());
            Toast.makeText(this, model.toString(), Toast.LENGTH_SHORT).show();
        });
        searchView.setQueryHint(getString(R.string.search));
        searchView.setOnSearchClickListener(view -> adapter.hideProgress());
        Disposable disposable = RxSearchObservable.fromView(searchView)
                .debounce(350, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    Filter filter = adapter.getFilter();
                    filter.filter(s);
                });
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.loadMore(pagination.pageNumber);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
        presenter.unsubscribe();
    }

    private void initializeViews() {
        if (pagination.pageNumber == 1) {
            recyclerView.setVisibility(View.VISIBLE);
            searchView.setVisibility(View.VISIBLE);
            findViewById(R.id.progressBarCentral).setVisibility(View.GONE);
        }
    }

    //Contract Methods

    @Override
    public void hideProgress() {
        adapter.hideProgress();
    }

    @Override
    public void addList(List<PersonModel> list) {
        initializeViews();
        pagination.isLoading = false;
        if (searchView.isIconified()) {
            // the data is only adapted when the SearchView is not in use
            adapter.addList(list);
        } else {
            pagination.pageNumber--;
        }
    }

    @Override
    public void showNoPages() {
        Snackbar.make(findViewById(R.id.contentPagination),
                getString(R.string.exception_no_items),
                Snackbar.LENGTH_SHORT)
                .show();
    }

    @Override
    public void loadMore() {
        if (searchView.isIconified()) {
            // The data will be requested when the SearchView is not in use
            adapter.showProgress();
            presenter.loadMore(pagination.pageNumber);
        } else {
            pagination.pageNumber--;
            pagination.isLoading = false;
        }
    }

}