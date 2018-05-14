package themedicall.com.MediaPediaSection;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import themedicall.com.MediPediaAdapter.BrandFormsAndSimilarMedicinesAdapter;
import themedicall.com.MediPediaGetterSetter.BrandFormsAndSimilarMedicineGetterSetter;
import themedicall.com.NavigationDrawer;
import themedicall.com.R;

import java.util.ArrayList;

public class BrandPage extends NavigationDrawer {

    LayoutInflater layoutInflater;
    View view;
    BrandFormsAndSimilarMedicinesAdapter adapter;
    RecyclerView recyclerView;
    RecyclerView recyclerViewSimilarMedicines;
    ArrayList<BrandFormsAndSimilarMedicineGetterSetter> listInfos;
    public static ArrayList<BrandFormsAndSimilarMedicineGetterSetter> drugForm = new ArrayList<BrandFormsAndSimilarMedicineGetterSetter>();
    public static ArrayList<BrandFormsAndSimilarMedicineGetterSetter> similerBrands = new ArrayList<BrandFormsAndSimilarMedicineGetterSetter>();
    LinearLayoutManager linearLayoutManager;
    LinearLayoutManager linearLayoutManager1;
    ScrollView scrollView;

    LinearLayout indication;
    LinearLayout precaution;
    LinearLayout contraIndication;
    LinearLayout dose;
    LinearLayout sideEffect;
    LinearLayout modeOfAction;
    LinearLayout interaction;
    LinearLayout pregnancyType;
    LinearLayout pregnancyTypeDescription;
    LinearLayout indicationfocused;
    LinearLayout precautionfocused;
    LinearLayout contraIndicationfocused;
    LinearLayout dosefocused;
    LinearLayout sideEffectfocused;
    LinearLayout modeOfActionfocused;
    LinearLayout interactionfocused;
    LinearLayout pregnancyTypefocused;
    LinearLayout pregnancytypedescriptionfocused;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_brand_page);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.activity_brand_page, null, false);
        drawer.addView(view, 0);

        instantiatee();
        clickListeners();

    }

    private void instantiatee() {
        scrollView = (ScrollView) findViewById(R.id.brand_page_scroll_view);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_drug_forms);
        recyclerViewSimilarMedicines = (RecyclerView) findViewById(R.id.recycler_view_similar_brands);
        linearLayoutManager = new LinearLayoutManager(BrandPage.this,LinearLayoutManager.HORIZONTAL,false);
        linearLayoutManager1 = new LinearLayoutManager(BrandPage.this,LinearLayoutManager.HORIZONTAL,false);
        drugForm.add(new BrandFormsAndSimilarMedicineGetterSetter("PLASIL","120 ml","Pacific Pharma","Syrup"));
        drugForm.add(new BrandFormsAndSimilarMedicineGetterSetter("PLASIL","50 ml","Pacific Pharma","Syrup"));
        drugForm.add(new BrandFormsAndSimilarMedicineGetterSetter("PLASIL","120 mg","Pacific Pharma","tab"));
        adapter = new BrandFormsAndSimilarMedicinesAdapter(BrandPage.this,drugForm);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);


        similerBrands.add(new BrandFormsAndSimilarMedicineGetterSetter("CLOPAN","10 mg","SIZA Pharma","Metoclopramide (HCL)"));
        similerBrands.add(new BrandFormsAndSimilarMedicineGetterSetter("MAXOLON","10 mg","GSK","Metoclopramide (HCL)"));
        similerBrands.add(new BrandFormsAndSimilarMedicineGetterSetter("KANAMIDE","10 mg","Karka pharma","Metoclopramide (HCL)"));
        adapter = new BrandFormsAndSimilarMedicinesAdapter(BrandPage.this,similerBrands);

        recyclerViewSimilarMedicines.setLayoutManager(linearLayoutManager1);
        recyclerViewSimilarMedicines.setAdapter(adapter);
        scrollView.setFocusable(true);
        scrollView.setFocusableInTouchMode(true);

        indication = (LinearLayout) findViewById(R.id.ll_brand_page_indication);
        precaution = (LinearLayout) findViewById(R.id.ll_brand_page_precaution);
        contraIndication = (LinearLayout) findViewById(R.id.ll_brand_page_contra_indication);
        dose = (LinearLayout) findViewById(R.id.ll_brand_page_dose);
        sideEffect = (LinearLayout) findViewById(R.id.ll_brand_page_side_effect);
        modeOfAction = (LinearLayout) findViewById(R.id.ll_brand_page_mode_of_action);
        interaction = (LinearLayout) findViewById(R.id.ll_brand_page_interaction);
        pregnancyType = (LinearLayout) findViewById(R.id.ll_brand_page_pregnancy_type);
        pregnancyTypeDescription = (LinearLayout) findViewById(R.id.ll_brand_page_pregnancy_type_description);

        indicationfocused = (LinearLayout) findViewById(R.id.brands_detail_indications);
        precautionfocused = (LinearLayout) findViewById(R.id.brands_detail_precaution);
        contraIndicationfocused = (LinearLayout) findViewById(R.id.brands_detail_contraIndication);
        dosefocused = (LinearLayout) findViewById(R.id.brands_detail_dose);
        sideEffectfocused = (LinearLayout) findViewById(R.id.brands_detail_sideEffect);
        modeOfActionfocused = (LinearLayout) findViewById(R.id.brands_detail_modeOfAction);
        interactionfocused = (LinearLayout) findViewById(R.id.brands_detail_interaction);
        pregnancyTypefocused = (LinearLayout) findViewById(R.id.brands_detail_pregnancyType);
        pregnancytypedescriptionfocused = (LinearLayout) findViewById(R.id.brands_detail_pregnancyTypeDescription);

    }

    private void clickListeners() {
        indication.setOnClickListener(indicationOnClickListener);
        precaution.setOnClickListener(precautionOnClickListener);
        contraIndication.setOnClickListener(contraIndicationOnClickListener);
        dose.setOnClickListener(doseOnClickListener);
        sideEffect.setOnClickListener(sideEffectOnClickListener);
        modeOfAction.setOnClickListener(modeOfActionOnClickListener);
        interaction.setOnClickListener(interactionOnClickListener);
        pregnancyType.setOnClickListener(pregnancyTypeOnClickListener);
        pregnancyTypeDescription.setOnClickListener(pregnancyTypeDescriptionOnClickListener);
    }

    private View.OnClickListener indicationOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    scrollView.smoothScrollTo(0, indicationfocused.getTop());
                }
            });
        }
    };

    private View.OnClickListener precautionOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    scrollView.smoothScrollTo(0, precautionfocused.getTop());
                }
            });
        }
    };

    private View.OnClickListener contraIndicationOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    scrollView.smoothScrollTo(0, contraIndicationfocused.getTop());
                }
            });
        }
    };

    private View.OnClickListener doseOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    scrollView.smoothScrollTo(0, dosefocused.getTop());
                }
            });
        }
    };

    private View.OnClickListener sideEffectOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    scrollView.smoothScrollTo(0, sideEffectfocused.getTop());
                }
            });
        }
    };

    private View.OnClickListener modeOfActionOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    scrollView.smoothScrollTo(0, modeOfActionfocused.getTop());
                }
            });
        }
    };

    private View.OnClickListener interactionOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    scrollView.smoothScrollTo(0, interactionfocused.getTop());
                }
            });
        }
    };

    private View.OnClickListener pregnancyTypeOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    scrollView.smoothScrollTo(0, pregnancyTypefocused.getTop());
                }
            });
        }
    };

    private View.OnClickListener pregnancyTypeDescriptionOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    scrollView.smoothScrollTo(0, pregnancytypedescriptionfocused.getTop());
                }
            });
        }
    };



}
