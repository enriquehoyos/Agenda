package com.quique.agendabinsa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.quique.agendabinsa.model.Client;
import com.quique.agendabinsa.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class EditActivity extends AppCompatActivity {

    TextView tvEdit, txtContactList, txtClientList;
    EditText etName, etHome, etPostalCode, etDistrict, etPhone, etEmail;
    Button btnSave, btnDelete;
    Spinner spType, spClientList, spContactList, spClientContact;
    LinearLayout llName, llAddress, llPostalCode, llDistrict, llPhone, llEmail, llClientContact;
    List<String> nombresClientes;
    List<String> nombresContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        etName = findViewById(R.id.etName);
        etHome = findViewById(R.id.etHome);
        etPostalCode = findViewById(R.id.etPostalCode);
        etDistrict = findViewById(R.id.etDistrict);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        tvEdit = findViewById(R.id.tvEdit);

        llName = findViewById(R.id.llName);
        llAddress = findViewById(R.id.llAddress);
        llPostalCode = findViewById(R.id.llPostalCode);
        llDistrict = findViewById(R.id.llDistrict);
        llPhone = findViewById(R.id.llPhone);
        llEmail = findViewById(R.id.llEmail);
        llClientContact = findViewById(R.id.llClientContact);

        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);
        spType = findViewById(R.id.spType);
        spContactList = findViewById(R.id.spContactList);
        txtContactList = findViewById(R.id.txtContactList);
        spClientList = findViewById(R.id.spClientList);
        txtClientList = findViewById(R.id.txtClientList);
        spClientContact = findViewById(R.id.spClientContact);

        fillSpinnerType();
        fillSpinnerClient();

        spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0 ){
                    clear();
                    llName.setVisibility(View.VISIBLE);
                    llAddress.setVisibility(View.VISIBLE);
                    llPostalCode.setVisibility(View.VISIBLE);
                    llDistrict.setVisibility(View.VISIBLE);

                    llClientContact.setVisibility(View.GONE);
                    llPhone.setVisibility(View.GONE);
                    llEmail.setVisibility(View.GONE);
                    txtContactList.setVisibility(View.GONE);
                    spContactList.setVisibility(View.GONE);
                    tvEdit.setText(R.string.edit);

                    fillSpinnerClient();
                }else{
                    clear();
                    llName.setVisibility(View.VISIBLE);
                    llAddress.setVisibility(View.GONE);
                    llPostalCode.setVisibility(View.GONE);
                    llDistrict.setVisibility(View.GONE);

                    txtContactList.setVisibility(View.VISIBLE);
                    spContactList.setVisibility(View.VISIBLE);
                    llClientContact.setVisibility(View.VISIBLE);
                    llPhone.setVisibility(View.VISIBLE);
                    llEmail.setVisibility(View.VISIBLE);
                    tvEdit.setText(R.string.edit);

                    fillSpinnerClient();
                    fillSpinnerContacts();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spClientList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getClients(spClientList.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spContactList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getContacts(spContactList.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spType.getSelectedItem().toString() == "Cliente" ){
                    saveClient();
                }else {
                    saveContact();
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spType.getSelectedItem().toString() == "Cliente"){
                    deleteClient();
                }else{
                    deleteContact();
                }
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }
    public void fillSpinnerType(){
        String[] type = {"Cliente", "Contacto"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, type);
        spType.setAdapter(adapter);
    }
    public void fillSpinnerClient(){
        nombresClientes = obtenerNombresClientesDesdeBaseDeDatos();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, nombresClientes);
        spClientList.setAdapter(adapter);
        spClientContact.setAdapter(adapter);
    }

    public void fillSpinnerContacts(){
        nombresContacts = obtenerNombresContactosDesdeBaseDeDatos();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, nombresContacts);
        spContactList.setAdapter(adapter);
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
    private List<String> obtenerNombresContactosDesdeBaseDeDatos() {
        List<String> nombresClientes = new ArrayList<>();
        DataBaseSQLiteHelper conexion = new DataBaseSQLiteHelper(this, "Agenda", null , 1);
        try {
            SQLiteDatabase bdRead = conexion.getReadableDatabase();

            Cursor cursor = bdRead.rawQuery("SELECT name FROM contacts", null);

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
    private List<Contact> getContacts(String nameSpin) {
        List<Contact> contacts = new ArrayList<>();
        List<Client> clients = new ArrayList<>();
        DataBaseSQLiteHelper conexion = new DataBaseSQLiteHelper(this, "Agenda", null , 1);
        try {
            SQLiteDatabase bdRead = conexion.getReadableDatabase();
            SQLiteDatabase bdWrite = conexion.getWritableDatabase();

            Cursor cursor = bdRead.rawQuery("SELECT clientId, name, phone, email FROM contacts WHERE name="+ "'"+ nameSpin+"'", null);

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String clientIdcursor = cursor.getString(cursor.getColumnIndex("clientId"));
                    String nameClient = getClientName(clientIdcursor);
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    String phone = cursor.getString(cursor.getColumnIndex("phone"));
                    String email = cursor.getString(cursor.getColumnIndex("email"));

                    spClientContact.setSelection(getPositionClientOnSpinner(nameClient));
                    etName.setText(name);
                    etPhone.setText(phone);
                    etEmail.setText(email);
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

    private void getClients(String nameSpin) {
        DataBaseSQLiteHelper conexion = new DataBaseSQLiteHelper(this, "Agenda", null , 1);
        try {
            SQLiteDatabase bdRead = conexion.getReadableDatabase();
            Cursor cursor = bdRead.rawQuery("SELECT name, address, postalCod, district FROM clients WHERE name="+ "'"+ nameSpin+"'", null);

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    String address = cursor.getString(cursor.getColumnIndex("address"));
                    String postalCod = cursor.getString(cursor.getColumnIndex("postalCod"));
                    String district = cursor.getString(cursor.getColumnIndex("district"));

                    etName.setText(name);
                    etHome.setText(address);
                    etPostalCode.setText(postalCod);
                    etDistrict.setText(district);
                }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conexion.close();
        }
    }
    public int getPositionClientOnSpinner(String nameClient){
        int position = -1;

        for(int i = 0; i < nombresClientes.size() ; i++){
            if(nombresClientes.get(i).equals(nameClient)) {
                position = i;
            }
        }

        return position;
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

    public void saveClient(){
        DataBaseSQLiteHelper dataBaseSQLiteHelper = new DataBaseSQLiteHelper(this, "Agenda", null, 1 );
        SQLiteDatabase bd= dataBaseSQLiteHelper.getWritableDatabase();
        String currentClient = getCurrentClient(spClientList.getSelectedItem().toString());
        String name = etName.getText().toString();
        String home = etHome.getText().toString();
        String postalCode = etPostalCode.getText().toString();
        String district = etDistrict.getText().toString();

        ContentValues registro=new ContentValues();
        registro.put("name",name);
        registro.put("address",home);
        registro.put("postalCod",postalCode);
        registro.put("district",district);
        int cant=bd.update("clients", registro,"name=" + "'"+ currentClient+"'",null);
        bd.close();
        clear();
        reload();
        if(cant==1){
            Toast.makeText(this,"Se Modifico el Articulo",Toast.LENGTH_SHORT).show();
        } else{
            Toast.makeText(this,"No se modifico el articulo No existe con dicho codigo",Toast.LENGTH_SHORT).show();
        }
    }

    public void saveContact(){
        DataBaseSQLiteHelper dataBaseSQLiteHelper = new DataBaseSQLiteHelper(this, "Agenda", null, 1 );
        SQLiteDatabase bd= dataBaseSQLiteHelper.getWritableDatabase();
        String currentUser = getCurrentUser(spContactList.getSelectedItem().toString());
        String name = etName.getText().toString();
        String client = spClientContact.getSelectedItem().toString();
        String phone = etPhone.getText().toString();
        String email = etEmail.getText().toString();

        ContentValues registro=new ContentValues();
        registro.put("clientId",getClientId(client));
        registro.put("name",name);
        registro.put("phone",phone);
        registro.put("email",email);
        int cant=bd.update("contacts", registro,"name=" + "'"+ currentUser +"'",null);
        bd.close();
        clear();
        reload();
        if(cant==1){
            Toast.makeText(this,"Se Modifico el Articulo",Toast.LENGTH_SHORT).show();
        } else{
            Toast.makeText(this,"No se modifico el articulo No existe con dicho codigo",Toast.LENGTH_SHORT).show();
        }
    }

    public String getCurrentClient(String name){
        String currentClient = "";
        DataBaseSQLiteHelper conexion = new DataBaseSQLiteHelper(this, "Agenda", null , 1);
        try {
            SQLiteDatabase bdRead = conexion.getReadableDatabase();

            Cursor cursor = bdRead.rawQuery("SELECT name FROM clients Where name =" + "'"+ name+"'" , null);

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String userCursor = cursor.getString(cursor.getColumnIndex("name"));
                    currentClient =  userCursor;
                }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conexion.close();
        }
        return currentClient;
    }


    public String getCurrentUser(String name){
        String currentUser = "";
        DataBaseSQLiteHelper conexion = new DataBaseSQLiteHelper(this, "Agenda", null , 1);
        try {
            SQLiteDatabase bdRead = conexion.getReadableDatabase();

            Cursor cursor = bdRead.rawQuery("SELECT name FROM contacts Where name =" + "'"+ name+"'" , null);

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String userCursor = cursor.getString(cursor.getColumnIndex("name"));
                    currentUser =  userCursor;
                }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conexion.close();
        }
        return currentUser;
    }

    public void deleteContact(){
        DataBaseSQLiteHelper admin=new DataBaseSQLiteHelper(this,"Agenda",null,1);
        SQLiteDatabase bd=admin.getWritableDatabase();
        String phone = etPhone.getText().toString();
        int cant = bd.delete("contacts","phone="+phone,null);
        bd.close();
        if(cant == 1){
            clear();
            reload();
            Toast.makeText(this,"Se borro el Contacto",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this,"No se borro el Contacto",Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteClient(){
        int cant = -1;
        DataBaseSQLiteHelper admin=new DataBaseSQLiteHelper(this,"Agenda",null,1);
        SQLiteDatabase bd=admin.getWritableDatabase();
        String name = etName.getText().toString();
        try {
          cant  = bd.delete("clients","name="+ "'"+ name+"'",null);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        bd.close();
        if(cant == 1){
            Toast.makeText(this,"Se borro el Contacto",Toast.LENGTH_SHORT).show();
            clear();
            reload();
        }
        else
        {
            Toast.makeText(this,"No se borro el Contacto",Toast.LENGTH_SHORT).show();
        }
    }

    public void clear(){
        etName.setText("");
        etHome.setText("");
        etPostalCode.setText("");
        etDistrict.setText("");
        etPhone.setText("");
        etEmail.setText("");
    }
    public void reload(){
        fillSpinnerType();
        fillSpinnerClient();
        fillSpinnerContacts();
    }
}