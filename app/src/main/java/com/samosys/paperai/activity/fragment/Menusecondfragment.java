package com.samosys.paperai.activity.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.samosys.paperai.R;
import com.samosys.paperai.activity.Bean.ProjctBean;
import com.samosys.paperai.activity.activity.NewProjctActivity;
import com.samosys.paperai.activity.adapter.MenuPr0jectAdapter;
import com.samosys.paperai.activity.utils.AppConstants;
import com.samosys.paperai.activity.utils.CustomFonts;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Menusecondfragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Menusecondfragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Menusecondfragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView txtSpceName,txtNoVisible,txt_allprct;
    RecyclerView RV_projct;
    MenuPr0jectAdapter adapter;
    ImageView img_addprojet;
    ArrayList<ProjctBean> mylist;
    EditText edt_menuproject;
    String id = "", name = "";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    CustomFonts customFonts;
    private OnFragmentInteractionListener mListener;

    public Menusecondfragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment Menusecondfragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Menusecondfragment newInstance(String param1) {
        Menusecondfragment fragment = new Menusecondfragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);

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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_menusecondfragment, container, false);
        RV_projct = (RecyclerView) v.findViewById(R.id.RV_projct);
        img_addprojet = (ImageView) v.findViewById(R.id.img_addprojet);
        txtSpceName = (TextView) v.findViewById(R.id.txtSpceName);
        txt_allprct = (TextView) v.findViewById(R.id.txt_allprct);
        txtNoVisible = (TextView) v.findViewById(R.id.txtNoVisible);
        edt_menuproject=(EditText)v.findViewById(R.id.edt_menuproject);
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event



    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

            // load data here
            mylist.clear();

            id = AppConstants.loadPreferences(getActivity(), "workid");
            name = AppConstants.loadPreferences(getActivity(), "workname");
            Log.e("WORKNAMESECond2", id + "===" + name);
            txtSpceName.setText(name);
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Project");
            query.whereEqualTo("workspaceID", id);
            query.whereEqualTo("workspace",ParseObject.createWithoutData("WorkSpace", id));
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null) {

                        Log.e("WORKNAME", objects.size() + "");
                        for (int i = 0; i < objects.size(); i++) {
                            String objectId = objects.get(i).getObjectId();
                            String name = objects.get(i).getString("name");
                            String updatedAt = objects.get(i).getString("updatedAt");
                            String workspaceID = objects.get(i).getString("workspaceID");
                            String objective = objects.get(i).getString("objective");
                            String createdAt = objects.get(i).getString("createdAt");
                            String image = objects.get(i).getString("image");
                            String type = objects.get(i).getString("type");


                            mylist.add(new ProjctBean(objectId, name, updatedAt, workspaceID, objective, createdAt, image, type, objects.get(i)));
                        }
//                        if (mylist.size()>0){
//                            txtNoVisible.setVisibility(View.GONE);
//                            RV_projct.setVisibility(View.VISIBLE);
//                            adapter = new MenuPr0jectAdapter(getActivity(), mylist,"frag");
//                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
//                            RV_projct.setLayoutManager(layoutManager);
//                            RV_projct.setAdapter(adapter);
//                            adapter.notifyDataSetChanged();}
//                        else {
//                            txtNoVisible.setVisibility(View.VISIBLE);
//                            RV_projct.setVisibility(View.GONE);
//                        }



                    } else {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();

                        Log.e("lse", e.getMessage());
                        // error
                    }
                }
            });
        }else{
            // fragment is no longer visible
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mylist = new ArrayList<>();
        customFonts=new CustomFonts(getActivity());
        txtSpceName.setTypeface(customFonts.HelveticaNeueMedium);
        txtNoVisible.setTypeface(customFonts.HelveticaNeueMedium);
        txt_allprct.setTypeface(customFonts.HelveticaNeueMedium);
        img_addprojet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewProjctActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
//        edt_menuproject.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (s.length()>0){
//                adapter.getFilter().filter(s.toString());}
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
