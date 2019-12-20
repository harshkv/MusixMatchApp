package com.example.musixmatchapplication;
/*
Sahil Sood
Harshitha Keshavaraju Vijayalakshmi
Group 32
 */
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button buttonSearch;
    SeekBar seekBar;
    EditText et_name;
    TextView tv_limit,tv_Results;
    RadioGroup radioGroup;
    RadioButton rb_track,rb_artist;
    String rb_value="s_track_rating";
    ArrayList<Song> songList;
    ProgressBar progressBar;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonSearch = (Button) findViewById(R.id.buttonSearch);
        seekBar =(SeekBar) findViewById(R.id.seekBar);
        et_name =(EditText) findViewById(R.id.et_name);
        tv_limit =(TextView)  findViewById(R.id.tv_limit);
        tv_Results = (TextView) findViewById(R.id.tv_Results);
        radioGroup =(RadioGroup) findViewById(R.id.radioGroup);
        rb_track =(RadioButton) findViewById(R.id.rb_track);
        rb_artist =(RadioButton) findViewById(R.id.rb_artist);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        setTitle("MusixMatch Track Search");


        seekBar.setMax(25);
        seekBar.setProgress(5);
        rb_track.setChecked(true);
        tv_limit.setText("Limit: "+seekBar.getProgress());



        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBar.setProgress(progress);
                tv_limit.setText("Limit: "+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected()==true) {

                    final String name = et_name.getText().toString();
                    final String limit = String.valueOf(seekBar.getProgress());
                    if(name.equals("")){
                        et_name.setError("Please enter value");
                    }else {
                        String url = "http://api.musixmatch.com/ws/1.1/track.search?q="+name+"&"+rb_value+"=desc&page_size="+limit+"&apikey=76746932ac3c66cb3863bbe9d091853a";
                        new GetDataAsync().execute(url);
                        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                if(checkedId ==R.id.rb_track){
                                    rb_value = "s_track_rating";
                                    String url = "http://api.musixmatch.com/ws/1.1/track.search?q="+name+"&"+rb_value+"=desc&page_size="+limit+"&apikey=76746932ac3c66cb3863bbe9d091853a";
                                    new GetDataAsync().execute(url);
                                }
                                else if(checkedId ==R.id.rb_artist){
                                    rb_value = "s_track_artist";
                                    String url = "http://api.musixmatch.com/ws/1.1/track.search?q="+name+"&"+rb_value+"=desc&page_size="+limit+"&apikey=76746932ac3c66cb3863bbe9d091853a";
                                    new GetDataAsync().execute(url);
                                }

                            }

                        });
                    }


                } else {
                    Toast.makeText(MainActivity.this, "Not connected", Toast.LENGTH_SHORT).show();
                }}   });}


    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInformation = connectivityManager.getActiveNetworkInfo();

        if (networkInformation == null || !networkInformation.isConnected()){
            return false;
        }

        return true;

    }
    private class GetDataAsync extends AsyncTask<String, Void, ArrayList<Song>> {
        HttpURLConnection connection = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Song> doInBackground(String... strings) {
            songList = new ArrayList<>();
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");
                    JSONObject root = new JSONObject(json);
                    JSONObject message = root.getJSONObject("message");
                    JSONObject body = message.getJSONObject("body");
                    JSONArray tracks = body.getJSONArray("track_list");
                    for (int i=0;i<tracks.length();i++) {
                        JSONObject trackJson = tracks.getJSONObject(i);
                        JSONObject eachTrack = trackJson.getJSONObject("track");
                        Song song = new Song();
                        song.setAlbum_name(eachTrack.getString("album_name"));
                        song.setArtist_name(eachTrack.getString("artist_name"));
                        song.setDate(eachTrack.getString("updated_time"));
                        song.setTrack_name(eachTrack.getString("track_name"));
                        song.setUrl((eachTrack.getString("track_share_url")));
                        songList.add(song);


                }



            }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                //Close the connections
            }
            return songList;
        }

            @Override
        protected void onPostExecute(ArrayList<Song> songs) {
            super.onPostExecute(songs);
                progressBar.setVisibility(View.INVISIBLE);
                Log.i("demo", "size" + songs.size());
                if(songs.size() > 0) {
                    recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
                    recyclerView.setHasFixedSize(true);

                    layoutManager = new LinearLayoutManager(MainActivity.this);
                    recyclerView.setLayoutManager(layoutManager);
                    mAdapter = new SongAdapter(songs);
                    mAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(mAdapter);

                }else{
                    Toast.makeText(MainActivity.this, "No Search Result Found!", Toast.LENGTH_SHORT).show();

}



        }
    }


}


