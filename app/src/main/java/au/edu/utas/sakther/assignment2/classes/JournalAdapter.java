package au.edu.utas.sakther.assignment2.classes;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import au.edu.utas.sakther.assignment2.R;
import au.edu.utas.sakther.assignment2.model.JournalModel;
import au.edu.utas.sakther.assignment2.ui.ViewJournal;


public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.ViewHolder> {
    Context context;
    ArrayList<JournalModel> data;
    private ArrayList<JournalModel> arraylist;

    public JournalAdapter(Context context, ArrayList<JournalModel> data) {
        this.context = context;
        this.data = data;
        this.arraylist = new ArrayList<JournalModel>();
        this.arraylist.addAll(data);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.journal_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final JournalModel obj = data.get(position);

        holder.title.setText(obj.getTitle());
        holder.body.setText(obj.getBody());

        String dateTime=obj.getDate()+" "+obj.getTime();

        holder.dateTime.setText(dateTime);

        if (!obj.getLocation().isEmpty()) {
//            holder.locationIcon.setVisibility(View.VISIBLE);
            holder.location.setText(obj.getLocation());
        }
        else {
//            holder.locationIcon.setVisibility(View.INVISIBLE);
            holder.location.setVisibility(View.INVISIBLE);
        }

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


        if (obj.getImage() != null) {
            byte[] decodedByte = obj.getImage();
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
            holder.image.setImageBitmap(bitmap);
        }

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ViewJournal.class);
                intent.putExtra("id", obj.getId());
                intent.putExtra("title", obj.getTitle());
                intent.putExtra("body", obj.getBody());
                intent.putExtra("mood", obj.getMood());
//                intent.putExtra("image", obj.getImage());
                intent.putExtra("location", obj.getLocation());
                intent.putExtra("tag", obj.getTag());
                intent.putExtra("date", obj.getDate());
                intent.putExtra("time", obj.getTime());


               // CreateJournal.bytes=obj.getImage();
                context.startActivity(intent);
//                ((Activity) context).finish();

            }
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, body, mood,dateTime,location;

        ImageView image, moodImage,locationIcon;
        CardView card;


        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvTitle);
            body = itemView.findViewById(R.id.tvBody);
            mood = itemView.findViewById(R.id.tvMood);
            dateTime = itemView.findViewById(R.id.tvDateTime);
            location = itemView.findViewById(R.id.tvLocation);
            image = itemView.findViewById(R.id.image);
            moodImage = itemView.findViewById(R.id.ivMood);
//            locationIcon=itemView.findViewById(R.id.location);
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
            for (JournalModel jd : arraylist) {
                if (jd.getTitle().toLowerCase(Locale.getDefault()).contains(charText)) {
                    data.add(jd);
                }
            }
        }
        notifyDataSetChanged();
    }

}
