package dz.islem.githubapi.interfaces;

public interface IRecyclerViewPresenter {

    int getDataSize();

    void clear();

    void searchGithubRepos(String date);
}

