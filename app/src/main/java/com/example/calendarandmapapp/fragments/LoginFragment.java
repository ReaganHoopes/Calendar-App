package com.example.calendarandmapapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.calendarandmapapp.R;
import com.example.calendarandmapapp.databinding.FragmentLoginBinding;
import com.example.calendarandmapapp.viewmodels.UserViewModel;


public class LoginFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentLoginBinding binding = FragmentLoginBinding.inflate(getLayoutInflater());
        UserViewModel viewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);

        viewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                NavHostFragment.findNavController(this).navigate(R.id.action_loginFragment_to_mainEventListFragment);
            }
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), message -> {
            binding.error.setText(message);
        });

        binding.loginButton.setOnClickListener(view -> {
            viewModel.signIn(binding.loginEmailAddress.getText().toString(), binding.logInPassword.getText().toString());
        });

        binding.goToSignUpButton.setOnClickListener(view -> {
            NavController controller = NavHostFragment.findNavController(this);
            controller.navigate(R.id.action_loginFragment_to_signupFragment);
        });

        return binding.getRoot();
    }
}