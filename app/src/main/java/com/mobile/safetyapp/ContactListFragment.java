package com.mobile.safetyapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.ListFragment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class ContactListFragment extends ListFragment {
    private SQLiteDatabase db;
    private Cursor cursor;
    private contactlistInterface contactlistInterface;

    public ContactListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            SQLiteOpenHelper sqLiteOpenHelper=new ContactSQLiteOpenHelper(inflater.getContext());
            db=sqLiteOpenHelper.getReadableDatabase();
            cursor =db.query("CONTACTS", new String[]{"_id","NAME"}, null,null,null,null,null);
            CursorAdapter cursorAdapter=new SimpleCursorAdapter(inflater.getContext(),
                    android.R.layout.simple_list_item_1,cursor,
                    new String[]{"NAME"},
                    new int[]{android.R.id.text1},
                    0);

            setListAdapter(cursorAdapter);
        } catch (Exception e) {
            Toast.makeText(this.getActivity(), "Database not available", Toast.LENGTH_SHORT).show();
        }

        return super.onCreateView(inflater,container,savedInstanceState);
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        if(contactlistInterface!=null){
            contactlistInterface.listItemClicked(id);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.contactlistInterface= (ContactListFragment.contactlistInterface) context;

    }

    interface contactlistInterface{
        void listItemClicked(long id);
    }

}