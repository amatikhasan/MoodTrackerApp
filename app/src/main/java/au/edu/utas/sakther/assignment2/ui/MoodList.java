package au.edu.utas.sakther.assignment2.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import au.edu.utas.sakther.assignment2.R;
import au.edu.utas.sakther.assignment2.classes.MoodAdapter;
import au.edu.utas.sakther.assignment2.db.DBHelper;
import au.edu.utas.sakther.assignment2.model.MoodModel;

public class MoodList extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    RecyclerView recyclerView;
    ArrayList<MoodModel> obj ;
    MoodAdapter adapter;
    TextView emptyList;
    Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    private BottomSheetBehavior mBehavior;
    private BottomSheetDialog mBottomSheetDialog;
    private View bottom_sheet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_list);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Mood List");

        drawerLayout = findViewById(R.id.drawer);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_viw);
        navigationView.setNavigationItemSelectedListener(this);

        obj = new ArrayList<>();

        emptyList=findViewById(R.id.emptyList);
        recyclerView=findViewById(R.id.rvList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        bottom_sheet = findViewById(R.id.bottom_sheet);
        mBehavior = BottomSheetBehavior.from(bottom_sheet);

        DBHelper dbHelper = new DBHelper(getApplicationContext());
        obj=dbHelper.showMoodList();

        adapter = new MoodAdapter(this, obj);
        recyclerView.setAdapter(adapter);


        if(obj.size()==0) {
//            recyclerView.setVisibility(View.GONE);
            emptyList.setVisibility(View.VISIBLE);
        }

    }

    public void fabBtn(View view){
//        showBottomSheetDialog();
        showMoodDialog();
    }

    private void showBottomSheetDialog() {

        // Get the calander
        Calendar c = Calendar.getInstance();

        final String date,time;
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        SimpleDateFormat stf = new SimpleDateFormat("h:mm a");
        Date now = c.getTime();
        date = sdf.format(now);
        time=stf.format(now);

        Log.d("check", "date time: "+date+" "+time);

        final DBHelper dbHelper = new DBHelper(getApplicationContext());

        if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

        final View view = getLayoutInflater().inflate(R.layout.sheet_list, null);

        mBottomSheetDialog = new BottomSheetDialog(this);
        mBottomSheetDialog.setContentView(view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        mBottomSheetDialog.show();
        mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mBottomSheetDialog = null;
            }
        });

        ((View) view.findViewById(R.id.mood_very_happy)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(), "Preview  clicked", Toast.LENGTH_SHORT).show();

                MoodModel moodModel =new MoodModel("Very Happy",50,date,time);
                dbHelper.insertMood(moodModel);

                obj=dbHelper.showMoodList();
                adapter = new MoodAdapter(getApplicationContext(), obj);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                mBottomSheetDialog.cancel();

                emptyList.setVisibility(View.GONE);
            }
        });

        ((View) view.findViewById(R.id.mood_happy)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MoodModel moodModel =new MoodModel("Happy",40,date,time);
                dbHelper.insertMood(moodModel);

                obj=dbHelper.showMoodList();
                Log.d("check", "happy: "+obj.size());

                adapter = new MoodAdapter(getApplicationContext(), obj);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                mBottomSheetDialog.cancel();
                emptyList.setVisibility(View.GONE);
            }
        });

        ((View) view.findViewById(R.id.mood_neutral)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MoodModel moodModel =new MoodModel("Neutral",30,date,time);
                dbHelper.insertMood(moodModel);

                obj=dbHelper.showMoodList();
                adapter = new MoodAdapter(getApplicationContext(), obj);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                mBottomSheetDialog.cancel();
                emptyList.setVisibility(View.GONE);
            }
        });

        ((View) view.findViewById(R.id.mood_sad)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MoodModel moodModel =new MoodModel("Sad",20,date,time);
                dbHelper.insertMood(moodModel);

                obj=dbHelper.showMoodList();
                adapter = new MoodAdapter(getApplicationContext(), obj);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                mBottomSheetDialog.cancel();
                emptyList.setVisibility(View.GONE);
            }
        });

        ((View) view.findViewById(R.id.mood_depressed)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MoodModel moodModel =new MoodModel("Depressed",10,date,time);
                dbHelper.insertMood(moodModel);

                obj=dbHelper.showMoodList();
                adapter = new MoodAdapter(getApplicationContext(), obj);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                mBottomSheetDialog.cancel();
                emptyList.setVisibility(View.GONE);
            }
        });
    }

    public void showMoodDialog(){
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.add_mood_layout, null);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
        final DBHelper dbHelper = new DBHelper(getApplicationContext());


        ((View) view.findViewById(R.id.mood_very_happy)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(), "Preview  clicked", Toast.LENGTH_SHORT).show();

                moodData[0] =new MoodModel("Very Happy",100,date,time);

                dbHelper.insertMood(moodData[0]);
                obj=dbHelper.showMoodList();
                adapter = new MoodAdapter(MoodList.this, obj);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                emptyList.setVisibility(View.GONE);

                alertDialog.cancel();

            }
        });

        ((View) view.findViewById(R.id.mood_happy)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               moodData[0] =new MoodModel("Happy",80,date,time);

                dbHelper.insertMood(moodData[0]);
                obj=dbHelper.showMoodList();
                adapter = new MoodAdapter(MoodList.this, obj);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                emptyList.setVisibility(View.GONE);

                alertDialog.cancel();

            }
        });

        ((View) view.findViewById(R.id.mood_neutral)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                moodData[0] =new MoodModel("Neutral",60,date,time);

                dbHelper.insertMood(moodData[0]);
                obj=dbHelper.showMoodList();
                adapter = new MoodAdapter(MoodList.this, obj);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                emptyList.setVisibility(View.GONE);

                alertDialog.cancel();

            }
        });

        ((View) view.findViewById(R.id.mood_sad)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                moodData[0] =new MoodModel("Sad",40,date,time);

                dbHelper.insertMood(moodData[0]);
                obj=dbHelper.showMoodList();
                adapter = new MoodAdapter(MoodList.this, obj);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                emptyList.setVisibility(View.GONE);

                alertDialog.cancel();

            }
        });

        ((View) view.findViewById(R.id.mood_depressed)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                moodData[0] =new MoodModel("Depressed",20,date,time);

                dbHelper.insertMood(moodData[0]);
                obj=dbHelper.showMoodList();
                adapter = new MoodAdapter(MoodList.this, obj);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                emptyList.setVisibility(View.GONE);

                alertDialog.cancel();

            }
        });



        builder.setCancelable(true)
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_journal_list) {
            startActivity(new Intent(this, MyJournal.class));
            finish();
        }

        if (id == R.id.menu_Mood_list) {
            startActivity(new Intent(this, MoodList.class));
            finish();
        }
        if (id == R.id.menu_chart) {
            startActivity(new Intent(this, MoodTracker.class));
//            finish();
        }


        return false;
    }

    @Override
    public void onBackPressed() {

        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to Exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {

                        finish();
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
}
