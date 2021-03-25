package au.edu.utas.sakther.assignment2.classes;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import au.edu.utas.sakther.assignment2.R;
import au.edu.utas.sakther.assignment2.db.DBHelper;
import au.edu.utas.sakther.assignment2.model.MoodModel;

public class MoodAdapter extends RecyclerView.Adapter<MoodAdapter.ViewHolder> {
    Context context;
    ArrayList<MoodModel> data;
    private ArrayList<MoodModel> arraylist;


    public MoodAdapter(Context context, ArrayList<MoodModel> data) {
        this.context = context;
        this.data = data;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.mood_item_layout, parent, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final MoodModel obj = data.get(position);

        String dateTime=obj.getDate()+" "+obj.getTime();

        holder.date.setText(obj.getDate());
        holder.time.setText(obj.getTime());

        if (obj.getMood().equals("Very Happy")){
            holder.moodImage.setImageDrawable(context.getResources().getDrawable(R.drawable.very_happy));
            holder.mood.setText("Very Happy");
            holder.mood.setTextColor(context.getResources().getColor(R.color.very_happy));
        }

        if (obj.getMood().equals("Happy")){
            holder.moodImage.setImageDrawable(context.getResources().getDrawable(R.drawable.happy));
            holder.mood.setText("Happy");
            holder.mood.setTextColor(context.getResources().getColor(R.color.happy));
        }

        if (obj.getMood().equals("Neutral")){
            holder.moodImage.setImageDrawable(context.getResources().getDrawable(R.drawable.neutral));
            holder.mood.setText("Neutral");
            holder.mood.setTextColor(context.getResources().getColor(R.color.neutral));
        }

        if (obj.getMood().equals("Sad")){
            holder.moodImage.setImageDrawable(context.getResources().getDrawable(R.drawable.sad));
            holder.mood.setText("Sad");
            holder.mood.setTextColor(context.getResources().getColor(R.color.sad));
        }

        if (obj.getMood().equals("Depressed")){
            holder.moodImage.setImageDrawable(context.getResources().getDrawable(R.drawable.depressed));
            holder.mood.setText("Depressed");
            holder.mood.setTextColor(context.getResources().getColor(R.color.depressed));
        }


        holder.dotMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu=new PopupMenu(context, holder.dotMenu);
                popupMenu.getMenuInflater().inflate(R.menu.nav_dot_menu,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {


                        if (item.getTitle().equals("Delete")){


                            final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                            builder.setMessage("Are you sure you want to delete this Mood?")
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(final DialogInterface dialog, final int id) {
                                            DBHelper dbHelper=new DBHelper(context);
                                            dbHelper.deleteMood(obj.getId());
                                            data.remove(position);
                                            notifyDataSetChanged();

                                            Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        public void onClick(final DialogInterface dialog, final int id) {
                                            dialog.cancel();
                                        }
                                    });

                            final android.app.AlertDialog alert = builder.create();
                            alert.show();
                        }
                        if (item.getTitle().equals("Update")){
                            showMoodDialog(obj.getId(),holder);
                        }

                        return true;
                    }
                });

                popupMenu.show();
            }
        });
    }

    private void showMoodDialog(final int id, final ViewHolder holder) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.add_mood_layout, null);

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();


        // Get the calander
        Calendar c = Calendar.getInstance();

        final String date,time;
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        SimpleDateFormat stf = new SimpleDateFormat("h:mm a");
        Date now = c.getTime();
        date = sdf.format(now);
        time=stf.format(now);

        Log.d("check", "date time: "+date+" "+time);

        final MoodModel[] moodData = new MoodModel[1];
        final DBHelper dbHelper = new DBHelper(context);

        ((View) view.findViewById(R.id.mood_very_happy)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(), "Preview  clicked", Toast.LENGTH_SHORT).show();

                MoodModel moodModel =new MoodModel(id,"Very Happy",50,date,time);
                dbHelper.updateMood(moodModel);
                Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show();

                holder.moodImage.setImageDrawable(context.getResources().getDrawable(R.drawable.very_happy));
                holder.mood.setText("Very Happy");
                holder.mood.setTextColor(context.getResources().getColor(R.color.very_happy));
                holder.date.setText(date);
                holder.time.setText(time);

                alertDialog.cancel();

            }
        });

        ((View) view.findViewById(R.id.mood_happy)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MoodModel moodModel =new MoodModel(id,"Happy",40,date,time);
                dbHelper.updateMood(moodModel);
                Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show();

                holder.moodImage.setImageDrawable(context.getResources().getDrawable(R.drawable.happy));
                holder.mood.setText("Happy");
                holder.mood.setTextColor(context.getResources().getColor(R.color.happy));
                holder.date.setText(date);
                holder.time.setText(time);

                alertDialog.cancel();
            }
        });

        ((View) view.findViewById(R.id.mood_neutral)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MoodModel moodModel =new MoodModel(id,"Neutral",30,date,time);
                dbHelper.updateMood(moodModel);
                Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show();

                holder.moodImage.setImageDrawable(context.getResources().getDrawable(R.drawable.neutral));
                holder.mood.setText("Neutral");
                holder.mood.setTextColor(context.getResources().getColor(R.color.neutral));
                holder.date.setText(date);
                holder.time.setText(time);

                alertDialog.cancel();
            }
        });

        ((View) view.findViewById(R.id.mood_sad)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MoodModel moodModel =new MoodModel(id,"Sad",20,date,time);
                dbHelper.updateMood(moodModel);
                Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show();

                holder.moodImage.setImageDrawable(context.getResources().getDrawable(R.drawable.sad));
                holder.mood.setText("Sad");
                holder.mood.setTextColor(context.getResources().getColor(R.color.sad));
                holder.date.setText(date);
                holder.time.setText(time);

                alertDialog.cancel();
            }
        });

        ((View) view.findViewById(R.id.mood_depressed)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MoodModel moodModel =new MoodModel(id,"Depressed",10,date,time);
                dbHelper.updateMood(moodModel);
                Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show();

                holder.moodImage.setImageDrawable(context.getResources().getDrawable(R.drawable.depressed));
                holder.mood.setText("Depressed");
                holder.mood.setTextColor(context.getResources().getColor(R.color.depressed));
                holder.date.setText(date);
                holder.time.setText(time);

                alertDialog.cancel();
            }
        });

        builder.setCancelable(true)
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        // Toast.makeText(contex, "Menu Added in Schedule", Toast.LENGTH_SHORT).show();
//                    }
//                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mood,date,time;

        ImageView moodImage,dotMenu;
        CardView card;


        public ViewHolder(View itemView) {
            super(itemView);

            mood = itemView.findViewById(R.id.tvMood);
            date = itemView.findViewById(R.id.tvDate);
            time = itemView.findViewById(R.id.tvTime);

            moodImage = itemView.findViewById(R.id.ivMood);
            dotMenu = itemView.findViewById(R.id.dot_menu);
            card = itemView.findViewById(R.id.card_list);
        }

    }

    // Filter Class to filter and show the result in list view
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        data.clear();
        if (charText.length() == 0) {
            data.addAll(arraylist);
        } else {
            for (MoodModel wp : arraylist) {
                if (wp.getDate().toLowerCase(Locale.getDefault()).contains(charText)) {
                    data.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}
