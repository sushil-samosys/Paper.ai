package com.samosys.paperai.activity.autocomplete;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.samosys.paperai.R;
import com.samosys.paperai.activity.Bean.ProjctBean;

import com.samosys.paperai.activity.activity.PostfeedActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by samosys on 17/7/18.
 */

public class ProjectPresenter extends RecyclerViewPresenter<ProjctBean> {

    protected Adapter adapter;
    Context mcontext;

    public ProjectPresenter(PostfeedActivity postfeedActivity) {
        super(postfeedActivity);
        this.mcontext=postfeedActivity;
    }

    @Override
    protected PopupDimensions getPopupDimensions() {
        PopupDimensions dims = new PopupDimensions();
        dims.width = 600;
        dims.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        return dims;
    }

    @Override
    protected RecyclerView.Adapter instantiateAdapter() {
        adapter = new Adapter();
        return adapter;
    }

    @Override
    protected void onQuery(@Nullable CharSequence query) {
        List<ProjctBean> all = PostfeedActivity.projectList;
        if (TextUtils.isEmpty(query)) {
            adapter.setData(all);
        } else {
            query = query.toString().toLowerCase();
            List<ProjctBean> list = new ArrayList<>();
            for (ProjctBean u : all) {
                if (u.getName().toLowerCase().contains(query) ) {
                    list.add(u);
                }
            }
            adapter.setData(list);
            Log.e("UserPresenter", "found "+list.size()+" users for query "+query);
        }
        adapter.notifyDataSetChanged();
    }

    class Adapter extends RecyclerView.Adapter<Adapter.Holder> {

        private List<ProjctBean> data;

        public class Holder extends RecyclerView.ViewHolder {
            private View root;
            private TextView fullname;
            private TextView username;
            private ImageView userImg;
            public Holder(View itemView) {
                super(itemView);
                root = itemView;
                fullname = ((TextView) itemView.findViewById(R.id.fullname));
                username = ((TextView) itemView.findViewById(R.id.username));
                userImg = ((ImageView) itemView.findViewById(R.id.userImg));
            }
        }

        public void setData(List<ProjctBean> data) {
            this.data = data;
        }

        @Override
        public int getItemCount() {
            return (isEmpty()) ? 1 : data.size();
        }

        @Override
        public Adapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Adapter.Holder(LayoutInflater.from(getContext()).inflate(R.layout.user, parent, false));
        }

        private boolean isEmpty() {
            return data == null || data.isEmpty();
        }

        @Override
        public void onBindViewHolder(Adapter.Holder holder, int position) {
            if (isEmpty()) {
                holder.fullname.setText("No user here!");
                holder.username.setText("Sorry!");
                holder.root.setOnClickListener(null);
                return;
            }
            final ProjctBean user = data.get(position);
//            holder.fullname.setText(user.getName());
            holder.fullname.setText("#" + user.getName());

            Log.e("USERIMAGE",user.getImage());
            Picasso.with(mcontext).load(user.getImage()).error(R.mipmap.sign_up_workspace)
                    .into(holder.userImg, new Callback() {
                        @Override
                        public void onSuccess() {
                            // holder.img_progrss.setVisibility(View.GONE);

                        }

                        @Override
                        public void onError() {
                            //holder.img_progrss.setVisibility(View.GONE);
                        }
                    });

            holder.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dispatchClick(user);
                }
            });
        }
    }
}
