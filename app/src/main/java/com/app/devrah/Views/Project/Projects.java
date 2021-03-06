package com.app.devrah.Views.Project;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.app.devrah.Adapters.ProjectsAdapter;
import com.app.devrah.Network.End_Points;
import com.app.devrah.R;
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


public class Projects extends Fragment implements View.OnClickListener {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public EditText etDescription;
    Button btnAddProject;
    View view;
    ProjectsAdapter adapter;
    String description;
    ImageView showSpinner;
    String groupId;
    EditText etProjectGroup;
    ArrayAdapter<String> timeAdapter;
    LinearLayout laEt, laSpinner;
    // String[] spinnerValues;
    List<String> spinnerValues;
    List<String> spinnerGroupIds;
    String groupData;
    List<ProjectsPojo> listPojo;
    ProjectsPojo projectPojoData;
    ListView lv;
    EditText title, spinnerData;
    String projectData;
    ProgressDialog ringProgressDialog;
    String GroupName;
    private Spinner spinnerProjectGroup;
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    AlertDialog alertDialog;
    SwipeRefreshLayout mySwipeRefreshLayout;
    EditText edtSeach;
    public Projects() {
        // Required empty public constructor
    }

    public static Projects newInstance(String param1, String param2) {
        Projects fragment = new Projects();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_projects, container, false);
        btnAddProject = (Button) view.findViewById(R.id.buttonAddProject);
        listPojo = new ArrayList<>();
        lv = (ListView) view.findViewById(R.id.projectsListView);
        mySwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        //   spinnerValues = new String[]{};
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        ProjectsActivity.status="1";
                        getProjectsData();
                        mySwipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
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
                if(filteredLeaves.size()<1){
                    projectPojoData = new ProjectsPojo();
                    projectPojoData.setData("No project found");
                    projectPojoData.setId("");
                    projectPojoData.setProjectStatus("");
                    projectPojoData.setProjectCreatedBy("");
                    projectPojoData.setProjectDescription("");
                    filteredLeaves.add(projectPojoData);
                }
                adapter = new ProjectsAdapter(getActivity(), filteredLeaves);
                lv.setAdapter(adapter);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //getSpinnerData();

        getProjectsData();
//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(getActivity(),BoardsActivity.class);
//                startActivity(intent);
//
//            }
//        });


        btnAddProject.setOnClickListener(this);

//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_projects, container, false);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
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

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.buttonAddProject:


                showCustomDialog();
                // showDialog();
                //    Toast.makeText(getContext(),"Button Pressed",Toast.LENGTH_LONG).show();
                break;

        }

    }

    public void showCustomDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View customView = layoutInflater.inflate(R.layout.custom_alert_for_projects, null);
        alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setCancelable(false);
        title = (EditText) customView.findViewById(R.id.input_title);
        etDescription = (EditText) customView.findViewById(R.id.etEmail);
        showKeyBoard(title);
        laEt = (LinearLayout) customView.findViewById(R.id.laEt);
        laSpinner = (LinearLayout) customView.findViewById(R.id.laSpinner);
        etProjectGroup = (EditText) customView.findViewById(R.id.etCustomSpinnerData);


        ImageView showET = (ImageView) customView.findViewById(R.id.showET);
        showSpinner = (ImageView) customView.findViewById(R.id.imageShowSpinner);

        spinnerProjectGroup = (Spinner) customView.findViewById(R.id.spinProjectGroup);
        //   spinnerData = (EditText)customView.findViewById(R.id.etCustomSpinnerData);

        getSpinnerData();

        // getProjectsData();

        showET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                laEt.setVisibility(View.VISIBLE);
                laSpinner.setVisibility(View.GONE);
