package themedicall.com;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import themedicall.com.GetterSetter.PharmacyGetterSetter;
import themedicall.com.Globel.CustomProgressDialog;


import java.util.ArrayList;
import java.util.List;


public class NearByPharmacyFragment extends Fragment {
    public static RecyclerView recyclerView_nearBy_pharmacy;
    public List<PharmacyGetterSetter> nearByPharmacyList;
    public static TextView emptyViewNearByList;
    public static ProgressBar bar;
    public static LinearLayoutManager linearLayoutManager ;
    CustomProgressDialog dialog;

    public NearByPharmacyFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_near_by_pharmacy, container, false);


        initiate(view);

        return view;
    }

    public void initiate(View view)
    {


        emptyViewNearByList = (TextView) view.findViewById(R.id.emptyViewNearByList);
        bar = (ProgressBar) view.findViewById(R.id.loadmore_Near_by_progress);
        recyclerView_nearBy_pharmacy = (RecyclerView) view.findViewById(R.id.recycler_view_nearby_pharmacy);
        recyclerView_nearBy_pharmacy.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity() , LinearLayoutManager.VERTICAL, false);
        recyclerView_nearBy_pharmacy.setLayoutManager(linearLayoutManager);

        nearByPharmacyList = new ArrayList<>();

    }

}
