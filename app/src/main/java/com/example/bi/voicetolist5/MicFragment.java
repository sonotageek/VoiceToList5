package com.example.bi.voicetolist5;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bi.voicetolist5.Database.DBAdapter;
import com.example.bi.voicetolist5.Database.DatabaseHelper;

import java.util.ArrayList;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MicFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MicFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MicFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    ImageButton btnRecord;
    Button btnSave, btnCancel;
    EditText voiceText;
    LinearLayout voiceTextLayout;
    DatabaseHelper dbHelper;
    TextView titleText;

    public MicFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MicFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MicFragment newInstance(String param1, String param2) {
        MicFragment fragment = new MicFragment();
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
       // return inflater.inflate(R.layout.fragment_mic, container, false);

       View view = inflater.inflate(R.layout.fragment_mic, container, false);
       setVoiceTextLayout(view);  //set up layout
       return view;
    }


    //set up layout for Fragment
    public void setVoiceTextLayout(View view){
        voiceTextLayout = (LinearLayout) view.findViewById(R.id.voiceTextLayout);
        titleText = (TextView) view.findViewById(R.id.titleText);
        btnRecord = (ImageButton) view.findViewById(R.id.btnRecord);
        btnSave = (Button) view.findViewById(R.id.btnSave);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        voiceText = (EditText) view.findViewById(R.id.voiceText);
        voiceTextLayout.setVisibility(View.INVISIBLE);
        dbHelper = new DatabaseHelper(getContext());
        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordSpeech(v);
            }
        });
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
            //Toast.makeText(context, "Mic", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Checks the orientation of the screen

        //https://stackoverflow.com/questions/24200641/setcontentview-giving-error-in-onconfigurationchanged

        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.fragment_mic, null);
        ViewGroup rootView = (ViewGroup) getView();
        rootView.removeAllViews();
        rootView.addView(view);
        //this is added here otherwise there are problems with layout not proper and crashes if 'mic' btn is pressed
        setVoiceTextLayout(view);
       // https://stackoverflow.com/questions/15069633/enable-display-orientation-change-when-certain-fragments-are-visible-and-disable
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //Toast.makeText(getContext(), "Landscape", Toast.LENGTH_SHORT).show();
            Snackbar.make(getView(), "Select mic", Snackbar.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 10: if (resultCode == RESULT_OK && data!= null){
                titleText.setVisibility(View.INVISIBLE); //added 27/08/17
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                voiceTextLayout.setVisibility(View.VISIBLE);
                voiceText.setText(result.get(0));

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = voiceText.getText().toString();
                        if (!name.isEmpty()) {
                            AddData(name);
                            voiceTextLayout.setVisibility(View.INVISIBLE);
                            titleText.setVisibility(View.VISIBLE);
                        }else {
                            Toast.makeText(getContext(), "Enter Data", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        voiceText.setText("");
                        voiceTextLayout.setVisibility(View.INVISIBLE);
                        titleText.setVisibility(View.VISIBLE);
                    }
                });
            } //end case
        } //end switch
    } //end onActivityResult


    public void AddData(String name) {
        DBAdapter db = new DBAdapter(getContext());
        db.openDB();
        if (db.add(name)){  //addTask() works as well
            Toast.makeText(getContext(), "Added", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Not saved", Toast.LENGTH_SHORT).show();
        }
        db.closeDB();

    }

    //record btn
    public void recordSpeech(View view) {
        if (view.getId() == R.id.btnRecord){
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak");
            try {
                startActivityForResult(intent, 10);
            } catch (ActivityNotFoundException e){
                Toast.makeText(getContext(), "Sorry your device does not support this", Toast.LENGTH_SHORT).show();
            }
        }
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
