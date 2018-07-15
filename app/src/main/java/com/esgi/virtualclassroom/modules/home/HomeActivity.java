package com.esgi.virtualclassroom.modules.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.esgi.virtualclassroom.R;
import com.esgi.virtualclassroom.modules.classroomcreation.ClassroomCreationActivity;
import com.esgi.virtualclassroom.modules.classrooms.ClassroomsFragment;
import com.esgi.virtualclassroom.modules.login.LogInActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity implements HomeView {
    private HomePresenter presenter;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.home_view_pager) ViewPager viewPager;
    @BindView(R.id.home_tab_layout) TabLayout tabLayout;

    @OnClick(R.id.classroom_add_button)
    void onClassroomAddButtonClick() {
        this.presenter.onClassroomAddButtonClick();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new HomePresenter(this);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        HomeViewPagerAdapter adapter = new HomeViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(ClassroomsFragment.newInstance(getString(R.string.home_fragment_title_upcoming)), getString(R.string.home_fragment_title_upcoming));
        adapter.addFragment(ClassroomsFragment.newInstance(getString(R.string.home_fragment_title_live)), getString(R.string.home_fragment_title_live));
        adapter.addFragment(ClassroomsFragment.newInstance(getString(R.string.home_fragment_title_past)), getString(R.string.home_fragment_title_past));

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_classrooms_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_log_out:
                this.presenter.signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void signOut() {
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showClassroomCreationActivity() {
        Intent intent = new Intent(this, ClassroomCreationActivity.class);
        startActivity(intent);
    }
}
