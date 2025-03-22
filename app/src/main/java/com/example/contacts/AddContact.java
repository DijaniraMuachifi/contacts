package com.example.contacts;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

public class AddContact extends Fragment {

    private ImageView contactPhoto, backButton;
    private TextInputEditText contactName, contactEmail, contactPhone;
    private Uri selectedImageUri;
    private AppDatabase db;

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    contactPhoto.setImageURI(selectedImageUri);
                }
            }
    );

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_contact, container, false);

        contactPhoto = view.findViewById(R.id.contactPhoto);
        backButton = view.findViewById(R.id.backButton);
        contactName = view.findViewById(R.id.contactName);
        contactEmail = view.findViewById(R.id.contactEmail);
        contactPhone = view.findViewById(R.id.contactPhone);
        Button saveContactButton = view.findViewById(R.id.saveContactButton);

        db = Room.databaseBuilder(getContext().getApplicationContext(), AppDatabase.class, "contact_db").allowMainThreadQueries().build();

        contactPhoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImage.launch(intent);
        });

        backButton.setOnClickListener(v -> Navigation.findNavController(v).popBackStack());

        saveContactButton.setOnClickListener(v -> {
            saveContact();
        });

        return view;
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

        User user = new User();
        user.fullname = name;
        user.email = email;
        user.phone = phone;
        user.photoPath = photoPath;

        db.userDao().insertAll(user);

        Toast.makeText(getContext(), "Contato salvo com sucesso", Toast.LENGTH_SHORT).show();
        Navigation.findNavController(getView()).popBackStack();
    }
} 
