package com.app.devrah.Views.BoardExtended;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.app.devrah.Adapters.CustomViewPagerAdapter;
import com.app.devrah.Adapters.FragmentBoardsAdapter;
import com.app.devrah.Network.End_Points;
import com.app.devrah.R;
import com.app.devrah.pojo.CardAssociatedCheckBox;
import com.app.devrah.pojo.CardAssociatedCoverPojo;
import com.app.devrah.pojo.CardAssociatedLabelsPojo;
import com.app.devrah.pojo.CardAssociatedMembersPojo;
import com.app.devrah.pojo.ProjectsPojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.Context.MODE_PRIVATE;
import static com.app.devrah.Network.End_Points.DELETE_LIST;
import static com.app.devrah.Network.End_Points.GET_CARDS_FOR_LIST;
import static com.app.devrah.Network.End_Points.SAVE_CARD_BY_LIST_ID;
import static com.app.devrah.Network.End_Points.UPDATE_COLOR_BG_LIST;


public class ChildFragmentBoardExtended extends Fragment {

    Button noColor;
    Button blueColor;
    Button brownColor;
    Button grayColor ;
    Button greenColor;
    Button orangeColor;
    Button purpleColor;
    Button yellowColor;
    Button redColor ;
    private List<ProjectsPojo> HistoryList = new ArrayList<ProjectsPojo>();
    Spinner Projects,Postions,boards;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    View view;
    ImageView tvAddCard;
    String childname;
    EditText   edt;
    public     TextView tvName;
    ImageView boardMenu;
    FragmentBoardsAdapter adapter;
    List<ProjectsPojo> listPojo,labelsPojoList;
    List<CardAssociatedLabelsPojo> cardLabelsPojoList;
    List<CardAssociatedMembersPojo> cardMembersPojoList;
    List<CardAssociatedCoverPojo> cardCoverPojoList;
    List<CardAssociatedCheckBox> cardCheckboxPojoList;
  //  CardAssociatedLabelsAdapter cardAssociatedLabelsAdapter;
    CustomViewPagerAdapter VPadapter;
    ProjectsPojo boardsFragmentPojoData;
    CardAssociatedLabelsPojo labelsPojo;
    ListView lv;
    RecyclerView cardAssociatedLabelRecycler;
    ArrayList<String> boards_name ;
    ArrayList<String> boards_ids ;
    RelativeLayout relativeLayout;

    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    ProgressDialog ringProgressDialog;

    String id,p_id,b_id,list_id,list_color;
    int row;
    List<String> spinnerValues;
    List<String> spinnerGroupIds;
    List<String> postions_list;
    EditText edtSeach;


    private OnFragmentInteractionListener mListener;
    static ChildFragmentBoardExtended fragment;
    String check1="";

    public ChildFragmentBoardExtended() {

         }


    public static ChildFragmentBoardExtended newInstance(String param1, String param2) {
         fragment = new ChildFragmentBoardExtended();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onPause(){
        super.onPause();
        getActivity().unregisterReceiver(broadcastReceiver);

    }
    @Override
    public void onResume(){
        super.onResume();
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter("updateComplete"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        view =inflater.inflate(R.layout.fragment_child_fragment_board_extended, container, false);
        View emV = inflater.inflate(R.layout.empty_list_bg,null);
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter("updateComplete"));
//        cardAssociatedLabelRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true));


        tvName = (TextView)view.findViewById(R.id.textName);
        boardMenu = (ImageView)view.findViewById(R.id.menuBoards);

        tvAddCard = (ImageView)view.findViewById(R.id.addCard);
        Bundle bundle = this.getArguments();
        childname = bundle.getString("data");
        p_id = bundle.getString("p_id");
        b_id = bundle.getString("b_id");
        list_id = bundle.getString("list_id");
        list_color=bundle.getString("list_color");

         check1=bundle.getString("listName");
        row=0;

        tvName.setText(childname);

try {
    getCardList(list_id);
}catch (OutOfMemoryError error){
  error.printStackTrace();
}

        if(check1.equals("")) {
            if (list_color.equals("") || list_color == null) {
                tvName.setBackgroundColor(getActivity().getResources().getColor(R.color.float_transparent));
            } else if (list_color.equals("00A2E8")) {
                tvName.setBackgroundColor(getActivity().getResources().getColor(R.color.blue));
            } else if (list_color.equals("B97A57")) {
                tvName.setBackgroundColor(getActivity().getResources().getColor(R.color.brown));
            } else if (list_color.equals("B5E61D")) {
                tvName.setBackgroundColor(getActivity().getResources().getColor(R.color.green));
            } else if (list_color.equals("FF7F27")) {
                tvName.setBackgroundColor(getActivity().getResources().getColor(R.color.orange));
            } else if (list_color.equals("A349A4")) {
                tvName.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPurple));
            } else if (list_color.equals("f2d600")) {
                tvName.setBackgroundColor(getActivity().getResources().getColor(R.color.colorYellow));
            } else if (list_color.equals("eb5a46")) {
                tvName.setBackgroundColor(getActivity().getResources().getColor(R.color.colorRed));
            } else if (list_color.equals("C3C3C3")) {
                tvName.setBackgroundColor(getActivity().getResources().getColor(R.color.gray));
            }
        }

        lv = (ListView) view.findViewById(R.id.boardFragmentListView);
        edtSeach = (EditText) getActivity().findViewById(R.id.edtSearch);
        edtSeach.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String nameToSearch = edtSeach.getText().toString();
                ArrayList<ProjectsPojo> filteredLeaves = new ArrayList<ProjectsPojo>();

                for (ProjectsPojo data : listPojo) {
                    if (data.getData().toLowerCase().contains(nameToSearch.toLowerCase())) {
                        filteredLeaves.add(data);
                    }


                }
                adapter = new FragmentBoardsAdapter(getActivity(), filteredLeaves,cardLabelsPojoList,cardMembersPojoList,cardCoverPojoList,0,list_id,cardCheckboxPojoList,broadcastReceiver);
                lv.setAdapter(adapter);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //relativeLayout=(RelativeLayout)findViewById(R.id.layoutTestRecycleView);
//        cardAssociatedLabelRecycler=(RecyclerView)view.findViewById(R.id.labelsListView);


        boardMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(getActivity(), boardMenu);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.list_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                       // Toast.makeText(getActivity(),"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                        switch (item.getItemId()) {
                            case R.id.addNewCard:
                                showCustomDialog();
                              //  boardMenu.setBackgroundColor(getActivity().getResources().getColor(R.color.lightGreen));
                                // TODO Something when menu item selected
                                return true;
                            case R.id.updateListName:
                                if(list_color==null)
                                    list_color="";
                               customDialogueUpdateListName(childname,list_color);
                                //  boardMenu.setBackgroundColor(getActivity().getResources().getColor(R.color.lightGreen));
                                // TODO Something when menu item selected
                                return true;

                            case R.id.changeListColor:
                                // TODO Something when menu item selected
                                changeListColorPopup();
                                return true;

                            case R.id.copyList:
                               showDialog("copy");
                                return true;
                            case R.id.moveList:

                                showDialog("move");
                                return true;
                            case R.id.deleteList:
                                new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Confirmation!")
                                        .setCancelText("Cancel")
                                        .setConfirmText("OK").setContentText("Are You sure you want to Remove this list")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                deleteList();
                                                sDialog.dismiss();
                                            }
                                        })
                                        .showCancelButton(true)
                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                sweetAlertDialog.dismiss();
                                            }
                                        })
                                        .show();

                                // TODO Something when menu item selected
                                return true;

                        }
                        return true;
                    }
                });
                popup.show();
