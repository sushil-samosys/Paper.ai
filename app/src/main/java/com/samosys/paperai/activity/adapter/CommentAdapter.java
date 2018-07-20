package com.samosys.paperai.activity.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.samosys.paperai.R;
import com.samosys.paperai.activity.Bean.CommentBean;
import com.samosys.paperai.activity.activity.CommentActivity;
import com.samosys.paperai.activity.utils.CustomFonts;
import com.samosys.paperai.activity.utils.First_Char_Capital;

import java.util.ArrayList;

/**
 * Created by samosys on 3/4/18.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.Holder> {
    Context context;
    private ArrayList<CommentBean> filterlist;
    CustomFonts customFonts;


    public CommentAdapter(CommentActivity commentActivity, ArrayList<CommentBean> mylist) {
        this.context = commentActivity;
        this.filterlist = mylist;
        customFonts = new CustomFonts(commentActivity);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_item, parent, false);
        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {
        String sourceString =   "</b> " + filterlist.get(position).getTxt();
        holder.userName.setText("@" + filterlist.get(position).getPost_user_name());
        holder.user_coment.setText(Html.fromHtml(sourceString));
        filterlist.get(position).getUser_imge().getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] data, ParseException e) {
                if (e == null) {
                    Bitmap bmp = BitmapFactory
                            .decodeByteArray(
                                    data, 0,
                                    data.length);

                    // initialize
                    holder.imgwork.setImageBitmap(bmp);
                }
            }
        });
    }//

    @Override
    public int getItemCount() {
        return filterlist.size();
    }


    public class Holder extends RecyclerView.ViewHolder {
        private  TextView userName, user_coment;

        private ImageView imgwork;

        public Holder(View itemView) {
            super(itemView);

            userName = (TextView) itemView.findViewById(R.id.userName);
            user_coment = (TextView) itemView.findViewById(R.id.user_coment);
            imgwork = (ImageView) itemView.findViewById(R.id.com_userImage);


            userName.setTypeface(customFonts.HelveticaNeueBold);
            user_coment.setTypeface(customFonts.HelveticaNeueMedium);
            //  comment_timeago.setTypeface(customFonts.HelveticaNeueLight);
        }
    }


}

