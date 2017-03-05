package com.chanpyaeaung.moviez.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.chanpyaeaung.moviez.R;
import com.chanpyaeaung.moviez.view.fragment.MoviesListFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Chan Pyae Aung on 4/3/17.
 */

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);


        getSupportFragmentManager().beginTransaction()
                .add(R.id.framelayout, new MoviesListFragment())
                .commit();
    }


}
