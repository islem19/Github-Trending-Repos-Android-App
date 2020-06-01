package dz.islem.githubapi.ui.interfaces;

public interface IRecyclerViewPresenter {

    int getDataSize();

    void clear();

    void searchGithubRepos(String date);
}

