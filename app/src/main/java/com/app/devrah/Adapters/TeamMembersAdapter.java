package com.app.devrah.Adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.devrah.Network.End_Points;
import com.app.devrah.Views.BoardsActivity;
import com.app.devrah.R;
import com.app.devrah.Views.CreateNewTeamActivity;
import com.app.devrah.pojo.TeamMembersPojo;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Rizwan Butt on 19-Jun-17.
 */

public class TeamMembersAdapter extends BaseAdapter {

    List<TeamMembersPojo> membersList;
    Activity activity;
    private LayoutInflater inflater;
    ProgressDialog ringProgressDialog;
    private static final int MY_SOCKET_TIMEOUT_MS = 10000;


    public TeamMembersAdapter(Activity activity, List<TeamMembersPojo> membersList) {
        this.activity = activity;
        this.membersList = membersList;

        //  super(activity,R.layout.custom_layout_for_projects,projectsList);
    }


    @Override
    public int getCount() {
        return membersList.size();
    }

    @Override
    public Object getItem(int position) {
        return   membersList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

       ViewHolder holder = new ViewHolder();
        if (inflater== null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.members_list_items, null);

        holder.data = (TextView) convertView.findViewById(R.id.memberName);
        holder.data.setText(membersList.get(position).getData());
        holder.profilePic=(ImageView)convertView.findViewById(R.id.memberProfilePic);
        holder.removeUser=(ImageView)convertView.findViewById(R.id.removeAccessTeam);
        holder.removeUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertRemove(membersList.get(position).getId(),position);
            }
        });
        Picasso.with(activity)
                .load("http://m1.cybussolutions.com/kanban/uploads/profile_pictures/" + membersList.get(position).getImage())
                .placeholder(R.drawable.bg_circle)
                .into(holder.profilePic);


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(activity, "hello", Toast.LENGTH_SHORT).show();
               /* Intent intent = new Intent(activity,BoardsActivity.class);
                activity.startActivity(intent);*/

            }
        });

        return convertView;
    }

    public void alertRemove(String id,int position) {
        deleteUserFromTeam(id,position);
    }

    public void deleteUserFromTeam(final String id, final int position) {
        ringProgressDialog = ProgressDialog.show(activity, "", "Please wait ...Removing User....", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();


        StringRequest request = new StringRequest(Request.Method.POST, End_Points.DELETE_USER_FROM_TEAM,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ringProgressDialog.dismiss();
                        new SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Success!")
                                .setConfirmText("OK").setContentText("Removed User Successfully")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismiss();

                                        membersList.remove(position);
                                        notifyDataSetChanged();
                                    }
                                })
                                .show();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                ringProgressDialog.dismiss();
                if (error instanceof NoConnectionError) {

                    new SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error!")
                            .setConfirmText("OK").setContentText("No Internet Connection")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                } else if (error instanceof TimeoutError) {

                    new SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error!")
                            .setConfirmText("OK").setContentText("Connection TimeOut! Please check your internet connection.")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("associatedid",id);

                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(request);
    }


    public static class ViewHolder{
        TextView data;
        ImageView profilePic,removeUser;
    }

}