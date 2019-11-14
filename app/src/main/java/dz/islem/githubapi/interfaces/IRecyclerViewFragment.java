package dz.islem.githubapi.interfaces;

import java.util.List;

import dz.islem.githubapi.models.ItemModel;

public interface IRecyclerViewFragment {

    void notifySearchResults(List<ItemModel> mData);

    void display(String message);

    void displayError(String error);

    void updateRefreshLayout(boolean refresh);

}
