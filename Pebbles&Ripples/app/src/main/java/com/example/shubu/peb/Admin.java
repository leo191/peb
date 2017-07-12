package com.example.shubu.peb;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Admin extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ListView listView;
    UserAdapter userAdapter;
    AddUserAdapter addUserAdapter;
    UserLocalStore userLocalStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        listView=(ListView)findViewById(R.id.Admin_listview);
        userAdapter=new UserAdapter(this,R.layout.all_users_layout);
        addUserAdapter=new AddUserAdapter(this,R.layout.activity_register);
        userLocalStore = new UserLocalStore(this);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.all_users) {
            //opens all users in listview. tap a user to update or delete
            ServerRequests serverRequests=new ServerRequests(this);
            userAdapter.list.clear();
            userAdapter.notifyDataSetChanged();
            serverRequests.fetchAllUserInBackground(new GetUserCallback() {
                @Override
                public void done(User returnedUser) {

                }

                @Override
                public void done(String returnedUser) {
                    JSONObject jObject = null;
                    User user;
                    try {
                        jObject = new JSONObject(returnedUser);
                        JSONArray jsonArray = jObject.getJSONArray("user");
                        int count=0;
                        while(count<jsonArray.length()) {
                            JSONObject jsonObject = jsonArray.getJSONObject(count);
                            String name = jsonObject.getString("name");
                            String age = jsonObject.getString("age");
                            String username = jsonObject.getString("username");
                            String password = jsonObject.getString("password");
                            int intage = Integer.parseInt(age);
                            user = new User(name, intage, username, password);
                            userAdapter.add(user);
                            count++;
                        }userAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
            listView.setAdapter(userAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    User user=(User) userAdapter.getItem(position);
                    Intent intent=new Intent(Admin.this,EditUser.class);
                    intent.putExtra("User",user);
                    startActivity(intent);
                }
            });

        } else if (id == R.id.add_user) {


        } else if (id == R.id.late_comers) {

        } else if (id == R.id.logout) {

            userLocalStore.clearUserData();
            userLocalStore.setUserLoggedIn(false);

            startActivity(new Intent(this, Login.class));

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
