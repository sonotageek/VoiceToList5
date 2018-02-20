package com.example.bi.voicetolist5.Item;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bi.voicetolist5.Database.Constants;
import com.example.bi.voicetolist5.Database.DBAdapter;
import com.example.bi.voicetolist5.Database.DatabaseHelper;
import com.example.bi.voicetolist5.MainActivity;
import com.example.bi.voicetolist5.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;



public class ItemDetail extends AppCompatActivity {

    Bundle extras;
    String name;
    String date;
    String time;
    int id;
    TextView txtTask, idTxt , dateTxt, timeText;
    EditText editTextTask;
   // ImageButton btnSave, btnCancel;
    LinearLayout dataLayout;
    RelativeLayout editLayout;
    SQLiteDatabase db;

    Menu menu;
    boolean myItemShouldBeEnabled = false;
    ShareActionProvider shareActionProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

      //  idTxt = (TextView) findViewById(R.id.idTxt);
        txtTask = (TextView) findViewById(R.id.textDetailTask);
        dateTxt = (TextView) findViewById(R.id.dateText);
        timeText = (TextView) findViewById(R.id.timeText);
        dataLayout = (LinearLayout) findViewById(R.id.dataLayout);

        editLayout = (RelativeLayout) findViewById(R.id.editTaskLayout);
        editTextTask = (EditText)findViewById(R.id.edittextDetailTask);
        // btnSave = (ImageButton) findViewById(R.id.btnSaveTask);
        // btnCancel= (ImageButton) findViewById(R.id.btnCancelTask);

