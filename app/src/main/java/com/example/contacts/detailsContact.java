package com.example.contacts;


import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.room.Room;

import com.example.contacts.database.AppDatabase;
import com.example.contacts.models.User;

public class detailsContact extends Fragment {

    private ImageView contactPhoto, backButton, editButton, favoriteButton, deleteButton;
    private TextView contactName, contactPhone, mobileNumber, contactEmail, shareLocation, shareContact;
    private AppDatabase db;
    private int contactId;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details_contact, container, false);

        contactPhoto = view.findViewById(R.id.contactPhoto);
        backButton = view.findViewById(R.id.backButton);
        editButton = view.findViewById(R.id.editButton);
        favoriteButton = view.findViewById(R.id.favoriteButton);
        deleteButton = view.findViewById(R.id.deleteButton);
        contactName = view.findViewById(R.id.contactName);
        contactPhone = view.findViewById(R.id.contactPhone);
        mobileNumber = view.findViewById(R.id.mobileNumber);
        contactEmail = view.findViewById(R.id.contactEmail);
        // Removido shareLocation e shareContact

        db = Room.databaseBuilder(getContext().getApplicationContext(), AppDatabase.class, "contact_db").allowMainThreadQueries().build();

        if (getArguments() != null) {
            contactId = getArguments().getInt("contactId", -1);
            if (contactId != -1) {
                loadContactDetails();
            }
        }

        backButton.setOnClickListener(v -> Navigation.findNavController(v).popBackStack());
        editButton.setOnClickListener(v -> editContact());
        favoriteButton.setOnClickListener(v -> toggleFavorite());
        deleteButton.setOnClickListener(v -> showDeleteConfirmationDialog());

        return view;
    }

    private void loadContactDetails() {
        User user = db.userDao().loadById(contactId);
        if (user != null) {
            contactName.setText(user.fullname);
            contactPhone.setText(user.phone);
            mobileNumber.setText(user.phone);
            contactEmail.setText(user.email);

            if (user.photoPath != null && !user.photoPath.isEmpty()) {
                Uri imageUri = Uri.parse(user.photoPath);
                contactPhoto.setImageURI(imageUri);
            }

            if (user.isFavorite) {
                favoriteButton.setImageResource(android.R.drawable.star_big_on);
            } else {
                favoriteButton.setImageResource(android.R.drawable.star_big_off);
            }
        }
    }

    private void editContact() {
        Bundle bundle = new Bundle();
        bundle.putInt("contactId", contactId);
        Navigation.findNavController(getView()).navigate(R.id.action_detailsContact_to_editContact, bundle);
    }

    private void toggleFavorite() {
        User user = db.userDao().loadById(contactId);
        if (user != null) {
            user.isFavorite = !user.isFavorite;
            db.userDao().update(user);
            loadContactDetails();
            String message = user.isFavorite ? "Contato adicionado aos favoritos" : "Contato removido dos favoritos";
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Excluir Contato")
                .setMessage("Tem certeza que deseja excluir este contato?")
                .setPositiveButton("Sim", (dialog, which) -> deleteContact())
                .setNegativeButton("Não", null)
                .show();
    }

    private void deleteContact() {
        User user = db.userDao().loadById(contactId);
        if (user != null) {
            db.userDao().delete(user);
            Toast.makeText(getContext(), "Contato excluído com sucesso", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(getView()).navigate(R.id.action_detailsContact_to_listagem2);
        }
    }
}
