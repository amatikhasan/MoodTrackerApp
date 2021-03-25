package au.edu.utas.sakther.assignment2.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import au.edu.utas.sakther.assignment2.R;
import au.edu.utas.sakther.assignment2.classes.SpinnerAdapter;
import au.edu.utas.sakther.assignment2.db.DBHelper;
import au.edu.utas.sakther.assignment2.model.JournalModel;
import au.edu.utas.sakther.assignment2.model.MoodModel;

import static android.util.Log.e;

public class CreateJournal extends AppCompatActivity {
    boolean isUpdate = false;
    TextView tvMood;
    EditText title, body, location,tag;
    Spinner spMood;
    ImageView image,ivMood;
    byte[] byteArray;
    public static byte[] bytes;
    private static final int IMAGE_REQUEST = 1;
    private Uri filePath;
    String mTitle, mBody, mLocation,mMood="Very Happy",mDate,mTime,mTag;
    int id;
    DBHelper dbHelper;
    Bundle extras;
    Toolbar toolbar;

    private BottomSheetBehavior mBehavior;
    private BottomSheetDialog mBottomSheetDialog;
    private View bottom_sheet;

    ArrayList<String> moods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_journal);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Create Journal");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title = findViewById(R.id.etTitle);
        body = findViewById(R.id.etBody);
        location = findViewById(R.id.etLocation);
        tag = findViewById(R.id.etTag);
        image = (ImageView) findViewById(R.id.ivItemImage);
//        ivMood=findViewById(R.id.ivMood);
//        tvMood=findViewById(R.id.tvMood);
        spMood=findViewById(R.id.spMood);
        dbHelper=new DBHelper(getApplicationContext());


        bottom_sheet = findViewById(R.id.bottom_sheet);
        mBehavior = BottomSheetBehavior.from(bottom_sheet);

        moods=new ArrayList<>();
        moods.add("Very Happy");
        moods.add("Happy");
        moods.add("Neutral");
        moods.add("Sad");
        moods.add("Depressed");
        
        spMood.setAdapter(new SpinnerAdapter(this,R.layout.spinner_layout,moods));

        //get Intent Data
        extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getInt("id");
            mTitle = extras.getString("title");
            mBody = extras.getString("body");
            mLocation = extras.getString("location");
            mTag = extras.getString("tag");

            mMood = extras.getString("mood");

            byteArray=dbHelper.getByte(id);
//            byteArray=extras.getByteArray("image");
           // Log.d("check", "onCreate: "+byteArray);

            getSupportActionBar().setTitle("Update Journal");
            isUpdate = true;
            title.setText(mTitle);
            body.setText(mBody);
            location.setText(mLocation);
            tag.setText(mTag);

            if(byteArray!=null) {
                //Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                image.setImageBitmap(bitmap);
                // btnDelete.setVisibility(View.VISIBLE);
            }

            SpinnerAdapter adapter= (SpinnerAdapter) spMood.getAdapter();
            int position=adapter.getPosition(mMood);
            spMood.setSelection(position);

        }
        Log.d("Extra Data Check", id + " " + mTitle + " " + isUpdate);

        checkFilePermissions();


