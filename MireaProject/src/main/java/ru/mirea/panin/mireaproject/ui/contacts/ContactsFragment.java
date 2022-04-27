package ru.mirea.panin.mireaproject.ui.contacts;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ru.mirea.panin.mireaproject.R;
import ru.mirea.panin.mireaproject.databinding.FragmentContactsBinding;

public class ContactsFragment extends Fragment {
    private FragmentContactsBinding binding;
    private List<Contact> contacts;
    private ArrayList<HashMap<String, String>> arrayList;
    private SimpleAdapter adapter;
    private ContactDao contactDao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentContactsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        AppDb db = App.getInstance().getDatabase();
        contactDao = db.contactDao();
        ListView listView = binding.contactsList;
        arrayList = new ArrayList<>();

        Observer<List<Contact>> observer = new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> cont) {
                contacts = cont;
                HashMap<String, String> contactsList;
                arrayList.clear();
                for (int i = 0; i < contacts.size(); i++) {
                    contactsList = new HashMap<>();
                    contactsList.put("Name", contacts.get(i).name);
                    contactsList.put("Number", contacts.get(i).number);
                    arrayList.add(contactsList);
                }
                adapter.notifyDataSetChanged();
            }
        };
        contactDao.getAll().observe(getViewLifecycleOwner(), observer);

        adapter = new SimpleAdapter(requireContext(), arrayList, android.R.layout.simple_list_item_2,
                new String[]{"Name", "Number"},
                new int[]{android.R.id.text1, android.R.id.text2});
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + contacts.get(i).number));
            startActivity(intent);
        });

        listView.setOnItemLongClickListener((adapterView, view, index, l) -> {
            AlertDialog.Builder adb = new AlertDialog.Builder(requireContext());
            adb.setTitle("Choose options");
            adb.setNegativeButton("Edit", (dialogInterface, i) -> {
                showDialog(contacts.get(index), true);
            });
            adb.setPositiveButton("Delete", (dialog, which)
                    -> new Thread(() -> contactDao.delete(contacts.get(index))).start());
            adb.show();
            return true;
        });

        binding.addContBtn.setOnClickListener(view -> {
            showDialog(new Contact(), false);
        });
        return root;
    }

    public void showDialog(Contact contact, boolean edit) {
        Dialog dialog = new Dialog(requireActivity());
        dialog.setCancelable(false);
        dialog.setTitle("Change contact");
        dialog.setContentView(R.layout.custom_dialog);
        EditText num = dialog.findViewById(R.id.numberField);
        EditText name = dialog.findViewById(R.id.nameField);
        if (edit) {
            name.setText(contact.name);
            num.setText(contact.number);
        }
        dialog.findViewById(R.id.apply).setOnClickListener(view -> {

            contact.name = name.getText().toString();
            contact.number = num.getText().toString();
            if (contact.number.equals("")) {
                Toast.makeText(requireContext(), "No number entered", Toast.LENGTH_SHORT).show();
                return;
            }
            if (edit)
                new Thread(() -> contactDao.update(contact)).start();
            else
                new Thread(() -> contactDao.insert(contact)).start();
            dialog.dismiss();
        });

        dialog.findViewById(R.id.cancel).setOnClickListener(view -> dialog.dismiss());
        dialog.show();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}