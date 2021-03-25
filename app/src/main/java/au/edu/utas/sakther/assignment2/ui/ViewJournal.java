package au.edu.utas.sakther.assignment2.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import au.edu.utas.sakther.assignment2.R;
import au.edu.utas.sakther.assignment2.db.DBHelper;

public class ViewJournal extends AppCompatActivity {

    TextView tvTitle,tvBody,tvMood,tvDateTime,tvLocation,tvTag;
    ImageView image,ivMood;
    byte[] byteArray;
    public static byte[] bytes;
    String mTitle, mBody, mLocation,mMood="Happy",mDate,mTime,mTag;
    int id;
    Bundle extras;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_journal);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Journal");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvTitle = findViewById(R.id.tvTitle);
        tvBody = findViewById(R.id.tvBody);
        tvLocation = findViewById(R.id.tvLocation);
        tvTag = findViewById(R.id.tvTag);
        tvDateTime = findViewById(R.id.tvDateTime);
        image = (ImageView) findViewById(R.id.ivItemImage);
        ivMood=findViewById(R.id.ivMood);
        tvMood=findViewById(R.id.tvMood);

        DBHelper dbHelper=new DBHelper(this);

        //get Intent Data
        extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getInt("id");
            mTitle = extras.getString("title");
            mBody = extras.getString("body");
            mLocation = extras.getString("location");
            mTag = extras.getString("tag");
            mDate=extras.getString("date");
            mTime=extras.getString("time");

            mMood = extras.getString("mood");

            byteArray=dbHelper.getByte(id);
//            byteArray=extras.getByteArray("image");
            // Log.d("check", "onCreate: "+byteArray);


            tvTitle.setText(mTitle);
            tvBody.setText(mBody);
            tvLocation.append(mLocation);
            tvTag.append(mTag);
            String mDateTime=mDate+" "+mTime;
            tvDateTime.setText(mDateTime);

            if(byteArray!=null) {
                //Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                image.setImageBitmap(bitmap);
                // btnDelete.setVisibility(View.VISIBLE);
            }

            if (mMood.equals("Very Happy")){
                ivMood.setImageDrawable(getResources().getDrawable(R.drawable.very_happy));
                tvMood.setText("Very Happy");
                tvMood.setTextColor(getResources().getColor(R.color.very_happy));
            }

            if (mMood.equals("Happy")){
                ivMood.setImageDrawable(getResources().getDrawable(R.drawable.happy));
                tvMood.setText("Happy");
                tvMood.setTextColor(getResources().getColor(R.color.happy));
            }

            if (mMood.equals("Neutral")){
                ivMood.setImageDrawable(getResources().getDrawable(R.drawable.neutral));
                tvMood.setText("Neutral");
                tvMood.setTextColor(getResources().getColor(R.color.neutral));
            }

            if (mMood.equals("Sad")){
                ivMood.setImageDrawable(getResources().getDrawable(R.drawable.sad));
                tvMood.setText("Sad");
                tvMood.setTextColor(getResources().getColor(R.color.sad));
            }

            if (mMood.equals("Depressed")){
                ivMood.setImageDrawable(getResources().getDrawable(R.drawable.depressed));
                tvMood.setText("Depressed");
                tvMood.setTextColor(getResources().getColor(R.color.depressed));
            }
        }
        Log.d("Extra Data Check", id + " " + mTitle );
    }

    public void deleteItem(){


        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this Journal?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int ids) {

                        DBHelper dbHelper=new DBHelper(getApplicationContext());

                        dbHelper.deleteJournal(id);
                        startActivity(new Intent(getApplicationContext(), MyJournal.class));
                        finishAffinity();

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

//        startActivity(new Intent(this, MyJournal.class));
        finish();
    }

    //For Action Bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_view_journal, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //for toolbar arrow
            case android.R.id.home:
                finish();
                break;
            case R.id.menuEdit:

                Intent intent = new Intent(this, CreateJournal.class);
                intent.putExtra("id", id);
                intent.putExtra("title", mTitle);
                intent.putExtra("body", mBody);
                intent.putExtra("mood", mMood);
//                intent.putExtra("image", byteArray);
                intent.putExtra("location", mLocation);
                intent.putExtra("tag", mTag);
                intent.putExtra("date", mDate);
                intent.putExtra("time", mTime);


                // CreateJournal.bytes=obj.getImage();
                 startActivity(intent);
                //Toast.makeText(getApplicationContext(), "Save Button Clicked", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menuDelete:
                deleteItem();
                //Toast.makeText(getApplicationContext(), "Delete Button Clicked", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menuShare:

                String text=mTitle+" \n"+
                        "mood: "+mMood+" \n"+
                        "date: "+mDate+" \n"+
                        mBody;
                Intent intent2=new Intent(android.content.Intent.ACTION_SEND);
                intent2.setType("text/plain");
                intent2.putExtra(android.content.Intent.EXTRA_SUBJECT,mTitle);
                intent2.putExtra(Intent.EXTRA_TITLE,mTitle);
                intent2.putExtra(Intent.EXTRA_TEXT,text);
                startActivity(Intent.createChooser(intent2,"Share Using"));

                break;

        }

        return true;
    }
}
