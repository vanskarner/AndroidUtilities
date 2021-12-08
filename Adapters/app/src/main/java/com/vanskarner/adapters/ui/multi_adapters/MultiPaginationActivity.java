package com.vanskarner.adapters.ui.multi_adapters;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.vanskarner.adapters.R;
import com.vanskarner.adapters.common.adapters.Pagination;
import com.vanskarner.adapters.common.adaptersothers.BindItem;
import com.vanskarner.adapters.common.adaptersothers.SingleAdapter;
import com.vanskarner.adapters.common.adaptersothers.LoadAdapter;
import com.vanskarner.adapters.ui.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class MultiPaginationActivity extends BaseActivity implements MultiPaginationContract.view,
        Pagination.OnLoadMoreListener {

    RecyclerView recyclerView;
    List<BindItem> list = new ArrayList<>();
    SingleAdapter adapter = new SingleAdapter();
    Pagination pagination = new Pagination(this,
            Pagination.LAST_POSITION_COMPLETE);
    MultiPaginationContract.presenter presenter;

    @Override
    protected void injectPresenter() {
        presenter = new MultiPaginationPresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_pagination_activity);
        setupView();
    }

    private void setupView() {
        recyclerView = findViewById(R.id.recyclerPersons);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(pagination);
        /*adapter.add(new MyAdapters.AdapterOne());
        adapter.add(new MyAdapters.AdapterSecond());
        adapter.add(new MyAdapters.AdapterThird());*/
        adapter.add(new LoadAdapter(R.layout.item_loading));
        adapter.setList(list);
/*        adapter.setOnItemClickListener(view -> {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            PersonModel model = list.get(viewHolder.getAdapterPosition());
            Toast.makeText(this, model.toString(), Toast.LENGTH_SHORT).show();
        });*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (pagination.pageNumber == 1) {
            findViewById(R.id.progressBarCentral).setVisibility(View.VISIBLE);
            pagination.onLoadMore();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (pagination.isLoading) {
            pagination.isLoading = false;
            pagination.pageNumber--;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unsubscribe();
    }

    //Contract Methods

    @Override
    public void hideProgress() {
        findViewById(R.id.progressBarCentral).setVisibility(View.GONE);
        adapter.hideProgress();
    }

    @Override
    public void addList(List<BindItem> list) {
        pagination.isLoading = false;
        adapter.addList(list);
    }

    @Override
    public void showNoPages() {
        Snackbar.make(findViewById(R.id.contentPagination),
                getString(R.string.exception_no_items), Snackbar.LENGTH_SHORT)
                .show();
    }

    @Override
    public void onLoadMore(int page) {
        if (page != 1) {
            adapter.showProgress();
        }
        presenter.loadMore(page);
    }

}
