package com.example.calendarandmapapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.calendarandmapapp.R;
import com.example.calendarandmapapp.databinding.FragmentSignupBinding;
import com.example.calendarandmapapp.viewmodels.UserViewModel;


public class SignupFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentSignupBinding binding = FragmentSignupBinding.inflate(inflater, container, false);
        UserViewModel viewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), message ->{
            binding.signUpError.setText(message);
        });

        binding.signUpButton.setOnClickListener(view -> {
            viewModel.signUp(binding.signUpEmail.getText().toString(),
                    binding.signUpEmailConfirmation.getText().toString(),
                    binding.signUpPassword.getText().toString(),
                    binding.signUpPasswordConfirmation.getText().toString());
        });

        viewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                NavHostFragment.findNavController(this).navigate(R.id.action_signupFragment_to_mainEventListFragment);
            }
        });

        return binding.getRoot();
    }
}
