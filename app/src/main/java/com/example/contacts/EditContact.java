package com.example.contacts;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.room.Room;

import com.example.contacts.database.AppDatabase;
import com.example.contacts.models.User;
import com.google.android.material.textfield.TextInputEditText;

public class EditContact extends Fragment {

    private ImageView contactPhoto, backButton;
    private TextInputEditText contactName, contactEmail, contactPhone;
    private AppDatabase db;
    private int contactId;
    private Uri selectedImageUri;

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    contactPhoto.setImageURI(selectedImageUri);
                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_contact, container, false);

        contactPhoto = view.findViewById(R.id.contactPhoto);
        backButton = view.findViewById(R.id.backButton);
        contactName = view.findViewById(R.id.contactName);
        contactEmail = view.findViewById(R.id.contactEmail);
        contactPhone = view.findViewById(R.id.contactPhone);
        Button saveButton = view.findViewById(R.id.saveButton);

        db = Room.databaseBuilder(getContext().getApplicationContext(), AppDatabase.class, "contact_db").allowMainThreadQueries().build();

        if (getArguments() != null) {
            contactId = getArguments().getInt("contactId", -1);
            if (contactId != -1) {
                loadContactDetails();
            }
        }

        contactPhoto.setOnClickListener(v -> selectImage());
        backButton.setOnClickListener(v -> Navigation.findNavController(v).popBackStack());
        saveButton.setOnClickListener(v -> saveContact());

        return view;
    }

    private void loadContactDetails() {
        User user = db.userDao().loadById(contactId);
        if (user != null) {
            contactName.setText(user.fullname);
            contactEmail.setText(user.email);
            contactPhone.setText(user.phone);

            if (user.photoPath != null && !user.photoPath.isEmpty()) {
                selectedImageUri = Uri.parse(user.photoPath);
                contactPhoto.setImageURI(selectedImageUri);
            }
        }
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        pickImage.launch(intent);
    }

    private void saveContact() {
        String name = contactName.getText().toString();
        String email = contactEmail.getText().toString();
        String phone = contactPhone.getText().toString();
        String photoPath = selectedImageUri != null ? selectedImageUri.toString() : null;

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            Toast.makeText(getContext(), "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = db.userDao().loadById(contactId);
        if (user != null) {
            user.fullname = name;
            user.email = email;
            user.phone = phone;
            user.photoPath = photoPath;
            db.userDao().update(user);

            Toast.makeText(getContext(), "Contato atualizado com sucesso", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(getView()).popBackStack();
        }
    }
}
