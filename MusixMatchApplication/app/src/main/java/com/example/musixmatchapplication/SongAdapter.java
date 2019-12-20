package com.example.musixmatchapplication;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.viewHolder>{
    ArrayList<Song> sdata;
    public SongAdapter(ArrayList<Song> sdata) {
        this.sdata = sdata;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song_item, parent, false);
        viewHolder viewHolder = new viewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Song song = sdata.get(position);
        if(song.album_name.equals("null")) {
            holder.tv_album.setText("Album: No Information");
        }else{
            holder.tv_album.setText("Album: " +song.album_name);
        }
        if(song.artist_name.equals("null")) {
            holder.tv_artist.setText("Artist: No Information");
        }else{
            holder.tv_artist.setText("Artist: "+song.artist_name);
        }
        if(song.track_name.equals("null")) {
            holder.tv_track.setText("Track: No Information");
        }else{
            holder.tv_track.setText("Track: "+song.track_name);
        }
        if(song.date.equals("null")) {
            holder.tv_date.setText("Date: No Information");
        }else{
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
            String strDate = formatter.format(date);
            holder.tv_date.setText("Date: "+strDate);
        }
        holder.song = song;

    }

    @Override
    public int getItemCount() {
        return sdata.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder{
        TextView tv_artist, tv_album, tv_date,tv_track;
        Song song;




        public viewHolder(@NonNull final View itemView) {
            super(itemView);
            tv_artist = itemView.findViewById(R.id.tv_artist);
            tv_album = itemView.findViewById(R.id.tv_album);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_track = itemView.findViewById(R.id.tv_trackname);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = song.url;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    itemView.getContext().startActivity(i);
                }
            });
        }
    }
}

