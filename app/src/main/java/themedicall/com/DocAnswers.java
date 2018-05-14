package themedicall.com;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import themedicall.com.Adapter.AskQuestionRecycleView;
import themedicall.com.GetterSetter.AskQuestionGetterSetter;


import java.util.ArrayList;
import java.util.List;


public class DocAnswers extends Fragment {

    RecyclerView recyclerView_answer;
    List<AskQuestionGetterSetter> answersList;
    public DocAnswers() {
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


        View view = inflater.inflate(R.layout.fragment_doc_answers, container, false);

        recyclerView_answer = (RecyclerView) view.findViewById(R.id.recycler_view_answer);
        recyclerView_answer.setHasFixedSize(true);
        recyclerView_answer.setLayoutManager(new LinearLayoutManager(getContext() , LinearLayoutManager.VERTICAL , false));
        answersList = new ArrayList<>();
        prepareAnswersList();

        return view;

    }

    public void prepareAnswersList()
    {

        answersList.add(new AskQuestionGetterSetter("1" , "Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text" , "Adeel Khalil" , "25" , "2" , "25-Mar-2017"));
        answersList.add(new AskQuestionGetterSetter("1" , "Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text" , "Adeel Khalil" , "25" , "2" , "25-Mar-2017"));
        answersList.add(new AskQuestionGetterSetter("1" , "Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text" , "Adeel Khalil" , "25" , "2" , "25-Mar-2017"));
        answersList.add(new AskQuestionGetterSetter("1" , "Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text" , "Adeel Khalil" , "25" , "2" , "25-Mar-2017"));
        answersList.add(new AskQuestionGetterSetter("1" , "Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text" , "Adeel Khalil" , "25" , "2" , "25-Mar-2017"));
        answersList.add(new AskQuestionGetterSetter("1" , "Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text" , "Adeel Khalil" , "25" , "2" , "25-Mar-2017"));
        answersList.add(new AskQuestionGetterSetter("1" , "Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text" , "Adeel Khalil" , "25" , "2" , "25-Mar-2017"));
        answersList.add(new AskQuestionGetterSetter("1" , "Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text" , "Adeel Khalil" , "25" , "2" , "25-Mar-2017"));
        answersList.add(new AskQuestionGetterSetter("1" , "Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text" , "Adeel Khalil" , "25" , "2" , "25-Mar-2017"));
        answersList.add(new AskQuestionGetterSetter("1" , "Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text" , "Adeel Khalil" , "25" , "2" , "25-Mar-2017"));


        AskQuestionRecycleView askQuestionRecycleView = new AskQuestionRecycleView(answersList);
        recyclerView_answer.setAdapter(askQuestionRecycleView);
        askQuestionRecycleView.notifyDataSetChanged();



    }

}
