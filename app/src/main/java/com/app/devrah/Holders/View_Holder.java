package com.app.devrah.Holders;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.support.v7.widget.RecyclerView;

import com.app.devrah.R;

/**
 * Created by AQSA SHaaPARR on 6/7/2017.
 */

public class View_Holder extends RecyclerView.ViewHolder {

   public TextView tvcardName,idCardComment,comment;
   public ImageView imgCard,CommentMenu;
    public LinearLayout laEditDelete;
   public CardView cv;


    public View_Holder(View itemView) {
        super(itemView);

        cv= (CardView)itemView.findViewById(R.id.cardView);
        idCardComment  =  (TextView)itemView.findViewById(R.id.id_of_person);
        tvcardName   = (TextView)itemView.findViewById(R.id.name_of_card);
        comment = (TextView)itemView.findViewById(R.id.comment);
        CommentMenu = (ImageView)itemView.findViewById(R.id.commentMenu);
laEditDelete = (LinearLayout)itemView.findViewById(R.id.edit_delete_layout);

        imgCard = (ImageView)itemView.findViewById(R.id.imageCardView);



    }
}
