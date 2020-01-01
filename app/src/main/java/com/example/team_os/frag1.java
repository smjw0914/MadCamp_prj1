package com.example.team_os;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class frag1 extends Fragment {

    public frag1() {
    }

    private Activity Myactivity;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_tab1, container, false);
        Myactivity = this.getActivity();
        final ListView listView = root.findViewById(R.id.listView1);
        ArrayList list = getAllContacts();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(Myactivity, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
        return root;
    }

    @SuppressWarnings("ConstantConditions")
    private ArrayList getAllContacts() {

        ArrayList<String> nameList = new ArrayList<>();

        ContentResolver cr = this.getContext().getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    String phoneNo = "0";
                    while (pCur != null && pCur.moveToNext()) {
                        phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }
                    if (!phoneNo.equals("0")) {
                        name = "name:" + name + "\nphonenum: " + phoneNo;
                    }
                    pCur.close();
                }
                nameList.add(name);
            }
        }
        if (cur != null) {
            cur.close();
        }
        Log.i("contactinfo", nameList.toString());
        return nameList;
    }
}


