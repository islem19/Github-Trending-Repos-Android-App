package dz.islem.githubapi.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import butterknife.ButterKnife;
import dz.islem.githubapi.R;
import dz.islem.githubapi.ui.base.BaseActivity;
import dz.islem.githubapi.ui.home.MainActivity;

public class SplashActivity extends BaseActivity<SplashViewModel> {
    private static final long DELAY = 1500;

    @Override
    public SplashViewModel createViewModel() {
        return null;
    }

    @Override
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        mainScreen();
    }
    
    private void mainScreen(){
        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }, DELAY);
    }
}
