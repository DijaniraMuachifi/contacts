package com.example.contacts;


import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.contacts.database.AppDatabase;
import com.example.contacts.models.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class listagem extends Fragment {

    private AppDatabase db;
    private RecyclerView recyclerView; //mostra os dados, facilita e torna a exibicao de grande conjunto de dados
    private ContactAdapter adapter; //adapta os dados para a renderizacao
    private SearchView searchView; //pesquisa os dados
    private ImageView favoriteIcon; //icone favorito

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listagem, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        searchView = view.findViewById(R.id.searchView);
        favoriteIcon = view.findViewById(R.id.favoriteIcon);

        db = Room.databaseBuilder(getContext().getApplicationContext(), AppDatabase.class, "contact_db").allowMainThreadQueries().build();

        loadContacts();

        FloatingActionButton btnNew = view.findViewById(R.id.btnnew);
        btnNew.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_listagem_to_addContact));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });

        favoriteIcon.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_listagem_to_listFavorite);
        });

        return view;
    }

    private void loadContacts() {
        List<User> contactList = db.userDao().getAll();
        adapter = new ContactAdapter(contactList);
        recyclerView.setAdapter(adapter);
    }

    private void filter(String text) {
        List<User> filteredList = db.userDao().getAll();

        if (text.isEmpty()) {
            adapter.setFilteredList(filteredList);
        } else {
            List<User> filtered = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                filtered = filteredList.stream()
                        .filter(user -> user.fullname.toLowerCase().contains(text.toLowerCase()))
                        .toList();
            }
            adapter.setFilteredList(filtered);
        }
    }
}
