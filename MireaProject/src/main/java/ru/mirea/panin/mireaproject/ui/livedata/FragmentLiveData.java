package ru.mirea.panin.mireaproject.ui.livedata;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.mirea.panin.mireaproject.databinding.FragmentLivedataBinding;

public class FragmentLiveData extends Fragment {
    private FragmentLivedataBinding binding;
    private TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLivedataBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        textView = binding.networkStateView;
        LiveData<String> networkLiveData = NetworkLiveData.getInstance(requireContext());
        networkLiveData.observe(getViewLifecycleOwner(), s -> textView.setText(s));
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}