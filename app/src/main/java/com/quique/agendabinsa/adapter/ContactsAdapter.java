package com.quique.agendabinsa.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quique.agendabinsa.R;
import com.quique.agendabinsa.interfaces.ContactListener;
import com.quique.agendabinsa.model.Contact;

import java.util.List;

/**
 * Created by Quique on 06/11/2023.
 */
public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder>{
    private List<Contact> contactList;
    private ContactListener contactListener;

    public ContactsAdapter(List<Contact> contactList, ContactListener contactListener){
        this.contactList = contactList;
        this.contactListener = contactListener;
    }

    @NonNull
    @Override
    public ContactsAdapter.ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listContacts = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ContactsViewHolder(listContacts);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsAdapter.ContactsViewHolder holder, int position) {
        Contact contact = contactList.get(position);

        // Obtener los datos de la lista
        String name = contact.getName();
        String nameClient = contact.getNameClient();
        String phone = contact.getPhone();
        String email = contact.getEmail();
        holder.tvNameContact.setText(name);
        holder.tvClient.setText(nameClient);
        holder.llItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactListener.onItemClick(contact);
            }
        });

    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public class ContactsViewHolder extends RecyclerView.ViewHolder{

        LinearLayout llItems;
        TextView tvNameContact;
        TextView tvClient;
        public ContactsViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvNameContact = itemView.findViewById(R.id.tvNameContact);
            this.tvClient = itemView.findViewById(R.id.tvClient);
            this.llItems = itemView.findViewById(R.id.llItems);
        }
    }
}
