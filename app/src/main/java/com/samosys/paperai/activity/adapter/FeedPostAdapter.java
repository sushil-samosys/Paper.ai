package com.samosys.paperai.activity.adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.samosys.paperai.R;
import com.samosys.paperai.activity.AudioVisualizer.LineBarVisualizer;
import com.samosys.paperai.activity.Bean.ShowPostBean;
import com.samosys.paperai.activity.activity.CommentActivity;
import com.samosys.paperai.activity.activity.MyProfileActivity;
import com.samosys.paperai.activity.activity.OtherUserProfileActivity;
import com.samosys.paperai.activity.utils.CustomFonts;
import com.samosys.paperai.activity.utils.First_Char_Capital;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by samosys on 31/3/18.
 */

public class FeedPostAdapter extends RecyclerView.Adapter<FeedPostAdapter.Holder> {
    public static final int VIEW_TYPE_DEFAULT = 1;
    Context context;
    CustomFonts customFonts;
    boolean like = false;
    ArrayList<ShowPostBean> listitem;
    AQuery aq;
    String objectid = "";
    private DisplayImageOptions options;
    private ImageLoader imageLoader;
    private int img_height, img_height_pro, img_width, img_width_pro;

    public FeedPostAdapter(FragmentActivity activity, ArrayList<ShowPostBean> listitem) {
        this.context = activity;
        this.listitem = listitem;
        customFonts = new CustomFonts(context);
        aq = new AQuery(activity);
        BitmapDrawable bd = (BitmapDrawable) context.getResources().getDrawable(R.mipmap.img_feed_center_1);
        img_height = bd.getBitmap().getHeight();
        img_width = bd.getBitmap().getWidth() + (img_width);

        BitmapDrawable bd1 = (BitmapDrawable) context.getResources().getDrawable(R.mipmap.home_active);
        img_height_pro = bd1.getBitmap().getHeight() + (img_height_pro);
        img_width_pro = bd1.getBitmap().getWidth() + (img_width_pro);


        options = new DisplayImageOptions.Builder()

                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        // ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context));
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.new_feed_item, parent, false);
        return new Holder(itemView);
    }


    @Override
    public void onBindViewHolder(final Holder holder, final int position) {

//        ParseQuery<ParseObject> query = ParseQuery.getQuery("post_like");
//        query.whereEqualTo("post_id", ParseObject.createWithoutData("Post", listitem.get(position).getObjectId()));
//        query.whereEqualTo("user_id", ParseObject.createWithoutData("_User", ParseUser.getCurrentUser().getObjectId()));
//
//        query.findInBackground(new FindCallback<ParseObject>() {
//            public void done(List<ParseObject> objects, ParseException e) {
//
//                if (e == null) {
//
//
//                    for (int i = 0; i < objects.size(); i++) {
//                        ParseObject parseObject = objects.get(i);
//                        holder.img_like.setImageDrawable(context.getResources().getDrawable(R.mipmap.home_like_active));
//
//                    }
//
//
//                } else {
//
//
//                    Log.e("else", e.getMessage());
//                    // error
//                }
//            }
//        });

     //   selectBookmark(position, holder);


        holder.num_likes.setText(listitem.get(position).getLikesCount() + " Likes");
        holder.num_comment.setText(listitem.get(position).getCommentCount() + " Comments");
        holder.post_text.setText(First_Char_Capital.capitalizeString(listitem.get(position).getText()));
        holder.poster_name.setText(First_Char_Capital.capitalizeString(listitem.get(position).getUsername()));
        holder.post_time.setText(listitem.get(position).getUpdatedAt());

        if (listitem.get(position).getPost_type().equals("0")) {
            holder.feed_image.setVisibility(View.GONE);
            holder.ll_visual.setVisibility(View.GONE);
            holder.RL_imagefeed.setVisibility(View.GONE);
            holder.img_progrss.setVisibility(View.GONE);
            //holder.post_text.setText(First_Char_Capital.capitalizeString(listitem.get(position).getText()));
            holder.post_text.setTextSize(17);
            holder.post_text.setMaxLines(20);
            holder.post_text.setPadding(10, 0, 0, 0);
            holder.post_text.setTypeface(customFonts.calibri);
        } else if (listitem.get(position).getPost_type().equals("1")) {
            holder.feed_image.setVisibility(View.VISIBLE);
            holder.ll_visual.setVisibility(View.GONE);
            if (!listitem.get(position).getPost_image().equals("no_image")) {
//            holder.RL_imagefeed.setVisibility(View.VISIBLE);
//            holder.post_text.setText(listitem.get(position).getText());
//            Picasso.with(context).load(listitem.get(position).getPost_image()).error(R.mipmap.img_feed_center_1)
//                    .fit().into(holder.feed_image, new Callback() {
//                @Override
//                public void onSuccess() {
//                   holder.img_progrss.setVisibility(View.GONE);
//
//                }
//
//                @Override
//                public void onError() {
//                    holder.img_progrss.setVisibility(View.GONE);
//                }
//            });
                File file = new File(listitem.get(position).getPost_image());
                if (file.exists()) {
                    BitmapAjaxCallback cb = new BitmapAjaxCallback();
                    cb.targetWidth(500).rotate(false);
                    aq.id(holder.feed_image).image(new File(listitem.get(position).getPost_image()), true, 500, cb);
                } else {

                    if (!listitem.get(position).getPost_image().equalsIgnoreCase("")) {
                        aq.ajax(listitem.get(position).getPost_image(), File.class, new AjaxCallback<File>() {
                            @Override
                            public void callback(String url, File bm, AjaxStatus status) {


                                if (bm != null) {
                                    holder.img_progrss.setVisibility(View.GONE);
                                    BitmapAjaxCallback cb = new BitmapAjaxCallback();
                                    cb.targetWidth(1000).rotate(true);
                                    aq.id(holder.feed_image).image(bm, true, 500, cb);
                                } else {
                                    holder.img_progrss.setVisibility(View.GONE);
                                    holder.feed_image.setImageResource(R.mipmap.sign_up_project);
                                }
                            }
                        });
                    } else {
                        holder.img_progrss.setVisibility(View.GONE);
                        holder.feed_image.setImageResource(R.mipmap.sign_up_project);
                    }
                }


                holder.feed_image.getLayoutParams().height = (img_height * 3) / 2;
            } else {
//            holder.RL_imagefeed.setVisibility(View.GONE);

            }
        } else if (listitem.get(position).getPost_type().equals("2")) {


            holder.visualizer_feed.setDensity(50);
            holder.mediaPlayer = new MediaPlayer();

            // Set your media player to the visualizer.
            holder.visualizer_feed.setPlayer(holder.mediaPlayer.getAudioSessionId());
            holder.visualizer_feed.setColor(ContextCompat.getColor(context, R.color.colorPrimary));
            holder.feed_image.setVisibility(View.GONE);
            holder.ll_visual.setVisibility(View.VISIBLE);
            holder.img_progrss.setVisibility(View.GONE);
            holder.RL_imagefeed.setBackgroundColor(context.getResources().getColor(R.color.white));

            try {

                holder.mediaPlayer.setDataSource(listitem.get(position).getPost_file());
                holder.mediaPlayer.prepare();
                // holder.   mediaPlayer.start();
                holder.mediaPlayer.setLooping(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


//        holder.feed_image.getLayoutParams().width = img_width;

        if (listitem.get(position).getUser_imge() != null) {
            ParseFile parseFile = listitem.get(position).getUser_imge();
            String a = parseFile.getUrl();

            Picasso.with(context).load(a).error(R.mipmap.home_user)
                    .placeholder(R.mipmap.home_user)
                    .fit().into(holder.poster_image, new Callback() {
                @Override
                public void onSuccess() {
                    // holder.img_progrss.setVisibility(View.GONE);

                }

                @Override
                public void onError() {
                    //holder.img_progrss.setVisibility(View.GONE);
                }
            });
            holder.ib_play_pause_feed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.mediaPlayer != null) {
                        if (holder.mediaPlayer.isPlaying()) {
                            holder.mediaPlayer.pause();
                            holder.ib_play_pause_feed.setImageDrawable(ContextCompat.getDrawable(
                                    context,
                                    R.drawable.ic_play_red_48dp));
                        } else {
                            holder.mediaPlayer.start();
                            holder.ib_play_pause_feed.setImageDrawable(ContextCompat.getDrawable(
                                    context,
                                    R.drawable.ic_pause_red_48dp));
                        }
                    }
                }
            });


//            listitem.get(position).getUser_imge().getDataInBackground(new GetDataCallback() {
//                @Override
//                public void done(byte[] data, ParseException e) {
//                    Bitmap bmp = BitmapFactory
//                            .decodeByteArray(
//                                    data, 0,
//                                    data.length);
//
//                    // initialize
//                    holder.poster_image.setImageBitmap(bmp);
//                    holder.poster_image_comment.setImageBitmap(bmp);
////                    holder.poster_image.getLayoutParams().height = img_height_pro;
////                    holder.poster_image.getLayoutParams().width = img_width_pro;
//                }
//            });
        }
        holder.img_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.img_like.getDrawable().getConstantState() != context.getResources().getDrawable(R.mipmap.home_like_active).getConstantState()) {
                    holder.img_like.setImageDrawable(context.getResources().getDrawable(R.mipmap.home_like_active));
                    like = true;
                } else {
                    like = false;
                    holder.img_like.setImageDrawable(context.getResources().getDrawable(R.mipmap.home_like));
                }
                likeUnlike(position, holder, like);
            }
        });
        holder.poster_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = listitem.get(position).getPost_userID();
                if (id.equals(ParseUser.getCurrentUser().getObjectId())) {
                    Intent i = new Intent(context, MyProfileActivity.class);
//                i.putExtra("id", listitem.get(position).getPost_userID());
                    context.startActivity(i);
                } else {
                    Intent i = new Intent(context, OtherUserProfileActivity.class);
//                i.putExtra("id", listitem.get(position).getPost_userID());
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
        holder.rl_commnet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra("id", listitem.get(position).getObjectId());
                context.startActivity(intent);
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

                if (e == null) {


                    for (int i = 0; i < objects.size(); i++) {
                        ParseObject parseObject = objects.get(i);
                        holder.img_bookmark.setImageDrawable(context.getResources().getDrawable(R.mipmap.home_bookmark_active));

                    }


                } else {


                    Log.e("else", e.getMessage());
                    // error
                }
            }
        });
    }

    private void deletemark(int position, Holder holder) {

        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("PostSocial");

        query1.whereEqualTo("user", ParseObject.createWithoutData("_User", ParseUser.getCurrentUser().getObjectId()));

        query1.whereEqualTo("post", ParseObject.createWithoutData("Post", listitem.get(position).getObjectId()));
        query1.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {

                    for (ParseObject delete : objects) {
                        delete.deleteInBackground();

                    }
//
                }
            }
        });
    }

    private void bookmardapi(final int position, final Holder holder) {

        ParseObject gameScore = new ParseObject("PostSocial");

        gameScore.put("user", ParseObject.createWithoutData("_User", ParseUser.getCurrentUser().getObjectId()));
        gameScore.put("post", ParseObject.createWithoutData("Post", listitem.get(position).getObjectId()));
        gameScore.put("type", "2");

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

    private void likeUnlike(final int position, final Holder holder, final boolean like) {

        ParseObject gameScore = new ParseObject("PostSocial");
        //gameScore.put("user", ParseUser.getCurrentUser().getObjectId());
        gameScore.put("user", ParseObject.createWithoutData("_User", ParseUser.getCurrentUser().getObjectId()));

        gameScore.put("post", ParseObject.createWithoutData("Post", listitem.get(position).getObjectId()));
//        gameScore.put("likesCount", 1);
        gameScore.put("type", "1");
        Log.e("post", listitem.get(position).getObjectId());
        gameScore.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if (e == null) {
                    if (like) {

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


                        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("PostSocial");

                        query1.whereEqualTo("user", ParseObject.createWithoutData("_User", ParseUser.getCurrentUser().getObjectId()));

                        query1.whereEqualTo("post", ParseObject.createWithoutData("Post", listitem.get(position).getObjectId()));
                        query1.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> objects, ParseException e) {

                                if (e == null) {

                                    for (ParseObject delete : objects) {
                                        delete.deleteInBackground();

                                    }
//
                                }
                            }
                        });

//                        query1.getInBackground(objectid, new GetCallback<ParseObject>() {
//                            public void done(ParseObject object, ParseException e) {
//                                if (e == null) {
//                                    object.deleteInBackground();
//                                    Toast.makeText(context, "Delete Projct Successfully", Toast.LENGTH_SHORT).show();
////                                    ((ProjectSettingActivity)context).getprojectlist();
////                                        ((HomeFeedActivity)context).getprojectlist();
//                                } else {
//                                    Log.e("unloike_error", e.getMessage());
//                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });

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
        protected MediaPlayer mediaPlayer;
        ProgressBar img_progrss;
        LineBarVisualizer visualizer_feed;
        LinearLayout ll_visual;
        ImageButton ib_play_pause_feed;
        private TextView post_time, post_text, num_likes, num_comment, poster_name;
        private ImageView poster_image_comment, img_bookmark, poster_image, feed_image, img_like, img_more;
        private RelativeLayout rl_commnet, RL_imagefeed;

        public Holder(View itemView) {
            super(itemView);
            // customFonts = new CustomFonts(context);
            rl_commnet = (RelativeLayout) itemView.findViewById(R.id.rl_commnet);
            visualizer_feed = (LineBarVisualizer) itemView.findViewById(R.id.visualizer_feed);
            RL_imagefeed = (RelativeLayout) itemView.findViewById(R.id.RL_imagefeed);
            img_bookmark = (ImageView) itemView.findViewById(R.id.img_bookmark);
            poster_image_comment = (ImageView) itemView.findViewById(R.id.poster_image_comment);
            feed_image = (ImageView) itemView.findViewById(R.id.feed_image);
            poster_image = (ImageView) itemView.findViewById(R.id.poster_image);
            img_more = (ImageView) itemView.findViewById(R.id.img_more);
            ib_play_pause_feed = (ImageButton) itemView.findViewById(R.id.ib_play_pause_feed);
            img_like = (ImageView) itemView.findViewById(R.id.img_like);
            post_time = (TextView) itemView.findViewById(R.id.post_time);
            ll_visual = (LinearLayout) itemView.findViewById(R.id.ll_visual);
            post_text = (TextView) itemView.findViewById(R.id.post_text);
            num_likes = (TextView) itemView.findViewById(R.id.num_likes);
            poster_name = (TextView) itemView.findViewById(R.id.poster_name);
            img_progrss = (ProgressBar) itemView.findViewById(R.id.img_progrss);
            num_comment = (TextView) itemView.findViewById(R.id.num_comment);


            //  txtworkurl.setTextColor(context.getResources().getColor(R.color.txt_light));
            num_likes.setTypeface(customFonts.calibri);
            poster_name.setTypeface(customFonts.CabinBold);
            post_text.setTypeface(customFonts.CabinBold);
            post_time.setTypeface(customFonts.calibri);
            num_comment.setTypeface(customFonts.calibri);

        }
    }


}

