package com.example.bi.voicetolist5.Swipe;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.example.bi.voicetolist5.Database.DBAdapter;
import com.example.bi.voicetolist5.Item.Item;
import com.example.bi.voicetolist5.Item.ItemDetail;
import com.example.bi.voicetolist5.R;

import java.util.ArrayList;



//import com.example.bi.voicelist4.R;

/**
 * Created by bi on 14-Aug-17.
 */

public class SwipeRecyclerAdapter extends RecyclerSwipeAdapter<SwipeRecyclerAdapter.SwipeViewHolder> {

    private Context context;
    private ArrayList<Item> itemList;
    int selectPos;



    private Item[] sItems;


    public SwipeRecyclerAdapter(Context context, ArrayList<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }


    @Override
    public SwipeRecyclerAdapter.SwipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.swipe_layout, parent, false);

        return new SwipeViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final SwipeRecyclerAdapter.SwipeViewHolder viewHolder, final int position) {
        //final RecyclerView.ViewHolder mHolder=viewHolder;
        final SwipeRecyclerAdapter.SwipeViewHolder mSwipeHolder =viewHolder;

        final Item item = itemList.get(position);
        viewHolder.tvName.setText(item.getName());
        viewHolder.tvDate.setText(item.getDate());
        viewHolder.tvTime.setText(item.getTime());



        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

        //drag from left
       // viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left, viewHolder.swipeLayout.findViewById(R.id.bottom_wrapper1));

        //drag from right
        viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, viewHolder.swipeLayout.findViewById(R.id.bottom_wrapper));

        // //handling different event when swiping
        viewHolder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onStartOpen(SwipeLayout layout) {

                //when the SurfaceView totally cover the BottomView.
            }

            @Override
            public void onOpen(SwipeLayout layout) {
                //when the BottomView totally show.
            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onClose(SwipeLayout layout) {
                //when the SurfaceView totally cover the BottomView.
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                //you are swiping.
            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                //when user's hand released.
            }
        });

        viewHolder.swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "onClick: " + item.getName(), Toast.LENGTH_SHORT).show();
                openDetailTask(v, position);
            }
        });



        //https://stackoverflow.com/questions/36374310/how-to-make-a-selectable-list-with-recyclerview
        viewHolder.btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Item item=itemList.get(selectPos);
                //int id = item.getId();
                getSelectedSongs();
                // gd = holder.groupname.getText().toString();
                  v.setTag(item);

               // itemList.add(sItems[position]);
                if (itemList.contains(item)) {
                    viewHolder.btnLocation.setBackgroundColor(Color.parseColor("#FFE21F3C"));
                    Toast.makeText(context,"Not implemented", Toast.LENGTH_SHORT ).show();
                    v.getTag();

                } else {
                    //else as viewholders are reused
                    viewHolder.btnLocation.setBackgroundColor(Color.parseColor("#f8f8f8"));
                }

            }


        });

        //https://stackoverflow.com/questions/25392722/background-selector-in-recyclerview-item
     /*   viewHolder.btnLocation.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    viewHolder.btnLocation.setBackgroundColor(Color.parseColor("#f8f8f8"));
                }if (event.getAction()==MotionEvent.ACTION_UP){
                    viewHolder.btnLocation.setBackgroundColor(Color.WHITE);
                }
                return false;
            }
        });*/

    /*    viewHolder.btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //   viewHolder.btnLocation.setBackgroundColor(Color.parseColor("#FFBE1E2E"));
              //   viewHolder.btnLocation.setImageResource(R.drawable.ic_action_cancel);
               //  Toast.makeText(v.getContext(), "Notification ", Toast.LENGTH_SHORT).show();
                if(selectPos==position)
                {
                    //change color like
                    Toast.makeText(v.getContext(), "Notification ", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //revert back to regular color
                    Toast.makeText(v.getContext(), "deselected ", Toast.LENGTH_SHORT).show();
                }


            }
        });
*/


        viewHolder.tvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=  viewHolder.tvName.getText().toString();
                //Toast.makeText(v.getContext(), "Share: " + name, Toast.LENGTH_SHORT).show();
                share(name);
            }
        });

        viewHolder.tvEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openDetailTask(v, position);
            }
        });

        viewHolder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              selectPos = position;//deleting from top. This fixes it.

                Snackbar snackbar = Snackbar.make(v, "Are you sure you want to delete?", Snackbar.LENGTH_LONG)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteItem();
                            }
                        });
                snackbar.setActionTextColor(Color.parseColor("#FF8BD4F2"));
                snackbar.show();

                mItemManger.removeShownLayouts(viewHolder.swipeLayout);
                // deleteItem();
                // itemList.remove(position); //This causes app to crash
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, itemList.size());
                mItemManger.closeAllItems();
              //Toast.makeText(v.getContext(), "  Deleted: " + viewHolder.tvName.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        mItemManger.bindView(viewHolder.itemView, position);

    }



    @Override
    public int getItemCount() {
        return itemList.size();
    }


    //https://stackoverflow.com/questions/36374310/how-to-make-a-selectable-list-with-recyclerview
    public Item[] getSelectedSongs() {
        Item[] songs = new Item[itemList.size()];

        return itemList.toArray(songs);
    }


    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    public class SwipeViewHolder extends RecyclerView.ViewHolder {

        public SwipeLayout swipeLayout;
        public TextView tvName;
        public TextView tvDate , tvTime;
        public TextView tvDelete;
        public TextView tvEdit;
        public TextView tvShare;
        public ImageButton btnLocation;

        public SwipeViewHolder(View itemView) {
            super(itemView);

            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            tvName = (TextView) itemView.findViewById(R.id.nameText);
            tvDate = (TextView) itemView.findViewById(R.id.dateTxt);
            tvTime = (TextView) itemView.findViewById(R.id.timeTxt);
            tvDelete = (TextView) itemView.findViewById(R.id.tvDelete);
            tvEdit = (TextView) itemView.findViewById(R.id.tvEdit);
            tvShare = (TextView) itemView.findViewById(R.id.tvShare);
            btnLocation = (ImageButton) itemView.findViewById(R.id.btnLocation);

          /*  btnLocation.setOnTouchListener(new View.OnTouchListener() {
               @Override
               public boolean onTouch(View v, MotionEvent event) {
                   if(event.getAction() == MotionEvent.ACTION_DOWN){
                       btnLocation.setBackgroundColor(Color.parseColor("#f8f8f8"));
                   }if (event.getAction()==MotionEvent.ACTION_UP){
                        btnLocation.setBackgroundColor(Color.WHITE);
                   }

                   return false;
               }
           });*/

        /*    btnLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    selectPos = pos;

                    if (pos == selectPos){
                    btnLocation.setBackgroundColor(Color.parseColor("#FFBE1E2E"));
                    btnLocation.setImageResource(R.drawable.ic_action_cancel);
                    Toast.makeText(v.getContext(), "Notification " + selectPos, Toast.LENGTH_SHORT).show();

                    } else{
                        Toast.makeText(v.getContext(), "deselcted " + selectPos, Toast.LENGTH_SHORT).show();
                    }
                }
            });
            */

            //https://stackoverflow.com/questions/28570788/select-items-in-recyclerview
            // selectPos = getAdapterPosition();
            //notifyDataSetChanged();
        }
    }


    //delete item
    public void deleteItem() {
        //GEtid
        Item item = itemList.get(selectPos);
        int id = item.getId();
        //delete from db
        DBAdapter dbAdapter = new DBAdapter(context);
        dbAdapter.openDB();
        if (dbAdapter.delete(id)) {
            //delete from arraylist as well
            itemList.remove(selectPos);
            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Not deleted", Toast.LENGTH_SHORT).show();
        }
        dbAdapter.closeDB();
        notifyDataSetChanged();
        //this.notifyItemRemoved(selectPos);
    }


    //share using different options
    public void share(String name){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        //whatsappIntent.setPackage("com.whatsapp"); //this send data on whatsapp only
        intent.putExtra(Intent.EXTRA_TEXT, name);
        try {
           // context.startActivity(whatsappIntent);//this send data on whatsapp only
            context.startActivity(Intent.createChooser(intent, "Choose.."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "Cant share", Toast.LENGTH_SHORT).show();
        }
    } //end share()


    //goes to Detail Task
    public void openDetailTask(View v , int pos){
        selectPos = pos;  //solves problem with same row being selected . This used since i cant use getAdapterPosition()
        Item item = itemList.get(selectPos);  //crashes here when scrolling through search 27/08/17
        Intent intent = new Intent(context, ItemDetail.class);
        Bundle extras=new Bundle();
        extras.putString("Task", item.getName());
        extras.putInt("id", item.getId());
        extras.putString("date", item.getDate());
        extras.putString("time", item.getTime());
        intent.putExtras(extras);
        v.getContext().startActivity(intent);  //problem here
    }

    //if ALarm is used
    public void setUpAlarm(String time){

    }





}