        getData();//Bundle data
        txtTask.setText(name);
      //  idTxt.setText(Integer.toString(id)); //(String.ValueOf(id)) //works as well
        dateTxt.setText(date);
        timeText.setText(time);
    }


    //get Bundle data
    public void getData(){
        extras = getIntent().getExtras();
        name = extras.getString("Task");
        date = extras.getString("date");
        time = extras.getString("time");
        id = extras.getInt("id");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detail_task, menu);
        this.menu = menu; //to be used with menu actions
        MenuItem menuItem = menu.findItem(R.id.action_share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        shareActionProvider.setShareIntent(getDefaultIntent());
        //shareActionProvider.setShareHistoryFileName(null); this doesnt work

        return true;

        //addded this in PrepareOptionsMenu
        //initialise save and cancel menu attributes
      /* MenuItem itemSave = menu.findItem(R.id.saveItem);
        MenuItem itemCancel = menu.findItem(R.id.cancelItem);
        itemSave.getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP); //https://androidforums.com/threads/how-to-change-color-of-menu-item-icon-in-android.916282/
        itemCancel.getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
       itemCancel.setVisible(false);// //makes test icon invisible
        itemSave.setVisible(false);// //makes test icon invisible
        */
    }


    private Intent getDefaultIntent(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, name);
        intent.setType("text/plain");
        intent.createChooser(intent, "share using.."); //didnt make much of  difference  here
        return  intent;
    }


    //This is for sharing with watsapp only
    private void shareIntent(){
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, name);

          try {
            startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ItemDetail.this, "Cant share", Toast.LENGTH_SHORT).show();
        }
    }



 //This can be used instead of initialing menu icons in onCreate()
 @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
       // this.menu = menu;
        MenuItem item = menu.findItem(R.id.saveItem);
        MenuItem item2 = menu.findItem(R.id.cancelItem);
        item.getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        item2.getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);

        if (myItemShouldBeEnabled) {
            item.setVisible(true);
            item2.setVisible(true);
           // item.setEnabled(true);
           // item.getIcon().setAlpha(255);
        } else {
            // disabled
            item.setVisible(false);
            item2.setVisible(false);
           // item.setEnabled(false);
          //  item.getIcon().setAlpha(130);

        }
        return true;
      //return super.onPrepareOptionsMenu(menu);
    }



    public void setmenuVisibleOn(Menu menu){
        MenuItem itemSave = menu.findItem(R.id.saveItem);
        MenuItem itemCancel = menu.findItem(R.id.cancelItem);
        menu.findItem(R.id.editItem).setVisible(false); //makes edit icon invisible
        itemSave.setVisible(true);
        itemCancel.setVisible(true);
        menu.findItem(R.id.delItem).setVisible(false);
        menu.findItem(R.id.delItem).getIcon().setAlpha(90);
        menu.findItem(R.id.action_share).setVisible(false);
        //menu.findItem(R.id.shareItem).getIcon().setAlpha(90); //this crashes when SharedActionProvider class is used
    }


    //changes visibility and attr of menu icons when Edit Menu is selected
    public void setmenuVisibleOff(Menu menu){
        menu.findItem(R.id.saveItem).setVisible(false);
        menu.findItem(R.id.cancelItem).setVisible(false);
        menu.findItem(R.id.editItem).setVisible(true); //makes edit icon visible
        menu.findItem(R.id.delItem).setVisible(true);
        menu.findItem(R.id.action_share).setVisible(true);
        menu.findItem(R.id.delItem).getIcon().setAlpha(255);
        //menu.findItem(R.id.shareItem).getIcon().setAlpha(255); //this crashes when SharedActionProvider class is used
    }


    //set TextView to visible
    public void textLayoutVisibilty(){
        dataLayout.setVisibility(View.VISIBLE);
        txtTask.setVisibility(View.VISIBLE);
        editLayout.setVisibility(View.INVISIBLE);
    }


    public boolean onOptionsItemSelected(final MenuItem item) {
        MenuItem itemSave = menu.findItem(R.id.saveItem);
        MenuItem itemCancel = menu.findItem(R.id.cancelItem);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        int id = item.getItemId();

        switch (id) {
            case R.id.editItem: //edit task
                //setVisibility of menus icons
                setmenuVisibleOn(menu);

                //setVisibility for layout
                dataLayout.setVisibility(View.INVISIBLE);
                txtTask.setVisibility(View.INVISIBLE); //set TextView to invisible
                editLayout.setVisibility(View.VISIBLE);

                editTextTask.setText(name);


                editTextTask.setSelection(editTextTask.getText().length());  //works as well

                //maybe an if statement might resolve the bug with edit menu when back button is pressed
                itemSave.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        String editT = editTextTask.getText().toString(); //get data
                        updateRow(editT); //update row in db table
                        textLayoutVisibilty();  //setVisibility for layout
                        setmenuVisibleOff(menu); //setVisibility of menus icons
                        txtTask.setMovementMethod(new ScrollingMovementMethod());//Dont think this makes much of a difference here. ???????
                        txtTask.setText(editT);

                        //date and time is updated - maybe try to use existing methods in DB adapter. Could haev used Date as in DBapter
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yy");
                        SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");
                        dateTxt.setText(format1.format(calendar.getTime())); //getTime()
                        timeText.setText(formatTime.format(calendar.getTime()));
                        return true;
                    }
                });

                itemCancel.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        //setVisibility for layout and  of menus icons
                        textLayoutVisibilty();
                        setmenuVisibleOff(menu);
                        return true;
                    }
                });
                return true;

          /*  case R.id.shareItem: //share task on whatsapp
                share(name);
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                return true;*/

            case R.id.action_share:
                //http://stackandroid.com/tutorial/android-shareactionprovider-tutorial/
                //https://www.youtube.com/watch?v=oiQYPhB2oYU

                return true;
            case R.id.delItem: //delete task
                Snackbar snackbar = Snackbar.make(findViewById(R.id.activity_item_detail), "Are you sure you want to delete?", Snackbar.LENGTH_LONG)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteItem();
                            }
                        });
                snackbar.setActionTextColor(Color.parseColor("#FF8BD4F2"));
                snackbar.show();
                return true;

            default:
                finish(); //this works if setmenuVisibleOff(menu) is defined in the method so it doesnt need to be here below
                         //this has solved the bug  when back in toolbar is pressed
                return super.onOptionsItemSelected(item);
        }

    }


    //share on whatsapp
    public void share(String name){
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, name);
        try {
            startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ItemDetail.this, "Cant share", Toast.LENGTH_SHORT).show();
        }
    } //end share()




    //updates row after editing task
    public void updateRow(String editT) {
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        //these 2 lines added so that I can access to getTime() in adapter
        DBAdapter dbAdapter = new DBAdapter(this);
        dbAdapter.openDB();
        getData();
        try
        {
            db = dbHelper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(Constants.COL_2,editT ); //item.getName()
            cv.put(Constants.COL_DATE, dbAdapter.getCDate());
            cv.put(Constants.COL_TIME, dbAdapter.getCTime()); //dbAdapter.getDateTime()); //updates time and date

            // update row
            db.update("task", cv,  Constants.COL_1 + " = ?",  new String[] { String.valueOf(id)});
            Toast.makeText(getApplicationContext(), "updated", Toast.LENGTH_SHORT).show(); //
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Not updated", Toast.LENGTH_SHORT).show();
        }
    }



    //maybe add this in DBAdapter
    private void deleteItem()
    {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        getData();
        try
        {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            int result = db.delete("task", Constants.COL_1 + " = ?",  new String[] { String.valueOf(id)});// This seems to avoid deleting dupicates and only selected row .
            if (result>0){
                Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
            }
        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(), "Not deleted", Toast.LENGTH_SHORT).show();
        }
       // Intent i = new Intent(this, MainActivity.class);
        // startActivity(i);
        Intent ii=new Intent(this, MainActivity.class);
        startActivity(ii);

    } //end deleteItem()




    //http://programmerguru.com/android-tutorial/how-to-change-the-back-button-behaviour/
    public void backButtonSelected(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Leave App");
        alertDialog.setMessage("Are you sure you want to leave this page?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();

    }

    @Override
    public void onBackPressed() {
        //  super.onBackPressed(); //without this it stays on the same page
        backButtonSelected();
        // setmenuVisibleOff(menu);
        // Not sure about this code
        // KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK);
        //this.getWindow().openPanel(Window.FEATURE_OPTIONS_PANEL, event);
    }


    //onBackPressed can also be used instead
    @Override
    public void finish() {
        super.finish();
        setmenuVisibleOff(menu);
    }
}




