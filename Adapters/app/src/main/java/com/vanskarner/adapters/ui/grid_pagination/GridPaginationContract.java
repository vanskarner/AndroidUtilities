package com.vanskarner.adapters.ui.grid_pagination;

import com.vanskarner.adapters.models.PersonModel;
import com.vanskarner.adapters.ui.BasePresenter;

import java.util.List;

interface GridPaginationContract {
    interface view {
        void hideProgress();

        void addList(List<PersonModel> list);

        void showNoPages();
    }

    interface presenter extends BasePresenter {

        void loadMore(int pageNumber);
    }
}
