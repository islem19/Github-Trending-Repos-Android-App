package dz.islem.githubapi.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

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
import dz.islem.githubapi.utils.Util;

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
    private static int mPageCount=1;

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
        mSwipeRefreshLayout.setRefreshing(true);
        requestDataModel(Util.getSharedPrefs(getContext()));
    }

    private void initView(){
        FloatingActionButton mFloatingActionButton = mView.findViewById(R.id.actionButton);
        mRecyclerView = mView.findViewById(R.id.recyclerView);
        mSwipeRefreshLayout = mView.findViewById(R.id.swipeRefreshLayout);
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
        map.put("q","created:>");
        map.put("sort","stars");
        map.put("order","desc");
        map.put("page",String.valueOf(mPageCount));
    }

    private void requestDataModel(String date){
        map.put("q","created:>"+date);

        RemoteManager.newInstance().getRepositories(map).enqueue(new Callback<RepoModel>() {
            @Override
            public void onResponse(Call<RepoModel> call, Response<RepoModel> response) {
                mData = response.body().getItems();
                if (!mData.isEmpty())
                    addData();
                else
                    Util.showSnack(mView,true,"Sorry! No More Repos :(");
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<RepoModel> call, Throwable t) {
                Log.e("tag", "onFailure: "+t.toString() );
                Util.showSnack(mView,true,"Sorry! can't load Repos, try again :(");
                mSwipeRefreshLayout.setRefreshing(false);
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
                if (Util.isNetworkAvailable(getContext())){
                    mSwipeRefreshLayout.setRefreshing(true);
                    mPageCount++;
                    requestDataModel(Util.getSharedPrefs(getContext()));
                }
                else
                    mSwipeRefreshLayout.setRefreshing(false);
                Util.showSnack(mView,Util.isNetworkAvailable(getContext()),null);
            }
        }
    };

    @Override
    public void onClick(View view) {
        new DatePickerDialog(getContext(), date,
                Util.getYear(getContext()),
                Util.getMonth(getContext())-1,
                Util.getDay(getContext())).show();
    }

    public DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            Util.putSharedPrefes(getContext(),year,monthOfYear+1,dayOfMonth);
            mSwipeRefreshLayout.setRefreshing(true);
            mPageCount = 1;
            clearData();
            loadData();
        }

    };

    @Override
    public void onRefresh() {
        mPageCount = 1;
        clearData();
        loadData();
    }

    private void loadData(){
        if (Util.isNetworkAvailable(getContext())){
            getActivity().findViewById(R.id.sample_main_layout).findViewById(R.id.imgview).setVisibility(View.GONE);
            requestDataModel(Util.getSharedPrefs(getContext()));
        }
        else{
            getActivity().findViewById(R.id.sample_main_layout).findViewById(R.id.imgview).setVisibility(View.VISIBLE);
            mSwipeRefreshLayout.setRefreshing(false);
        }
        Util.showSnack(mView,Util.isNetworkAvailable(getContext()),null);
    }
}