/*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayDialog();
            }
        });*/

 /* @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == "New"){
            displayDialog();
        } else if (item.getTitle() == "Delete"){
            adapter.deletePlanet();
        }
        else if (item.getTitle() == "Edit"){
            Toast.makeText(this, "Edit", Toast.LENGTH_SHORT).show();
            //  editData(int id,"string");  ????
        }
        return super.onContextItemSelected(item);
    }*/

/*
*  private void displayDialog() {
        Dialog d = new Dialog(this);
        d.setTitle("SQLite Database");
      //  d.setContentView(R.layout.dialog_layout);
        d.setContentView(R.layout.add_task_layout);
        //nameEditText = (EditText) d.findViewById(R.id.nameEditText);
        nameEditText = (EditText) d.findViewById(R.id.addTaskEditTxt);
        //saveBtn = (Button) d.findViewById(R.id.btnSave);
        saveBtn = (Button) d.findViewById(R.id.btnAdd);
       // retreiveBtn = (Button) d.findViewById(R.id.btnRetrieve);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save(nameEditText.getText().toString());
            }
        });
       retreiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getItem();
            }
        d.show();
        } //end displayDialog()
    *
 */




/*
*
*  //maybe an if statement might resolve the bug with edit menu when back button is pressed
        if (id == R.id.editItem){
            //setVisibility of menus icons
            setmenuVisibleOn(menu);
            //setVisibility for Edit layout
            dataLayout.setVisibility(View.INVISIBLE);
            txtTask.setVisibility(View.INVISIBLE); //set TextView to invisible
            editLayout.setVisibility(View.VISIBLE);
            editTextTask.setText(name);

            itemSave.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    String editT = editTextTask.getText().toString(); //get data
                    updateRow(editT); //update row in db table
                    textLayoutVisibilty(); //set Visibility for textLayout
                    setmenuVisibleOff(menu); //setVisibility of menus icons
                    txtTask.setMovementMethod(new ScrollingMovementMethod());//Dont think this makes much of a difference here. ???????
                    txtTask.setText(editT);

                    //date and time is updated - maybe try to use existing methods in DB adapter. Could haev used Date as in DBapter
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yy");
                    SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");
                    dateTxt.setText(format1.format(calendar.getTime())); //getTime()
                    timeText.setText(formatTime.format(calendar.getTime()));
                    return true;
                }
            });
            itemCancel.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    //setVisibility for Text layout
                    textLayoutVisibilty();
                    //setVisibility of menus icons
                    setmenuVisibleOff(menu);
                    return true;
                }
            });

        } //end if

        else if (id == R.id.shareItem){//share task on whatsapp
            share(name);
            Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
        }

        else if (id == R.id.delItem){
            Snackbar snackbar = Snackbar.make(findViewById(R.id.activity_item_detail), "Are you sure you want to delete?", Snackbar.LENGTH_LONG)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteItem();
                        }
                    });
            snackbar.setActionTextColor(Color.parseColor("#FF8BD4F2"));
            snackbar.show();
        }

        else{
            //setVisibility of menus and textlayout
            //textLayoutVisibilty();
            setmenuVisibleOff(menu);

        }
* */


/*
    * public void setupFacebookShareIntent() {

      ShareDialog shareDialog;

      FacebookSdk.sdkInitialize(getApplicationContext());

      shareDialog = new ShareDialog(this);



      ShareLinkContent linkContent = new ShareLinkContent.Builder()

              .setContentTitle("Title")

              .setContentDescription(

                      "\"Body Of Test Post\"")

              .setContentUrl(Uri.parse("http://someurl.com/here"))

              .build();



      shareDialog.show(linkContent);

  }
    * */

