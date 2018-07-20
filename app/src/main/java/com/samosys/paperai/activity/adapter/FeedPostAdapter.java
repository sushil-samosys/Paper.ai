package com.samosys.paperai.activity.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
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
import com.samosys.paperai.activity.activity.FullImageActivity;
import com.samosys.paperai.activity.activity.MyProfileActivity;
import com.samosys.paperai.activity.activity.OtherUserProfileActivity;
import com.samosys.paperai.activity.activity.VideoFullscreenActivity;
import com.samosys.paperai.activity.utils.CustomFonts;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * Created by samosys on 31/3/18.
 */

public class FeedPostAdapter extends RecyclerView.Adapter<FeedPostAdapter.Holder> {
    // 0- text  1-image  2 - audio  3 - video 4 = image/audio

    Context context;
    CustomFonts customFonts;
    AQuery aq;
    private boolean like = false;
    private ArrayList<ShowPostBean> listitem;
    private DisplayImageOptions displayImageOptions;
    private int img_height, img_height_pro, img_width, img_width_pro;
    private MediaController mediaControls;

    public FeedPostAdapter(Activity activity, ArrayList<ShowPostBean> listitem) {
        this.context = activity;
        this.listitem = listitem;
        customFonts = new CustomFonts(context);

        BitmapDrawable bd = (BitmapDrawable) context.getResources().getDrawable(R.mipmap.img_feed_center_1);
        img_height = bd.getBitmap().getHeight() + (img_height);
        img_width = bd.getBitmap().getWidth() + (img_width);

        BitmapDrawable bd1 = (BitmapDrawable) context.getResources().getDrawable(R.mipmap.home_active);
        img_height_pro = bd1.getBitmap().getHeight() + (img_height_pro);
        img_width_pro = bd1.getBitmap().getWidth() + (img_width_pro);

        aq=new AQuery(context);
        displayImageOptions = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.mipmap.sign_up_workspace)
                .showImageOnFail(R.mipmap.sign_up_workspace)
                .cacheInMemory(true)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context));

    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.new_feed_item, parent, false);
        return new Holder(itemView);
    }


    @Override
    public void onBindViewHolder(final Holder holder, final int position) {


        if (mediaControls == null) {

            mediaControls = new MediaController(context);


        }
        if (holder.mediaPlayer == null) {
            holder.mediaPlayer = new MediaPlayer();
        }

        holder.num_likes.setText(listitem.get(position).getLikesCount() + " Likes");
        holder.num_comment.setText(listitem.get(position).getCommentCount() + " Comments");

        holder.poster_name.setText(listitem.get(position).getUsername());

        holder.post_time.setText(listitem.get(position).getUpdatedAt());

        holder.post_text.setText(listitem.get(position).getText());


        if (listitem.get(position).getPost_type().equals("0")) {
            holder.feed_image.setVisibility(View.GONE);
            holder.ll_visual.setVisibility(View.GONE);
            holder.RL_imagefeed.setVisibility(View.GONE);
            holder.img_progrss.setVisibility(View.GONE);
            holder.card_view.setVisibility(View.VISIBLE);

            holder.post_text.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    context.getResources().getDimension(R.dimen._15sdp));
            holder.post_text.setTypeface(customFonts.calibri);
        } else if (listitem.get(position).getPost_type().equals("1")) {
            Log.e("ImageURL", listitem.get(position).getPost_image());
            holder.feed_image.setVisibility(View.VISIBLE);
            holder.ll_visual.setVisibility(View.GONE);
            holder.rl_vidio.setVisibility(View.GONE);
            holder.RL_imagefeed.setVisibility(View.VISIBLE);
            holder.card_view.setVisibility(View.VISIBLE);
            holder.feed_image.getLayoutParams().height = img_height + (img_height);
            holder.feed_image.getLayoutParams().width = img_width + (img_width);//

            File newFile = new File(listitem.get(position).getPost_image());



            ImageLoader.getInstance().displayImage(listitem.get(position).getPost_image(),
                    holder.feed_image, displayImageOptions, new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            super.onLoadingComplete(imageUri, view, loadedImage);
                            holder.img_progrss.setVisibility(View.GONE);
                        }
                    });


        } else if (listitem.get(position).getPost_type().equals("2")) {
            holder.RL_imagefeed.setVisibility(View.VISIBLE);

            holder.feed_image.setVisibility(View.GONE);
            holder.ll_visual.setVisibility(View.VISIBLE);
            holder.img_progrss.setVisibility(View.GONE);
            holder.visualizer_feed.setDensity(90f);
            holder.card_view.setVisibility(View.VISIBLE);
            holder.visualizer_feed.setPlayer(holder.mediaPlayer.getAudioSessionId());
            holder.visualizer_feed.setColor(ContextCompat.getColor(context, R.color.colorAccent));
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
                            try {
                                holder.mediaPlayer.start();
                                holder.mediaPlayer.setDataSource(listitem.get(position).getPost_file());

                                holder.mediaPlayer.setLooping(false);
                                holder.ib_play_pause_feed.setImageDrawable(ContextCompat.getDrawable(
                                        context,
                                        R.drawable.ic_pause_red_48dp));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }
            });
            holder.mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                public void onPrepared(MediaPlayer mp) {
//                        mp.start();
                    holder.mediaPlayer.start();
                }
            });


        } else if (listitem.get(position).getPost_type().equals("3")) {
            holder.feed_image.setVisibility(View.GONE);
            holder.ll_visual.setVisibility(View.GONE);
            holder.rl_vidio.setVisibility(View.VISIBLE);
            holder.RL_imagefeed.setVisibility(View.VISIBLE);
            holder.img_progrss.setVisibility(View.GONE);
            holder.card_view.setVisibility(View.VISIBLE);
            //

            try {


                String item = listitem.get(position).getPost_file();
                Bitmap b = ThumbnailUtils.createVideoThumbnail(item, MediaStore.Video.Thumbnails.MINI_KIND);
                Glide.with(context).load(b).into(holder.videoView.thumbImageView);

                holder.videoView.setUp(
                        item, JZVideoPlayer.SCREEN_WINDOW_LIST,
                        "");


            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (listitem.get(position).getPost_type().equals("4")) {


            holder.feed_image.setVisibility(View.VISIBLE);
            holder.ll_visual.setVisibility(View.VISIBLE);
            holder.rl_vidio.setVisibility(View.GONE);
            holder.RL_imagefeed.setVisibility(View.VISIBLE);
//            if (!listitem.get(position).getPost_image().equals("no_image")) {
            holder.card_view.setVisibility(View.VISIBLE);
            File file = new File(listitem.get(position).getPost_image());

            holder.feed_image.getLayoutParams().height = img_height + (img_height);
            holder.feed_image.getLayoutParams().width = img_width + (img_width);
//
            ImageLoader.getInstance().displayImage(listitem.get(position).getPost_image(),
                    holder.feed_image, displayImageOptions, new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            super.onLoadingComplete(imageUri, view, loadedImage);
                            holder.img_progrss.setVisibility(View.GONE);
                        }
                    });


            holder.visualizer_feed.setPlayer(holder.mediaPlayer.getAudioSessionId());
            holder.visualizer_feed.setColor(ContextCompat.getColor(context, R.color.colorAccent));
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
                            try {
                                holder.mediaPlayer.start();
                                holder.mediaPlayer.setDataSource(listitem.get(position).getPost_file());


                                holder.mediaPlayer.setLooping(false);
                                holder.ib_play_pause_feed.setImageDrawable(ContextCompat.getDrawable(
                                        context,
                                        R.drawable.ic_pause_red_48dp));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }
            });
            holder.mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                public void onPrepared(MediaPlayer mp) {
//                        mp.start();
                    holder.mediaPlayer.start();
                }
            });


        }

