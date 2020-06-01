package dz.islem.githubapi.ui.interfaces;

import java.util.List;

import dz.islem.githubapi.data.model.ItemModel;

public interface IRecyclerViewFragment {

    void notifySearchResults(List<ItemModel> mData);

    void display(String message);

    void displayError(String error);

    void updateRefreshLayout(boolean refresh);

}