//
//                View popupView =getActivity().getLayoutInflater().inflate(R.layout.ui_for_popup_view, null);
//                PopupWindow popupWindow = new PopupWindow(popupView, ActionMenuView.LayoutParams.WRAP_CONTENT, ActionMenuView.LayoutParams.WRAP_CONTENT);
//                popupWindow.setFocusable(true);


            }
        });


        lv.setEmptyView(emV);


        tvAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showCustomDialog();

            }
        });


        return view;
    }
    BroadcastReceiver broadcastReceiver =  new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Bundle b = intent.getExtras();

            String cardId = b.getString("cardId");
            String isCompleted = b.getString("isComplete");
            if(cardId!=""){
                for (int i=0;i<listPojo.size();i++){
                    if(listPojo.get(i).getId().equals(cardId)){
                        listPojo.get(i).setIsCardComplete(isCompleted);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

        }
    };
   /* public void upDateData(String id,String isCardComplete){
        for(int i=0;i<listPojo.size();i++){
            if(listPojo.get(i).getId().equals(id)){
                listPojo.get(i).setIsCardComplete(isCardComplete);
            }
           adapter.notifyDataSetChanged();
        }
    }*/

    private void customDialogueUpdateListName(String listName, final String list_color) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View customView = inflater.inflate(R.layout.update_card_name_dialog, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setCancelable(false);
        alertDialog.setView(customView);
        alertDialog.show();

        Button cancel, copy;
        final EditText etListName= (EditText) customView.findViewById(R.id.etCardName);
        final TextView heading= (TextView) customView.findViewById(R.id.heading);
        heading.setText("Update List Name");
        etListName.setText(listName);
        etListName.setSelection(etListName.getText().length());
        showKeyBoard(etListName);

        copy = (Button) customView.findViewById(R.id.copy);
        cancel = (Button) customView.findViewById(R.id.close);
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String check=etListName.getText().toString();
                if(!check.equals("") && check!="" && check.trim().length()>0) {
                    updateListName(etListName.getText().toString(),list_color);
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etListName.getWindowToken(), 0);
                    alertDialog.dismiss();
                }else {
                    Toast.makeText(getActivity(),"List Name is must!",Toast.LENGTH_LONG).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etListName.getWindowToken(), 0);

                alertDialog.dismiss();

            }
        });
    }

    private void updateListName(final String s,final String list_color) {
        ringProgressDialog = ProgressDialog.show(getContext(), "", "Please wait ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();
        final SharedPreferences pref = getActivity().getSharedPreferences("UserPrefs", MODE_PRIVATE);
        StringRequest request = new StringRequest(Request.Method.POST, End_Points.UPDATE_LIST_NAME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ringProgressDialog.dismiss();
                      // if (bgColor.equals("C3C3C3")){
                           // tvName.setText(s);
                        ParentBoardExtendedFragment.removeSpecificPage(ParentBoardExtendedFragment.getCurrentPosition(),"delete");
                        ParentBoardExtendedFragment.updateBundleData(s,p_id,b_id,list_id,list_color,"",ParentBoardExtendedFragment.getCurrentPosition());

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                ringProgressDialog.dismiss();
                if (error instanceof NoConnectionError) {


                    Toast.makeText(getActivity(), "check your internet connection", Toast.LENGTH_SHORT).show();
                    /*new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error!")
                            .setConfirmText("OK").setContentText("No Internet Connection")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismiss();
                                    getActivity().finish();
                                }
                            })
                            .show();*/
                } else if (error instanceof TimeoutError) {


                    Toast.makeText(getActivity(), "TimeOut eRROR", Toast.LENGTH_SHORT).show();
                   /* new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error!")
                            .setConfirmText("OK").setContentText("Connection TimeOut! Please check your internet connection.")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismiss();
                                    getActivity().finish();
                                }
                            })
                            .show();*/
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("brd_id", BoardExtended.boardId);
                params.put("prjct_id", BoardExtended.projectId);
                params.put("lst_id", list_id);
                params.put("name", s);
                params.put("userId",pref.getString("user_id","") );
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(request);
    }

    private void changeListColorPopup() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.custom_alert_dialogbox_for_bg_color_list,null);
        final AlertDialog alertDialog = new  AlertDialog.Builder(getContext()).create();

        alertDialog.setCancelable(false);
         noColor = (Button)customView.findViewById(R.id.noColor);
         blueColor = (Button)customView.findViewById(R.id.blueColor);
         brownColor = (Button)customView.findViewById(R.id.brownColor);
         grayColor = (Button)customView.findViewById(R.id.grayColor);
         greenColor = (Button)customView.findViewById(R.id.greenColor);
         orangeColor = (Button)customView.findViewById(R.id.orangeColor);
         purpleColor = (Button)customView.findViewById(R.id.purpleColor);
         yellowColor = (Button)customView.findViewById(R.id.yellowColor);
         redColor = (Button)customView.findViewById(R.id.redColor);
         ImageView crossIcon= (ImageView) customView.findViewById(R.id.crossIcon);
         crossIcon.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 alertDialog.dismiss();
             }
         });

        noColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                changeListBgColor("");
            }
        });
        blueColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
               // tvName.setBackgroundColor(getActivity().getResources().getColor(R.color.blue));
                changeListBgColor("00A2E8");
            }
        });
        brownColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                //tvName.setBackgroundColor(getActivity().getResources().getColor(R.color.brown));
                changeListBgColor("B97A57");
            }
        });
        grayColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
               // tvName.setBackgroundColor(getActivity().getResources().getColor(R.color.light_black));
                changeListBgColor("C3C3C3");
            }
        });
        greenColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                //tvName.setBackgroundColor(getActivity().getResources().getColor(R.color.green));
                changeListBgColor("B5E61D");
            }
        });
        orangeColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
               // tvName.setBackgroundColor(getActivity().getResources().getColor(R.color.colorOrange));
                changeListBgColor("FF7F27");
            }
        });
        purpleColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
              //  tvName.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPurple));
                changeListBgColor("A349A4");
            }
        });
        yellowColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
               // tvName.setBackgroundColor(getActivity().getResources().getColor(R.color.colorYellow));
                changeListBgColor("f2d600");
            }
        });
        redColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
               // tvName.setBackgroundColor(getActivity().getResources().getColor(R.color.colorRed));
                changeListBgColor("eb5a46");
            }
        });

        alertDialog.setView(customView);
        alertDialog.show();
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.popup_edit_delete, menu);
//
//        int positionOfMenuItem = 0;
//        MenuItem item = menu.getItem(positionOfMenuItem);
//        SpannableString s = new SpannableString("My red MenuItem");
//        s.setSpan(new ForegroundColorSpan(Color.RED), 0, s.length(), 0);
//        item.setTitle(s);
//    }



     public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public void passDataToFragment(String someValue){

    }

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }

    public void showCustomDialog(){


        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.custom_alert_dialogbox,null);
        final AlertDialog alertDialog = new  AlertDialog.Builder(getContext()).create();

        alertDialog.setCancelable(false);
        edt = (EditText)customView.findViewById(R.id.input_watever);
        showKeyBoard(edt);
        TextView addCard = (TextView)customView.findViewById(R.id.btn_add_board);
        TextView addCardAndMore = (TextView)customView.findViewById(R.id.btn_add_board1);
        TextView headingTitle = (TextView)customView.findViewById(R.id.headingTitle);
        headingTitle.setText("Add Card");
        addCard.setText("Save and Close");
        final TextView cancelbtn = (TextView) customView.findViewById(R.id.btn_cancel);
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyBoard(edt);
                alertDialog.dismiss();
            }
        });
        addCardAndMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String projectData = edt.getText().toString();
                String  boardsFragmentData = edt.getText().toString();

                if (!(boardsFragmentData.equals("")) && boardsFragmentData.trim().length()>0) {
                   /* boardsFragmentPojoData = new ProjectsPojo();
                    boardsFragmentPojoData.setData(boardsFragmentData);
                    listPojo.add(boardsFragmentPojoData);
                    adapter = new FragmentBoardsAdapter(getActivity(), listPojo);


                    lv.setAdapter(adapter);*/
                    row++;
                    saveCardByListId(boardsFragmentData,row);
                    edt.setText("");
                }
                else {
                    Toast.makeText(getActivity(),"Please Enter Card Name",Toast.LENGTH_SHORT).show();
                }




            }
        });
        addCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String projectData = edt.getText().toString();
                String  boardsFragmentData = edt.getText().toString();

                if (!(boardsFragmentData.equals("")) && boardsFragmentData.trim().length()>0) {
                   /* boardsFragmentPojoData = new ProjectsPojo();
                    boardsFragmentPojoData.setData(boardsFragmentData);
                    listPojo.add(boardsFragmentPojoData);
                    adapter = new FragmentBoardsAdapter(getActivity(), listPojo);


                    lv.setAdapter(adapter);*/
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edt.getWindowToken(), 0);
                   row++;
                   saveCardByListId(boardsFragmentData,row);

                    alertDialog.dismiss();
                }
                else {
                    Toast.makeText(getActivity(),"Please Enter Card Name",Toast.LENGTH_SHORT).show();
                }




            }
        });

        alertDialog.setView(customView);
        alertDialog.show();

    }

    private void showKeyBoard(EditText title) {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
    }
    private void hideKeyBoard(EditText title) {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(title.getWindowToken(), 0);
    }
    public void getCardList(final String lsitId) {

      /* final ProgressDialog ringProgressDialog = ProgressDialog.show(getContext(), "", "Please wait ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();*/

        final SharedPreferences pref = getActivity().getSharedPreferences("UserPrefs", MODE_PRIVATE);
        StringRequest request = new StringRequest(Request.Method.POST, GET_CARDS_FOR_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                      //  ringProgressDialog.dismiss();
                        if (!(response.equals("false"))) {

                            listPojo = new ArrayList<>();
                            labelsPojoList = new ArrayList<>();
                            cardLabelsPojoList = new ArrayList<>();
                            cardMembersPojoList = new ArrayList<>();
                            cardCoverPojoList = new ArrayList<>();
                            cardCheckboxPojoList = new ArrayList<>();

                            try {
                                ProjectsPojo projectsPojo = null;
                                JSONObject mainObject=new JSONObject(response);
                                JSONArray jsonArrayCards = mainObject.getJSONArray("cards");
                                JSONArray jsonArrayLabels = mainObject.getJSONArray("labels");
                                JSONArray jsonArrayMembers = mainObject.getJSONArray("members");
                                JSONArray jsonArrayAttachments = mainObject.getJSONArray("attachments");
                                JSONArray jsonArrayAttachmentsCover = mainObject.getJSONArray("attachment_cover");
                                JSONArray jsonArrayCheckbox = mainObject.getJSONArray("checkbox");
                                //JSONObject cardsObject = jsonArray.getJSONObject(0);

                                row=jsonArrayCards.length();
                                for (int i = 0; i < jsonArrayCards.length(); i++) {
                                    JSONObject jsonObject = jsonArrayCards.getJSONObject(i);
                                    JSONArray jsonArray=jsonArrayAttachments.getJSONArray(i);

                                    //JSONObject jsonObject1 = jsonArrayAttachments.getJSONObject(i);

                                     projectsPojo = new ProjectsPojo();
                                 // CardAssociatedLabelsPojo labelsPojo = new CardAssociatedLabelsPojo();
                                    projectsPojo.setId(jsonObject.getString("id"));
                                    projectsPojo.setData(jsonObject.getString("card_name"));
                                    projectsPojo.setAttachment(jsonObject.getString("file_name"));
                                    projectsPojo.setDueDate(jsonObject.getString("card_end_date"));
                                    projectsPojo.setDuetTime(jsonObject.getString("card_due_time"));
                                    projectsPojo.setStartDate(jsonObject.getString("card_start_date"));
                                    projectsPojo.setCardDescription(jsonObject.getString("card_description"));
                                    projectsPojo.setIsCardComplete(jsonObject.getString("card_is_complete"));
                                    projectsPojo.setStartTime(jsonObject.getString("card_start_time"));
                                    projectsPojo.setIsCardLocked(jsonObject.getString("is_locked"));
                                    projectsPojo.setIsCardSubscribed(jsonObject.getString("subscribed"));
                                   // projectsPojo.setCardAssignedMemberId(jsonObject.getString("crd_assigned_membr_id"));
                                    projectsPojo.setnOfAttachments(String.valueOf(jsonArray.length()));
                                    projectsPojo.setAssignedTo(jsonObject.getString("assigned_to"));

                                   // projectsPojo.setBoardAssociatedLabelsId(jsonObject.getString("board_assoc_label_id"));
                                    //projectsPojo.setLabels(jsonObject.getString("label_color"));

//                                    cardLabelsPojoList.add(labelsPojo);

                                    listPojo.add(projectsPojo);
                                   // getLabelsList(jsonObject.getString("id"));

                                }
                                for(int j=0;j<jsonArrayLabels.length();j++){
                                    CardAssociatedLabelsPojo labelsPojo = new CardAssociatedLabelsPojo();
                                    JSONArray jsonArray=jsonArrayLabels.getJSONArray(j);
                                    String[] labels = new String[jsonArray.length()];
                                    String[] labelText = new String[jsonArray.length()];
                                    for (int k=0;k<jsonArray.length();k++){

                                        JSONObject jsonObject=jsonArray.getJSONObject(k);
                                        labels[k]=jsonObject.getString("label_color");
                                        if(jsonObject.getString("label_text")==null || jsonObject.getString("label_text").equals("null")){
                                            labelText[k]="";
                                        }else {
                                            labelText[k] = jsonObject.getString("label_text");
                                        }
                                    }
                                    labelsPojo.setLabels(labels);
                                    labelsPojo.setLabelText(labelText);
                                    cardLabelsPojoList.add(labelsPojo);
                                }

                                for(int j=0;j<jsonArrayMembers.length();j++){
                                    CardAssociatedMembersPojo membersPojo = new CardAssociatedMembersPojo();
                                    JSONArray jsonArray=jsonArrayMembers.getJSONArray(j);
                                    String[] members = new String[jsonArray.length()];
                                    String[] labelText = new String[jsonArray.length()];
                                    String[] gp_picture = new String[jsonArray.length()];
                                    String[] membersId=new String[jsonArray.length()];
                                    String subsribed = "";
                                    String isCurrentMemberAssigned="0";
                                   // SharedPreferences pref = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                                    for (int k=0;k<jsonArray.length();k++){

                                        JSONObject jsonObject=jsonArray.getJSONObject(k);
                                        members[k]=jsonObject.getString("profile_pic");
                                        labelText[k]=jsonObject.getString("initials");
                                        gp_picture[k]=jsonObject.getString("gp_picture");
                                        membersId[k]=jsonObject.getString("uid");
                                       if(jsonObject.getString("uid").equals(pref.getString("user_id",""))) {
                                            subsribed = jsonObject.getString("subscribed");
                                            isCurrentMemberAssigned="1";
                                        }
                                       /* if(jsonObject.getString("label_text")==null || jsonObject.getString("label_text").equals("null")){
                                            labelText[k]="";
                                        }else {
                                            labelText[k] = jsonObject.getString("label_text");
                                        }*/
                                    }
                                    membersPojo.setMembers(members);
                                    membersPojo.setInitials(labelText);
                                    membersPojo.setGp_pictures(gp_picture);
                                    membersPojo.setMemberSubscribed(subsribed);
                                    membersPojo.setMemberId(membersId);
                                    membersPojo.setIsCurrentMemberAssigned(isCurrentMemberAssigned);
                                  //  labelsPojo.setLabelText(labelText);
                                    cardMembersPojoList.add(membersPojo);
                                }
                                for(int j=0;j<jsonArrayAttachmentsCover.length();j++){
                                    CardAssociatedCoverPojo membersPojo = new CardAssociatedCoverPojo();
                                    JSONArray jsonArray=jsonArrayAttachmentsCover.getJSONArray(j);
                                    String[] attachmentName = new String[jsonArray.length()];
                                    String[] makeCover = new String[jsonArray.length()];
                                   // String attachmentName=null;
                                    //String makeCover=null;
                                    for (int k=0;k<jsonArray.length();k++){

                                        JSONObject jsonObject=jsonArray.getJSONObject(k);
                                        attachmentName[k]=jsonObject.getString("file_name");
                                        makeCover[k]=jsonObject.getString("make_cover");
                                       /* if(jsonObject.getString("label_text")==null || jsonObject.getString("label_text").equals("null")){
                                            labelText[k]="";
                                        }else {
                                            labelText[k] = jsonObject.getString("label_text");
                                        }*/
                                    }
                                    membersPojo.setFileName(attachmentName);
                                    membersPojo.setIsCover(makeCover);
                                    //  labelsPojo.setLabelText(labelText);
                                    cardCoverPojoList.add(membersPojo);
                                }
                                for(int j=0;j<jsonArrayCheckbox.length();j++){
                                    CardAssociatedCheckBox checkBoxPojo = new CardAssociatedCheckBox();
                                    JSONArray jsonArray=jsonArrayCheckbox.getJSONArray(j);
                                    checkBoxPojo.setTotalCheckBoxes(jsonArray.length());
                                   int checked=0;
                                    for (int k=0;k<jsonArray.length();k++){

                                        JSONObject jsonObject=jsonArray.getJSONObject(k);
                                       if(jsonObject.getString("is_checked").equals("1")){
                                           checked++;
                                       }
                                    }
                                    checkBoxPojo.setCheckedCheckBox(String.valueOf(checked));

                                    //  labelsPojo.setLabelText(labelText);
                                    cardCheckboxPojoList.add(checkBoxPojo);
                                }

                                adapter = new FragmentBoardsAdapter(getActivity(), listPojo,cardLabelsPojoList,cardMembersPojoList,cardCoverPojoList,0,list_id,cardCheckboxPojoList,broadcastReceiver);
                                lv.setAdapter(adapter);
                               /* for(int i=0;i<listPojo.size();i++){
                                   // && !listPojo.get(i).getAssignedTo().equals(pref.getString("user_id","")
                                    if(listPojo.get(i).getIsCardLocked().equals("1")){
                                        //  Collections.emptyList();
                                        if(cardMembersPojoList.get(i).getIsCurrentMemberAssigned().equals("0")) {
                                            listPojo.remove(i);
                                            cardLabelsPojoList.remove(i);
                                            cardMembersPojoList.remove(cardMembersPojoList.get(i));
                                            cardCoverPojoList.remove(cardCoverPojoList.get(i));
                                            adapter.notifyDataSetChanged();
                                        }
                                    }
                                }*/


                                /*try {
                                    cardAssociatedLabelsAdapter = new CardAssociatedLabelsAdapter(getActivity(), cardLabelsPojoList);
                                    cardAssociatedLabelRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true));
                                    cardAssociatedLabelRecycler.setAdapter(cardAssociatedLabelsAdapter);
                                }catch (Exception e){
                                    String s=e.toString();
                                }*/
                            } catch (JSONException e) {
                                e.printStackTrace();


                            }

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

             //ringProgressDialog.dismiss();
                if (error instanceof NoConnectionError) {


                    Toast.makeText(getActivity(), "check your internet connection", Toast.LENGTH_SHORT).show();
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error!")
                            .setConfirmText("OK").setContentText("check your internet connection")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismiss();
                                    getActivity().finish();
                                }
                            })
                            .show();
                } else if (error instanceof TimeoutError) {
                    //getActivity().finish();
                    //    Toast.makeText(getActivity(), "TimeOut eRROR", Toast.LENGTH_SHORT).show();
                   // ChildFragmentBoardExtended fragment = new ChildFragmentBoardExtended();
                   /* if(fragment.isVisible()) {
                        new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Error!")
                                .setConfirmText("OK").setContentText("Connection TimeOut! Please check your internet connection.")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismiss();
                                        getActivity().finish();
                                    }
                                })
                                .show();
                    }*/
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("board_id", BoardExtended.boardId);
                params.put("project_id", BoardExtended.projectId);
                 params.put("userId", pref.getString("user_id",""));
                 params.put("list_id", lsitId);
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(request);

    }
    public void saveCardByListId(final String cardName, final int row) {
        ringProgressDialog = ProgressDialog.show(getContext(), "", "Please wait ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();
        final SharedPreferences pref = getActivity().getSharedPreferences("UserPrefs", MODE_PRIVATE);
        StringRequest request = new StringRequest(Request.Method.POST, SAVE_CARD_BY_LIST_ID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    String[] labels=new String[0];
                        ringProgressDialog.dismiss();
                       /* if (!(response.equals("false"))) {
                            boardsFragmentPojoData = new ProjectsPojo();
                            labelsPojo = new CardAssociatedLabelsPojo();
                            boardsFragmentPojoData.setId(response.trim().toString());
                            boardsFragmentPojoData.setData(cardName);
                            labelsPojo.setLabels(labels);
                            listPojo.add(boardsFragmentPojoData);
                            cardLabelsPojoList.add(labelsPojo);
                            adapter = new FragmentBoardsAdapter(getActivity(), listPojo,cardLabelsPojoList);

                            adapter.notifyDataSetChanged();
                            lv.setAdapter(adapter);


                        }*/
                        Toast.makeText(getActivity(), "Card Saved Successfully !",Toast.LENGTH_LONG).show();
                        getCardList(list_id);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                ringProgressDialog.dismiss();
                if (error instanceof NoConnectionError) {


                    Toast.makeText(getActivity(), "check your internet connection", Toast.LENGTH_SHORT).show();
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error!")
                            .setConfirmText("OK").setContentText("check your internet connection")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismiss();
                                    getActivity().finish();
                                }
                            })
                            .show();
                } else if (error instanceof TimeoutError) {


                    Toast.makeText(getActivity(), "TimeOut eRROR", Toast.LENGTH_SHORT).show();
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error!")
                            .setConfirmText("OK").setContentText("Connection TimeOut! Please check your internet connection.")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismiss();
                                    getActivity().finish();
                                }
                            })
                            .show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("board_id", BoardExtended.boardId);
                params.put("prjct_id", BoardExtended.projectId);
                params.put("userId", pref.getString("user_id",""));
                params.put("list_id", list_id);
                params.put("name", cardName);
                params.put("row", String.valueOf(row));
                params.put("my_card", "");
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(request);
    }
    public void deleteList() {
        ringProgressDialog = ProgressDialog.show(getContext(), "", "Please wait ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();
        final SharedPreferences pref = getActivity().getSharedPreferences("UserPrefs", MODE_PRIVATE);
        StringRequest request = new StringRequest(Request.Method.POST, DELETE_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ringProgressDialog.dismiss();

                        ParentBoardExtendedFragment.removeSpecificPage(ParentBoardExtendedFragment.getCurrentPosition(),"delete");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                ringProgressDialog.dismiss();
                if (error instanceof NoConnectionError) {


                    Toast.makeText(getActivity(), "check your internet connection", Toast.LENGTH_SHORT).show();
                    /*new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error!")
                            .setConfirmText("OK").setContentText("No Internet Connection")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismiss();
                                    getActivity().finish();
                                }
                            })
                            .show();*/
                } else if (error instanceof TimeoutError) {


                    Toast.makeText(getActivity(), "TimeOut eRROR", Toast.LENGTH_SHORT).show();
                   /* new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error!")
                            .setConfirmText("OK").setContentText("Connection TimeOut! Please check your internet connection.")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismiss();
                                    getActivity().finish();
                                }
                            })
                            .show();*/
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("boardId", BoardExtended.boardId);
                params.put("projectId", BoardExtended.projectId);
                params.put("listId", list_id);
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(request);
    }
    public void changeListBgColor(final String bgColor) {
        ringProgressDialog = ProgressDialog.show(getContext(), "", "Please wait ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();
        final SharedPreferences pref = getActivity().getSharedPreferences("UserPrefs", MODE_PRIVATE);
        StringRequest request = new StringRequest(Request.Method.POST, UPDATE_COLOR_BG_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ringProgressDialog.dismiss();
                        if(bgColor.equals("")) {
                            tvName.setBackgroundColor(getActivity().getResources().getColor(R.color.colorWhite));
                            ParentBoardExtendedFragment.removeSpecificPage(ParentBoardExtendedFragment.getCurrentPosition(),"delete");
                            ParentBoardExtendedFragment.updateBundleData(childname,p_id,b_id,list_id,"","",ParentBoardExtendedFragment.getCurrentPosition());
                        }else if (bgColor.equals("00A2E8")){
                            tvName.setBackgroundColor(getActivity().getResources().getColor(R.color.blue));
                            ParentBoardExtendedFragment.removeSpecificPage(ParentBoardExtendedFragment.getCurrentPosition(),"delete");
                            ParentBoardExtendedFragment.updateBundleData(childname,p_id,b_id,list_id,"00A2E8","",ParentBoardExtendedFragment.getCurrentPosition());
                           // ParentBoardExtendedFragment.updateBundleData(childname,p_id,b_id,list_id,"00A2E8","");
                        }else if (bgColor.equals("B97A57")){
                            tvName.setBackgroundColor(getActivity().getResources().getColor(R.color.brown));
                            ParentBoardExtendedFragment.removeSpecificPage(ParentBoardExtendedFragment.getCurrentPosition(),"delete");
                            ParentBoardExtendedFragment.updateBundleData(childname,p_id,b_id,list_id,"B97A57","",ParentBoardExtendedFragment.getCurrentPosition());

                        }else if (bgColor.equals("B5E61D")){
                            tvName.setBackgroundColor(getActivity().getResources().getColor(R.color.green));
                            ParentBoardExtendedFragment.removeSpecificPage(ParentBoardExtendedFragment.getCurrentPosition(),"delete");
                            ParentBoardExtendedFragment.updateBundleData(childname,p_id,b_id,list_id,"B5E61D","",ParentBoardExtendedFragment.getCurrentPosition());

                        }else if (bgColor.equals("FF7F27")){
                            tvName.setBackgroundColor(getActivity().getResources().getColor(R.color.orange));
                            ParentBoardExtendedFragment.removeSpecificPage(ParentBoardExtendedFragment.getCurrentPosition(),"delete");
                            ParentBoardExtendedFragment.updateBundleData(childname,p_id,b_id,list_id,"FF7F27","",ParentBoardExtendedFragment.getCurrentPosition());

                        }else if (bgColor.equals("A349A4")){
                            tvName.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPurple));
                            ParentBoardExtendedFragment.removeSpecificPage(ParentBoardExtendedFragment.getCurrentPosition(),"delete");
                            ParentBoardExtendedFragment.updateBundleData(childname,p_id,b_id,list_id,"A349A4","",ParentBoardExtendedFragment.getCurrentPosition());

                        }else if (bgColor.equals("f2d600")){
                            tvName.setBackgroundColor(getActivity().getResources().getColor(R.color.colorYellow));
                            ParentBoardExtendedFragment.removeSpecificPage(ParentBoardExtendedFragment.getCurrentPosition(),"delete");
                            ParentBoardExtendedFragment.updateBundleData(childname,p_id,b_id,list_id,"f2d600","",ParentBoardExtendedFragment.getCurrentPosition());

                        }else if (bgColor.equals("eb5a46")){
                            tvName.setBackgroundColor(getActivity().getResources().getColor(R.color.colorRed));
                            ParentBoardExtendedFragment.removeSpecificPage(ParentBoardExtendedFragment.getCurrentPosition(),"delete");
                            ParentBoardExtendedFragment.updateBundleData(childname,p_id,b_id,list_id,"eb5a46","",ParentBoardExtendedFragment.getCurrentPosition());


                        }else if (bgColor.equals("C3C3C3")){
                            tvName.setBackgroundColor(getActivity().getResources().getColor(R.color.gray));
                            ParentBoardExtendedFragment.removeSpecificPage(ParentBoardExtendedFragment.getCurrentPosition(),"delete");
                            ParentBoardExtendedFragment.updateBundleData(childname,p_id,b_id,list_id,"C3C3C3","",ParentBoardExtendedFragment.getCurrentPosition());

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                ringProgressDialog.dismiss();
                if (error instanceof NoConnectionError) {


                    Toast.makeText(getActivity(), "check your internet connection", Toast.LENGTH_SHORT).show();
                    /*new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error!")
                            .setConfirmText("OK").setContentText("No Internet Connection")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismiss();
                                    getActivity().finish();
                                }
                            })
                            .show();*/
                } else if (error instanceof TimeoutError) {


                    Toast.makeText(getActivity(), "TimeOut eRROR", Toast.LENGTH_SHORT).show();
                   /* new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error!")
                            .setConfirmText("OK").setContentText("Connection TimeOut! Please check your internet connection.")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismiss();
                                    getActivity().finish();
                                }
                            })
                            .show();*/
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("board_id", BoardExtended.boardId);
                params.put("prjct_id", BoardExtended.projectId);
                params.put("list_id", list_id);
                params.put("bgcolor", bgColor);
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(request);
    }


    private void showDialog(final String data) {

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View customView = inflater.inflate(R.layout.copy_move_list, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();

        alertDialog.setView(customView);
        alertDialog.show();

        TextView heading, sub , pos;

        TextView cancel, copy;

        heading = (TextView) customView.findViewById(R.id.heading);
        sub = (TextView) customView.findViewById(R.id.board_txt);
        pos = (TextView) customView.findViewById(R.id.pos_txt);
        Postions = (Spinner) customView.findViewById(R.id.position);
        Projects = (Spinner) customView.findViewById(R.id.projects_group);
        boards = (Spinner) customView.findViewById(R.id.board);
        copy = (TextView) customView.findViewById(R.id.copy);
        cancel = (TextView) customView.findViewById(R.id.close);


        getSpinnerData();

        if (data.equals("move")) {
            heading.setText("Move List");
            sub.setText("Boards: ");
            copy.setText("Move");
        }


        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (data.equals("move")) {
                    if(Projects.getSelectedItemPosition()!=-1) {
                        if(boards.getSelectedItemPosition()!=-1 && boards.getSelectedItemPosition()!=0) {
                            moveList();
                            alertDialog.dismiss();
                        }else {
                            Toast.makeText(getActivity(), "Please Select Board First", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(getActivity(), "Please Select Project First", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if(Projects.getSelectedItemPosition()!=-1) {
                        if(boards.getSelectedItemPosition()!=-1 && boards.getSelectedItemPosition()!=0) {
                        copyList();
                            alertDialog.dismiss();
                        }else {
                            Toast.makeText(getActivity(), "Please Select Board First", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(getActivity(), "Please Select Project First", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                alertDialog.dismiss();

            }
        });

    }

    public void getSpinnerData() {


        spinnerValues = new ArrayList<>();
        spinnerGroupIds = new ArrayList<>();
        postions_list = new ArrayList<>();


        StringRequest request = new StringRequest(Request.Method.POST, End_Points.GET_ALL_PROJECS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        spinnerValues.add(0,"Select");
                        spinnerGroupIds.add(0,"0");
                        try {

                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {

                                JSONObject object = new JSONObject(array.getString(i));
                                spinnerValues.add(String.valueOf(object.get("project_name")));
                                spinnerGroupIds.add(String.valueOf(object.get("project_id")));

                            }

                            ArrayAdapter<String> projectADdapter;
                            projectADdapter = new ArrayAdapter<String>(getActivity(), R.layout.nothing_selected_spinnerdate, spinnerValues);
                            projectADdapter.setDropDownViewResource(R.layout.nothing_selected_spinnerdate);
                            Projects.setAdapter(projectADdapter);
                            Projects.setOnItemSelectedListener(new CustomOnItemSelectedListener_boards());


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                if (error instanceof NoConnectionError) {

                    Toast.makeText(getActivity(), "check your internet connection", Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {

                    Toast.makeText(getActivity(), "Connection time out Error", Toast.LENGTH_SHORT).show();

                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                SharedPreferences pref = getActivity().getSharedPreferences("UserPrefs", MODE_PRIVATE);
                String userId = pref.getString("user_id", "");

                params.put("userId", userId);
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);

    }
    public class CustomOnItemSelectedListener_boards implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, final int pos,
                                   long id) {
            if(pos!=-1 && pos!=0)
            getBorads(spinnerGroupIds.get(pos));



        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub

        }

    }

    public class CustomOnItemSelectedListener_position implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, final int pos,
                                   long id) {
            if(pos!=0 && pos!=-1)
            getPosition(boards_ids.get(pos));



        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub

        }

    }


    public void getPosition(final String P_id) {

        StringRequest request = new StringRequest(Request.Method.POST, End_Points.GET_POSITION_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        postions_list = new ArrayList<>();
                        if(response.equals("0")){
                            postions_list.add(1+"");
                        }
                        for (int i = 1; i <=Integer.valueOf(response)+1; i++) {

                            postions_list.add(i+"");
                        }

                        ArrayAdapter<String> projectADdapter;
                        projectADdapter = new ArrayAdapter<String>(getActivity(), R.layout.nothing_selected_spinnerdate, postions_list);
                        projectADdapter.setDropDownViewResource(R.layout.nothing_selected_spinnerdate);
                        Postions.setAdapter(projectADdapter);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                if (error instanceof NoConnectionError) {

                    Toast.makeText(getActivity(), "check your internet connection", Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {

                    Toast.makeText(getActivity(), "Connection time out Error", Toast.LENGTH_SHORT).show();

                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put("b_id", P_id);
                params.put("p_id", p_id);
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);

    }

    public void getBorads(final String p_Id) {
        final ProgressDialog ringProgressDialog;
        ringProgressDialog = ProgressDialog.show(getActivity(), "", "Please wait ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        final StringRequest request = new StringRequest(Request.Method.POST, End_Points.GET_WORK_BOARD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ringProgressDialog.dismiss();

                        boards_ids = new ArrayList<>();
                        boards_name = new ArrayList<>();

                        boards_name.add(0,"Select");
                        boards_ids.add(0,"0");
                        if(response.equals("{\"nodata\":0}")){
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                                    (getActivity(),R.layout.nothing_selected_spinnerdate,boards_name);
                            dataAdapter.setDropDownViewResource(R.layout.nothing_selected_spinnerdate);

                            boards.setAdapter(dataAdapter);
                        }


                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0 ; i < jsonArray.length();i++)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                boards_name.add(jsonObject.getString("board_name"));
                                boards_ids.add(jsonObject.getString("id"));

                            }

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                                    (getActivity(),R.layout.nothing_selected_spinnerdate,boards_name);
                            dataAdapter.setDropDownViewResource(R.layout.nothing_selected_spinnerdate);

                            boards.setAdapter(dataAdapter);

                            boards.setOnItemSelectedListener(new CustomOnItemSelectedListener_position());



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                ringProgressDialog.dismiss();
                if (error instanceof NoConnectionError) {

                    new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error!")
                            .setConfirmText("OK").setContentText("check your internet connection")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismiss();

                                }
                            })
                            .show();
                } else if (error instanceof TimeoutError) {

                    new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
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

                params.put("p_id",p_Id);
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);


    }


    public void moveList() {


        ringProgressDialog = ProgressDialog.show(getActivity(), "", "Please wait ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, End_Points.MOVE_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ringProgressDialog.dismiss();
                        Toast.makeText(getActivity(), "Moved Successfully", Toast.LENGTH_SHORT).show();
                        //getActivity().finish();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ringProgressDialog.dismiss();

                if (error instanceof NoConnectionError) {

                    Toast.makeText(getActivity(), "check your internet connection", Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {

                    Toast.makeText(getActivity(), "Connection time out Error", Toast.LENGTH_SHORT).show();

                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                int pos1 = Postions.getSelectedItemPosition();
                int pos = Projects.getSelectedItemPosition();
                int bord = boards.getSelectedItemPosition();
                final SharedPreferences pref = getActivity().getSharedPreferences("UserPrefs", MODE_PRIVATE);


                params.put("originalboardid", b_id);
                params.put("originalprojectid", p_id);
                params.put("originallistid", list_id);
                params.put("project_id_to", spinnerGroupIds.get(pos));
                params.put("position_to", postions_list.get(pos1));
                params.put("board_id_to", boards_ids.get(bord));
                params.put("userId",pref.getString("user_id",""));
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);

    }

    public void copyList() {


        ringProgressDialog = ProgressDialog.show(getActivity(), "", "Please wait ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, End_Points.COPY_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ringProgressDialog.dismiss();
                        Toast.makeText(getActivity(), "Copied Successfully", Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ringProgressDialog.dismiss();

                if (error instanceof NoConnectionError) {

                    Toast.makeText(getActivity(), "check your internet connection", Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {

                    Toast.makeText(getActivity(), "Connection time out Error", Toast.LENGTH_SHORT).show();

                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                int pos = Projects.getSelectedItemPosition();
                int pos1 = Postions.getSelectedItemPosition();
                int bord = boards.getSelectedItemPosition();
                final SharedPreferences pref = getActivity().getSharedPreferences("UserPrefs", MODE_PRIVATE);

                params.put("originalboardid", b_id);
                params.put("originalprojectid", p_id);
                params.put("originallistid", list_id);
                params.put("project_id_to", spinnerGroupIds.get(pos));
                params.put("position_to", postions_list.get(pos1));
                params.put("board_id_to", boards_ids.get(bord));
                params.put("userId",pref.getString("user_id",""));
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);

    }

/*public void getLabelsList(final String cardId){
    StringRequest request = new StringRequest(Request.Method.POST, GET_CARD_ASSOC_LABELS,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {


                    if (!(response.equals("false"))) {


                        cardLabelsPojoList = new ArrayList<>();

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                           // row=jsonArray.length();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                CardAssociatedLabelsPojo labelsPojo = new CardAssociatedLabelsPojo();

                                labelsPojo.setLabel(jsonObject.getString("label_color"));
                                cardLabelsPojoList.add(labelsPojo);


                            }

                                cardAssociatedLabelsAdapter=new CardAssociatedLabelsAdapter(getActivity(),cardLabelsPojoList);
                                cardAssociatedLabelRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true));
                                cardAssociatedLabelRecycler.setAdapter(cardAssociatedLabelsAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();


                        }

                    }
                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

//                ringProgressDialog.dismiss();
            if (error instanceof NoConnectionError) {


                Toast.makeText(getActivity(), "No internet", Toast.LENGTH_SHORT).show();
                new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error!")
                        .setConfirmText("OK").setContentText("No Internet Connection")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismiss();
                                getActivity().finish();
                            }
                        })
                        .show();
            } else if (error instanceof TimeoutError) {


                Toast.makeText(getActivity(), "TimeOut eRROR", Toast.LENGTH_SHORT).show();
                new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error!")
                        .setConfirmText("OK").setContentText("Connection TimeOut! Please check your internet connection.")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismiss();
                                getActivity().finish();
                            }
                        })
                        .show();
            }
        }
    }) {
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {

            Map<String, String> params = new HashMap<>();
            params.put("board_id", BoardExtended.boardId);
           *//* params.put("project_id", BoardExtended.projectId);
            // params.put("userId", pref.getString("user_id",""));
            params.put("list_id", lsitId);*//*
            return params;
        }
    };

    request.setRetryPolicy(new DefaultRetryPolicy(
            10000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
    requestQueue.add(request);
}*/




}
