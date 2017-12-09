package com.selecto.vladrevuk.test.app;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.selecto.vladrevuk.test.app.Adapters.MainActivityAdapter;
import com.selecto.vladrevuk.test.app.Classes.Constants;
import com.selecto.vladrevuk.test.app.Classes.Utils;
import com.selecto.vladrevuk.test.app.Structures.ContactStructure;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String LAYOUT_MANAGER_KEY = "POSITION";
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    MainActivityAdapter mAdapter;

    ArrayList<ContactStructure> myDataset = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        Utils.checkPermission(this, Constants.PERMISSIONS_REQUIRED,Constants.PERMISSION_REQUEST_CODE,true);

        updateForm();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateData();
    }

    void updateForm(){

        myDataset.clear();
        mRecyclerView.setAdapter(null);

        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        Cursor contacts = getContentResolver().query(uri, null, null, null, ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC");
        if (contacts.getCount() > 0) {
            while (contacts.moveToNext()) {
                String id = contacts.getString(contacts
                        .getColumnIndex(ContactsContract.Contacts._ID));
                String name = contacts
                        .getString(contacts
                                .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String img = contacts
                        .getString(contacts
                                .getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI));

                ContactStructure mContactStructure = new ContactStructure(id,img, name);
                myDataset.add(mContactStructure);

            }
        }

        mAdapter = new MainActivityAdapter(this, myDataset);
        mRecyclerView.setAdapter(mAdapter);

    }

    void updateData(){

        ArrayList<ContactStructure> newList = new ArrayList<>();
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        Cursor contacts = getContentResolver().query(uri, null, null, null, ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC");
        if (contacts.getCount() > 0) {
            while (contacts.moveToNext()) {
                String id = contacts.getString(contacts
                        .getColumnIndex(ContactsContract.Contacts._ID));
                String name = contacts
                        .getString(contacts
                                .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String img = contacts
                        .getString(contacts
                                .getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI));



                ContactStructure mContactStructure = new ContactStructure(id,img, name);
                newList.add(mContactStructure);
            }
        }

        if (myDataset.size() == newList.size()){

            for (int i=0; i<myDataset.size();i++){
                if (myDataset.get(i).get_id().equals(newList.get(i).get_id())){

                    boolean change = false;

                    if (!String.valueOf(myDataset.get(i).getName()).equals(String.valueOf(newList.get(i).getName()))){
                        myDataset.set(i,newList.get(i));
                        change = true;
                    } else if (!String.valueOf(myDataset.get(i).getImg()).equals(String.valueOf(newList.get(i).getImg()))){
                        myDataset.set(i,newList.get(i));
                        change = true;
                    }

                    if (change){
                        mAdapter.setItemToPostion(newList.get(i),i);
                    }

                } else {
                    updateForm();
                    break;
                }
            }

        } else {
            updateForm();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(LAYOUT_MANAGER_KEY, mRecyclerView.getLayoutManager().onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }
    
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        updateForm();
        if (savedInstanceState!=null) {
            Parcelable recyclerViewLayoutState = savedInstanceState.getParcelable(LAYOUT_MANAGER_KEY);
            mRecyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewLayoutState);
        }
        super.onRestoreInstanceState(savedInstanceState);

    }
}
