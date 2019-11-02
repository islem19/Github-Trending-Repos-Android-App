package dz.islem.githubapi.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dz.islem.githubapi.R;
import dz.islem.githubapi.adapters.RecyclerAdapter;
import dz.islem.githubapi.models.ItemModel;
import dz.islem.githubapi.models.RepoModel;
import dz.islem.githubapi.presenters.RecyclerViewPresenter;
import dz.islem.githubapi.remote.RemoteManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecyclerViewFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private View mView;
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<ItemModel> mData = new ArrayList<>();
    private RecyclerViewPresenter mRecyclerViewPresenter;
    private static Map<String, String> map = new HashMap<>();
    protected static int mPageCount=1;

    public static RecyclerViewFragment newInstance() {
        return new RecyclerViewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRecyclerViewPresenter = new RecyclerViewPresenter(mData);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.main_fragment, container, false);
        initView();
        setupRecycler();

        return mView;
    }

    @Override
    public void onStart () {
        super.onStart();
        initMap();
        requestDataModel();
    }

    private void initView(){
        FloatingActionButton mFloatingActionButton = mView.findViewById(R.id.actionButton);
        mRecyclerView = mView.findViewById(R.id.recyclerView);
        mSwipeRefreshLayout = mView.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mFloatingActionButton.setOnClickListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(this);

    }

    private void setupRecycler(){
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new RecyclerAdapter(mRecyclerViewPresenter);
        mRecyclerView.setLayoutManager(mLayoutManager);
        int scrollPosition = 0;
        mRecyclerView.scrollToPosition(scrollPosition);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addOnScrollListener(mOnScrollListener);
    }

    private void initMap(){
        map.put("q","created:>2019-10-10");
        map.put("sort","stars");
        map.put("order","desc");
        map.put("page",String.valueOf(mPageCount));
    }

    private void requestDataModel(){

        RemoteManager.newInstance().getRepositories(map).enqueue(new Callback<RepoModel>() {
            @Override
            public void onResponse(Call<RepoModel> call, Response<RepoModel> response) {
                mData = response.body().getItems();
                addData();
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<RepoModel> call, Throwable t) {

            }
        });

    }

    private void addData(){
        mRecyclerViewPresenter.addAll(mData);
        mAdapter.notifyDataSetChanged();
    }

    private void clearData(){
        mRecyclerViewPresenter.clear();
        mAdapter.notifyDataSetChanged();
    }


    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView,int newState)
        {
            int totalItemCount = mLayoutManager.getItemCount();
            int lastVisibleItem = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();

            if (totalItemCount> 1 && lastVisibleItem >= totalItemCount - 1 )
            {
                    mSwipeRefreshLayout.setRefreshing(true);
                    mPageCount++;
                    requestDataModel();
            }
        }
    };

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onRefresh() {

    }
}



