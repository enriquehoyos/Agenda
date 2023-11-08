package com.quique.agendabinsa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.quique.agendabinsa.model.Contact;

public class DetailContactActivity extends AppCompatActivity {

    Contact contact;
    EditText etClient, etName,  etPhone, etEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_contact);

        etName = findViewById(R.id.etName);
        etClient = findViewById(R.id.etClient);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);

        Intent intent = getIntent();
        contact = intent.getParcelableExtra("contact");

        etClient.setText(contact.getNameClient());
        etName.setText(contact.getName());
        etPhone.setText(contact.getPhone());
        etEmail.setText(contact.getEmail());


    }
}