package dz.islem.githubapi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import dz.islem.githubapi.fragments.RecyclerViewFragment;

public class App extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null)
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.sample_content_fragment, RecyclerViewFragment.newInstance())
                    .commit();

    }
}
