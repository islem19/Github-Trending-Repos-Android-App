package dz.islem.githubapi;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;


import androidx.fragment.app.Fragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import dz.islem.githubapi.fragments.RecyclerViewFragment;
import dz.islem.githubapi.utils.Util;

import static java.sql.DriverManager.println;
import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;


@Config(sdk = Build.VERSION_CODES.P)
@RunWith(RobolectricTestRunner.class)
@PowerMockIgnore({ "org.mockito.*", "org.robolectric.*", "android.*", "androidx.*" })
@PrepareForTest(Util.class)
public class AppTest {

    private Activity mActivity;
    private Context mContext;

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Before
    public void setUp(){
        mActivity = Robolectric.buildActivity(App.class).create().get();
        mContext = mActivity.getApplicationContext();
    }

    @Test
    public void checkAppActivity(){
        println("Check App activity...");
        assertNotNull(mActivity);
    }

    @Test
    public void checkAppName(){

        String mAppName = mActivity.getResources().getString(R.string.app_name);
        assertEquals("check App name","Github Trending Repos",mAppName);
    }

    @Test
    public void checkPermissionApp() throws Exception {

        String[] actualPermissions = mActivity.getPackageManager().getPackageInfo(mActivity.getPackageName(), PackageManager.GET_PERMISSIONS).requestedPermissions;

        String[] expectedPermissions = new String[] { Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE , Manifest.permission.ACCESS_WIFI_STATE};

        assertArrayEquals("check permissions",expectedPermissions,actualPermissions);

    }

    @Test
    public void checkImageViewParam(){

        ImageView mImageView = mActivity.findViewById(R.id.imgview);

        assertNotNull(mImageView);

        int mWidth = mImageView.getLayoutParams().width;
        assertEquals("Width is not the same", 400,mWidth);

        int mHeight = mImageView.getLayoutParams().height;
        assertEquals("Height is not the same", 700,mHeight);
    }

    @Test
    public void imageViewShouldBeGone(){

        PowerMockito.mockStatic(Util.class);
        Mockito.when(Util.isNetworkAvailable(mContext)).thenReturn(true);

        mActivity = Robolectric.buildActivity(App.class).create().restart().get();

        ImageView mImageView = mActivity.findViewById(R.id.imgview);
        assertThat(mImageView.getVisibility(), equalTo(View.GONE));
    }

    @Test
    public void imageViewShouldBeVisible(){

        PowerMockito.mockStatic(Util.class);
        Mockito.when(Util.isNetworkAvailable(mContext)).thenReturn(false);

        mActivity = Robolectric.buildActivity(App.class).create().restart().get();

        ImageView mImageView = mActivity.findViewById(R.id.imgview);
        assertThat(mImageView.getVisibility(), equalTo(View.VISIBLE));
    }


    @Test
    public void canLoadRecyclerFragment(){

        PowerMockito.mockStatic(Util.class);
        Mockito.when(Util.isNetworkAvailable(mContext)).thenReturn(true);

        mActivity = Robolectric.buildActivity(App.class).create().restart().get();

        Fragment mFragment = ((App) mActivity).getSupportFragmentManager().findFragmentById(R.id.sample_content_fragment);

        assertNotNull(mFragment);
        assertTrue(mFragment instanceof RecyclerViewFragment);
    }

    @Test
    public void canNotLoadRecyclerFragment(){

        PowerMockito.mockStatic(Util.class);
        Mockito.when(Util.isNetworkAvailable(mContext)).thenReturn(false);

        mActivity = Robolectric.buildActivity(App.class).create().restart().get();

        Fragment mFragment = ((App) mActivity).getSupportFragmentManager().findFragmentById(R.id.sample_content_fragment);

        assertFalse(mFragment instanceof RecyclerViewFragment);
    }

}

