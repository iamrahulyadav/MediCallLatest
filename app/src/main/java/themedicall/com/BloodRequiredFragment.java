package themedicall.com;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class BloodRequiredFragment extends Fragment {

    ImageView becomeBloodDonor , saveLife , appealBlood , bloodCompetibility , bloodfacts;
    ImageView apos , aneg , bpos , bneg , opos , oneg , abpos , abneg ;
    public BloodRequiredFragment() {
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
        View view =  inflater.inflate(R.layout.fragment_blood_required, container, false);



        initiate(view);
        clickListener();

        return view;

    }


    public void initiate(View view)
    {
        becomeBloodDonor = (ImageView) view.findViewById(R.id.becomeBloodDonor);
        appealBlood = (ImageView) view.findViewById(R.id.appealBlood);
        saveLife = (ImageView) view.findViewById(R.id.saveLife);
        bloodCompetibility = (ImageView) view.findViewById(R.id.bloodCompetibility);
        bloodfacts = (ImageView) view.findViewById(R.id.bloodfacts);

        apos = (ImageView) view.findViewById(R.id.apos);
        aneg = (ImageView) view.findViewById(R.id.aneg);
        bpos = (ImageView) view.findViewById(R.id.bpos);
        bneg = (ImageView) view.findViewById(R.id.bneg);
        opos = (ImageView) view.findViewById(R.id.opos);
        oneg = (ImageView) view.findViewById(R.id.oneg);
        abpos = (ImageView) view.findViewById(R.id.abpos);
        abneg = (ImageView) view.findViewById(R.id.abneg);
    }


    public void clickListener()
    {
        apos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext() , BloodGroupListing.class);
                intent.putExtra("blood_group_id" , "1");
                startActivity(intent);
            }
        });

        aneg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext() , BloodGroupListing.class);
                intent.putExtra("blood_group_id" , "2");
                startActivity(intent);
            }
        });


        bpos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext() , BloodGroupListing.class);
                intent.putExtra("blood_group_id" , "3");
                startActivity(intent);
            }
        });


        bneg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext() , BloodGroupListing.class);
                intent.putExtra("blood_group_id" , "4");
                startActivity(intent);
            }
        });



        opos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext() , BloodGroupListing.class);
                intent.putExtra("blood_group_id" , "5");
                startActivity(intent);
            }
        });



        oneg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext() , BloodGroupListing.class);
                intent.putExtra("blood_group_id" , "6");
                startActivity(intent);
            }
        });


        abpos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext() , BloodGroupListing.class);
                intent.putExtra("blood_group_id" , "7");
                startActivity(intent);
            }
        });


        abneg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext() , BloodGroupListing.class);
                intent.putExtra("blood_group_id" , "8");
                startActivity(intent);
            }
        });



        becomeBloodDonor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SignUp.class);
                intent.putExtra("item_position" , 6);
                startActivity(intent);
            }
        });

        saveLife.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Blood.viewPager.setCurrentItem(1);

            }
        });


        appealBlood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity() , AppealBlood.class);
                startActivity(intent);
            }
        });

        bloodCompetibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity() , BloodComptatibility.class);
                startActivity(intent);
            }
        });


        bloodfacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity() , BloodFacts.class);
                startActivity(intent);
            }
        });


    }



//    public void setBloodGroup()
//    {
//        BloodGroupCustomAdapterGridView bloodGroupCustomAdapterGridView = new BloodGroupCustomAdapterGridView(getContext(), gridViewBloodGroupImageId);
//        bloodGroupGridView.setAdapter(bloodGroupCustomAdapterGridView);
//        bloodGroupCustomAdapterGridView.notifyDataSetChanged();
//
//        bloodGroupGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int i, long id) {
//
//                if(i == 0)
//                {
//                    Intent intent = new Intent(getContext() , BloodGroupListing.class);
//                    intent.putExtra("blood_group_id" , "1");
//                    startActivity(intent);
//                }
//                else if(i == 1)
//                {
//                    Intent intent = new Intent(getContext() , BloodGroupListing.class);
//                    intent.putExtra("blood_group_id" , "2");
//                    startActivity(intent);
//                }
//                else if(i == 2)
//                {
//                    Intent intent = new Intent(getContext() , BloodGroupListing.class);
//                    intent.putExtra("blood_group_id" , "3");
//                    startActivity(intent);
//                }
//                else if(i == 3)
//                {
//                    Intent intent = new Intent(getContext() , BloodGroupListing.class);
//                    intent.putExtra("blood_group_id" , "4");
//                    startActivity(intent);
//                }
//                else if(i == 4)
//                {
//                    Intent intent = new Intent(getContext() , BloodGroupListing.class);
//                    intent.putExtra("blood_group_id" , "5");
//                    startActivity(intent);
//                }
//                else if(i == 5)
//                {
//                    Intent intent = new Intent(getContext() , BloodGroupListing.class);
//                    intent.putExtra("blood_group_id" , "6");
//                    startActivity(intent);
//                }
//                else if(i == 6)
//                {
//                    Intent intent = new Intent(getContext() , BloodGroupListing.class);
//                    intent.putExtra("blood_group_id" , "7");
//                    startActivity(intent);
//                }
//                else if(i == 7)
//                {
//                    Intent intent = new Intent(getContext() , BloodGroupListing.class);
//                    intent.putExtra("blood_group_id" , "8");
//                    startActivity(intent);
//                }
//
//
//            }
//        });
//    }
}
