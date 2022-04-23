package com.example.voiceassistant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.voiceassistant.R;
import com.example.voiceassistant.model.chatMessage;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    ArrayList<chatMessage> arrayList;
    LayoutInflater layoutInflater;
    Context context;

    public CustomAdapter(ArrayList<chatMessage> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }



    private static final int LAYOUT_ONE = 0;
    private static final int LAYOUT_TWO = 1;

    @Override
    public int getItemViewType(int position)
    {
        if(arrayList.get(position).getIsUser() == true)
            return LAYOUT_TWO;
        else
            return LAYOUT_ONE;
    }

    // Her bir satır için temsil edilecek olan arayüz belirlenir.
    @NonNull
    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = null;


        if(viewType==LAYOUT_ONE)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_bubble_out,parent,false);

        }
        else
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_bubble,parent,false);

        }


        layoutInflater = LayoutInflater.from(context);








        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }



    // Her bir satırın içeriği belirlenir.
    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.ViewHolder holder, int position) {

        holder.singleMessage.setText(arrayList.get(position).getMessage());
        holder.ll_liste_elemani.setTag(holder);
        // Listedeki elemanlara tıklanıdığında yapılacak olan işlem...
        holder.ll_liste_elemani.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewHolder holder1 = (ViewHolder)view.getTag();
                int position = holder1.getPosition();
                String yazdir = arrayList.get(position).getMessage();

                Toast.makeText(context, ""+yazdir, Toast.LENGTH_SHORT).show();
            }
        });

    }

    // Listedeki eleman sayısı kadar işlemin yapılmasını sağladık. Elle de bir değer verilebilirdi.
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    // Elemanlarımıza erişip tanımladığımız yer
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView singleMessage;
        ConstraintLayout ll_liste_elemani;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            singleMessage = itemView.findViewById(R.id.singleMessage);
            ll_liste_elemani = itemView.findViewById(R.id.chatBubble);
        }
    }
}