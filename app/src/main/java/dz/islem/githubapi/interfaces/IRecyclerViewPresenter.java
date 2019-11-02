package dz.islem.githubapi.interfaces;

import android.content.Context;


import java.util.List;

import dz.islem.githubapi.adapters.RecyclerAdapter;
import dz.islem.githubapi.models.ItemModel;

public interface IRecyclerViewPresenter {

    void onBindViewHolderAtPosition(RecyclerAdapter.ViewHolder view, int position);

    int getDataSize();

    void clear();

    void addAll(List<ItemModel> mData);
}

