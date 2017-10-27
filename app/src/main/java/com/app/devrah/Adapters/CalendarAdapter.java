package com.app.devrah.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.devrah.R;
import com.app.devrah.Views.BoardExtended.BoardExtended;
import com.app.devrah.Views.CardActivity;
import com.app.devrah.pojo.CalendarPojo;
import com.app.devrah.pojo.CardAssociatedCalendarLabelsPojo;
import com.app.devrah.pojo.CardAssociatedCalendarMembersPojo;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Hamza Android on 10/16/2017.
 */
public class CalendarAdapter extends BaseAdapter implements View.OnTouchListener {
    List<CalendarPojo> projectsList;
    Activity activity;
    private LayoutInflater inflater;
    List<CardAssociatedCalendarLabelsPojo> labelList;
    List<CardAssociatedCalendarMembersPojo> memberList;
    int membercount;
    String BoardsListData;

    public CalendarAdapter(Activity activity, List<CalendarPojo> projectsList, List<CardAssociatedCalendarLabelsPojo> labelList, List<CardAssociatedCalendarMembersPojo> memberList, int membercount) {
        this.activity = activity;
        this.projectsList = projectsList;
        this.labelList = labelList;
        this.memberList = memberList;
        this.membercount=membercount;
     //   this.list_id=list_id;

        //  super(activity,R.layout.custom_layout_for_projects,projectsList);
    }


    @Override
    public int getCount() {
        return projectsList.size();
    }

