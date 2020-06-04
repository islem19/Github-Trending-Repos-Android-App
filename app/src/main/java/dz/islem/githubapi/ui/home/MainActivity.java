package dz.islem.githubapi.ui.home;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.lifecycle.ViewModelProvider;

import butterknife.BindView;
import butterknife.ButterKnife;
import dz.islem.githubapi.App;
import dz.islem.githubapi.R;
import dz.islem.githubapi.data.DataManager;
import dz.islem.githubapi.ui.base.BaseActivity;
import dz.islem.githubapi.utils.Util;

public class MainActivity extends BaseActivity<MainViewModel> {

    @BindView(R.id.sample_main_layout)
    LinearLayout mainLayout;
    @BindView(R.id.imgview)
    ImageView imageView;

    @Override
    public MainViewModel createViewModel() {
        MainViewModelFactory factory = new MainViewModelFactory(DataManager.getInstance(App.getInstance()));
        return new ViewModelProvider(this,factory).get(MainViewModel.class);
    }

    @Override
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (Util.isNetworkAvailable(App.getInstance()))
            getSupportFragmentManager().beginTransaction()
            .replace(R.id.sample_content_fragment, MainFragment.getInstance())
            .commit();
        else {
            Util.showSnack(mainLayout,true,"No Internet Connection! ");
            imageView.setVisibility(View.VISIBLE);
        }
    }
}
