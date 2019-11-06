package dz.islem.githubapi.presenters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dz.islem.githubapi.adapters.RecyclerAdapter;
import dz.islem.githubapi.interfaces.IRecyclerViewFragment;
import dz.islem.githubapi.interfaces.IRecyclerViewPresenter;
import dz.islem.githubapi.models.ItemModel;
import dz.islem.githubapi.models.RepoModel;
import dz.islem.githubapi.remote.RemoteManager;
import dz.islem.githubapi.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecyclerViewPresenter implements IRecyclerViewPresenter {
    private List<ItemModel> mData = new ArrayList<>();
    private static Map<String, String> map = new HashMap<>();
    private IRecyclerViewFragment mIRecyclerViewFragment;

    public RecyclerViewPresenter(IRecyclerViewFragment mIRecyclerViewFragment){
        this.mIRecyclerViewFragment = mIRecyclerViewFragment;

    }
    @Override
    public void onBindViewHolderAtPosition(RecyclerAdapter.ViewHolder view, int position) {
        ItemModel data = mData.get(position);
        String mCount = (data.getStar_count() >= 1000 ? data.getStar_count()/1000+"k" : String.valueOf(data.getStar_count()));

        view.setTitle(data.getName());
        view.setDescription(data.getDescription());
        view.setAvatar(data.getOwners().getAvatar_url());
        view.setLanguage(data.getLanguage());
        view.setLicense(data.getLicenses().getName());
        view.setStarCount(mCount);
    }

    @Override
    public int getDataSize() {
        return (mData == null ? 0 : mData.size());
    }

    @Override
    public void clear() {
        mData.clear();
        mIRecyclerViewFragment.notifySearchResults();
    }

    @Override
    public void copyToClip(Context context, int position) {
        ClipboardManager clipboard = (ClipboardManager)  context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Git URL", mData.get(position).getClone_url());
        clipboard.setPrimaryClip(clip);

        Toast.makeText(context,"Link Copied to the Clipboard",Toast.LENGTH_SHORT).show();
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

        RemoteManager.newInstance().getRepositories(map).enqueue(new Callback<RepoModel>() {
            @Override
            public void onResponse(Call<RepoModel> call, Response<RepoModel> response) {
                List<ItemModel> res = response.body().getItems();
                if (res != null && !res.isEmpty()){
                    mData.addAll(res);
                    mIRecyclerViewFragment.notifySearchResults();
                }
                else
                    mIRecyclerViewFragment.displayError("No Github Repos to Load :/");
                mIRecyclerViewFragment.updateRefreshLayout(false);
            }

            @Override
            public void onFailure(Call<RepoModel> call, Throwable t) {
                mIRecyclerViewFragment.displayError("Error: can't reach github API ");
                mIRecyclerViewFragment.updateRefreshLayout(false);
            }
        });
    }

}
