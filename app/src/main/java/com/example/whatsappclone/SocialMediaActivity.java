package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;


import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class SocialMediaActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    //private TabAdapter tabAdapter;


    private ArrayList<String> whatsappuser;
    private ArrayAdapter arrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_media);

        setTitle("Whatsapp Clone");

        toolbar=findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);

//        viewPager=findViewById(R.id.viewPager);
//        tabAdapter=new TabAdapter(getSupportFragmentManager());
//        viewPager.setAdapter(tabAdapter);

//        tabLayout=findViewById(R.id.tabLayout);
//        tabLayout.setupWithViewPager(viewPager,false);


        final ListView listView= findViewById(R.id.viewUserList);
        listView.setOnItemClickListener(this);

        whatsappuser = new ArrayList();
        arrayAdapter=new ArrayAdapter(this, android.R.layout.simple_list_item_1,whatsappuser);

        final SwipeRefreshLayout mySwipeRefreshLayout = findViewById(R.id.swipeContainer);


        //listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        //listView.setOnItemClickListener(this);
        //listView.setOnItemLongClickListener(this);

        try {
            //final TextView txtLoadingUsers = findViewById(R.id.txtLoadingUsers);
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Users are fetching......!!");
            progressDialog.show();
            ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();

            parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());

            parseQuery.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> users, ParseException e) {
                    if (e == null && users.size() > 0) {
                        for (ParseUser user : users) {
                            whatsappuser.add(user.getUsername());
                        }

                        listView.setAdapter(arrayAdapter);
                        //txtLoadingUsers.animate().alpha(0).setDuration(1000);
                        listView.setVisibility(View.VISIBLE);
                        progressDialog.dismiss();

//                        for(String twitterUser:arrayList){
//                            if(ParseUser.getCurrentUser().getList("fanOf")!=null) {
//                                if (ParseUser.getCurrentUser().getList("fanOf").contains(twitterUser)) {
//
//                                    followedUser = followedUser + twitterUser +"\n";
//                                    listView.setItemChecked(arrayList.indexOf(twitterUser), true);
//                                    FancyToast.makeText(twitter_user.this, ParseUser.getCurrentUser().getUsername()+ " is following "+followedUser, Toast.LENGTH_SHORT,FancyToast.INFO,true).show();
//                                }
//                            }
//                        }
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

        mySwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try{
                    ParseQuery<ParseUser>  parseQuery = ParseUser.getQuery();
                    parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
                    parseQuery.whereNotContainedIn("username",whatsappuser);
                    parseQuery.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> objects, ParseException e) {
                            if(objects.size()>0 && e==null){
                                for(ParseUser user: objects)
                                    whatsappuser.add(user.getUsername());

                                arrayAdapter.notifyDataSetChanged();
                                if(mySwipeRefreshLayout.isRefreshing()){
                                    mySwipeRefreshLayout.setRefreshing(false);
                                }
                            }else if (mySwipeRefreshLayout.isRefreshing()){
                                mySwipeRefreshLayout.setRefreshing(false);
                            }

                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.my_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if(item.getItemId()==R.id.postImageItem){
//            if(Build.VERSION.SDK_INT>=23 && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
//                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},3000);
//            }else {
//                captureImage();
//            }
//        }else if(item.getItemId()==R.id.logoutUserItem){
        if(item.getItemId()==R.id.logoutUserItem){
            FancyToast.makeText(this,ParseUser.getCurrentUser().getUsername()+" is logged out.", Toast.LENGTH_SHORT,FancyToast.INFO,true).show();
            ParseUser.getCurrentUser().logOut();
            finish();
            Intent intent=new Intent(SocialMediaActivity.this, MainActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(SocialMediaActivity.this,WhatsappChat.class);
        intent.putExtra("selectedUser", whatsappuser.get(position));
        startActivity(intent);
    }


//    }
}