    @Override
    public Object getItem(int position) {
        return   projectsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder vh;
        final ViewHolder holder = new ViewHolder();
        if (inflater== null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.cards_calendar_view, null);


        vh = new ViewHolder();
        convertView.setTag(vh);


        holder.favouriteIcon= (ImageView) convertView.findViewById(R.id.favouriteIcon);
        holder.checkBox= (CheckBox) convertView.findViewById(R.id.cbComplete);
        holder.attachment= (ImageView) convertView.findViewById(R.id.cardImage);

        holder.membersView = (LinearLayout)convertView.findViewById(R.id.membersListView);
        holder.labelsView = (LinearLayout)convertView.findViewById(R.id.labelsLayout);

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.checkBox.isChecked()){
                    projectsList.get(position).setIsCardComplete("1");
                   // ((BoardExtended)activity).updateChildFragmentsCardData(projectsList.get(position).getId(),"1");

                }else {
                    projectsList.get(position).setIsCardComplete("0");
                  //  ((BoardExtended)activity).updateChildFragmentsCardData(projectsList.get(position).getId(),"0");

                }
                notifyDataSetChanged();
            }
        });
        /*if(!projectsList.get(position).getAttachment().equals("") || projectsList.get(position).getAttachment()!=null){
            if(projectsList.get(position).getIsCover().equals("1")) {
                holder.attachment.setVisibility(View.VISIBLE);
                Picasso.with(activity)
                        .load("http://m1.cybussolutions.com/kanban/uploads/card_uploads/" + projectsList.get(position).getAttachment())
                        .into(holder.attachment);
            }
        }*/

        holder.data = (TextView) convertView.findViewById(R.id.tvFragmentBoardsList);
        holder.nOfAttachments = (TextView) convertView.findViewById(R.id.nOfAttachments);
        holder.attachmentIcon = (ImageView) convertView.findViewById(R.id.attachmentIcon);
        holder.subscribe = (ImageView) convertView.findViewById(R.id.subscribedIcon);
        holder.descriptionIcon = (ImageView) convertView.findViewById(R.id.descriptionIcon);
        holder.dueDate= (TextView) convertView.findViewById(R.id.dateLabel);
        holder.nOfAttachments.setVisibility(View.GONE);
        holder.attachmentIcon.setVisibility(View.GONE);

        holder.data.setText(projectsList.get(position).getData());
        BoardsListData = projectsList.get(position).getData();

        holder.dueDate.setVisibility(View.GONE);
        if(!projectsList.get(position).getDueDate().equals("null")) {
            holder.dueDate.setVisibility(View.VISIBLE);
            holder.dueDate.setText(projectsList.get(position).getDueDate());
        }
        else {
            holder.dueDate.setText("");

        }


        if(!projectsList.get(position).getnOfAttachments().equals("0")){
            holder.dueDate.setVisibility(View.VISIBLE);
            holder.nOfAttachments.setVisibility(View.VISIBLE);
            holder.attachmentIcon.setVisibility(View.VISIBLE);
            holder.nOfAttachments.setText(projectsList.get(position).getnOfAttachments());
        }else {
            holder.nOfAttachments.setVisibility(View.GONE);
            holder.attachmentIcon.setVisibility(View.GONE);
        }
       /* String memberSubscribed[]=memberList.get(position).getMemberSubscribed();
        SharedPreferences pref = activity.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        for(int i=0;i<memberSubscribed.length;i++){
            if(memberList.get(i))

        }*/
        if(memberList.get(position).getMemberSubscribed().equals("1")){
            holder.dueDate.setVisibility(View.VISIBLE);
            holder.subscribe.setVisibility(View.VISIBLE);
        }else {
            holder.subscribe.setVisibility(View.GONE);
        }
        if(projectsList.get(position).getCardDescription().equals("") || projectsList.get(position).getCardDescription().equals("null")){
            holder.descriptionIcon.setVisibility(View.GONE);
        }else {
            holder.dueDate.setVisibility(View.VISIBLE);
            holder.descriptionIcon.setVisibility(View.VISIBLE);
        }
        if(projectsList.get(position).getIsCardComplete().equals("1")){
            holder.checkBox.setChecked(true);
        }else {
            holder.checkBox.setChecked(false);
        }

        // if(projectsList.get(position).getnOfAttachments().length()>0){
        // holder.nOfAttachments.setText(projectsList.get(position).getnOfAttachments());
        //}
        // SharedPreferences pref = activity.getSharedPreferences("UserPrefs", MODE_PRIVATE);
      /* if(projectsList.get(position).getIsCardLocked().equals("1") && !projectsList.get(position).getAssignedTo().equals(pref.getString("user_id",""))){
          //  Collections.emptyList();
           projectsList.remove(projectsList.get(position));
        //    notifyDataSetChanged();
       }*/

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(activity, "hello", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(activity,CardActivity.class);
                intent.putExtra("CardHeaderData", projectsList.get(position).getData());
                intent.putExtra("card_id",projectsList.get(position).getId());
                intent.putExtra("cardduedate",projectsList.get(position).getDueDate());
                intent.putExtra("cardduetime",projectsList.get(position).getDuetTime());
                intent.putExtra("cardstartdate",projectsList.get(position).getStartDate());
                intent.putExtra("cardstarttime",projectsList.get(position).getStartTime());
                intent.putExtra("cardDescription",projectsList.get(position).getCardDescription());
                intent.putExtra("isComplete",projectsList.get(position).getIsCardComplete());
                intent.putExtra("isLocked",projectsList.get(position).getIsCardLocked());
                intent.putExtra("isSubscribed",memberList.get(position).getMemberSubscribed());
                intent.putExtra("list_id",projectsList.get(position).getListId());
                intent.putExtra("project_id", BoardExtended.projectId);
                intent.putExtra("board_id", BoardExtended.boardId);
                intent.putExtra("board_name", BoardExtended.bTitle);
                intent.putExtra("project_title", BoardExtended.pTitle);
                intent.putExtra("fromMyCards","false");
                activity.finish();
                activity.startActivity(intent);

            }
        });
        String[] labels=labelList.get(position).getLabels();
        String[] labelNames=labelList.get(position).getLabelText();
        if(labels.length<1){
            HorizontalScrollView scrollView= (HorizontalScrollView) convertView.findViewById(R.id.labelScroller);
            scrollView.setVisibility(View.GONE);
        }else {
            HorizontalScrollView scrollView= (HorizontalScrollView) convertView.findViewById(R.id.labelScroller);
            scrollView.setVisibility(View.VISIBLE);
        }
        holder.labelsView.removeAllViews();

        for (int i = 0; i < labels.length; i++) {
            TextView image = new TextView(activity);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(90,50);
            layoutParams.setMargins(10,0,0,0);
            //image.setLayoutParams(new android.view.ViewGroup.LayoutParams(60, 60));
            image.setLayoutParams(layoutParams);
            // image.setBackground(activity.getResources().getDrawable(R.drawable.bg_circle));
            // image.setImageDrawable(activity.getResources().getDrawable(R.drawable.bg__dashboard_calender));20170704090751.jpg
            image.setMaxHeight(20);
            image.setMaxWidth(50);

            // image.setText(l);
            //image.setBackgroundColor(activity.getResources().getColor(R.color.light_black));
            // image.setTextColor(activity.getResources().getColor(R.color.black));
            image.setGravity(Gravity.CENTER);

            // Adds the view to the layout

            if(labels[i].equals("blue")) {
                image.setBackgroundColor(activity.getResources().getColor(R.color.blue));
                //  break;
            }else if(labels[i].equals("sky-blue")) {
                image.setBackgroundColor(activity.getResources().getColor(R.color.wierdBlue));
                //break;
            }else if(labels[i].equals("red")) {
                image.setBackgroundColor(activity.getResources().getColor(R.color.colorRed));
                //break;
            }else if(labels[i].equals("yellow")) {
                image.setBackgroundColor(activity.getResources().getColor(R.color.colorYellow));
                //break;
            }else if(labels[i].equals("purple")) {
                image.setBackgroundColor(activity.getResources().getColor(R.color.purple));
                //textView.setText(labelNames[i]);
                //break;
            }else if(labels[i].equals("pink")) {
                image.setBackgroundColor(activity.getResources().getColor(R.color.pink));
                //break;
            }else if(labels[i].equals("orange")) {
                image.setBackgroundColor(activity.getResources().getColor(R.color.colorOrange));
                //break;
            }else if(labels[i].equals("black")) {
                image.setBackgroundColor(activity.getResources().getColor(R.color.black));
                //break;
            }else if(labels[i].equals("green")) {
                image.setBackgroundColor(activity.getResources().getColor(R.color.colorGreen));

                //break;
            }else if(labels[i].equals("dark-green")) {
                image.setBackgroundColor(activity.getResources().getColor(R.color.darkgreen));
                //break;
            }else if(labels[i].equals("lime")) {
                image.setBackgroundColor(activity.getResources().getColor(R.color.lightGreen));
                //break;
            }
            holder.labelsView.addView(image);
        }
        String imageUrl[]=memberList.get(position).getMembers();
        String initials[]=memberList.get(position).getInitials();
        String gp_picture[]=memberList.get(position).getGp_pictures();
        if(imageUrl.length<1){
            HorizontalScrollView scrollView= (HorizontalScrollView) convertView.findViewById(R.id.memberScroller);
            scrollView.setVisibility(View.GONE);
        }else {
            HorizontalScrollView scrollView= (HorizontalScrollView) convertView.findViewById(R.id.memberScroller);
            scrollView.setVisibility(View.VISIBLE);
        }
        holder.membersView.removeAllViews();
        // if(membercount==0) {
        for (int i = 0; i < imageUrl.length; i++) {

            if ((imageUrl[i].equals("null") || imageUrl[i].equals("")) && (gp_picture[i].equals("null") || gp_picture[i].equals(""))) {
                TextView image = new TextView(activity);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(80,80);
                layoutParams.setMargins(5,0,0,0);
                //image.setLayoutParams(new android.view.ViewGroup.LayoutParams(60, 60));
                image.setLayoutParams(layoutParams);
                image.setBackground(activity.getResources().getDrawable(R.drawable.bg_circle));
                // image.setImageDrawable(activity.getResources().getDrawable(R.drawable.bg__dashboard_calender));20170704090751.jpg
                image.setMaxHeight(20);
                image.setMaxWidth(20);
                image.setText(initials[i]);
                //image.setBackgroundColor(activity.getResources().getColor(R.color.light_black));
                image.setTextColor(activity.getResources().getColor(R.color.black));
                image.setGravity(Gravity.CENTER);

                // Adds the view to the layout
                holder.membersView.addView(image);
            } else if(!imageUrl[i].equals("null") && !imageUrl[i].equals("")){
                CircleImageView image = new CircleImageView(activity);
                image.setLayoutParams(new android.view.ViewGroup.LayoutParams(100, 80));
                // image.setImageDrawable(activity.getResources().getDrawable(R.drawable.bg__dashboard_calender));20170704090751.jpg
                image.setMaxHeight(20);
                image.setMaxWidth(20);

                Picasso.with(activity)
                        .load("http://m1.cybussolutions.com/kanban/uploads/profile_pictures/" + imageUrl[i])
                        .into(image);

                // Adds the view to the layout
                holder.membersView.addView(image);
            }else {
                CircleImageView image = new CircleImageView(activity);
                image.setLayoutParams(new android.view.ViewGroup.LayoutParams(100, 80));
                // image.setImageDrawable(activity.getResources().getDrawable(R.drawable.bg__dashboard_calender));20170704090751.jpg
                image.setMaxHeight(20);
                image.setMaxWidth(20);

                Picasso.with(activity)
                        .load( gp_picture[i])
                        .into(image);

                // Adds the view to the layout
                holder.membersView.addView(image);
            }
        }
//            membercount=1;
//        }
       /* SharedPreferences pref = activity.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
       if(projectsList.get(position).getIsCardLocked().equals("1") && !projectsList.get(position).getAssignedTo().equals(pref.getString("user_id",""))){
          // projectsList.remove(position);
          // notifyDataSetChanged();
       }*/
        return convertView;
    }


    public static class ViewHolder{
        LinearLayout membersView;
        LinearLayout labelsView;
        TextView data,dueDate,nOfAttachments;
        ImageView favouriteIcon,attachmentIcon;
        ImageView attachment,subscribe,descriptionIcon;
        CheckBox checkBox;

        public float lastTouchedX;
        public float lastTouchedY;


    }

    public void reLoadView()
    {
        notifyDataSetChanged();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        ViewHolder vh = (ViewHolder) view.getTag();

        vh.lastTouchedX = motionEvent.getX();
        vh.lastTouchedY = motionEvent.getY();

        return false;
    }


}