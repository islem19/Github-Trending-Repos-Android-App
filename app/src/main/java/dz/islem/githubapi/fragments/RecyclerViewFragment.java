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
import java.util.List;

import dz.islem.githubapi.R;
import dz.islem.githubapi.adapters.RecyclerAdapter;
import dz.islem.githubapi.models.ItemModel;
import dz.islem.githubapi.presenters.RecyclerViewPresenter;

public class RecyclerViewFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private View mView;
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<ItemModel> mData = new ArrayList<>();
    private RecyclerViewPresenter mRecyclerViewPresenter;

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

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onRefresh() {

    }
}



