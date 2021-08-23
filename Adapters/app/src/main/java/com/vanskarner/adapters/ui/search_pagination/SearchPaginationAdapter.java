package com.vanskarner.adapters.ui.search_pagination;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vanskarner.adapters.models.MovieModel;
import com.vanskarner.adapters.R;
import com.vanskarner.adapters.common.adapters.CustomEndlessAdapter;

import java.util.List;

class SearchPaginationAdapter
        extends CustomEndlessAdapter<MovieModel, SearchPaginationAdapter.ItemClickViewHolder> {

    private View.OnClickListener onItemClickListener;

    public SearchPaginationAdapter(List<MovieModel> list) {
        super(list);
    }

    public void setOnItemClickListener(View.OnClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    protected int setLoadLayout() {
        return R.layout.item_loading;
    }

    @Override
    protected ItemClickViewHolder setViewHolder(LayoutInflater inflater, ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_movie, parent, false);
        return new ItemClickViewHolder(view, onItemClickListener);
    }

    @Override
    public boolean filterCondition(MovieModel item, String filterPattern) {
        return item.getName().toLowerCase().contains(filterPattern);
    }

    @Override
    protected void bindItem(ItemClickViewHolder holder, MovieModel item, int position) {
        holder.itemName.setText(item.getName());
    }

    protected static class ItemClickViewHolder extends RecyclerView.ViewHolder {
        private TextView itemName;

        protected ItemClickViewHolder(@NonNull View itemView,
                                      View.OnClickListener onItemClickListener) {
            super(itemView);
            setupView(itemView);
            itemView.setTag(this);
            itemView.setOnClickListener(onItemClickListener);
        }


        protected void setupView(View itemView) {
            itemName = itemView.findViewById(R.id.tvItem);
        }

    }

}

