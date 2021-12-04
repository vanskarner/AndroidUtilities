package com.vanskarner.adapters.common.adaptersothers;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings({"unchecked", "rawtypes"})
class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Map<Integer, DelegateAdapter<AdapterItem, RecyclerView.ViewHolder>> mapAdapter = new HashMap<>();
    private final List<AdapterItem> list;

    public RecyclerAdapter(List<AdapterItem> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return Objects
                .requireNonNull(mapAdapter.get(viewType))
                .onCreateViewHolder(parent, LayoutInflater.from(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        search2(position, delegateAdapter -> delegateAdapter.onBindViewHolder(list.get(position), holder));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        final int[] layoutId = {0};
        search2(position, delegateAdapter -> layoutId[0] = delegateAdapter.getLayoutId());
        return layoutId[0];
    }


    private boolean isItsModel(DelegateAdapter delegateAdapter, int position) {
        return delegateAdapter.getModelClass().isInstance(list.get(position));
    }

    public void search2(int position, SearchAdapters listener) {
        for (Map.Entry<Integer, DelegateAdapter<AdapterItem, RecyclerView.ViewHolder>> entry :
                mapAdapter.entrySet()) {
            if (isItsModel(entry.getValue(), position)) {
                listener.something(entry.getValue());
                return;
            }
        }
    }

    interface SearchAdapters {
        void something(DelegateAdapter delegateAdapter);
    }

    public void addAdapter(DelegateAdapter delegateAdapter) {
        mapAdapter.put(delegateAdapter.getLayoutId(), delegateAdapter);
    }

}
