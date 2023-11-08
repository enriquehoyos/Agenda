package com.quique.agendabinsa.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

/**
 * Created by Quique on 06/11/2023.
 */
public class Contact implements Parcelable {
    private int id;
    private int clientId;
    private String name;
    private String phone;
    private String email;
    private String nameClient;

    public Contact(String nameClient, String name, String phone, String email) {
        this.nameClient = nameClient;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public Contact(String nameClient, String name) {
        this.nameClient = nameClient;
        this.name = name;
    }

    protected Contact(Parcel in) {
        id = in.readInt();
        clientId = in.readInt();
        name = in.readString();
        phone = in.readString();
        email = in.readString();
        nameClient = in.readString();
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    public String getNameClient() {
        return nameClient;
    }

    public void setNameClient(String nameClient) {
        this.nameClient = nameClient;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {

        dest.writeInt(id);
        dest.writeInt(clientId);
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(email);
        dest.writeString(nameClient);
    }
}
