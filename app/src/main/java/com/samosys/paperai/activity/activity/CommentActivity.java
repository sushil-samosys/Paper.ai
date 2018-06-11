package com.samosys.paperai.activity.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.samosys.paperai.R;
import com.samosys.paperai.activity.Bean.CommentBean;
import com.samosys.paperai.activity.adapter.CommentAdapter;
import com.samosys.paperai.activity.utils.AppConstants;

import java.util.ArrayList;
import java.util.List;

public class CommentActivity extends AppCompatActivity {
    String post_id = "";
    RecyclerView rv_comment;
    ArrayList<CommentBean> Mylist;
    EditText edt_comment;
    ImageView comment_send;
    CommentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        AppConstants.getstatusbar(CommentActivity.this);
        rv_comment = (RecyclerView) findViewById(R.id.rv_comment);
        edt_comment = (EditText) findViewById(R.id.edt_comment);
        comment_send = (ImageView) findViewById(R.id.comment_send);
        post_id = getIntent().getStringExtra("id");
        Log.e("post_id", post_id);
        Mylist = new ArrayList<>();

        getCommenetList();
        comment_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = edt_comment.getText().toString();
                if (comment.equals("") || comment == null) {
                    Toast.makeText(CommentActivity.this, "Pleasse enter comment ", Toast.LENGTH_SHORT).show();
                } else {
                    updatecomment(comment);
                }
            }
        });
    }

    private void updatecomment(String comment) {

        ParseObject gameScore = new ParseObject("post_comment");
        //gameScore.put("user", ParseUser.getCurrentUser().getObjectId());
        gameScore.put("user_id", ParseObject.createWithoutData("_User", ParseUser.getCurrentUser().getObjectId()));

        gameScore.put("post_id", ParseObject.createWithoutData("Post", post_id));
        gameScore.put("comment", comment);

        gameScore.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if (e == null) {
                    edt_comment.setText("");
                    getCommenetList();
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");

// Retrieve the object by id
                    query.getInBackground(post_id, new GetCallback<ParseObject>() {
                        public void done(ParseObject myobject, ParseException e) {
                            if (e == null) {


                                myobject.increment("CommentCount", 1);
                                myobject.saveInBackground();

//                                Toast.makeText(context, ""+count, Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e("Count_reroor", e.getMessage());

                            }
                        }
                    });


                } else {
                    Log.e("commentans", e.getMessage());

                }
            }
        });
    }

    private void getCommenetList() {

        Mylist.clear();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("post_comment");
        query.whereEqualTo("post_id", ParseObject.createWithoutData("Post", post_id));
        query.include("user_id");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {


                    for (int i = 0; i < objects.size(); i++) {
                        ParseObject parseObject = objects.get(i).getParseObject("user_id");
                        String post_user_id = parseObject.getObjectId();
                        String post_user_name = parseObject.getString("fullname");
                        ParseFile user_imge = (ParseFile) parseObject.get("profileimage");
                        String txt = objects.get(i).getString("comment");
                        String updatedAt = objects.get(i).getUpdatedAt().toString();
                        String createdAt = objects.get(i).getCreatedAt().toString();
                        Mylist.add(new CommentBean(post_user_id, post_user_name, user_imge, txt, updatedAt, createdAt));


                    }
                    adapter = new CommentAdapter(CommentActivity.this, Mylist);
                   // RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(CommentActivity.this);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(CommentActivity.this);
                    rv_comment.setHasFixedSize(true);
                    layoutManager.setReverseLayout(true);
                    layoutManager.setStackFromEnd(true);
                    rv_comment.setLayoutManager(layoutManager);

                    //  rv_comment.addItemDecoration(new SimpleDividerItemDecoration(CommentActivity.this));
                    rv_comment.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                } else {


                    Log.e("else", e.getMessage());
                    // error
                }
            }
        });
    }

}
