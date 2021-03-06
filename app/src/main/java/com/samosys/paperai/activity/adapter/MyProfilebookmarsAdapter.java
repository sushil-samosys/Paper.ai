package com.samosys.paperai.activity.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.samosys.paperai.activity.activity.OtherUserProfileActivity;
import com.samosys.paperai.R;
import com.samosys.paperai.activity.Bean.ShowPostBean;
import com.samosys.paperai.activity.utils.CustomFonts;
import com.samosys.paperai.activity.utils.First_Char_Capital;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by samosys on 3/4/18.
 */

public class MyProfilebookmarsAdapter  extends RecyclerView.Adapter<MyProfilebookmarsAdapter.Holder> {
    Context context;
    CustomFonts customFonts;

    ArrayList<ShowPostBean> listitem;


    public MyProfilebookmarsAdapter(FragmentActivity activity, ArrayList<ShowPostBean> listitem) {
        this.context = activity;
        this.listitem = listitem;
        customFonts = new CustomFonts(context);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_fed_item, parent, false);
        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("post_like");
        query.whereEqualTo("post_id", ParseObject.createWithoutData("Post", listitem.get(position).getObjectId()));
        query.whereEqualTo("user_id", ParseObject.createWithoutData("_User", ParseUser.getCurrentUser().getObjectId()));

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {


                    for (int i = 0; i < objects.size(); i++) {
                        ParseObject parseObject = objects.get(i);
                        holder.img_like.setImageDrawable(context.getResources().getDrawable(R.mipmap.home_like_active));

                    }


                } else {


                    Log.e("else", e.getMessage());
                    // error
                }
            }
        });

        selectBookmark(position,holder);


        holder.num_likes.setText(listitem.get(position).getLikesCount() + " Likes");
        holder.num_comment.setText(listitem.get(position).getCommentCount() + " Comments");
        holder.post_text.setText(listitem.get(position).getText());
        holder.poster_name.setText(listitem.get(position).getUsername());
        holder.post_time.setText(listitem.get(position).getUpdatedAt());


        if (listitem.get(position).getPost_image() != null) {
            Picasso.with(context)
                    .load(listitem.get(position).getPost_image())
                    .resize(600, 250)
                    .onlyScaleDown()
                    .centerCrop()

                    .into(holder.feed_image);
        }


        if (listitem.get(position).getUser_imge() != null) {
            listitem.get(position).getUser_imge().getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    Bitmap bmp = BitmapFactory
                            .decodeByteArray(
                                    data, 0,
                                    data.length);

                    // initialize
                    holder.poster_image.setImageBitmap(bmp);
                }
            });
        }

        holder.img_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeUnlike(position, holder);
            }
        });
        holder.poster_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = listitem.get(position).getPost_userID();

                if (id.equals(ParseUser.getCurrentUser().getObjectId())) {
//                    Intent i = new Intent(context, MyProfileActivity.class);
////                i.putExtra("id", listitem.get(position).getPost_userID());
//                    context.startActivity(i);
                }else {
                    Intent i = new Intent(context, OtherUserProfileActivity.class);
                i.putExtra("id", listitem.get(position).getPost_userID());
                    context.startActivity(i);
                }

            }
        });

        holder.img_bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.img_bookmark.getDrawable().getConstantState() != context.getResources().getDrawable(R.mipmap.home_bookmark_active).getConstantState()) {
                    bookmardapi(position, holder);
                    holder.img_bookmark.setImageDrawable(context.getResources().getDrawable(R.mipmap.home_bookmark_active));

                } else {


                    deletemark(position, holder);
                    holder.img_bookmark.setImageDrawable(context.getResources().getDrawable(R.mipmap.home_bookmark));


                }


            }
        });

        holder.img_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                PopupMenu popup = new PopupMenu(holder.img_more.getContext(), v);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.nav_share:
                                Toast.makeText(v.getContext(), "FriendRequest", Toast.LENGTH_LONG).show();
                                return true;
                            case R.id.nav_copy:
                                Toast.makeText(v.getContext(), "Block | Hide ", Toast.LENGTH_LONG).show();
                                return true;

                            default:
                                return false;
                        }
                        //return false;
                    }
                });
                popup.inflate(R.menu.menu_feed_share);
                popup.show();
            }
        });
    }

    private void selectBookmark(int position, final Holder holder) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("post_bookmarks");
        query.whereEqualTo("post_id", ParseObject.createWithoutData("Post", listitem.get(position).getObjectId()));
        query.whereEqualTo("user_id", ParseObject.createWithoutData("_User", ParseUser.getCurrentUser().getObjectId()));

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                Log.e("MYBOOK", objects.size()+"");
                if (e == null) {


                    for (int i = 0; i < objects.size(); i++) {
                        ParseObject parseObject = objects.get(i);
                        holder.img_bookmark.setImageDrawable(context.getResources().getDrawable(R.mipmap.home_bookmark_active));

                    }


                } else {


                    Log.e("MYBOOK_else", e.getMessage());
                    // error
                }
            }
        });
    }

    private void deletemark(int position, Holder holder) {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("post_bookmarks");
        query.whereEqualTo("post_id", ParseObject.createWithoutData("Post", listitem.get(position).getObjectId()));
        query.whereEqualTo("user_id", ParseObject.createWithoutData("_User", ParseUser.getCurrentUser().getObjectId()));

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {


                    for (int i = 0; i < objects.size(); i++) {
                        ParseObject parseObject = objects.get(i);
                        parseObject.deleteEventually();

                    }


                } else {


                    Log.e("else", e.getMessage());
                    // error
                }
            }
        });
    }

    private void bookmardapi(final int position, final Holder holder) {

        ParseObject gameScore = new ParseObject("post_bookmarks");

        gameScore.put("user_id", ParseObject.createWithoutData("_User", ParseUser.getCurrentUser().getObjectId()));
        gameScore.put("post_id", ParseObject.createWithoutData("Post", listitem.get(position).getObjectId()));

        gameScore.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if (e == null) {


                } else {
                    Log.e("ERRROr", e.getMessage());

                }
            }
        });
    }

    private void likeUnlike(final int position, final Holder holder) {

        ParseObject gameScore = new ParseObject("post_like");
        //gameScore.put("user", ParseUser.getCurrentUser().getObjectId());
        gameScore.put("user_id", ParseObject.createWithoutData("_User", ParseUser.getCurrentUser().getObjectId()));

        gameScore.put("post_id", ParseObject.createWithoutData("Post", listitem.get(position).getObjectId()));
        gameScore.put("likesCount", 1);
        Log.e("post_id", listitem.get(position).getObjectId());
        gameScore.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if (e == null) {
                    if (holder.img_like.getDrawable().getConstantState() != context.getResources().getDrawable(R.mipmap.home_like_active).getConstantState()) {

                        holder.img_like.setImageDrawable(context.getResources().getDrawable(R.mipmap.home_like_active));
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");

// Retrieve the object by id
                        query.getInBackground(listitem.get(position).getObjectId(), new GetCallback<ParseObject>() {
                            public void done(ParseObject myobject, ParseException e) {
                                if (e == null) {


                                    myobject.increment("likesCount", 1);
                                    myobject.saveInBackground();
                                    int count = myobject.getInt("likesCount");
                                    holder.num_likes.setText(count + " Likes");
//                                Toast.makeText(context, ""+count, Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.e("Count_reroor", e.getMessage());

                                }
                            }
                        });
                    } else {


                        holder.img_like.setImageDrawable(context.getResources().getDrawable(R.mipmap.home_like));
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");

// Retrieve the object by id
                        query.getInBackground(listitem.get(position).getObjectId(), new GetCallback<ParseObject>() {
                            public void done(ParseObject myobject, ParseException e) {
                                if (e == null) {


                                    myobject.increment("likesCount", -1);
                                    myobject.saveInBackground();
                                    int count = myobject.getInt("likesCount");
                                    holder.num_likes.setText(count + " Likes");
//                                Toast.makeText(context, ""+count, Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.e("Count_reroor", e.getMessage());

                                }
                            }
                        });


                    }


                } else {
                    Log.e("ERRROr", e.getMessage());

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listitem.size();
    }


    public class Holder extends RecyclerView.ViewHolder {
        TextView post_time, post_text, num_likes, num_comment, poster_name;


        ImageView img_bookmark,poster_image, feed_image, img_like,img_more;

        public Holder(View itemView) {
            super(itemView);
            // customFonts = new CustomFonts(context);
            img_bookmark = (ImageView) itemView.findViewById(R.id.img_bookmark);
            feed_image = (ImageView) itemView.findViewById(R.id.feed_image);
            poster_image = (ImageView) itemView.findViewById(R.id.poster_image);
            img_more = (ImageView) itemView.findViewById(R.id.img_more);
            img_like = (ImageView) itemView.findViewById(R.id.img_like);
            post_time = (TextView) itemView.findViewById(R.id.post_time);
            post_text = (TextView) itemView.findViewById(R.id.post_text);
            num_likes = (TextView) itemView.findViewById(R.id.num_likes);
            poster_name = (TextView) itemView.findViewById(R.id.poster_name);

            num_comment = (TextView) itemView.findViewById(R.id.num_comment);


            //  txtworkurl.setTextColor(context.getResources().getColor(R.color.txt_light));
            num_likes.setTypeface(customFonts.HelveticaNeueBold);
            poster_name.setTypeface(customFonts.HelveticaNeueBold);
            post_text.setTypeface(customFonts.HelveticaNeueMedium);
            post_time.setTypeface(customFonts.HelveticaNeueMedium);
            num_comment.setTypeface(customFonts.HelveticaNeueBold);

        }
    }


}