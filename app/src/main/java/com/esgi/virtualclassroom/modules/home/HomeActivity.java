package com.esgi.virtualclassroom.modules.home;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.esgi.virtualclassroom.R;
import com.esgi.virtualclassroom.modules.classroom.ClassroomActivity;
import com.esgi.virtualclassroom.modules.home.adapters.ViewPagerAdapter;
import com.esgi.virtualclassroom.modules.home.fragments.CurrentClassroomsFragment;
import com.esgi.virtualclassroom.modules.home.fragments.PastClassroomsFragment;
import com.esgi.virtualclassroom.modules.home.fragments.UpcomingClassroomsFragment;

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
        initializeViewPager();
    }

    private void initializeViewPager() {
        CurrentClassroomsFragment currentClassroomsFragment = new CurrentClassroomsFragment();
        UpcomingClassroomsFragment upcomingClassroomsFragment = new UpcomingClassroomsFragment();
        PastClassroomsFragment pastClassroomsFragment = new PastClassroomsFragment();
        currentClassroomsFragment.setPresenter(presenter);
        upcomingClassroomsFragment.setPresenter(presenter);
        pastClassroomsFragment.setPresenter(presenter);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(currentClassroomsFragment, getString(R.string.home_fragment_title_current));
        adapter.addFragment(upcomingClassroomsFragment, getString(R.string.home_fragment_title_upcoming));
        adapter.addFragment(pastClassroomsFragment, getString(R.string.home_fragment_title_past));

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void goToClassroom(String classroom) {
        Intent intent = new Intent(this, ClassroomActivity.class);
        startActivity(intent);
    }
}
