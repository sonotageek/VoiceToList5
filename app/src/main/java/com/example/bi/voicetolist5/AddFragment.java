package com.example.bi.voicetolist5;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bi.voicetolist5.Database.DBAdapter;
import com.example.bi.voicetolist5.Item.Item;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    ArrayList<Item> itemDataset=  new ArrayList<>(); //make sure this is initilised here
    EditText taskTxt;
    BottomNavigationView bottomNavigationView;

    public AddFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddFragment newInstance(String param1, String param2) {
        AddFragment fragment = new AddFragment();
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
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        taskTxt = (EditText) view.findViewById(R.id.addTaskEditTxt);
        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add_layout, menu);

        //set up menu icon color
      //  MenuItem item = menu.findItem(R.id.saveItem);
       // MenuItem item2 = menu.findItem(R.id.cancelItem);
       // item.getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        //item2.getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);

       //Dont think this worked
        //getView().findViewById(R.id.activity_main).setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String name = taskTxt.getText().toString();
        switch (item.getItemId()) {

            case R.id.saveItem:
                if (!name.isEmpty()) {
                    save(name);
                    taskTxt.setText("");
                }else {
                    Toast.makeText(getContext(), "Enter Data", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.cancelItem:
                 taskTxt.setText("");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
           // Toast.makeText(context, "Add", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    private void save(String name){
        DBAdapter db = new DBAdapter(getContext());
        db.openDB();
        if (db.add(name)){
            taskTxt.setText("");
        } else {
            Toast.makeText(getContext(), "Not saved", Toast.LENGTH_SHORT).show();
        }
        db.closeDB();
        //refresh
        getItem();
    }


    private void getItem(){
        itemDataset.clear(); //clears list and avoids duplication
        DBAdapter db = new DBAdapter(getContext());
        db.openDB();
        Item item = null;
        Cursor cursor = db.retrieive();
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String date= cursor.getString(2); //add date
            String time= cursor.getString(3);
            item = new Item();
            item.setId(id);
            item.setName(name);
            item.setDate(date); //add setDate in Item class
            item.setTime(time);
            itemDataset.add(item);
        }
        db.closeDB();
        //make sure Planet is not empty
     /*   if (itemDataset.size()>0){
            //recyclerView.setAdapter(adapter);
            recyclerView.setAdapter(swipeAdapter);
        }*/
    } //getPlanets()

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
