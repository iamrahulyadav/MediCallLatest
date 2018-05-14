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

import java.util.ArrayList;
import java.util.List;


public class AllPharmacyFragment extends Fragment {
    public static RecyclerView recyclerView_all_pharmacy;
    public  List<PharmacyGetterSetter> allPharmacyList;
    public static TextView emptyViewAllPharmacyList;
    public static LinearLayoutManager linearLayoutManager ;
    public static ProgressBar bar;
    public AllPharmacyFragment() {
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
        View view = inflater.inflate(R.layout.fragment_all_pharmacy, container, false);


        initiate(view);

        return view;
    }

    public void initiate(View view)
    {
        emptyViewAllPharmacyList = (TextView) view.findViewById(R.id.emptyViewAllPharmacyList);
        bar = (ProgressBar) view.findViewById(R.id.loadmore_all_pharmacy_progress);
        recyclerView_all_pharmacy = (RecyclerView) view.findViewById(R.id.recycler_view_all_pharmacy);
        recyclerView_all_pharmacy.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity() , LinearLayoutManager.VERTICAL, false);
        recyclerView_all_pharmacy.setLayoutManager(linearLayoutManager);
        allPharmacyList = new ArrayList<>();
    }


}
