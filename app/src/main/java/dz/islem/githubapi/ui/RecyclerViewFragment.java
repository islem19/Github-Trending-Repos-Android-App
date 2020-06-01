package dz.islem.githubapi.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
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

import java.util.List;

import dz.islem.githubapi.R;
import dz.islem.githubapi.ui.interfaces.IRecyclerViewFragment;
import dz.islem.githubapi.data.model.ItemModel;
import dz.islem.githubapi.utils.Constants;
import dz.islem.githubapi.utils.Util;

public class RecyclerViewFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener , IRecyclerViewFragment {
    private View mView;
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerViewPresenter mRecyclerViewPresenter;


    public static RecyclerViewFragment newInstance() {
        return new RecyclerViewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRecyclerViewPresenter = new RecyclerViewPresenter(this);
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
        mRecyclerViewPresenter.searchGithubRepos(Util.getSharedPrefs(getContext()));
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
        mAdapter = new RecyclerAdapter();
        mRecyclerView.setLayoutManager(mLayoutManager);
        int scrollPosition = 0;
        mRecyclerView.scrollToPosition(scrollPosition);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addOnScrollListener(mOnScrollListener);
    }

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView,int newState) {
            int totalItemCount = mLayoutManager.getItemCount();
            int lastVisibleItem = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();

            if (totalItemCount> 1 && lastVisibleItem >= totalItemCount - 1 )
            {
                if (Util.isNetworkAvailable(getContext())){
                    Constants.PAGE_COUNT++;
                    mRecyclerViewPresenter.searchGithubRepos(Util.getSharedPrefs(getContext()));
                }
                else
                    displayError("No Internet Connection :(");
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

            mRecyclerViewPresenter.clear();
            Constants.PAGE_COUNT = 1;

            if(Util.isNetworkAvailable(getContext())){
                showError(View.GONE);
                mRecyclerViewPresenter.searchGithubRepos(Util.getSharedPrefs(getContext()));
            }
            else {
                showError(View.VISIBLE);
                displayError("No Internet Connection :(");
            }
        }

    };

    @Override
    public void onRefresh() {
        Constants.PAGE_COUNT = 1;
        mRecyclerViewPresenter.clear();

        if(Util.isNetworkAvailable(getContext())){
            showError(View.GONE);
            mRecyclerViewPresenter.searchGithubRepos(Util.getSharedPrefs(getContext()));
        }else {
            updateRefreshLayout(false);
            showError(View.VISIBLE);
            displayError("No Internet Connection :(");
        }
    }

    @Override
    public void notifySearchResults(List<ItemModel> mData) {
        mAdapter.setData(mData);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void display(String message) {
        Util.showSnack(mView,false,message);
    }

    @Override
    public void displayError(String error) {
        Util.showSnack(mView,true,error);
    }

    @Override
    public void updateRefreshLayout(boolean refresh) {
        mSwipeRefreshLayout.setRefreshing(refresh);
    }

    private void showError(int Visibility){
        getActivity().findViewById(R.id.sample_main_layout).findViewById(R.id.imgview).setVisibility(Visibility);
    }
}



