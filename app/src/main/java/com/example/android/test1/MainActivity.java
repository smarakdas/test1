package com.example.android.test1;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    ArrayAdapter<String> itemsAdapter;
    Dbhelper myDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button)findViewById(R.id.button);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new task().execute("http://helpme.naitiksakhiya.in/model/happy_moments.php?action=getMoments");
                    }
                });
        task test = new task();
        test.execute();
       itemsAdapter =
                new ArrayAdapter<String>(
                        this,
                        R.layout.textview,
                        R.id.text1);


        ListView listView = (ListView)findViewById(R.id.list_item);
        listView.setAdapter(itemsAdapter);
    }

    public class task extends AsyncTask<String,Void,String[]>{

        @Override
        protected String[] doInBackground(String... params) {


            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;


            String dataJsonStr = null;


            try {

                URL url = new URL(params[0]);



                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {

                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {

                    return null;
                }
                dataJsonStr = buffer.toString();

            } catch (IOException e) {

                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {

                    }
                }
            }

            String[] result= null;
            try {

                JSONObject objJson = new JSONObject(dataJsonStr);
                JSONArray mainArr = objJson.getJSONArray("data");
                for (int i = 0; i < mainArr.length(); i++) {
                    JSONObject jsonObjectData = mainArr.getJSONObject(i);
                    String dataName = jsonObjectData.getString("name");
                    String dataSubject = jsonObjectData.getString("sub");
                    String dataMsg = jsonObjectData.getString("msg");
                    String dataTime = jsonObjectData.getString("time");
                    String dataImage = jsonObjectData.getString("img");
                    String finall = dataName + "/n" + dataSubject + "/n" + "dataMsg" + "/n" + dataTime + "/n" + dataImage + "/n";
                     myDb.insertData(dataName, dataSubject,dataMsg,dataTime,dataImage);
                    result[i] =finall;
                }

            return  result;
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;


        }

        @Override
        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);
            if (result != null) {
                itemsAdapter.clear();
                for (String s : result)
                    itemsAdapter.add(s);
            }
        }

}

}