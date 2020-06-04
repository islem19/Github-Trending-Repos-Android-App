package dz.islem.githubapi.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class RepoModel implements Serializable {
    @SerializedName("total_count")
    @Expose
    private int count;
    @SerializedName("incomplete_results")
    @Expose
    private boolean results;
    @SerializedName("items")
    @Expose
    private List<ItemModel> items;

    public RepoModel(int count, boolean results, List<ItemModel> items) {
        this.count = count;
        this.results = results;
        this.items = items;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isResults() {
        return results;
    }

    public void setResults(boolean results) {
        this.results = results;
    }

    public List<ItemModel> getItems() {
        return items;
    }

    public void setItems(List<ItemModel> items) {
        this.items = items;
    }
}


