package com.app.devrah.Views.Messages;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.app.devrah.R;

import java.util.HashMap;
import java.util.Map;

public class viewMessage extends AppCompatActivity {

    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    TextView tvTo;
    EditText  from ,message ,board,card,project,subject;
    String  strfrom ,strmessage ,strboard,strcard,strproject,strsubject;
    LinearLayout cancel_button,reply_btn;
    Toolbar toolbar;
    String messageType,message_id,isRead,projectId,boardId,cardId,msgDate,email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_message);
        toolbar = (Toolbar) findViewById(R.id.header);
        toolbar.setTitle("Read Message");
        from = (EditText) findViewById(R.id.etEmails);
        tvTo = (TextView) findViewById(R.id.to);
        board = (EditText) findViewById(R.id.boardSpinner);
        card = (EditText) findViewById(R.id.cardSpinner);
        project = (EditText) findViewById(R.id.projectSpinner);
        subject = (EditText) findViewById(R.id.etSubject);
        message = (EditText) findViewById(R.id.editor);
        cancel_button = (LinearLayout) findViewById(R.id.cancel_button);
        reply_btn = (LinearLayout) findViewById(R.id.reply_button);

        Intent intent = getIntent();

        strfrom = intent.getStringExtra("to");
        strmessage = intent.getStringExtra("message");
        strboard = intent.getStringExtra("board");
        strcard = intent.getStringExtra("card");
        strproject = intent.getStringExtra("project");
        strsubject = intent.getStringExtra("subject");
        messageType = intent.getStringExtra("messageType");
        message_id = intent.getStringExtra("message_id");
        isRead = intent.getStringExtra("isRead");
        projectId = intent.getStringExtra("p_id");
        boardId = intent.getStringExtra("b_id");
        cardId = intent.getStringExtra("c_id");
        msgDate = intent.getStringExtra("date");
        email = intent.getStringExtra("email");
        if(isRead.equals("0")){
            markAsRead();
        }
        if(messageType.equals("sent")){
            reply_btn.setVisibility(View.INVISIBLE);
            tvTo.setText("To :");
        }else {
            tvTo.setText("From :");
        }
        reply_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(viewMessage.this,Reply.class);
                intent.putExtra("to",strfrom.substring(1));
                intent.putExtra("project",strproject);
                intent.putExtra("card",strcard);
                intent.putExtra("board",strboard);
                intent.putExtra("subject",strsubject);
                intent.putExtra("message",strmessage);
                intent.putExtra("p_id",projectId);
                intent.putExtra("b_id",boardId);
                intent.putExtra("c_id",cardId);
                intent.putExtra("date",msgDate);
                intent.putExtra("email",email);
                finish();
                startActivity(intent);
            }
        });

        String finalString = strfrom.substring(1);
        from.setText(finalString);
        if(strboard.equals("null")){
            board.setText("NA");
        }else {
            board.setText(strboard);
        }
        if(strcard.equals("null"))
            card.setText("NA");
        else
        card.setText(strcard);
        if(strproject.equals("null"))
            project.setText("NA");
        else
        project.setText(strproject);
        subject.setText(strsubject);
        message.setText(Html.fromHtml(Html.fromHtml(strmessage).toString()));


        from.setClickable(false);
        board.setClickable(false);
        card.setClickable(false);
        project.setClickable(false);
        subject.setClickable(false);
        message.setClickable(false);

        from.setEnabled(false);
        board.setEnabled(false);
        card.setEnabled(false);
        project.setEnabled(false);
        subject.setEnabled(false);
        message.setEnabled(false);


        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    public void markAsRead() {
        SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);


        StringRequest request = new StringRequest(Request.Method.POST, End_Points.MARK_READ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof NoConnectionError) {

                } else if (error instanceof TimeoutError) {

                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                SharedPreferences pref = getApplicationContext().getSharedPreferences("UserPrefs", MODE_PRIVATE);
                String userId = pref.getString("user_id", "");


                params.put("userId", userId);
                params.put("id", message_id);
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(viewMessage.this);
        requestQueue.add(request);

    }
}
