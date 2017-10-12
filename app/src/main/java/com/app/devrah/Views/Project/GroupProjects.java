package com.app.devrah.Views.Project;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
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
import com.app.devrah.Adapters.CustomExpandableListAdapter;
import com.app.devrah.Adapters.GroupProjectAdapter;
import com.app.devrah.R;
import com.app.devrah.Views.readyInterface;
import com.app.devrah.pojo.GroupProjectsPojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.Context.MODE_PRIVATE;
import static com.app.devrah.Network.End_Points.GET_GROUP_PROJECTS;

public class GroupProjects extends Fragment implements View.OnClickListener,readyInterface {


    EditText edt;
    List<String> listDataHeader;

    HashMap<String, List<String>> listDataChild;
    HashMap<String, List<String>> listDataStatus;
    View view;
    Button addGrouProjects;
    ExpandableListView expandableLv;
    ListView AddGroupProjectListView;
    List<GroupProjectsPojo> pojoList;
     List<String> MyList;
     List<String> MYIDS;
    JSONArray projectsArray;
    String projectNameS, projectHeading;

    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    ArrayList<String> projectName;
    ArrayList<String> projectStatus;
    ArrayList<String> projectids;
    String groupProjectData;
    GroupProjectsPojo groupProjectsPojo;
    GroupProjectAdapter adapter;


    /////////////////////////////////////////
   public ArrayList<String> whatever, whateverId, projectId, projectNames, dummy;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    CustomExpandableListAdapter customAdapter;

    private OnFragmentInteractionListener mListener;
    SwipeRefreshLayout mySwipeRefreshLayout;

    public GroupProjects() {
        // Required empty public constructor
    }

    public static GroupProjects newInstance(String param1, String param2) {
        GroupProjects fragment = new GroupProjects();
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
        view = inflater.inflate(R.layout.fragment_group_projects, container, false);
     //   addGrouProjects = (Button) view.findViewById(R.id.buttonAddGroupProject);
      //  AddGroupProjectListView = (ListView) view.findViewById(R.id.groupProjectsListView);
        pojoList = new ArrayList<>();

        prepareDataList();

        expandableLv = (ExpandableListView) view.findViewById(R.id.lvExp);
        mySwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        //   spinnerValues = new String[]{};
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        prepareDataList();
                        mySwipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
//        addGrouProjects.setOnClickListener(this);
        // Inflate the layout for this fragment
        return view;


    }

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

       /* switch (v.getId()) {
            case R.id.buttonAddGroupProject:
                //  Toast.makeText(getContext(),"Button Pressed", Toast.LENGTH_LONG).show();
                showDialog();
                break;

        }


*/
    }

    @Override
    public void ready() {

        prepareDataList();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }


    public void showDialog() {



        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View customView = layoutInflater.inflate(R.layout.custom_alert_dialogbox, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        edt = (EditText) customView.findViewById(R.id.input_watever);

        TextView addCard = (TextView) customView.findViewById(R.id.btn_add_board);

        addCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupProjectData = edt.getText().toString();

                if (!(groupProjectData.isEmpty())) {

                    groupProjectsPojo = new GroupProjectsPojo();
                    groupProjectsPojo.setGroupProjectData(groupProjectData);
                    pojoList.add(groupProjectsPojo);
                    adapter = new GroupProjectAdapter(getActivity(), pojoList);
                    AddGroupProjectListView.setAdapter(adapter);


                } else
                    Toast.makeText(getActivity(), "No Text Entered", Toast.LENGTH_SHORT);


                alertDialog.dismiss();

            }
        });


        alertDialog.setView(customView);
        alertDialog.show();


    }

    public void prepareDataList() {

        final ProgressDialog ringProgressDialog;
        ringProgressDialog = ProgressDialog.show(getActivity(), "", "Please wait ...", true);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        listDataStatus = new HashMap<String, List<String>>();
        projectNames = new ArrayList<>();
         dummy = new ArrayList<String>();


        whatever = new ArrayList<>();
        whateverId = new ArrayList<>();
        projectId = new ArrayList<>();
        projectName = new ArrayList<>();
        projectStatus = new ArrayList<>();
        projectids = new ArrayList<>();

        ////////////////////////// STRING REQUEST ////////////////////////////GET_GROUP_PROJECTS


        StringRequest request = new StringRequest(Request.Method.POST,GET_GROUP_PROJECTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ringProgressDialog.dismiss();
                        //  ringProgressDialog.dismiss();
                        if (response.equals("false")) {
                            new SweetAlertDialog(getActivity().getApplicationContext(), SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Error!")
                                    .setConfirmText("OK").setContentText("Incorrect Username or Password")
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
                                //   projectNames = new ArrayList<>();

                                String firstname, email, lastName, profilePic;

                                //  JSONArray array = new JSONArray(response);
                                JSONObject object = new JSONObject(response);

                                String data =  object.getString("group_name");

                                JSONArray jsonArray = new JSONArray(data);


                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject obj = new JSONObject(jsonArray.getString(i));

                                    projectName.add(obj.getString("pg_id"));
                                    projectId.add(obj.getString("pg_name"));

                                    listDataChild.put(obj.getString("pg_id"),null);
                                }


                                String data2 =  object.getString("group_projects");

                                JSONArray jsonArray1 = new JSONArray(data2);

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    MyList = new ArrayList<>();
                                    for (int j = 0 ; j<jsonArray1.length();j++)
                                    {
                                        JSONObject jsonObject = jsonArray1.getJSONObject(j);
                                        String id = jsonObject.getString("project_group");
                                        String projectid =projectName.get(i) ;
                                        if(projectid.equals(id))
                                        {
                                            MyList.add( jsonObject.getString("project_name")+","+jsonObject.getString("project_status")+","+jsonObject.getString("project_id"));
                                            projectStatus.add(jsonObject.getString("project_status"));
                                            projectids.add(jsonObject.getString("project_id"));

                                        }


                                    }

                                    listDataChild.put(projectName.get(i)+"", MyList);
                                    listDataStatus.put(projectName.get(i)+"", projectStatus);
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            customAdapter = new CustomExpandableListAdapter(getActivity(), projectId, listDataChild , projectName,projectStatus,projectids,listDataStatus);
                            expandableLv.setAdapter(customAdapter);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ringProgressDialog.dismiss();
//                ringProgressDialog.dismiss();
                if (error instanceof NoConnectionError) {


                    Toast.makeText(getActivity().getApplicationContext(), "check your internet connection", Toast.LENGTH_SHORT).show();
//                    new SweetAlertDialog(getActivity().getC, SweetAlertDialog.ERROR_TYPE)
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


                    Toast.makeText(getActivity().getApplicationContext(),"TimeOut eRROR",Toast.LENGTH_SHORT).show();
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
                params.put("status", ProjectsActivity.status);
                // params.put("password",strPassword );
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(request);


    }

    public ArrayList<String> gth() {
        ArrayList<String>   projectName = new ArrayList<>();


        for (int p = 0; p < whatever.size(); p++)

        {

//            for (int i = 0; i < projectsArray.size(); i++) {

            for (int i = 0; i < projectName.size(); i++) {

                if (projectId.get(i).equals(whateverId.get(p))) {

                  //  dummy.add(projectName.get(i));
                    //dummy.add(whatever.get(p));
                    projectName.add(whatever.get(p));
                }
//
//                listDataChild.put(whatever.get(p), dummy);
//                customAdapter = new CustomExpandableListAdapter(getActivity().getApplicationContext(), whatever, listDataChild);
////

            //    expandableLv.setAdapter(customAdapter);
                // dummy.clear();
            }

        }
            return projectName;
        }

}





