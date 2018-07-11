package com.esgi.virtualclassroom.modules.home;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.esgi.virtualclassroom.R;
import com.esgi.virtualclassroom.modules.classrooms.ClassroomsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements HomeView {
    private HomePresenter presenter;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.home_view_pager) ViewPager viewPager;
    @BindView(R.id.home_tab_layout) TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new HomePresenter(this);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        HomeViewPagerAdapter adapter = new HomeViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(ClassroomsFragment.newInstance(getString(R.string.home_fragment_title_live)), getString(R.string.home_fragment_title_live));
        adapter.addFragment(ClassroomsFragment.newInstance(getString(R.string.home_fragment_title_upcoming)), getString(R.string.home_fragment_title_upcoming));
        adapter.addFragment(ClassroomsFragment.newInstance(getString(R.string.home_fragment_title_past)), getString(R.string.home_fragment_title_past));

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
