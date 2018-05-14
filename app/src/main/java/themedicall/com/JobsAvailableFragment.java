package themedicall.com;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import themedicall.com.Adapter.JobRequiredRecycleView;
import themedicall.com.GetterSetter.JobRequiredGetterSetter;

import java.util.ArrayList;
import java.util.List;

public class JobsAvailableFragment extends Fragment {
    RecyclerView recyclerView_job_required;
    List<JobRequiredGetterSetter> jobRequiredList;
    public JobsAvailableFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_jobs_available, container, false);


        recyclerView_job_required = (RecyclerView) view.findViewById(R.id.recycler_view_job_required);
        recyclerView_job_required.setHasFixedSize(true);
        recyclerView_job_required.setLayoutManager(new LinearLayoutManager(getContext() , LinearLayoutManager.VERTICAL , false));
        jobRequiredList = new ArrayList<>();

        prepareHappyUserList();
        return view;
    }

    public void prepareHappyUserList()
    {

        jobRequiredList.add(new JobRequiredGetterSetter("1" , "Job For Doctor Job For Doctor Job For Doctor Job For Doctor" , "Lahore" , "Male"  , "25 - 30"));
        jobRequiredList.add(new JobRequiredGetterSetter("1" , "Job For Doctor Job For Doctor Job For Doctor Job For Doctor" , "Islamabad" , "Male"  , "25 - 40"));
        jobRequiredList.add(new JobRequiredGetterSetter("1" , "Job For Doctor Job For Doctor Job For Doctor Job For Doctor" , "Multan" , "Female"  , "25 - 30"));
        jobRequiredList.add(new JobRequiredGetterSetter("1" , "Job For Doctor Job For Doctor Job For Doctor Job For Doctor" , "Karachi" , "Male"  , "25 - 30"));
        jobRequiredList.add(new JobRequiredGetterSetter("1" , "Job For Doctor Job For Doctor Job For Doctor Job For Doctor" , "Peshawar" , "Female"  , "25 - 50"));
        jobRequiredList.add(new JobRequiredGetterSetter("1" , "Job For Doctor Job For Doctor Job For Doctor Job For Doctor" , "Faisalabad" , "Male"  , "25 - 30"));
        jobRequiredList.add(new JobRequiredGetterSetter("1" , "Job For Doctor Job For Doctor Job For Doctor Job For Doctor" , "Kashmir" , "Female"  , "25 - 70"));


        JobRequiredRecycleView jobRequiredRecycleView = new JobRequiredRecycleView(jobRequiredList);
        recyclerView_job_required.setAdapter(jobRequiredRecycleView);
        jobRequiredRecycleView.notifyDataSetChanged();



    }

}
