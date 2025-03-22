package com.example.contacts;


import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.appcompat.widget.SearchView; // Importe a classe 
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.contacts.database.AppDatabase;
import com.example.contacts.models.User;

import java.util.List;

public class listFavorite extends Fragment {

    private AppDatabase db;
    private RecyclerView recyclerView;
    private ContactAdapter adapter;
    private SearchView searchView;
    private ImageView backButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_favorite, container, false);

        recyclerView = view.findViewById(R.id.favoriteRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        searchView = view.findViewById(R.id.searchView);
        backButton = view.findViewById(R.id.backButton);

        db = Room.databaseBuilder(getContext().getApplicationContext(), AppDatabase.class, "contact_db").allowMainThreadQueries().build();

        loadFavoriteContacts(); // Carrega apenas os contatos favoritos

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

        backButton.setOnClickListener(v -> Navigation.findNavController(v).popBackStack());

        return view;
    }

    private void loadFavoriteContacts() {
        List<User> favoriteList = db.userDao().getFavorites();
        adapter = new ContactAdapter(favoriteList);
        recyclerView.setAdapter(adapter);
    }

    private void filter(String text) {
        List<User> filteredList = db.userDao().getFavorites(); // Inicializa com todos os favoritos

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
