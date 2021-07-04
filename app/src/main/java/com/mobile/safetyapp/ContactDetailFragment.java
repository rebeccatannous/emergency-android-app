package com.mobile.safetyapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ContactDetailFragment extends Fragment {
    private long contactid;
    private UpdateLayout update;

    public ContactDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        listAttributes(contactid);
        View view = getView();
        deleteContact(view);
        editContact(view);
        sendMessage(view);
    }

    public void setId(long id) {
        this.contactid = id;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.update = (UpdateLayout) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            contactid = savedInstanceState.getLong("Contact id");
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_contact_detail_fragment, container, false);
    }


    public void sendMessage(View view) {

        Button wtsbtn = view.findViewById(R.id.whatsapp_button);
        Button smsbtn = view.findViewById(R.id.sms_button);
        wtsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText messageEditText = view.findViewById(R.id.contactmessage_editText);
                TextView phoneNumber = view.findViewById(R.id.contact_number_textview);
                CheckBox checkBox = view.findViewById(R.id.location_checkbox);
                String message = messageEditText.getText().toString();
                String phone = phoneNumber.getText().toString();
                update.send(message, phone, checkBox.isChecked(), false);
            }
        });
        smsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText messageEditText = view.findViewById(R.id.contactmessage_editText);
                TextView phoneNumber = view.findViewById(R.id.contact_number_textview);
                CheckBox checkBox = view.findViewById(R.id.location_checkbox);
                String message = messageEditText.getText().toString();
                String phone = phoneNumber.getText().toString();
                update.send(message, phone, checkBox.isChecked(), true);
            }
        });
    }

    public void deleteContact(View view) {

        Button deleteBtn = view.findViewById(R.id.delete_button);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            SQLiteDatabase db;

            @Override
            public void onClick(View v) {
                try {
                    SQLiteOpenHelper sqLiteOpenHelper = new ContactSQLiteOpenHelper(getContext());
                    db = sqLiteOpenHelper.getReadableDatabase();
                    db.delete("CONTACTS",
                            "_id= ?", new String[]{Long.toString(contactid)});
                    update.delete();
                    Toast.makeText(getContext(), "Contact Deleted", Toast.LENGTH_SHORT).show();


                } catch (Exception e) {
                    Toast.makeText(getContext(), "Database unavailable.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void editContact(View view) {

        Button updateBtn = view.findViewById(R.id.update_button);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            SQLiteDatabase db;

            @Override
            public void onClick(View v) {
                try {
                    SQLiteOpenHelper sqLiteOpenHelper = new ContactSQLiteOpenHelper(getContext());
                    db = sqLiteOpenHelper.getWritableDatabase();

                    TextView nametxtview = view.findViewById(R.id.contact_name_textview);
                    TextView phonetxtview = view.findViewById(R.id.contact_number_textview);
                    EditText nameeditText = view.findViewById(R.id.contactname_edittext);
                    EditText phoneeditText = view.findViewById(R.id.phonenumber_edittext);
                    EditText contactmsg = view.findViewById(R.id.contactmessage_editText);
                    LinearLayout options = view.findViewById(R.id.send_options_layout);
                    Button edit = view.findViewById(R.id.update_button);
                    Button delete = view.findViewById(R.id.delete_button);
                    edit.setVisibility(View.GONE);

                    options.setVisibility(View.GONE);
                    contactmsg.setVisibility(View.GONE);
                    phonetxtview.setVisibility(View.GONE);
                    nametxtview.setVisibility(View.GONE);
                    nameeditText.setVisibility(View.VISIBLE);
                    phoneeditText.setVisibility(View.VISIBLE);
                    delete.setVisibility(View.GONE);
                    Button button = view.findViewById(R.id.save_changes);
                    button.setVisibility(View.VISIBLE);

                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String newname = nameeditText.getText().toString();
                            String newphone = phoneeditText.getText().toString();
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
                            ContentValues content = new ContentValues();
                            content.put("NAME", newname);
                            content.put("PHONE", phonewithoutspace);
                            db.update("CONTACTS", content,
                                    "_id= ?", new String[]{Long.toString(contactid)});

                            nametxtview.setText(newname);
                            phonetxtview.setText(newphone);
                            edit.setVisibility(View.VISIBLE);
                            edit.setVisibility(View.VISIBLE);
                            options.setVisibility(View.VISIBLE);
                            contactmsg.setVisibility(View.VISIBLE);
                            phonetxtview.setVisibility(View.VISIBLE);
                            nametxtview.setVisibility(View.VISIBLE);
                            nameeditText.setVisibility(View.GONE);
                            phoneeditText.setVisibility(View.GONE);
                            button.setVisibility(View.GONE);
                            delete.setVisibility(View.VISIBLE);
                            Toast.makeText(getContext(), "Contact Edited", Toast.LENGTH_SHORT).show();
                            update.updateContactList();
                        }
                    });


                } catch (Exception e) {
                    Toast.makeText(getContext(), "Database unavailable.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void listAttributes(long id) {
        setId(id);

        View view = getView();
        TextView name = view.findViewById(R.id.contact_name_textview);
        TextView phone = view.findViewById(R.id.contact_number_textview);

        SQLiteDatabase db;
        Cursor cursor;

        try {

            SQLiteOpenHelper sqLiteOpenHelper = new ContactSQLiteOpenHelper(getContext());
            db = sqLiteOpenHelper.getReadableDatabase();
            cursor = ((SQLiteDatabase) db).query("CONTACTS",
                    new String[]{"NAME", "PHONE"},
                    "_id = ?", new String[]{Long.toString(id)},
                    null, null, null);

            //first search hit
            if (cursor.moveToFirst()) {
                String nameText = cursor.getString(0);
                String phoneText = cursor.getString(1);
                name.setText(nameText);
                phone.setText(phoneText);

            }
        } catch (Exception e) {
            Toast.makeText(this.getActivity(), "Database unavailable.", Toast.LENGTH_SHORT).show();
        }
    }

    interface UpdateLayout {
        void delete();
        void updateContactList();
        void send(String messageSent, String number, boolean location, boolean sms);
    }
}