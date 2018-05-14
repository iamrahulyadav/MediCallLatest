package themedicall.com;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import themedicall.com.Adapter.AskQuestionRecycleView;
import themedicall.com.GetterSetter.AskQuestionGetterSetter;

import java.util.ArrayList;
import java.util.List;

public class AskAQuestion extends NavigationDrawer {
    RecyclerView recyclerView_ask_question;
    List<AskQuestionGetterSetter> askQuestionsList;
    Button askAQuestionFromDoctor , selectSpecialityForAskQues;
    ArrayAdapter<String> specialityAdapter;
    String[] speciality;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_ask_aquestion);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.activity_ask_aquestion, null, false);
        drawer.addView(view, 0);

        initiate();
        clickListener();
        prepareHappyUserList();

    }

    public void initiate()
    {
        recyclerView_ask_question = (RecyclerView) findViewById(R.id.recycler_view_ask_question);
        askAQuestionFromDoctor = (Button) findViewById(R.id.askAQuestionFromDoctor);
        selectSpecialityForAskQues = (Button) findViewById(R.id.selectSpecialityForAskQues);
        recyclerView_ask_question.setHasFixedSize(true);
        recyclerView_ask_question.setLayoutManager(new LinearLayoutManager(AskAQuestion.this , LinearLayoutManager.VERTICAL , false));
        askQuestionsList = new ArrayList<>();
        speciality = getResources().getStringArray(R.array.speciality);

    }

    public void clickListener()
    {
        askAQuestionFromDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AskAQuestion.this , AskQuestionFromDoctor.class);
                startActivity(intent);
            }
        });

        selectSpecialityForAskQues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(AskAQuestion.this);
                dialog.setContentView(R.layout.custom_citylist_search);
                dialog.setTitle("Select Speciality");
                ListView specialityList = (ListView) dialog.findViewById(R.id.cityList);
                dialog.show();


                specialityAdapter = new ArrayAdapter<String>(AskAQuestion.this , R.layout.custom_list_item , speciality);
                specialityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                specialityList.setAdapter(specialityAdapter);

                specialityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        TextView textView = (TextView) view.findViewById(R.id.city_title);
                        String text = textView.getText().toString();

                        selectSpecialityForAskQues.setText(text);
                        dialog.dismiss();

                        //Toast.makeText(SignIn.this, "Pos "+text, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


    }

    public void prepareHappyUserList()
    {

        askQuestionsList.add(new AskQuestionGetterSetter("1" , "Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text" , "Adeel Khalil" , "25" , "2" , "25-Mar-2017"));
        askQuestionsList.add(new AskQuestionGetterSetter("1" , "Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text" , "Adeel Khalil" , "25" , "2" , "25-Mar-2017"));
        askQuestionsList.add(new AskQuestionGetterSetter("1" , "Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text" , "Adeel Khalil" , "25" , "2" , "25-Mar-2017"));
        askQuestionsList.add(new AskQuestionGetterSetter("1" , "Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text" , "Adeel Khalil" , "25" , "2" , "25-Mar-2017"));
        askQuestionsList.add(new AskQuestionGetterSetter("1" , "Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text" , "Adeel Khalil" , "25" , "2" , "25-Mar-2017"));
        askQuestionsList.add(new AskQuestionGetterSetter("1" , "Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text" , "Adeel Khalil" , "25" , "2" , "25-Mar-2017"));
        askQuestionsList.add(new AskQuestionGetterSetter("1" , "Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text" , "Adeel Khalil" , "25" , "2" , "25-Mar-2017"));
        askQuestionsList.add(new AskQuestionGetterSetter("1" , "Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text" , "Adeel Khalil" , "25" , "2" , "25-Mar-2017"));
        askQuestionsList.add(new AskQuestionGetterSetter("1" , "Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text" , "Adeel Khalil" , "25" , "2" , "25-Mar-2017"));
        askQuestionsList.add(new AskQuestionGetterSetter("1" , "Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text" , "Adeel Khalil" , "25" , "2" , "25-Mar-2017"));


        AskQuestionRecycleView askQuestionRecycleView = new AskQuestionRecycleView(askQuestionsList);
        recyclerView_ask_question.setAdapter(askQuestionRecycleView);
        askQuestionRecycleView.notifyDataSetChanged();



    }
}