//
        holder.rl_vidio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VideoFullscreenActivity.class);
                intent.putExtra("url", listitem.get(position).getPost_file());
                context.startActivity(intent);
            }
        });


        holder.feed_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FullImageActivity.class);
                intent.putExtra("url", listitem.get(position).getPost_image());

                context.startActivity(intent);
            }
        });


        if (listitem.get(position).getUser_imge() != null) {
            ParseFile parseFile = listitem.get(position).getUser_imge();
            String a = parseFile.getUrl();

            Picasso.with(context).load(a).error(R.mipmap.home_user)
                    .placeholder(R.mipmap.home_user)
                    .fit().into(holder.poster_image, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                }
            });
            Picasso.with(context).load(a).error(R.mipmap.home_user)
                    .placeholder(R.mipmap.home_user)
                    .fit().into(holder.poster_image_comment, new Callback() {
                @Override
                public void onSuccess() {


                }

                @Override
                public void onError() {

                }
            });


//
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


                    }


                } else {
                    Log.e("ERRROr", e.getMessage());

                }
            }
        });
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        JZVideoPlayerStandard.releaseAllVideos();

    }

    @Override
    public void onViewDetachedFromWindow(@NonNull Holder holder) {
        super.onViewDetachedFromWindow(holder);
        JZVideoPlayerStandard.releaseAllVideos();
    }

    @Override
    public int getItemCount() {
        return listitem.size();
    }


    public class Holder extends RecyclerView.ViewHolder {

        public View v;
        protected MediaPlayer mediaPlayer;
        private ProgressBar img_progrss;
        private LineBarVisualizer visualizer_feed;
        private  LinearLayout ll_visual;
        private ImageButton ib_play_pause_feed;
        private JZVideoPlayerStandard videoView;
        private ImageView img_playback, img_vol;
        private   TextView post_text,txt_commet;
        private   CardView card_view;

        private TextView post_time, num_likes, num_comment, poster_name;
        private ImageView poster_image_comment, img_bookmark, poster_image, feed_image, img_like, img_more;
        private RelativeLayout rl_vidio, rl_commnet, RL_imagefeed;

        public Holder(View itemView) {
            super(itemView);
            // customFonts = new CustomFonts(context);
            v = itemView;
            rl_commnet = (RelativeLayout) itemView.findViewById(R.id.rl_commnet);
            rl_vidio = (RelativeLayout) itemView.findViewById(R.id.rl_vidio);
            videoView = (JZVideoPlayerStandard) itemView.findViewById(R.id.videoplayer);
            visualizer_feed = (LineBarVisualizer) itemView.findViewById(R.id.visualizer_feed);
            RL_imagefeed = (RelativeLayout) itemView.findViewById(R.id.RL_imagefeed);
            img_bookmark = (ImageView) itemView.findViewById(R.id.img_bookmark);
            img_playback = (ImageView) itemView.findViewById(R.id.img_playback);
            card_view = (CardView) itemView.findViewById(R.id.card_view);
            img_vol = (ImageView) itemView.findViewById(R.id.img_vol);
            poster_image_comment = (ImageView) itemView.findViewById(R.id.poster_image_comment);
            feed_image = (ImageView) itemView.findViewById(R.id.feed_image);
            poster_image = (ImageView) itemView.findViewById(R.id.poster_image);
            img_more = (ImageView) itemView.findViewById(R.id.img_more);
            ib_play_pause_feed = (ImageButton) itemView.findViewById(R.id.ib_play_pause_feed);
            img_like = (ImageView) itemView.findViewById(R.id.img_like);
            post_time = (TextView) itemView.findViewById(R.id.post_time);
            ll_visual = (LinearLayout) itemView.findViewById(R.id.ll_visual);
            post_text = (TextView) itemView.findViewById(R.id.post_text);
            txt_commet = (TextView) itemView.findViewById(R.id.txt_commet);
            num_likes = (TextView) itemView.findViewById(R.id.num_likes);
            poster_name = (TextView) itemView.findViewById(R.id.poster_name);
            img_progrss = (ProgressBar) itemView.findViewById(R.id.img_progrss);
            num_comment = (TextView) itemView.findViewById(R.id.num_comment);
            num_likes.setTypeface(customFonts.calibri);
            poster_name.setTypeface(customFonts.CabinBold);
            post_text.setTypeface(customFonts.calibri);
            txt_commet.setTypeface(customFonts.calibri);
            post_time.setTypeface(customFonts.calibri);
            num_comment.setTypeface(customFonts.calibri);


        }


    }


}
