package themedicall.com;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import themedicall.com.Adapter.HealthEventListRecycleView;
import themedicall.com.GetterSetter.HealthEventGetterSetter;


import java.util.ArrayList;
import java.util.List;

public class HealthEvents extends NavigationDrawer {

    RecyclerView recyclerView_health_events;
    List<HealthEventGetterSetter> healthEventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_health_events);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.activity_health_events, null, false);
        drawer.addView(view, 0);


        initiate();
        prepareHappyUserList();
    }

    public void initiate()
    {
        recyclerView_health_events = (RecyclerView) findViewById(R.id.recycler_view_health_events);
        recyclerView_health_events.setHasFixedSize(true);
        recyclerView_health_events.setLayoutManager(new LinearLayoutManager(HealthEvents.this , LinearLayoutManager.VERTICAL , false));
        healthEventList = new ArrayList<>();
    }

    public void prepareHappyUserList()
    {
        healthEventList.add(new HealthEventGetterSetter("1", R.drawable.doctor , "Muhammad Adeel" , "09-27-2017" , R.drawable.name , "09-27-2017" , "04:00 PM", "09-27-2017" , "07:00 PM"  , "Blood Donation Camp" , "122 b Comercial Area Sector C , Bahria Town Lahore" , "10" , "10" , "10" , "10"));
        healthEventList.add(new HealthEventGetterSetter("1", R.drawable.doctor , "Muhammad Adeel" , "09-27-2017" , R.drawable.name , "09-27-2017" , "04:00 PM", "09-27-2017" , "07:00 PM"  , "Blood Donation Camp" , "122 b Comercial Area Sector C , Bahria Town Lahore" , "10" , "10" , "10" , "10"));
        healthEventList.add(new HealthEventGetterSetter("1", R.drawable.doctor , "Muhammad Adeel" , "09-27-2017" , R.drawable.name , "09-27-2017" , "04:00 PM", "09-27-2017" , "07:00 PM"  , "Blood Donation Camp" , "122 b Comercial Area Sector C , Bahria Town Lahore" , "10" , "10" , "10" , "10"));
        healthEventList.add(new HealthEventGetterSetter("1", R.drawable.doctor , "Muhammad Adeel" , "09-27-2017" , R.drawable.name , "09-27-2017" , "04:00 PM", "09-27-2017" , "07:00 PM"  , "Blood Donation Camp" , "122 b Comercial Area Sector C , Bahria Town Lahore" , "10" , "10" , "10" , "10"));
        healthEventList.add(new HealthEventGetterSetter("1", R.drawable.doctor , "Muhammad Adeel" , "09-27-2017" , R.drawable.name , "09-27-2017" , "04:00 PM", "09-27-2017" , "07:00 PM"  , "Blood Donation Camp" , "122 b Comercial Area Sector C , Bahria Town Lahore" , "10" , "10" , "10" , "10"));
        healthEventList.add(new HealthEventGetterSetter("1", R.drawable.doctor , "Muhammad Adeel" , "09-27-2017" , R.drawable.name , "09-27-2017" , "04:00 PM", "09-27-2017" , "07:00 PM"  , "Blood Donation Camp" , "122 b Comercial Area Sector C , Bahria Town Lahore" , "10" , "10" , "10" , "10"));
        healthEventList.add(new HealthEventGetterSetter("1", R.drawable.doctor , "Muhammad Adeel" , "09-27-2017" , R.drawable.name , "09-27-2017" , "04:00 PM", "09-27-2017" , "07:00 PM"  , "Blood Donation Camp" , "122 b Comercial Area Sector C , Bahria Town Lahore" , "10" , "10" , "10" , "10"));
        healthEventList.add(new HealthEventGetterSetter("1", R.drawable.doctor , "Muhammad Adeel" , "09-27-2017" , R.drawable.name , "09-27-2017" , "04:00 PM", "09-27-2017" , "07:00 PM"  , "Blood Donation Camp" , "122 b Comercial Area Sector C , Bahria Town Lahore" , "10" , "10" , "10" , "10"));
        healthEventList.add(new HealthEventGetterSetter("1", R.drawable.doctor , "Muhammad Adeel" , "09-27-2017" , R.drawable.name , "09-27-2017" , "04:00 PM", "09-27-2017" , "07:00 PM"  , "Blood Donation Camp" , "122 b Comercial Area Sector C , Bahria Town Lahore" , "10" , "10" , "10" , "10"));
        healthEventList.add(new HealthEventGetterSetter("1", R.drawable.doctor , "Muhammad Adeel" , "09-27-2017" , R.drawable.name , "09-27-2017" , "04:00 PM", "09-27-2017" , "07:00 PM"  , "Blood Donation Camp" , "122 b Comercial Area Sector C , Bahria Town Lahore" , "10" , "10" , "10" , "10"));



        HealthEventListRecycleView healthEventListRecycleView = new HealthEventListRecycleView(healthEventList);
        recyclerView_health_events.setAdapter(healthEventListRecycleView);
        healthEventListRecycleView.notifyDataSetChanged();



    }

}
