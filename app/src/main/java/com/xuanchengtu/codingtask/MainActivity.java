//Xuancheng Tu's Skillion coding task

package com.xuanchengtu.codingtask;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Collectors;

import org.json.*;

/*
    This class defines the content of main page. The main page contains a list that displays the
    brief information about each user in "name  (company name)" format.
 */
public class MainActivity extends AppCompatActivity {

    private String jsonRaw; //store the content of json file fetched from provided URL

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Users");

        //Thread that fetches the json file at runtime
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try  {
                    String stringUrl = "https://jsonplaceholder.typicode.com/users";
                    try {
                        InputStream inputStream = new URL(stringUrl).openStream();
                        URL url = new URL(stringUrl);
                        HttpURLConnection yc = (HttpURLConnection) url.openConnection();
                        yc.connect();
                        InputStream in = new BufferedInputStream(yc.getInputStream());
                        jsonRaw = new BufferedReader(new InputStreamReader(in))
                                .lines().collect(Collectors.joining("\n"));
                    }
                    catch(MalformedURLException e){
                        Log.e("Error","Error while fetching data!");
                        return;
                    }
                    catch(IOException e){
                        Log.e("Error","Error while writing to local file!");
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        });
        thread.start();

        //Ensure that data fetching is done before program proceed to next step
        try {
            thread.join();
        }
        catch(InterruptedException e){
            Log.e("Error","Error while joining threads!");
        }

        //Parsing json file
        JSONArray ja;
        try {
            ja = new JSONArray(jsonRaw);
        }
        catch(JSONException e){
            Log.e("Error","Error parsing users.json!");
            return;
        }

        //Store the data of all users in a User array
        User[] users = new User[ja.length()];
        for(int i = 0; i < ja.length(); i++){
            try {
                users[i] = new User();
                users[i].address = new Address();
                JSONObject jsonUser = (JSONObject) ja.get(i);
                users[i].name = (String) jsonUser.get("name");
                JSONObject jsonCompany = (JSONObject) jsonUser.get("company");
                users[i].companyName = (String) jsonCompany.get("name");
                users[i].email = (String) jsonUser.get("email");
                JSONObject jsonAddr = (JSONObject) jsonUser.get("address");
                users[i].address.street = (String) jsonAddr.get("street");
                users[i].address.suite = (String) jsonAddr.get("suite");
                users[i].address.city = (String) jsonAddr.get("city");
                users[i].address.zipcode = (String) jsonAddr.get("zipcode");
                JSONObject jsonGeo = (JSONObject) jsonAddr.get("geo");
                users[i].address.lat = (String) jsonGeo.get("lat");
                users[i].address.lng = (String) jsonGeo.get("lng");
            }
            catch (JSONException e){
                Log.e("Error","Error while reading json data!");
                return;
            }
        }

        //listEntry contains the brief information about users in the format "name (company name)",
        //which will be displayed on the list.
        String[] listEntry = new String[ja.length()];
        for(int i = 0; i < ja.length(); i++){
            listEntry[i] = users[i].name + "  (" + users[i].companyName + ")";
        }

        ListView userList = (ListView) findViewById(R.id.userList);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listEntry);
        userList.setAdapter(arrayAdapter);
        Context temp = this;

        //define action after one list entry is clicked.
        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(temp, DetailActivity.class);
                //save necessary information needed to be displayed on detail page
                String address = users[position].address.street + "\n" + users[position].address.suite + "\n" + users[position].address.city + "\n" + users[position].address.zipcode;
                String[] attrs = new String[]{users[position].name, users[position].companyName, users[position].email, address, users[position].address.lat, users[position].address.lng};
                i.putExtra("attrs", attrs);
                startActivity(i);
            }
        });
    }
}