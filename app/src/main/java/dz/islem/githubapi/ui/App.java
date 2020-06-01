package dz.islem.githubapi.ui;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import dz.islem.githubapi.R;
import dz.islem.githubapi.utils.Util;

public class App extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Util.isNetworkAvailable(getApplicationContext()))
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.sample_content_fragment, RecyclerViewFragment.newInstance())
                    .commit();
        else {
            Util.showSnack(findViewById(R.id.sample_main_layout),true,"No Internet Connection! ");
            findViewById(R.id.imgview).setVisibility(View.VISIBLE);
        }
    }
}
