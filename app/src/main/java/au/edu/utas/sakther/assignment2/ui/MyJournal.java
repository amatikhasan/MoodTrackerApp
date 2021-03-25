package au.edu.utas.sakther.assignment2.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import au.edu.utas.sakther.assignment2.R;
import au.edu.utas.sakther.assignment2.classes.JournalAdapter;
import au.edu.utas.sakther.assignment2.db.DBHelper;
import au.edu.utas.sakther.assignment2.model.JournalModel;

public class MyJournal extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    RecyclerView recyclerView;
    ArrayList<JournalModel> obj = new ArrayList<>();
    TextView emptyList;
    Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    JournalAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_journal);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Journal");

        drawerLayout = findViewById(R.id.drawer);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_viw);
        navigationView.setNavigationItemSelectedListener(this);

        emptyList=findViewById(R.id.emptyList);
        recyclerView=findViewById(R.id.rvList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DBHelper dbHelper = new DBHelper(getApplicationContext());
        obj=dbHelper.showJournalList();

        Log.d("check", "journalList: "+obj.size());


        if(obj.size()>0) {
            adapter = new JournalAdapter(this, obj);
            recyclerView.setAdapter(adapter);
        }
        else{
            recyclerView.setVisibility(View.GONE);
            emptyList.setVisibility(View.VISIBLE);
        }
    }

    public void fabBtn(View view){
        Intent intent=new Intent(this, CreateJournal.class);
//        finishAffinity();
        startActivity(intent);
//        finish();
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

    //For Action Bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_filter, menu);

        MenuItem item=menu.findItem(R.id.menuFilter);

        SearchView searchView= (SearchView) item.getActionView();
        searchView.setQueryHint("Search Journal");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

//                if (obj.size()>0)

                try {
                    adapter.filter(newText);
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
                return false;
            }
        });

        return true;
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
