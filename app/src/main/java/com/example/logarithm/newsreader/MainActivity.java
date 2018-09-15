package com.example.logarithm.newsreader;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    ListView listView;
    ArrayAdapter arrayAdapter;
    ArrayList<String> newslist,idList;
    JSONDownloader task;
    String json,json1;
    JSONArray jsonArray;
    Intent intent;



    class JSONDownloader extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... urls) {
            URL url;
            try {
                HttpURLConnection connection;
                url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                InputStream ip = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(ip);
                int data = reader.read();
                String result = "";
                while (data != -1) {
                    char ele = (char) data;
                    result = result + ele;
                    data = reader.read();
                }
                return result;
            } catch (Exception e) {

                e.printStackTrace();
                return null;
            }


        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        listView = findViewById(R.id.listview);
        newslist = new ArrayList<String>();
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, newslist);
        listView.setAdapter(arrayAdapter);
        try {
            task = new JSONDownloader();
             json=task.execute("https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty").get();
             jsonArray=new JSONArray(json);


        } catch (Exception e) {

            Log.i("The Exception is  ", e.toString());
            }
            idList=new ArrayList<String>();
            try{
                for(int i=0;i<20;i++) {
                    JSONDownloader task2 = new JSONDownloader();
                    json1 = task2.execute("https://hacker-news.firebaseio.com/v0/item/" + jsonArray.get(i) + ".json?print=pretty").get();
                    JSONObject jsonObject = new JSONObject(json1);
                    String url;
                    String title = jsonObject.getString("title");
                    newslist.add(title);
                    arrayAdapter.notifyDataSetChanged();

                }

            }catch (Exception e){
            e.printStackTrace();
            }
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        int itemid = jsonArray.getInt(position);
                        String url;
                        JSONDownloader task3=new JSONDownloader();
                        String json3=task3.execute("https://hacker-news.firebaseio.com/v0/item/" + String.valueOf(itemid) + ".json?print=pretty").get();
                        JSONObject jsonObject=new JSONObject(json3);
                        url=jsonObject.getString("url");
                        Log.i("URL",url);
                        intent=new Intent(getApplicationContext(),WebShower.class);
                        intent.putExtra("url",url);
                        startActivity(intent);

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });



    }
}
