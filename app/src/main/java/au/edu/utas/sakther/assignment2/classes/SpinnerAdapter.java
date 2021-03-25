package au.edu.utas.sakther.assignment2.classes;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import au.edu.utas.sakther.assignment2.R;

public class SpinnerAdapter extends ArrayAdapter<String> {

    ArrayList<String> moods;
    Context context;

    public SpinnerAdapter(Context context, int resourceId, ArrayList<String> moods){
        super(context,resourceId,moods);

        this.moods=moods;
        this.context=context;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=((Activity) context).getLayoutInflater();
        View row=inflater.inflate(R.layout.spinner_layout,parent,false);

        TextView tvMood=row.findViewById(R.id.tvMood);
        ImageView ivMood=row.findViewById(R.id.ivMood);

        tvMood.setText(moods.get(position));

        if (moods.get(position).equals("Very Happy")){
            ivMood.setImageDrawable(context.getResources().getDrawable(R.drawable.very_happy));
            tvMood.setText("Very Happy");
            tvMood.setTextColor(context.getResources().getColor(R.color.very_happy));
        }

        if (moods.get(position).equals("Happy")){
            ivMood.setImageDrawable(context.getResources().getDrawable(R.drawable.happy));
            tvMood.setText("Happy");
            tvMood.setTextColor(context.getResources().getColor(R.color.happy));
        }
        if (moods.get(position).equals("Neutral")){
            ivMood.setImageDrawable(context.getResources().getDrawable(R.drawable.neutral));
            tvMood.setText("Neutral");
            tvMood.setTextColor(context.getResources().getColor(R.color.neutral));
        }
        if (moods.get(position).equals("Sad")){
            ivMood.setImageDrawable(context.getResources().getDrawable(R.drawable.sad));
            tvMood.setText("Sad");
            tvMood.setTextColor(context.getResources().getColor(R.color.sad));
        }
        if (moods.get(position).equals("Depressed")){
            ivMood.setImageDrawable(context.getResources().getDrawable(R.drawable.depressed));
            tvMood.setText("Depressed");
            tvMood.setTextColor(context.getResources().getColor(R.color.depressed));
        }

        return row;
    }
}
