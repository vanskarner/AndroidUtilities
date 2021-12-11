package com.vanskarner.adapters.examples.simple;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vanskarner.adapters.R;
import com.vanskarner.adapters.common.adaptersothers.BindAdapter;
import com.vanskarner.adapters.databinding.ItemSimple2Binding;

class LinearAdapter implements BindAdapter<WomanModel, LinearAdapter.LinearVH> {

    @Override
    public LinearVH onCreateViewHolder(@NonNull ViewGroup parent, LayoutInflater inflater) {
        return new LinearVH(ItemSimple2Binding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(LinearVH viewHolder, WomanModel item) {
        viewHolder.binding.setWoman(item);
    }

    @Override
    public int setLayoutId() {
        return R.layout.item_simple2;
    }

    @Override
    public Class<WomanModel> setModelClass() {
        return WomanModel.class;
    }

    static class LinearVH extends RecyclerView.ViewHolder {
        ItemSimple2Binding binding;

        public LinearVH(@NonNull ItemSimple2Binding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
