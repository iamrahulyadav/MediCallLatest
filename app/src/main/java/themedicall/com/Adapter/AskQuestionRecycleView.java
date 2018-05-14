package themedicall.com.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import themedicall.com.GetterSetter.AskQuestionGetterSetter;
import themedicall.com.R;


import java.util.List;

/**
 * Created by Muhammad Adeel on 7/21/2017.
 */

public class AskQuestionRecycleView extends RecyclerView.Adapter<AskQuestionRecycleView.MyViewHolder>  {

    private List<AskQuestionGetterSetter> askQuestionList;
    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView askQuesRowId , askQuesRowQuestion , askQuesRowName , askQuesRowAge , askQuesRowNoOfAns , askQuesRowDate;


        public MyViewHolder(final View view) {
            super(view);

            askQuesRowId = (TextView) view.findViewById(R.id.askQuesRowId);
            askQuesRowQuestion = (TextView) view.findViewById(R.id.askQuesRowQuestion);
            askQuesRowName = (TextView) view.findViewById(R.id.askQuesRowName);
            askQuesRowAge = (TextView) view.findViewById(R.id.askQuesRowAge);
            askQuesRowNoOfAns = (TextView) view.findViewById(R.id.askQuesRowNoOfAns);
            askQuesRowDate = (TextView) view.findViewById(R.id.askQuesRowDate);


//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    Intent intent = new Intent(v.getContext() , HappyUserDetail.class);
//                    v.getContext().startActivity(intent);
//
//                    String id = happyUserId.getText().toString();
//                    Toast.makeText(v.getContext(), "id "+id, Toast.LENGTH_SHORT).show();
//                }
//            });

        }
    }

    public AskQuestionRecycleView(List<AskQuestionGetterSetter> adList) {
        this.askQuestionList = adList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ask_question_custom_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        AskQuestionGetterSetter ad = askQuestionList.get(position);
        holder.askQuesRowId.setText(ad.getAskQuesRowId());
        holder.askQuesRowQuestion.setText(ad.getAskQuesRowQuestion());
        holder.askQuesRowName.setText(ad.getAskQuesRowName());
        holder.askQuesRowAge.setText("Age "+ad.getAskQuesRowAge());
        holder.askQuesRowNoOfAns.setText(ad.getAskQuesRowNoOfAns());
        holder.askQuesRowDate.setText(ad.getAskQuesRowDate());


        holder.askQuesRowId.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return askQuestionList.size();
    }

}
