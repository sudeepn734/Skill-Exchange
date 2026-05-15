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
import com.example.skillexchangeapp.databinding.FragmentProfileBinding
import com.example.skillexchangeapp.ui.viewmodel.MainViewModel
import com.example.skillexchangeapp.ui.viewmodel.ViewModelFactory
import com.example.skillexchangeapp.utils.SessionManager
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels {
        ViewModelFactory((requireActivity().application as SkillExchangeApplication).repository)
    }

    private var currentUser: User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        setupSkillDropdown()

        val sessionManager = SessionManager(requireContext())
        val userId = sessionManager.getUserId()

        lifecycleScope.launch {
            viewModel.getUser(userId).collect { user ->
                user?.let {
                    currentUser = it
                    binding.tvProfileName.text = it.fullName
                    binding.tvProfileEmail.text = it.email
                    binding.etFullName.setText(it.fullName)
                    binding.etPhone.setText(it.phone)
                    binding.etVillage.setText(it.village)
                    binding.etPrimarySkill.setText(it.primarySkill)
                }
            }
        }

        binding.btnUpdateProfile.setOnClickListener {
            val name = binding.etFullName.text.toString()
            val phone = binding.etPhone.text.toString()
            val village = binding.etVillage.text.toString()
            val skill = binding.etPrimarySkill.text.toString()

            currentUser?.let {
                val updatedUser = it.copy(
                    fullName = name,
                    phone = phone,
                    village = village,
                    primarySkill = skill
                )
                viewModel.updateUser(updatedUser)
                Toast.makeText(context, "Profile Updated", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnViewHistory.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_history)
        }

        binding.btnSettings.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_settings)
        }

        binding.btnLogout.setOnClickListener {
            sessionManager.logout()
            findNavController().navigate(R.id.action_profile_to_login)
        }
    }

    private fun setupSkillDropdown() {
        val skills = resources.getStringArray(R.array.exchange_skills)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, skills)
        binding.etPrimarySkill.setAdapter(adapter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
