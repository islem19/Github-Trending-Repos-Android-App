package dz.islem.githubapi.interfaces;

public interface IRecyclerViewFragment {

    void notifySearchResults();

    void display(String message);

    void displayError(String error);

    void updateRefreshLayout(boolean refresh);

}
