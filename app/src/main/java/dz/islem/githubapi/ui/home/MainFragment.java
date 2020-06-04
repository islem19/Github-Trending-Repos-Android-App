package dz.islem.githubapi.ui.home;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import dz.islem.githubapi.App;
import dz.islem.githubapi.R;
import dz.islem.githubapi.data.DataManager;
import dz.islem.githubapi.ui.base.BaseFragment;
import dz.islem.githubapi.utils.Constants;
import dz.islem.githubapi.utils.Util;

public class MainFragment extends BaseFragment<MainViewModel> implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private View mView;
    private MainAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    @BindView(R.id.actionButton)
    FloatingActionButton mFloatingActionButton;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;


    public static MainFragment getInstance() {
        return new MainFragment();
    }

    @Override
    public MainViewModel getViewModel() {
        MainViewModelFactory factory = new MainViewModelFactory(DataManager.getInstance(App.getInstance()));
        return new ViewModelProvider(getActivity(), factory).get(MainViewModel.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.main_fragment, container, false);
        ButterKnife.bind(this,mView);
        initView();
        setupRecycler();

        return mView;
    }

    @Override
    public void onStart () {
        super.onStart();
        viewModel.getRepos().observe(this, itemModels -> {
            mAdapter.addData(itemModels);
            updateRefreshLayout(false);
        });
        viewModel.getError().observe(this, isError -> {
            if(isError) {
                displaySnackbar(true, "Can't load more github repos");
                updateRefreshLayout(false);
            }
        });

        if (DataManager.getInstance(App.getInstance()).getDate() == null )
            DataManager.getInstance(App.getInstance()).setDate(Util.getDefaultDate());

        updateRefreshLayout(true);
        displaySnackbar(false,"Loading...");
        viewModel.loadRepos(DataManager.getInstance(App.getInstance()).getDate());

    }

    private void initView(){
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
        android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mFloatingActionButton.setOnClickListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    private void setupRecycler(){
        mLayoutManager = new LinearLayoutManager(App.getInstance());
        mAdapter = new MainAdapter();
        mRecyclerView.setLayoutManager(mLayoutManager);
        int scrollPosition = 0;
        mRecyclerView.scrollToPosition(scrollPosition);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(App.getInstance(),DividerItemDecoration.VERTICAL));
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
                if (Util.isNetworkAvailable(App.getInstance())){
                    Constants.PAGE_COUNT++;
                    displaySnackbar(false, "Loading...");
                    viewModel.loadRepos(DataManager.getInstance(App.getInstance()).getDate());

                }
                else
                    displaySnackbar(true,"No internet Connection ! ");
            }
        }
    };

    @Override
    public void onClick(View view) {
        new DatePickerDialog(view.getContext(), date,
                Util.getYear(DataManager.getInstance(App.getInstance()).getDate()),
                Util.getMonth(DataManager.getInstance(App.getInstance()).getDate())-1,
                Util.getDay(DataManager.getInstance(App.getInstance()).getDate())).show();
    }

    public DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {

        DataManager.getInstance(App.getInstance()).setDate(Util.formatDate(year,monthOfYear+1,dayOfMonth));

        mAdapter.clearData();
        Constants.PAGE_COUNT = 1;

        if(Util.isNetworkAvailable(App.getInstance())){
            showError(View.GONE);
            displaySnackbar(false,"Loading...");
            viewModel.loadRepos(DataManager.getInstance(App.getInstance()).getDate());
        }
        else {
            showError(View.VISIBLE);
            displaySnackbar(true,"No Internet Connection :(");
        }
    };

    @Override
    public void onRefresh() {
        Constants.PAGE_COUNT = 1;
        mAdapter.clearData();

        if(Util.isNetworkAvailable(App.getInstance())){
            showError(View.GONE);
            displaySnackbar(false,"Loading...");
            viewModel.loadRepos(DataManager.getInstance(App.getInstance()).getDate());
        }else {
            updateRefreshLayout(false);
            showError(View.VISIBLE);
            displaySnackbar(true,"No Internet Connection :(");
        }
    }

    private void updateRefreshLayout(boolean refresh) {
        mSwipeRefreshLayout.setRefreshing(refresh);
    }

    private void showError(int Visibility){
        getActivity().findViewById(R.id.sample_main_layout).findViewById(R.id.imgview).setVisibility(Visibility);
    }

    private void displaySnackbar(boolean isError, String message){
        Util.showSnack(mView, isError, message);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewModel.onClear();
    }

}



