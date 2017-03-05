package com.chanpyaeaung.moviez.view.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.chanpyaeaung.moviez.R;
import com.chanpyaeaung.moviez.view.fragment.MovieDetailFragment;
import com.chanpyaeaung.moviez.view.fragment.MoviesListFragment;
import com.chanpyaeaung.moviez.view.fragment.WebFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Chan Pyae Aung on 4/3/17.
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setHomeFragment();
    }

    private void setHomeFragment() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.framelayout, new MoviesListFragment())
                .commit();
    }


    public void setFragment(Fragment fragment) {

        getSupportFragmentManager().beginTransaction()
                .add(R.id.framelayout, fragment)
                .addToBackStack(null)
                .commit();
    }

    public Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.framelayout);
    }

    @Override
    public void onBackPressed() {

//        if(getCurrentFragment() instanceof MovieDetailFragment) {
//            setFragment(new MoviesListFragment());
//        } else if(getCurrentFragment() instanceof WebFragment) {
//            super.onBackPressed();
//        } else {
//            super.onBackPressed();
//        }

        if(getSupportFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed();
        } else {
            getSupportFragmentManager().popBackStack();
        }

    }
}
