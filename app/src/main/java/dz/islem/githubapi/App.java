package dz.islem.githubapi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import dz.islem.githubapi.fragments.RecyclerViewFragment;
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
            Util.showSnack(findViewById(R.id.sample_main_layout),false);
            ImageView img = findViewById(R.id.imgview);
            img.setVisibility(View.VISIBLE);
        }
    }
}
