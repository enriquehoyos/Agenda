package com.quique.agendabinsa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import com.quique.agendabinsa.adapter.ContactsAdapter;
import com.quique.agendabinsa.interfaces.ContactListener;
import com.quique.agendabinsa.model.Client;
import com.quique.agendabinsa.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class ListaActivity extends AppCompatActivity implements ContactListener {

    TextView tvRegister;
    RecyclerView recyclerViewPersonas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        recyclerViewPersonas = findViewById(R.id.recyclerViewPersonas);
        tvRegister = findViewById(R.id.tvRegister);

        ContactsAdapter contactsAdapter = new ContactsAdapter(getContacts(), this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewPersonas.setLayoutManager(mLayoutManager);
        recyclerViewPersonas.setAdapter(contactsAdapter);
    }

    private List<Contact> getContacts() {
        int clientId = 0;
        List<Contact> contacts = new ArrayList<>();
        List<Client> clients = new ArrayList<>();
        DataBaseSQLiteHelper conexion = new DataBaseSQLiteHelper(this, "Agenda", null , 1);
        try {
            SQLiteDatabase bdRead = conexion.getReadableDatabase();
            SQLiteDatabase bdWrite = conexion.getWritableDatabase();

            Cursor cursor = bdRead.rawQuery("SELECT clientId, name, phone, email FROM contacts", null);

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String clientIdcursor = cursor.getString(cursor.getColumnIndex("clientId"));
                    String nameClient = getClientName(clientIdcursor);
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    String phone = cursor.getString(cursor.getColumnIndex("phone"));
                    String email = cursor.getString(cursor.getColumnIndex("email"));
                    Contact contact = new Contact(nameClient, name, phone, email);
                    contacts.add(contact);
                }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conexion.close();
        }
        return contacts;
    }

    public String getClientName(String client){
        String nameClient = "";
        DataBaseSQLiteHelper conexion = new DataBaseSQLiteHelper(this, "Agenda", null , 1);
        try {
            SQLiteDatabase bdRead = conexion.getReadableDatabase();

            Cursor cursor = bdRead.rawQuery("SELECT name FROM clients Where clientId =" + "'"+ client+"'" , null);

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    nameClient = name;
                }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conexion.close();
        }
        return nameClient;
    }

    @Override
    public void onItemClick(Contact contact) {
        Intent i = new Intent(ListaActivity.this, DetailContactActivity.class);
        i.putExtra("contact", contact);
        startActivity(i);

    }
}