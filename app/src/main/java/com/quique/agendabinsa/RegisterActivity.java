package com.quique.agendabinsa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    TextView tvRegister;
    EditText etName, etHome, etPostalCode, etPoblacion, etPhone, etEmail;
    Button btnSave;
    Spinner spType, spClient;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        etName = findViewById(R.id.etName);
        etHome = findViewById(R.id.etHome);
        etPostalCode = findViewById(R.id.etPostalCode);
        etPoblacion = findViewById(R.id.etDistrict);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        tvRegister = findViewById(R.id.tvRegister);

        btnSave = findViewById(R.id.btnSave);
        spType = findViewById(R.id.spType);
        spClient = findViewById(R.id.spClient);

        fillSpinnerType();

        spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0 ){
                    etName.setVisibility(View.VISIBLE);
                    etHome.setVisibility(View.VISIBLE);
                    etPostalCode.setVisibility(View.VISIBLE);
                    etPoblacion.setVisibility(View.VISIBLE);

                    spClient.setVisibility(View.GONE);
                    etPhone.setVisibility(View.GONE);
                    etEmail.setVisibility(View.GONE);
                    tvRegister.setText(R.string.registrarClient);
                }else{
                    etName.setVisibility(View.VISIBLE);
                    etHome.setVisibility(View.GONE);
                    etPostalCode.setVisibility(View.GONE);
                    etPoblacion.setVisibility(View.GONE);

                    spClient.setVisibility(View.VISIBLE);
                    etPhone.setVisibility(View.VISIBLE);
                    etEmail.setVisibility(View.VISIBLE);
                    tvRegister.setText(R.string.registrarContact);
                    fillSpinnerClient();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAgenda();
            }
        });
    }

    public void fillSpinnerType(){
        String[] type = {"Cliente", "Contacto"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, type);
        spType.setAdapter(adapter);
    }

    public void fillSpinnerClient(){
        List<String> nombresClientes = obtenerNombresClientesDesdeBaseDeDatos();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, nombresClientes);
        spClient.setAdapter(adapter);
    }
    private List<String> obtenerNombresClientesDesdeBaseDeDatos() {
        List<String> nombresClientes = new ArrayList<>();
        DataBaseSQLiteHelper conexion = new DataBaseSQLiteHelper(this, "Agenda", null , 1);
        try {
            SQLiteDatabase bdRead = conexion.getReadableDatabase();
            SQLiteDatabase bdWrite = conexion.getWritableDatabase();

            Cursor cursor = bdRead.rawQuery("SELECT name FROM clients", null);

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String nombre = cursor.getString(cursor.getColumnIndex("name"));
                    nombresClientes.add(nombre);
                }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conexion.close();
        }
        return nombresClientes;
    }


    private boolean clientExists(String name) {
        DataBaseSQLiteHelper conexion = new DataBaseSQLiteHelper(this, "Agenda", null, 1);
        SQLiteDatabase bd = conexion.getReadableDatabase();
        String[] parametros = {name};
        String[] campos = {"name"};
        Cursor cursor = bd.query("clients",
                campos,
                  "name =?",
                parametros,
                null,
                null,
                null);
        return cursor.moveToFirst();
    }

    public void saveAgenda(){
        if(spType.getSelectedItem().toString() == "Cliente" ){
            saveClient();
        }else {
            saveContact();
        }
    }

    private void saveClient() {
        if(validateDataClient()){
            DataBaseSQLiteHelper dataBaseSQLiteHelper = new DataBaseSQLiteHelper(this, "Agenda", null, 1 );
            SQLiteDatabase bd= dataBaseSQLiteHelper.getWritableDatabase();
            String name = etName.getText().toString();
            String home = etHome.getText().toString();
            String postalCode = etPostalCode.getText().toString();
            String poblacion = etPoblacion.getText().toString();
            if (clientExists(name)) {
                Toast.makeText(this, "Cliente ya registrado", Toast.LENGTH_SHORT).show();
                return;
            }

            ContentValues registro=new ContentValues();
            registro.put("name",name);
            registro.put("address",home);
            registro.put("postalCod",postalCode);
            registro.put("district",poblacion);

            bd.insert("clients",null,registro);
            bd.close();
            clear();
            Toast.makeText(this,"Se cargaron los datos del Cliente",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"Debe rellenar todos los campos",Toast.LENGTH_SHORT).show();
        }
    }

    private Boolean validateDataClient(){
        return etName.getText().length() > 0 && etHome.getText().length() > 0 && etPostalCode.getText().length() > 0 && etPoblacion.getText().length() > 0;
    }

    private Boolean validateDataContact(){
        return etName.getText().length() > 0 && etPhone.getText().length() > 0 && etEmail.getText().length() > 0;
    }

    private void saveContact(){
        if(validateDataClient()){

            DataBaseSQLiteHelper dataBaseSQLiteHelper = new DataBaseSQLiteHelper(this, "Agenda", null, 1 );
            SQLiteDatabase bd= dataBaseSQLiteHelper.getWritableDatabase();
            String name = etName.getText().toString();
            String client = spClient.getSelectedItem().toString();
            String phone = etPhone.getText().toString();
            String email = etEmail.getText().toString();
            if (contactExists(phone)) {
                Toast.makeText(this, "Cliente ya registrado", Toast.LENGTH_SHORT).show();
                return;
            }

            ContentValues registro=new ContentValues();
            registro.put("clientId",getClientId(client));
            registro.put("name",name);
            registro.put("phone",phone);
            registro.put("email",email);

            bd.insert("contacts",null,registro);
            bd.close();
            clear();
            Toast.makeText(this,"Se cargaron los datos del Contacto",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"Debe rellenar todos los campos",Toast.LENGTH_SHORT).show();
        }
    }

    public int getClientId(String client){
        int clientId = 0;
        DataBaseSQLiteHelper conexion = new DataBaseSQLiteHelper(this, "Agenda", null , 1);
        try {
            SQLiteDatabase bdRead = conexion.getReadableDatabase();

            Cursor cursor = bdRead.rawQuery("SELECT clientId FROM clients Where name =" + "'"+ client+"'" , null);

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String clientIdcursor = cursor.getString(cursor.getColumnIndex("clientId"));
                    clientId = Integer.parseInt(clientIdcursor);
                }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conexion.close();
        }
        return clientId;
    }

    private boolean contactExists(String phone) {
        DataBaseSQLiteHelper conexion = new DataBaseSQLiteHelper(this, "Agenda", null, 1);
        SQLiteDatabase bd = conexion.getReadableDatabase();
        String[] parametros = {phone};
        String[] campos = {"phone"};
        Cursor cursor = bd.query("contacts",
                campos,
                "phone =?",
                parametros,
                null,
                null,
                null);
        return cursor.moveToFirst();
    }

    public void clear(){
        etName.setText("");
        etHome.setText("");
        etPostalCode.setText("");
        etPoblacion.setText("");
        etPhone.setText("");
        etEmail.setText("");
    }
}