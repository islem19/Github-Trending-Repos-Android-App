package dz.islem.githubapi.presenters;

import java.util.List;

import dz.islem.githubapi.adapters.RecyclerAdapter;
import dz.islem.githubapi.interfaces.IRecyclerViewPresenter;
import dz.islem.githubapi.models.ItemModel;

public class RecyclerViewPresenter implements IRecyclerViewPresenter {
    private List<ItemModel> mData;

    public RecyclerViewPresenter(List<ItemModel> mData){
        this.mData = mData;
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
    }

    @Override
    public void addAll(List<ItemModel> mData) {
        this.mData.addAll(mData);
    }
}
