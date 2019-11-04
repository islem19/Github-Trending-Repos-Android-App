package dz.islem.githubapi.interfaces;

import android.content.Context;

import dz.islem.githubapi.adapters.RecyclerAdapter;

public interface IRecyclerViewPresenter {

    void onBindViewHolderAtPosition(RecyclerAdapter.ViewHolder view, int position);

    int getDataSize();

    void clear();

    void copyToClip(Context context, int position);

    void searchGithubRepos(String date);
}

