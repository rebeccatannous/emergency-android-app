package com.mobile.safetyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ContactsActivity extends AppCompatActivity implements ContactListFragment.contactlistInterface,  ContactDetailFragment.UpdateLayout {
    FrameLayout f1;
    FrameLayout f2;
    FrameLayout f3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        Intent intent = getIntent();
        Toolbar toolbar=findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
         f1=findViewById(R.id.contactlist_fragment);
        f2=findViewById(R.id.contact_framelayout);

        ContactListFragment contactlistFragment = new ContactListFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.contactlist_fragment, contactlistFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contactsmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        f1.setVisibility(View.GONE);
        f2.setVisibility(View.GONE);

        AddContactFragment addContactFragment=new AddContactFragment();
        LinearLayout linearLayout = findViewById(R.id.detail_container);
        linearLayout.setVisibility(View.GONE);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.contactadd_framelayout, addContactFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
        return super.onOptionsItemSelected(item);


    }

    @Override
    public void listItemClicked(long id) {
        ContactDetailFragment contactDetailFragment = new ContactDetailFragment();
        contactDetailFragment.setId(id);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.contact_framelayout, contactDetailFragment);
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();

    }

    @Override
    public void delete() {
        ContactListFragment contactListFragment = new ContactListFragment();
        FrameLayout contact_framelayout = findViewById(R.id.contact_framelayout);
        contact_framelayout.removeAllViews();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.contactlist_fragment, contactListFragment);
        ft.commit();
    }

    @Override
    public void updateContactList() {
        ContactListFragment contactListFragment = new ContactListFragment();
        FrameLayout contact_framelayout = findViewById(R.id.contact_framelayout);
        contact_framelayout.removeAllViews();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.contactlist_fragment, contactListFragment);
        ft.commit();
    }

    @Override
    public void send(String messageSent, String number, boolean location, boolean sms) {
        Intent intent = new Intent(ContactsActivity.this, MainActivity.class);
        intent.putExtra("sending", true);
        intent.putExtra("messageSent", messageSent);
        intent.putExtra("number", number);
        intent.putExtra("location", location);
        intent.putExtra("sms", sms);

        startActivity(intent);
    }
}