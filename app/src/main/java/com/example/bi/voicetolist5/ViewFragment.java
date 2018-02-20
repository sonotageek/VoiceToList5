package com.example.bi.voicetolist5;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.util.Attributes;
import com.example.bi.voicetolist5.Database.DBAdapter;
import com.example.bi.voicetolist5.Item.Item;
import com.example.bi.voicetolist5.Recycler.DividerItemDecoration;
import com.example.bi.voicetolist5.Swipe.SwipeRecyclerAdapter;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    RecyclerView recyclerView;
    EditText taskTxt;
    int mColumnCount = 2; //changing view
    SwipeRecyclerAdapter swipeAdapter;
    ArrayList<Item> itemDataset=  new ArrayList<>(); //make sure this is initilised here
    TextView nameTxt;

    Item item;
    DBAdapter db;
   // Cursor cursor = null;
    Cursor cursor;


    public ViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewFragment newInstance(String param1, String param2) {
        ViewFragment fragment = new ViewFragment();
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
        setHasOptionsMenu(true);  //top menu won't show up without this

        View view = inflater.inflate(R.layout.fragment_view, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

       // getItem(); //load data

        //this is used instead of getItem() now
        initialiseDB();
        cursor  = db.retrieive();
        sortSelect(cursor);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.divider)));//make sure correct library is imported here


        if (itemDataset.isEmpty()){
            Toast.makeText(getContext(), "no data found", Toast.LENGTH_SHORT).show();
        }
        swipeAdapter = new SwipeRecyclerAdapter(getContext(), itemDataset);
        ((SwipeRecyclerAdapter) swipeAdapter).setMode(Attributes.Mode.Single);
        recyclerView.setAdapter(swipeAdapter);
        return view;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_view_layout, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        //searchView.setOnQueryTextListener(this);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

               // http://camposha.info/source/android-sqlite-database-recyclerview-saveretrieve-searchfilter/
                //maybe I need to add the full code here to stop it from crashing
                itemDataset.clear(); //clears list and avoids duplication
                DBAdapter db = new DBAdapter(getContext());
                db.openDB();
                Item item = null;
                Cursor cursor = db.searchRetrieve(newText);
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
                if (itemDataset.size()>0){
                    recyclerView.setAdapter(swipeAdapter);
                } else // maybe try and fix //recyclerView and adapter here to stop it from crashing
                  //  Toast.makeText(getContext(), "no data found", Toast.LENGTH_SHORT).show();
                 //recyclerView.getRecycledViewPool().clear();
                  //  recyclerView.stopScroll();
                // recyclerView.swapAdapter(swipeAdapter, true);
                    swipeAdapter.notifyDataSetChanged();  //this seems to solve this problem here
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
           /* case R.id.search:
                Toast.makeText(getContext(), "Not yet implemented", Toast.LENGTH_SHORT).show();
                return true;*/
            case R.id.sortItem:
                displaySortDialog();
                return true;
            /*  case R.id.layoutView:
                ViewLDialog();
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
           // Toast.makeText(context, "View", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    private void displaySortDialog() {
        Dialog d = new Dialog(getContext());
        d.setTitle("Sort Data");
        //  d.setContentView(R.layout.dialog_layout);
        d.setContentView(R.layout.sort_date_layout);
        TextView sortDate = (TextView)  d.findViewById(R.id.sortDate);
        TextView sortDateDesc = (TextView)  d.findViewById(R.id.sortDateDesc);
        TextView sortAZ = (TextView)  d.findViewById(R.id.sortAZ);
        TextView sortZA = (TextView)  d.findViewById(R.id.sortZA);
        TextView sortFirstLast = (TextView)  d.findViewById(R.id.sortFL);

        //intialise DBAdapter and DB
      // initialiseDB(); //this is repetition but crashes without it
        //this causes problems when switiching frm sort to search.
        //Need to fix this


        sortDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initialiseDB();
               // Toast.makeText(getContext(), "Sort by Date Ascending", Toast.LENGTH_SHORT).show();
                Cursor cursor = db.sortDate();
                sortSelect(cursor);
            }
        });
        sortDateDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initialiseDB();
              //  Toast.makeText(getContext(), "Sort by Date Descending", Toast.LENGTH_SHORT).show();
                Cursor cursor = db.sortDateDesc();
                sortSelect(cursor);
            }
        });
        sortAZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initialiseDB();
               // Toast.makeText(getContext(), "Sort by A-Z", Toast.LENGTH_SHORT).show();
                Cursor cursor = db.sortAZ();
                sortSelect(cursor);

            }
        });
        sortZA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initialiseDB();
             //   Toast.makeText(getContext(), "Sort by Z-A", Toast.LENGTH_SHORT).show();
                Cursor cursor = db.sortZA();
                sortSelect(cursor);

            }
        });
        sortFirstLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initialiseDB();
              //  Toast.makeText(getContext(), "Sort by First entry to Last", Toast.LENGTH_SHORT).show();
                Cursor cursor = db.retrieive();
                sortSelect(cursor);
            }
        });
        d.show();
    } //end displayDialog()


    public void initialiseDB(){
        itemDataset.clear(); //clears list and avoids duplication
        db = new DBAdapter(getContext());
        db.openDB();
        item = null;
    }


    public void sortSelect(Cursor cursor){
        initialiseDB();
        this.cursor = cursor;
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String date= cursor.getString(2);
            String time= cursor.getString(3);
            item = new Item();
            item.setId(id);
            item.setName(name);
            item.setDate(date);
            item.setTime(time);
            itemDataset.add(item);
        }
        db.closeDB();
        //make sure Planet is not empty
        if (itemDataset.size()>0){
            recyclerView.setAdapter(swipeAdapter);
        }
    }


    private void ViewLDialog() {
        TextView gridView, linearView, gridBigView ;
        Dialog d = new Dialog(getContext());
        d.setTitle("SQLite Database");
        //  d.setContentView(R.layout.dialog_layout);
        d.setContentView(R.layout.list_view_layout);
       // gridView = (TextView)  d.findViewById(R.id.gridView);
        linearView = (TextView)  d.findViewById(R.id.linearView);
        gridBigView = (TextView)  d.findViewById(R.id.gridViewBig);

      /*  gridView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Sort by Grid", Toast.LENGTH_SHORT).show();
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), mColumnCount));
            }
        });*/

        linearView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "View by Linear", Toast.LENGTH_SHORT).show();
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            }
        });

        //need to sort this out. Implement this properly
        gridBigView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameTxt = (TextView) v.findViewById(R.id.nameText);
                Toast.makeText(getContext(), "View by Linear Big", Toast.LENGTH_SHORT).show();
       /*         ViewGroup.LayoutParams params=recyclerView.getLayoutParams();
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), mColumnCount));
                recyclerView.setLayoutParams(params);*/
            }
        });
        d.show();
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



/*

   //this works fine. If sortSelect() is used when recycler is initialised the app crashes
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
        if (itemDataset.size()>0){
            //recyclerView.setAdapter(adapter);
            recyclerView.setAdapter(swipeAdapter);
        }
    } //getPlanets()
* */