package com.example.skillexchangeapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.skillexchangeapp.R
import com.example.skillexchangeapp.SkillExchangeApplication
import com.example.skillexchangeapp.data.local.entity.User
import com.example.skillexchangeapp.databinding.FragmentRegisterBinding
import com.example.skillexchangeapp.ui.viewmodel.AuthState
import com.example.skillexchangeapp.ui.viewmodel.AuthViewModel
import com.example.skillexchangeapp.ui.viewmodel.ViewModelFactory
import com.example.skillexchangeapp.utils.SecurityUtils
import com.example.skillexchangeapp.utils.SessionManager
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels {
        ViewModelFactory((requireActivity().application as SkillExchangeApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        setupSkillDropdown()

        binding.btnRegister.setOnClickListener {
            val fullName = binding.etFullName.text.toString()
            val email = binding.etEmail.text.toString()
            val phone = binding.etPhone.text.toString()
            val village = binding.etVillage.text.toString()
            val primarySkill = binding.etPrimarySkill.text.toString()
            val experience = binding.etExperience.text.toString().toIntOrNull() ?: 0
            val password = binding.etPassword.text.toString()

            if (fullName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                val user = User(
                    fullName = fullName,
                    email = email,
                    phone = phone,
                    village = village,
                    primarySkill = primarySkill,
                    secondarySkills = "",
                    experienceYears = experience,
                    passwordHash = SecurityUtils.hashPassword(password)
                )
                viewModel.register(user)
            } else {
                Toast.makeText(context, "Please fill required fields", Toast.LENGTH_SHORT).show()
            }
        }

        observeViewModel()
    }

    private fun setupSkillDropdown() {
        val skills = resources.getStringArray(R.array.exchange_skills)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, skills)
        binding.etPrimarySkill.setAdapter(adapter)
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.authState.collect { state ->
                when (state) {
                    is AuthState.Success -> {
                        SessionManager(requireContext()).saveSession(state.userId)
                        findNavController().navigate(R.id.action_register_to_dashboard)
                    }
                    is AuthState.Error -> {
                        Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
