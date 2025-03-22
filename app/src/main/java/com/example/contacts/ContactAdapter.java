package com.example.contacts;


import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contacts.models.User;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private List<User> contactList;

    public ContactAdapter(List<User> contactList) {
        this.contactList = contactList;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        User contact = contactList.get(position);
        holder.contactName.setText(contact.fullname);
        holder.contactPhone.setText(contact.phone);

        if (contact.photoPath != null && !contact.photoPath.isEmpty()) {
            Uri imageUri = Uri.parse(contact.photoPath);
            holder.contactPhoto.setImageURI(imageUri);
        } else {
            holder.contactPhoto.setImageResource(R.mipmap.avacta_foreground);
        }

        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("contactId", contact.uid);
            Navigation.findNavController(v).navigate(R.id.action_listagem_to_detailsContact, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public void setFilteredList(List<User> filteredList) {
        this.contactList = filteredList;
        notifyDataSetChanged();
    }

    static class ContactViewHolder extends RecyclerView.ViewHolder {
        ImageView contactPhoto;
        TextView contactName, contactPhone;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            contactPhoto = itemView.findViewById(R.id.contactPhoto);
            contactName = itemView.findViewById(R.id.contactName);
            contactPhone = itemView.findViewById(R.id.contactPhone);
        }
    }
}
