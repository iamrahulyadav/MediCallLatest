package themedicall.com;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;


public class Jobs extends NavigationDrawer {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    public static ViewPager viewPager;

    // Custom Action bar
    View customActionBarView ;
    SearchView searchView;
    Button locationFilter;
    ImageView userIcon;
    RelativeLayout.LayoutParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_jobs);
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM );
        getSupportActionBar().setCustomView(R.layout.custom_actionbar_navigation_drawer);
        customActionBarView =getSupportActionBar().getCustomView();
        getSupportActionBar().setElevation(0);

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.activity_jobs, null, false);
        drawer.addView(view, 0);

        initiate();
        customActionBarChecks();
        searchView();



    }

    public void customActionBarChecks()
    {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(viewPager.getCurrentItem() == 1)
                {
                    locationFilter.setVisibility(View.GONE);
                    searchView.setVisibility(View.GONE);
                    params = (RelativeLayout.LayoutParams)userIcon.getLayoutParams();
                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                }
                else
                {
                    locationFilter.setVisibility(View.VISIBLE);
                    searchView.setVisibility(View.VISIBLE);
                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT , 0);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    public void initiate()
    {

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        locationFilter = (Button) customActionBarView.findViewById(R.id.locationFilter);
        userIcon = (ImageView) customActionBarView.findViewById(R.id.userIcon);
        searchView = (SearchView) customActionBarView.findViewById(R.id.searchView);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new JobsAvailableFragment(), "Available Jobs");
        adapter.addFragment(new JobsRequestFragment(), "Post Job");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public void searchView()
    {
        searchView.setMaxWidth(Integer.MAX_VALUE);
        EditText searchEditText = (EditText)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setHintTextColor(ContextCompat.getColor(getApplicationContext(), R.color.whiteColor));
        searchEditText.setHint("Search emergency contact");


    }
}
