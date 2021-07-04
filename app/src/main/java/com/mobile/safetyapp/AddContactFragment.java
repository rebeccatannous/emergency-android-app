package com.mobile.safetyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AddContactFragment extends Fragment {

    View view;

    public AddContactFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();

        view = getView();
        addContact();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_addcontactfragment, container, false);
    }


    public void addContact() {

        Button addbtn = view.findViewById(R.id.add_newcontact);
        addbtn.setOnClickListener(new View.OnClickListener() {
            SQLiteDatabase db;

            @Override
            public void onClick(View v) {
                try {
                    FrameLayout f2 = view.findViewById(R.id.contactlist_fragment);
                    FrameLayout f3 = view.findViewById(R.id.contact_framelayout);

                    SQLiteOpenHelper sqLiteOpenHelper = new ContactSQLiteOpenHelper(getContext());
                    db = sqLiteOpenHelper.getWritableDatabase();

                    EditText nameeditText = view.findViewById(R.id.add_name);
                    EditText phoneeditText = view.findViewById(R.id.add_phonenumber);


                    String newname = nameeditText.getText().toString();
                    String newphone = phoneeditText.getText().toString();

                    ContentValues content = new ContentValues();
                    String namewithoutspaces = newname.replaceAll(" ", "");
                    String phonewithoutspace = newphone.replaceAll(" ", "");
                    if (namewithoutspaces.equals("") || phonewithoutspace.equals("")) {
                        Toast.makeText(getContext(), "Fill empty fields", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    try {
                        int num = Integer.parseInt(phonewithoutspace);
                    } catch (NumberFormatException e) {
                        Toast.makeText(getContext(), "Enter Valid credentials", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    content.put("NAME", newname);
                    content.put("PHONE", phonewithoutspace);
                    db.insert("CONTACTS", null, content);

                    //f3.setVisibility(View.VISIBLE);
                    //f2.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "Contact Added", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);


                } catch (Exception e) {
                    Toast.makeText(getContext(), "Database unavailable.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}