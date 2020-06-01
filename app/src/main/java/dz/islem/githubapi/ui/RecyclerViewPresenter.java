package dz.islem.githubapi.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dz.islem.githubapi.ui.interfaces.IRecyclerViewFragment;
import dz.islem.githubapi.ui.interfaces.IRecyclerViewPresenter;
import dz.islem.githubapi.data.model.ItemModel;
import dz.islem.githubapi.data.model.RepoModel;
import dz.islem.githubapi.data.remote.RemoteManager;
import dz.islem.githubapi.utils.Constants;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RecyclerViewPresenter implements IRecyclerViewPresenter {

    private List<ItemModel> mData = new ArrayList<>();
    private static Map<String, String> map = new HashMap<>();
    private IRecyclerViewFragment mIRecyclerViewFragment;
    private Observable<RepoModel> mObservable;

    public RecyclerViewPresenter(IRecyclerViewFragment mIRecyclerViewFragment){
        this.mIRecyclerViewFragment = mIRecyclerViewFragment;
    }

    @Override
    public int getDataSize() {
        return (mData == null ? 0 : mData.size());
    }

    @Override
    public void clear() {
        mData.clear();
        mIRecyclerViewFragment.notifySearchResults(mData);
    }

    private void initMap(){
        map.put("q","created:>");
        map.put("sort","stars");
        map.put("order","desc");
        map.put("page",String.valueOf(Constants.PAGE_COUNT));
    }

    @Override
    public void searchGithubRepos(String date) {
        initMap();
        if(date != null && !date.isEmpty())
            map.put("q","created:>"+date);

        mIRecyclerViewFragment.updateRefreshLayout(true);
        mIRecyclerViewFragment.display("Loading...");

        mObservable = RemoteManager.newInstance().getRepositories(map);
        mObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(result -> result.getItems())
                .subscribe(this::handleResults, this::handleError);
    }

    private void handleResults(List<ItemModel> mList){
        if (mList != null && !mList.isEmpty()){
            mData.addAll(mList);
            mIRecyclerViewFragment.notifySearchResults(mData);
        }
        else
            mIRecyclerViewFragment.displayError("No Github Repos to Load :/");
        mIRecyclerViewFragment.updateRefreshLayout(false);

    }

    private void handleError(Throwable t){
        mIRecyclerViewFragment.displayError("Error: can't reach github API ");
        mIRecyclerViewFragment.updateRefreshLayout(false);
    }
}
