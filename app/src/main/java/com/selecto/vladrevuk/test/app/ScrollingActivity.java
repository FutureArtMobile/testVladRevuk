package com.selecto.vladrevuk.test.app;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.selecto.vladrevuk.test.app.Classes.Constants;
import com.selecto.vladrevuk.test.app.Classes.Utils;

public class ScrollingActivity extends AppCompatActivity {

    public static final String ID = "ID";
    private static final int PHONE = 1;
    private static final int EMAIL = 2;
    private static final int ADDRESS = 3;
    String id;
    String name;
    String PhoneMobile, PhoneHome, PhoneWork, PhoneOther;
    String EmailHome, EmailWork, EmailOther;
    String AddressHome, AddressWork, AddressOther;
    String img;
    int favorite;

    RequestOptions options;

    ImageView avatar;
    FloatingActionButton favorite_but;
    LinearLayout LinearPhoneMobile, LinearPhoneHome,  LinearPhoneWork,  LinearPhoneOther;
    LinearLayout LinearEmailHome,  LinearEmailWork,  LinearEmailOther;
    LinearLayout LinearAddressHome,  LinearAddressWork,  LinearAddressOther;
    View viewEmail, viewAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent!=null){
            id = intent.getStringExtra(ID);
        }

        options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(false);


        Utils.checkPermission(this, Constants.PERMISSIONS_REQUIRED,Constants.PERMISSION_REQUEST_CODE,true);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (id!=null){
            Uri uri = ContactsContract.Contacts.CONTENT_URI;
            Cursor contact = getContentResolver().query(uri, null, ContactsContract.Contacts._ID + "="+id, null, null);
            contact.moveToFirst();

            name = contact.getString(contact.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            img = contact.getString(contact.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));
            favorite = contact.getInt(contact.getColumnIndex(ContactsContract.Contacts.STARRED));

            getPhones(contact);
            getEmail();
            getAddress();

        } else {
            finish();
        }

        getSupportActionBar().setTitle(name);

        avatar = findViewById(R.id.avatar);
        if (img!=null){
            avatar.setVisibility(View.VISIBLE);

            Glide.with(this)
                    .load(img)
                    .apply(options)
                    .into(avatar);

        } else {
            avatar.setVisibility(View.GONE);
        }


        favorite_but = (FloatingActionButton) findViewById(R.id.favorite_but);
        if (favorite==1){
            favorite_but.setColorFilter(ContextCompat.getColor(this,
                    R.color.yellow));
        } else if (favorite==0){
            favorite_but.setColorFilter(ContextCompat.getColor(this,
                    R.color.white));
        }

        favorite_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favorite = favorite==1?0:1;

                ContentValues contentValues = new ContentValues();
                contentValues.put(ContactsContract.Contacts.STARRED,favorite);

                getContentResolver().update(ContactsContract.Contacts.CONTENT_URI, contentValues , ContactsContract.Contacts._ID + " = ?", new String[]{id});

                if (favorite==1){
                    favorite_but.setColorFilter(ContextCompat.getColor(ScrollingActivity.this,
                            R.color.yellow));
                } else if (favorite==0){
                    favorite_but.setColorFilter(ContextCompat.getColor(ScrollingActivity.this,
                            R.color.white));
                }
            }
        });

        boolean ic_phone = true;

        if (PhoneMobile!=null){
            LinearPhoneMobile = findViewById(R.id.LinearPhoneMobile);
            setInform(LinearPhoneMobile, PhoneMobile, "Mobile", ic_phone, PHONE);
            ic_phone = false;
        }

        if (PhoneHome!=null){
            LinearPhoneHome = findViewById(R.id.LinearPhoneHome);
            setInform(LinearPhoneHome, PhoneHome, "Home", ic_phone, PHONE);
            ic_phone = false;
        }

        if (PhoneWork!=null){
            LinearPhoneWork = findViewById(R.id.LinearPhoneWork);
            setInform(LinearPhoneWork, PhoneWork, "Work", ic_phone, PHONE);
            ic_phone = false;
        }

        if (PhoneOther!=null){
            LinearPhoneOther = findViewById(R.id.LinearPhoneOther);
            setInform(LinearPhoneOther, PhoneOther, "Other", ic_phone, PHONE);
        }

        boolean ic_email = true;

        if (EmailWork!=null){
            LinearEmailWork = findViewById(R.id.LinearEmailWork);
            setInform(LinearEmailWork, EmailWork, "Work", ic_email, EMAIL);
            ic_email = false;
        }

        if (EmailHome!=null){
            LinearEmailHome = findViewById(R.id.LinearEmailHome);
            setInform(LinearEmailHome, EmailHome, "Home", ic_email, EMAIL);
            ic_email = false;
        }

        if (EmailOther!=null){
            LinearEmailOther = findViewById(R.id.LinearEmailOther);
            setInform(LinearEmailOther, EmailOther, "Other", ic_email, EMAIL);
        }

        boolean ic_address = true;

        if (AddressWork!=null){
            LinearAddressWork = findViewById(R.id.LinearAddressWork);
            setInform(LinearAddressWork, AddressWork, "Work", ic_address, ADDRESS);
            ic_address = false;
        }

        if (AddressHome!=null){
            LinearAddressHome = findViewById(R.id.LinearAddressHome);
            setInform(LinearAddressHome, AddressHome, "Home", ic_address, ADDRESS);
            ic_address = false;
        }

        if (AddressOther!=null){
            LinearAddressOther = findViewById(R.id.LinearAddressOther);
            setInform(LinearAddressOther, AddressOther, "Other", ic_address, ADDRESS);
        }
    }

    void getPhones(Cursor contact){
        if (Integer
                .parseInt(contact.getString(contact
                        .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
            Cursor phoneCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                            + " = ?", new String[] { id }, null);
            while (phoneCursor.moveToNext()) {
                int phoneType = phoneCursor
                        .getInt(phoneCursor
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                String phoneNumber = phoneCursor
                        .getString(phoneCursor
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                switch (phoneType) {
                    case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                        PhoneMobile = phoneNumber;
                        break;
                    case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                        PhoneHome = phoneNumber;
                        break;
                    case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                        PhoneWork = phoneNumber;
                        break;
                    case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER:
                        PhoneOther = phoneNumber;
                        break;
                    default:
                        break;
                }
            }
            phoneCursor.close();
        }
    }

    void getEmail(){
        Cursor emailCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Email.CONTACT_ID
                        + " = ?", new String[] { id }, null);
        while (emailCursor.moveToNext()) {
            int emailType = emailCursor
                    .getInt(emailCursor
                            .getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
            String email = emailCursor
                    .getString(emailCursor
                            .getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
            switch (emailType) {
                case ContactsContract.CommonDataKinds.Email.TYPE_HOME:
                    EmailHome = email;
                    break;
                case ContactsContract.CommonDataKinds.Email.TYPE_WORK:
                    EmailWork = email;
                    break;
                case ContactsContract.CommonDataKinds.Email.TYPE_OTHER:
                    EmailOther = email;
                    break;
                default:
                    break;
            }
        }
        emailCursor.close();
    }

    void getAddress(){
        String[] columns = new String[] { ContactsContract.CommonDataKinds.StructuredPostal.DATA, ContactsContract.CommonDataKinds.StructuredPostal.TYPE};
        String where = ContactsContract.Data.RAW_CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
        String[] whereParameters = new String[]{String.valueOf(id), ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE};
        Cursor contacts = getContentResolver().query(ContactsContract.Data.CONTENT_URI, columns, where, whereParameters, null);
        while (contacts.moveToNext()) {
            int addressType = contacts
                    .getInt(contacts
                            .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.TYPE));
            String address = contacts
                    .getString(contacts
                            .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.DATA));

            switch (addressType) {
                case ContactsContract.CommonDataKinds.Email.TYPE_HOME:
                    AddressHome = address;
                    break;
                case ContactsContract.CommonDataKinds.Email.TYPE_WORK:
                    AddressWork = address;
                    break;
                case ContactsContract.CommonDataKinds.Email.TYPE_OTHER:
                    AddressOther = address;
                    break;
                default:
                    break;
            }
        }


        contacts.close();
    }

    void setInform(LinearLayout layout, String title, String text, boolean icon, int type){
        layout.setVisibility(View.VISIBLE);
        TextView titileTV = layout.findViewWithTag("title");
        titileTV.setText(title);
        TextView textTV = layout.findViewWithTag("text");
        textTV.setText(text);
        if (icon){
            ImageView iconIV = layout.findViewWithTag("icon");
            switch (type){
                case PHONE :
                    iconIV.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_phone));
                    break;
                case EMAIL :
                    viewEmail = findViewById(R.id.viewEmail);
                    viewEmail.setVisibility(View.VISIBLE);
                    iconIV.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_email));
                    break;
                case ADDRESS :
                    viewAddress = findViewById(R.id.viewAddress);
                    viewAddress.setVisibility(View.VISIBLE);
                    iconIV.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_location));
                    break;
            }
        }
    }
}
