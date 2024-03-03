package com.example.thesismaster_v10;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.thesismaster_v10.Adapter.QuestionAdapter;
import com.example.thesismaster_v10.Adapter.VPAdapter;
import com.example.thesismaster_v10.DBController.DatabaseHelper;
import com.example.thesismaster_v10.Model.QuestionModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity{


    private TabLayout tabLayout;
    private ViewPager viewPager;
    private VPAdapter vpAdapter;

    String value1;
    String value2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbarquestion);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);

        tabLayout.setupWithViewPager(viewPager);

        Bundle bundleget = getIntent().getExtras();
        if (bundleget != null) {
            value1 = bundleget.getString("thesis");
            value2 = bundleget.getString("theme");

        }
        OpenFragment openFragment = new OpenFragment();
        Bundle bundleput = new Bundle();
        bundleput.putString("thesis", value1);
        bundleput.putString("theme", value2);
        openFragment.setArguments(bundleput);

        DoneFragment doneFragment = new DoneFragment();
        Bundle bundleputdone = new Bundle();
        bundleputdone.putString("thesis", value1);
        bundleputdone.putString("theme", value2);
        doneFragment.setArguments(bundleputdone);

        vpAdapter = new VPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdapter.addFragment(openFragment, "OFFEN");

        vpAdapter.addFragment(doneFragment, "ERLEDIGT");

        viewPager.setAdapter(vpAdapter);

        }


}