//                spinnerProjectGroup.setVisibility(View.GONE);
//                spinnerData.setVisibility(View.VISIBLE);
            }
        });

        showSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                laSpinner.setVisibility(View.VISIBLE);
                laEt.setVisibility(View.GONE);

            }
        });


        TextView addCard = (TextView) customView.findViewById(R.id.addProject);
        TextView addCardAndMore = (TextView) customView.findViewById(R.id.addProject1);
        TextView cancel = (TextView) customView.findViewById(R.id.cancel_btn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyBoard(title);
                alertDialog.dismiss();
            }
        });
        addCard.setText("Save and Close");
        addCardAndMore.setText("Save and Add");
        addCardAndMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                projectData = title.getText().toString();
                final String projectDescription = etDescription.getText().toString();

                if (laEt.getVisibility() == View.VISIBLE) {
                    description = etProjectGroup.getText().toString();
                    if(etProjectGroup.getText().toString().equals("")){
                        Toast.makeText(getActivity(),"Please Enter Group Name",Toast.LENGTH_LONG).show();
                        return;
                    }else {
                        if(projectDescription.length()<=255) {
                            ProjectGroupEt();
                            etProjectGroup.setText("");
                            title.setText("");
                            etDescription.setText("");
                        }else {
                            Toast.makeText(getActivity(),"Description maximum limit is 255",Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                    // description = etDescription.getText().toString();

                }
                if (laSpinner.getVisibility() == View.VISIBLE) {
                    int pos=spinnerProjectGroup.getSelectedItemPosition();
                    if(pos!=-1){
                        GroupName = timeAdapter.getItem(spinnerProjectGroup.getSelectedItemPosition());
                        int index = spinnerValues.indexOf(GroupName);
                        groupId = spinnerGroupIds.get(index);
                        //description = spinnerProjectGroup.
                        //Toast.makeText(getActivity().getApplicationContext(), GroupName, Toast.LENGTH_SHORT).show();


                        if (!(projectData.isEmpty())) {
                           /* projectPojoData = new ProjectsPojo();
                            projectPojoData.setData(projectData);
                            projectPojoData.setDescription(description);
                            listPojo.add(projectPojoData);
                            adapter = new ProjectsAdapter(getActivity(), listPojo);
                            lv.setAdapter(adapter);*/
                            if(projectDescription.length()<=255) {
                                //alertDialog.dismiss();
                                ringProgressDialog = ProgressDialog.show(getActivity(), "Please wait ...", "", true);
                                ringProgressDialog.setCancelable(false);
                                ringProgressDialog.show();
                                StringRequest request = new StringRequest(Request.Method.POST, End_Points.ADD_NEW_PROJECT, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        ringProgressDialog.dismiss();
                                        if (response.equals("0")) {
                                            Toast.makeText(getActivity().getApplicationContext(), "Project name already exists!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            title.setText("");
                                            etDescription.setText("");
                                            getProjectsData();
                                            Toast.makeText(getActivity().getApplicationContext(), "Project Added Successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        ringProgressDialog.dismiss();
                                      //  alertDialog.dismiss();
                                        Toast.makeText(getActivity().getApplicationContext(), "check your internet connection", Toast.LENGTH_SHORT).show();

                                    }
                                }


                                )

                                {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {

                                        Map<String, String> params = new HashMap<>();
                                        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("UserPrefs", MODE_PRIVATE);
                                        String userId = pref.getString("user_id", "");

//                                params.put("id",userId);
//                                params.put("project_description",projectDescription);
//                                params.put("project_name",projectData);
//                                params.put("project_group",description);

                                        //  String userId = pref.getString("user_id","");

                                        params.put("id", userId);
                                        params.put("project_description", projectDescription);
                                        params.put("project_name", projectData);
                                        params.put("project_group", groupId);
                                        //   params.put("")

                                        //    params.put("pg_name",GroupName);


                                        // params.put()
                                        // params.put("password",strPassword );
                                        return params;
                                    }
                                };
                                RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
                                requestQueue.add(request);

                            }else
                            {
                                Toast.makeText(getActivity(),"Description maximum limit is 255",Toast.LENGTH_LONG).show();
                                return;
                            }
                        } else {
                            Toast.makeText(getActivity(), "Enter Project Name", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }else {
                        Toast.makeText(getActivity(), "Project Group is not Selected", Toast.LENGTH_SHORT).show();
                    }



                }
                //  final String projectDescription = etDescription.getText().toString();
                // = etDescription.getText().toString();
            }
        });

        addCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                projectData = title.getText().toString();
                final String projectDescription = etDescription.getText().toString();

                if (laEt.getVisibility() == View.VISIBLE) {
                    description = etProjectGroup.getText().toString();
                    if(etProjectGroup.getText().toString().equals("")){
                        Toast.makeText(getActivity(),"Please Enter Group Name",Toast.LENGTH_LONG).show();
                        return;
                    }else {
                        if(projectDescription.length()<=255) {
                            ProjectGroupEt();
                            hideKeyBoard(title);
                            alertDialog.dismiss();
                        }else {
                            Toast.makeText(getActivity(),"Description maximum limit is 255",Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                    // description = etDescription.getText().toString();

                }
                if (laSpinner.getVisibility() == View.VISIBLE) {
                    int pos=spinnerProjectGroup.getSelectedItemPosition();
                    if(pos!=-1){
                        GroupName = timeAdapter.getItem(spinnerProjectGroup.getSelectedItemPosition());
                        int index = spinnerValues.indexOf(GroupName);
                        groupId = spinnerGroupIds.get(index);
                        //description = spinnerProjectGroup.
                        //Toast.makeText(getActivity().getApplicationContext(), GroupName, Toast.LENGTH_SHORT).show();


                        if (!(projectData.isEmpty())) {
                           /* projectPojoData = new ProjectsPojo();
                            projectPojoData.setData(projectData);
                            projectPojoData.setDescription(description);
                            listPojo.add(projectPojoData);
                            adapter = new ProjectsAdapter(getActivity(), listPojo);
                            lv.setAdapter(adapter);*/
                            if(projectDescription.length()<=255) {
                                hideKeyBoard(title);
                            alertDialog.dismiss();
                            ringProgressDialog = ProgressDialog.show(getActivity(), "Please wait ...", "", true);
                            ringProgressDialog.setCancelable(false);
                            ringProgressDialog.show();
                            StringRequest request = new StringRequest(Request.Method.POST, End_Points.ADD_NEW_PROJECT, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    ringProgressDialog.dismiss();
                                    if (response.equals("0")) {
                                        Toast.makeText(getActivity().getApplicationContext(), "Project name already exists!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        getProjectsData();
                                        Toast.makeText(getActivity().getApplicationContext(), "Project Added Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    ringProgressDialog.dismiss();
                                    alertDialog.dismiss();
                                    Toast.makeText(getActivity().getApplicationContext(), "check your internet connection", Toast.LENGTH_SHORT).show();

                                }
                            }


                            )

                            {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {

                                    Map<String, String> params = new HashMap<>();
                                    SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("UserPrefs", MODE_PRIVATE);
                                    String userId = pref.getString("user_id", "");

//                                params.put("id",userId);
//                                params.put("project_description",projectDescription);
//                                params.put("project_name",projectData);
//                                params.put("project_group",description);

                                    //  String userId = pref.getString("user_id","");

                                    params.put("id", userId);
                                    params.put("project_description", projectDescription);
                                    params.put("project_name", projectData);
                                    params.put("project_group", groupId);
                                    //   params.put("")

                                    //    params.put("pg_name",GroupName);


                                    // params.put()
                                    // params.put("password",strPassword );
                                    return params;
                                }
                            };
                            RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
                            requestQueue.add(request);

                        }else
                            {
                                Toast.makeText(getActivity(),"Description maximum limit is 255",Toast.LENGTH_LONG).show();
                                return;
                            }
                        } else {
                            Toast.makeText(getActivity(), "Enter Project Name", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }else {
                        Toast.makeText(getActivity(), "Project Group is not Selected", Toast.LENGTH_SHORT).show();
                    }



                }
                //  final String projectDescription = etDescription.getText().toString();
                // = etDescription.getText().toString();
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

//    public void  showDialog(){
//
//    LayoutInflater layoutInflater = LayoutInflater.from(getContext());
//        View customView = layoutInflater.inflate(R.layout.custom_alert_dialogbox,null);
//        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
//        title = (EditText)customView.findViewById(R.id.input_watever);
//
//        TextView addCard = (TextView)customView.findViewById(R.id.btn_add_board);
//        addCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                projectData = title.getText().toString();
//
//                if (!(projectData.isEmpty())) {
//                    projectPojoData = new ProjectsPojo();
//                    projectPojoData.setData(projectData);
//                    listPojo.add(projectPojoData);
//                    adapter = new ProjectsAdapter(getActivity(), listPojo);
//
//
//                    lv.setAdapter(adapter);
//                }
//                else {
//                    Toast.makeText(getActivity(),"No Text Entered",Toast.LENGTH_SHORT).show();
//                }
//
//                alertDialog.dismiss();
//
//
//
//
//            }
//        });
//
//
//
//        alertDialog.setView(customView);
//alertDialog.show();
//
//
//
//    }
//

    ///////////////////////MAIN METHOD TO SHOW DATA///////////////////////////////////////////////
    public void getProjectsData() {

        final ProgressDialog ringProgressDialog;
        ringProgressDialog = ProgressDialog.show(getActivity(), "", "Please wait ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();


        StringRequest request = new StringRequest(Request.Method.POST, End_Points.GET_MEMBER_PROJECTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ringProgressDialog.dismiss();
                        edtSeach.setText("");
                        if (response.equals("false")) {
                            new SweetAlertDialog(getActivity().getApplicationContext(), SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Error!")
                                    .setConfirmText("OK").setContentText("Error")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismiss();

                                        }
                                    })
                                    .show();
                        } else {

                            String userid;

                            try {
                                listPojo = new ArrayList<>();
                                String firstname, email, lastName, profilePic;

                                JSONArray array = new JSONArray(response);
                                if(array.length()<1){
                                    projectPojoData = new ProjectsPojo();
                                    projectPojoData.setData("No project found");
                                    projectPojoData.setId("");
                                    projectPojoData.setProjectStatus("");
                                    projectPojoData.setProjectCreatedBy("");
                                    projectPojoData.setProjectDescription("");
                                    listPojo.add(projectPojoData);
                                }
                                for (int i = 0; i < array.length(); i++) {

                                    JSONObject object = new JSONObject(array.getString(i));


                                    projectPojoData = new ProjectsPojo();
                                    projectPojoData.setData(object.getString("project_name"));
                                    projectPojoData.setId(object.getString("project_id"));
                                    projectPojoData.setProjectStatus(object.getString("project_status"));
                                    projectPojoData.setProjectCreatedBy(object.getString("project_created_by"));
                                    projectPojoData.setProjectDescription(object.getString("project_description"));
                                    listPojo.add(projectPojoData);


                                }


                                adapter = new ProjectsAdapter(getActivity(), listPojo);


                                lv.setAdapter(adapter);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

              ringProgressDialog.dismiss();
                if (error instanceof NoConnectionError) {

                    Toast.makeText(getContext(), "check your internet connection", Toast.LENGTH_SHORT).show();

//                    new SweetAlertDialog(getActivity().getApplicationContext(), SweetAlertDialog.ERROR_TYPE)
//                            .setTitleText("Error!")
//                            .setConfirmText("OK").setContentText("No Internet Connection")
//                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                @Override
//                                public void onClick(SweetAlertDialog sDialog) {
//                                    sDialog.dismiss();
//                                }
//                            })
//                            .show();
                } else if (error instanceof TimeoutError) {

                    Toast.makeText(getActivity().getApplicationContext(), "Timeout Error", Toast.LENGTH_SHORT).show();

//                    new SweetAlertDialog(getActivity().getApplicationContext(), SweetAlertDialog.ERROR_TYPE)
//                            .setTitleText("Error!")
//                            .setConfirmText("OK").setContentText("Connection TimeOut! Please check your internet connection.")
//                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                @Override
//                                public void onClick(SweetAlertDialog sDialog) {
//                                    sDialog.dismiss();
//                                }
//                            })
//                            .show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("UserPrefs", MODE_PRIVATE);
                String userId = pref.getString("user_id", "");

                params.put("id", userId);
                params.put("status",ProjectsActivity.status);
                // params.put("password",strPassword );
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

    public void getSpinnerData() {


        spinnerValues = new ArrayList<>();
        spinnerGroupIds = new ArrayList<>();

        ringProgressDialog = ProgressDialog.show(getActivity(), "Please wait ...", "", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, End_Points.GET_SPINNER_GROUP_PROJECTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    //    Toast.makeText(getActivity().getApplicationContext(), response, Toast.LENGTH_SHORT).show();

                        ringProgressDialog.dismiss();
                        if (response.equals("false")) {
                            new SweetAlertDialog(getActivity().getApplicationContext(), SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Error!")
                                    .setConfirmText("OK").setContentText("Error")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismiss();

                                        }
                                    })
                                    .show();
                        } else {

                            String userid;

                            try {

                                String firstname, email, lastName, profilePic;
                                int pos=0;
                                JSONArray array = new JSONArray(response);
                                for (int i = 0; i < array.length(); i++) {

                                    JSONObject object = new JSONObject(array.getString(i));
                                    spinnerValues.add(String.valueOf(object.get("pg_name")));
                                    spinnerGroupIds.add(String.valueOf(object.get("pg_id")));
                                    if(object.get("pg_name").toString().equals("Unassigned"))
                                        pos=i;
                                   // Toast.makeText(getActivity().getApplicationContext(), spinnerValues.get(i), Toast.LENGTH_SHORT).show();
                                    //spinnerValues[i] = String.valueOf(object.get("pg_name"));

//                                    projectPojoData = new ProjectsPojo();
//                                    projectPojoData.setData(object.getString("project_name"));
//                                    projectPojoData.setId(object.getString("project_id"));
//                                    listPojo.add(projectPojoData);
                                }
                                timeAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.nothing_selected_spinnerdate, spinnerValues);
                                timeAdapter.setDropDownViewResource(R.layout.nothing_selected_spinnerdate);
                                spinnerProjectGroup.setAdapter(timeAdapter);

                                spinnerProjectGroup.setSelection(pos);


//                                spinnerProjectGroup.set

//                                adapter = new ProjectsAdapter(getActivity(), listPojo);
//
//
//                                lv.setAdapter(adapter);
//
//


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                ringProgressDialog.dismiss();
                //alertDialog.dismiss();
                if (error instanceof NoConnectionError) {

                    Toast.makeText(getActivity(), "check your internet connectionn", Toast.LENGTH_SHORT).show();


                } else if (error instanceof TimeoutError) {

                    Toast.makeText(getActivity(),"Connection TimeOut! Please check your internet connection."
                            , Toast.LENGTH_SHORT).show();

                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("UserPrefs", MODE_PRIVATE);
                String userId = pref.getString("user_id", "");

                params.put("user_id", userId);
                // params.put("password",strPassword );
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(request);

    }

    public void ProjectGroupEt() {

        description = etProjectGroup.getText().toString();


        final String projectDescription = etDescription.getText().toString();
        // = etDescription.getText().toString();

        if (!(projectData.isEmpty())) {
           /* projectPojoData = new ProjectsPojo();
            projectPojoData.setData(projectData);
            projectPojoData.setDescription(description);
            listPojo.add(projectPojoData);
            adapter = new ProjectsAdapter(getActivity(), listPojo);
            lv.setAdapter(adapter);*/

            StringRequest request = new StringRequest(Request.Method.POST, End_Points.ADD_NEW_PROJECT_ET, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String checkResponse=response.trim();
                    if(!checkResponse.equals("Project already exist")) {
                        getProjectsData();
                        Toast.makeText(getActivity().getApplicationContext(), "Project Added Successfully", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getActivity().getApplicationContext(), "Error, project's group name already exists!", Toast.LENGTH_SHORT).show();

                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(getActivity().getApplicationContext(), "check your internet connection", Toast.LENGTH_SHORT).show();

                }
            }


            )

            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new HashMap<>();
                    SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("UserPrefs", MODE_PRIVATE);
                    String userId = pref.getString("user_id", "");

                    params.put("id", userId);
                    params.put("project_description", projectDescription);
                    params.put("project_name", projectData);
                    //    params.put("project_group",description);

                    params.put("pg_name", description);
                    // params.put()
                    // params.put("password",strPassword );
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
            requestQueue.add(request);


        } else {
            Toast.makeText(getActivity(), "No Text Entered", Toast.LENGTH_SHORT).show();
        }
//
//        alertDialog.dismiss();


    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }


//
//        alertDialog.setView(customView);
//        alertDialog.show();


}







