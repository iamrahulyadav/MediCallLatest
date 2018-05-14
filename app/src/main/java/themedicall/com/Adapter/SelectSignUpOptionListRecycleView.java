package themedicall.com.Adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import themedicall.com.SignUpAmbulance;
import themedicall.com.SignUp;
import themedicall.com.GetterSetter.SelectSIgnUpOptionGetterSetter;
import themedicall.com.R;
import java.util.List;

/**
 * Created by Muhammad Adeel on 7/21/2017.
 */

public class SelectSignUpOptionListRecycleView extends RecyclerView.Adapter<SelectSignUpOptionListRecycleView.MyViewHolder>  {

    private List<SelectSIgnUpOptionGetterSetter> signUpOptionList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView singUpOptionImg ;
        public TextView singUpOptionId , singUpOptionName;

        public MyViewHolder(final View view) {
            super(view);


            singUpOptionImg = (ImageView) view.findViewById(R.id.singUpOptionImg);
            singUpOptionId = (TextView) view.findViewById(R.id.singUpOptionId);
            singUpOptionName = (TextView) view.findViewById(R.id.singUpOptionName);



            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int signUpId = Integer.parseInt(singUpOptionId.getText().toString());

                    if(signUpId == 7)
                    {
                        Intent intent = new Intent(v.getContext() , SignUpAmbulance.class);
                        v.getContext().startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(v.getContext() , SignUp.class);
                        intent.putExtra("item_position" , signUpId);
                        v.getContext().startActivity(intent);
                    }


                }
            });

        }
    }

    public SelectSignUpOptionListRecycleView(List<SelectSIgnUpOptionGetterSetter> adList) {
        this.signUpOptionList = adList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.select_sign_up_option_custom_row, parent, false);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SelectSIgnUpOptionGetterSetter ad = signUpOptionList.get(position);

        holder.singUpOptionImg.setImageResource(ad.getSignUpOptionImg());
        holder.singUpOptionId.setText(ad.getSignUpOptionId());
        holder.singUpOptionName.setText(ad.getSignUpOptionName());
        holder.singUpOptionId.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return signUpOptionList.size();
    }

}
