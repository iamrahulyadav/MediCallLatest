package themedicall.com.MediaPediaSection;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import themedicall.com.MediPediaAdapter.CompanyAdapter;
import themedicall.com.MediPediaGetterSetter.ListInfo;
import themedicall.com.NavigationDrawer;
import themedicall.com.R;

import java.util.ArrayList;
import java.util.List;

public class CompnyDetail extends NavigationDrawer {

    RecyclerView recyclerView;
    LayoutInflater layoutInflater;
    View view;
    LinearLayoutManager linearLayoutManager;
    //DividerItemDecoration dividerItemDecoration;
    CompanyAdapter brandAdapter;
    public static ArrayList<ListInfo> genericNames = new ArrayList<ListInfo>();
    public static ArrayList<ListInfo> brandNames = new ArrayList<ListInfo>();
    public static ArrayList<ListInfo> companyNames = new ArrayList<ListInfo>();
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_compny_detail);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);


        MediPedia.cBool = false;
        layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.activity_compny_detail, null, false);
        drawer.addView(view, 0);

        instantiate();

        brandNames.add(new ListInfo("Huzaifa",""+1));
        brandNames.add(new ListInfo("Asif",""+2));
        brandNames.add(new ListInfo("Ali",""+3));
        brandNames.add(new ListInfo("Kashif",""+4));
        brandNames.add(new ListInfo("Junaid",""+5));
        brandNames.add(new ListInfo("Adeel",""+6));
        brandNames.add(new ListInfo("Huzaifa",""+7));
        brandNames.add(new ListInfo("Asif",""+8));
        brandNames.add(new ListInfo("Ali",""+9));
        brandNames.add(new ListInfo("Kashif",""+10));
        brandNames.add(new ListInfo("Junaid",""+11));
        brandNames.add(new ListInfo("Adeel",""+12));

    }

    private void instantiate() {

        // recyclerView = (RecyclerView) findViewById(R.id.recycler_view_brands_list);
        //linearLayoutManager = new LinearLayoutManager(CompanyDetails.this, LinearLayoutManager.HORIZONTAL, false );
        //dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        //recyclerView.addItemDecoration(dividerItemDecoration);
        //recyclerView.setLayoutManager(linearLayoutManager);
        //brandAdapter = new CompanyAdapter(CompanyDetails.this,brandNames);
        //recyclerView.setAdapter(brandAdapter);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        //viewPager.setOffscreenPageLimit(2);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CompanyInfoFragment(), "Company Info");
        adapter.addFragment(new CompanyBrandFragment(), "Company Brands");
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

}
