package themedicall.com.MediaPediaSection;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import themedicall.com.MediPediaAdapter.FindBrandAdapter;
import themedicall.com.MediPediaGetterSetter.ListInfo;
import themedicall.com.NavigationDrawer;
import themedicall.com.R;

import java.util.ArrayList;

public class FindBrand extends NavigationDrawer {

    View view;
    RecyclerView recyclerView;
    FindBrandAdapter findBrandAdapter;
    public static ArrayList<ListInfo> genericNames = new ArrayList<ListInfo>();
    static Boolean fd =false;
    GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_find_brand);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.activity_find_brand, null, false);
        drawer.addView(view, 0);


        initiate();

    }

    private void initiate() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewFindDoctor);
        findBrandAdapter = new FindBrandAdapter(FindBrand.this,genericNames);
        gridLayoutManager = new GridLayoutManager(FindBrand.this , 1);
        for (int i=0; i<20;i++){
            genericNames.add(new ListInfo("Sample Name",""+i));
        }
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(findBrandAdapter);
        fd = true;
        MediPedia.bBool=false;
    }

}