//        showBottomSheetDialog();

        spMood.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mMood = spMood.getSelectedItem().toString();

                spMood.setSelection(position);

                Log.d("Check", "Spinner: " + mMood);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void selectImage(View view) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View inflaterView = layoutInflater.inflate(R.layout.add_image_layout, null);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(inflaterView);

        final AlertDialog alertDialog = builder.create();


        ((View) inflaterView.findViewById(R.id.brows)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(), "Preview  clicked", Toast.LENGTH_SHORT).show();

                browsImages();
                alertDialog.cancel();

            }
        });

        ((View) inflaterView.findViewById(R.id.capture)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(), "Preview  clicked", Toast.LENGTH_SHORT).show();

                captureImage();
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

    public void browsImages() {


        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // special intent for Samsung file manager
        Intent sIntent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
        // if you want any file type, you can skip next line
        sIntent.putExtra("CONTENT_TYPE", "image/*");
        sIntent.addCategory(Intent.CATEGORY_DEFAULT);

        Intent chooserIntent;
        if (getPackageManager().resolveActivity(sIntent, 0) != null) {
            // it is device with samsung file manager
            chooserIntent = Intent.createChooser(sIntent, "Select File");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{intent});
        } else {
            chooserIntent = Intent.createChooser(intent, "Select File");
        }
        startActivityForResult(chooserIntent, IMAGE_REQUEST);


    }

    public void captureImage(){
        Intent intent=new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,0);
    }


    private void checkFilePermissions() {
        if (Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.M) {
            int permissionCheck = CreateJournal.this.checkSelfPermission("Manifest.permission.READ_EXTERNAL_STORAGE");
            permissionCheck += CreateJournal.this.checkSelfPermission("Manifest.permission.WRITE_EXTERNAL_STORAGE");
            if (permissionCheck != 0) {
                this.requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1001); //Any number
            }
        } else {
            Log.d("Check", "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }


    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 0 && resultCode == RESULT_OK && data != null){
            try {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            image.setImageBitmap(bitmap);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 25, stream);
            byteArray = stream.toByteArray();
            // bitmap.recycle();

                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.d("check", "onActivityResult: " + image);

        }

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                image.setImageBitmap(bitmap);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 25, stream);
                byteArray = stream.toByteArray();

                long length=byteArray.length;

               // bitmap.recycle();
                stream.close();

                File img=new File(String.valueOf(filePath));
                long size=img.length();

                Log.d("check", "onActivityResult: " +size+" "+length);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void initBottomSheet(View view){
        showBottomSheetDialog();
    }

    private void showBottomSheetDialog() {
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

                ivMood.setImageDrawable(getResources().getDrawable(R.drawable.very_happy));
                tvMood.setText("Very Happy");
                tvMood.setTextColor(getResources().getColor(R.color.very_happy));

                mMood="Very Happy";

                mBottomSheetDialog.cancel();
            }
        });

        ((View) view.findViewById(R.id.mood_happy)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivMood.setImageDrawable(getResources().getDrawable(R.drawable.happy));
                tvMood.setText("Happy");
                tvMood.setTextColor(getResources().getColor(R.color.happy));

                mMood="Happy";

                mBottomSheetDialog.cancel();
            }
        });

        ((View) view.findViewById(R.id.mood_neutral)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivMood.setImageDrawable(getResources().getDrawable(R.drawable.neutral));
                tvMood.setText("Neutral");
                tvMood.setTextColor(getResources().getColor(R.color.neutral));

                mMood="Neutral";

                mBottomSheetDialog.cancel();
            }
        });

        ((View) view.findViewById(R.id.mood_sad)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivMood.setImageDrawable(getResources().getDrawable(R.drawable.sad));
                tvMood.setText("Sad");
                tvMood.setTextColor(getResources().getColor(R.color.sad));

                mMood="Sad";

                mBottomSheetDialog.cancel();
            }
        });

        ((View) view.findViewById(R.id.mood_depressed)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivMood.setImageDrawable(getResources().getDrawable(R.drawable.depressed));
                tvMood.setText("Depressed");
                tvMood.setTextColor(getResources().getColor(R.color.depressed));

                mMood="Depressed";

                mBottomSheetDialog.cancel();
            }
        });


    }

    public void addList(){

        // Get the calander
        Calendar c = Calendar.getInstance();

        String date,time;
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        SimpleDateFormat stf = new SimpleDateFormat("h:mm a");
        Date now = c.getTime();
        mDate = sdf.format(now);
        mTime=stf.format(now);

        Log.d("check", "date time: "+mDate+" "+mTime);

        mTitle = title.getText().toString().trim();
        mBody = body.getText().toString().trim();
        mLocation = location.getText().toString().trim();
        mTag = tag.getText().toString().trim();

        if (!mTitle.isEmpty()&&!mBody.isEmpty()) {


            if (isUpdate) {
                JournalModel journalModel = new JournalModel(id, mTitle, mBody, mMood, byteArray, mDate, mTime, mLocation,mTag);
                int id = dbHelper.updateList(journalModel);
                Log.d("check", "addList: " + id);
            } else {
                JournalModel journalModel = new JournalModel(mTitle, mBody, mMood, byteArray, mDate, mTime, mLocation,mTag);

                MoodModel moodModel = null;
                if (mMood.equals("Very Happy")) {
                     moodModel = new MoodModel(mMood, 50, mDate, mTime);
                }
                if (mMood.equals("Happy")) {
                    moodModel = new MoodModel(mMood, 40, mDate, mTime);
                }
                if (mMood.equals("Neutral")) {
                    moodModel = new MoodModel(mMood, 30, mDate, mTime);
                }
                if (mMood.equals("Sad")) {
                    moodModel = new MoodModel(mMood, 20, mDate, mTime);
                }
                if (mMood.equals("Depressed")) {
                    moodModel = new MoodModel(mMood, 10, mDate, mTime);
                }

                int id = dbHelper.insertList(journalModel);
                int id2 = dbHelper.insertMood(moodModel);

                Log.d("check", "addList: " + id);
            }

            Log.d("check", "addList 2: " + id);

            startActivity(new Intent(this, MyJournal.class));
            finishAffinity();
        }
        else {
            Toast.makeText(this, "Required fields are missing!", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteItem(){


        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this Journal?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int ids) {

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
        if (isUpdate) getMenuInflater().inflate(R.menu.menu_update, menu);
        if (!isUpdate) {
            getMenuInflater().inflate(R.menu.menu_add, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //for toolbar arrow
            case android.R.id.home:
//                startActivity(new Intent(getApplicationContext(), MyJournal.class));
                finish();
                break;
            case R.id.menuSave:
               addList();
                //Toast.makeText(getApplicationContext(), "Save Button Clicked", Toast.LENGTH_SHORT).show();
                break;


        }

        return true;
    }
}
