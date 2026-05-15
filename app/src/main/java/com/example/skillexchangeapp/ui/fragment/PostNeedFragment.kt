package com.example.skillexchangeapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.skillexchangeapp.R
import com.example.skillexchangeapp.SkillExchangeApplication
import com.example.skillexchangeapp.data.local.entity.NeedPost
import com.example.skillexchangeapp.databinding.FragmentPostNeedBinding
import com.example.skillexchangeapp.ui.viewmodel.MainViewModel
import com.example.skillexchangeapp.ui.viewmodel.ViewModelFactory
import com.example.skillexchangeapp.utils.SessionManager

class PostNeedFragment : Fragment() {

    private var _binding: FragmentPostNeedBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels {
        ViewModelFactory((requireActivity().application as SkillExchangeApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostNeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        setupSkillDropdown()

        binding.btnSubmit.setOnClickListener {
            clearErrors()

            val title = binding.etTitle.text.toString().trim()
            val description = binding.etDescription.text.toString().trim()
            val skill = binding.etSkillRequired.text.toString().trim()
            val hours = binding.etHours.text.toString().toIntOrNull() ?: 0
            val location = binding.etLocation.text.toString().trim()
            val urgency = binding.etUrgency.text.toString().trim().ifBlank { "Normal" }

            if (title.isNotEmpty() && skill.isNotEmpty() && hours > 0 && location.isNotEmpty()) {
                val userId = SessionManager(requireContext()).getUserId()
                val post = NeedPost(
                    userId = userId,
                    title = title,
                    description = description,
                    skillRequired = skill,
                    estimatedHours = hours,
                    urgencyLevel = urgency,
                    location = location
                )
                viewModel.postNeed(post)
                Toast.makeText(context, "Need posted to the skill board", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            } else {
                if (title.isEmpty()) binding.tilTitle.error = "Add a short title"
                if (skill.isEmpty()) binding.tilSkill.error = "Add the required skill"
                if (hours <= 0) binding.tilHours.error = "Enter skill points / hours"
                if (location.isEmpty()) binding.tilLocation.error = "Add village or area"
                Toast.makeText(context, "Please complete the highlighted fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupSkillDropdown() {
        val skills = resources.getStringArray(R.array.exchange_skills)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, skills)
        binding.etSkillRequired.setAdapter(adapter)
    }

    private fun clearErrors() {
        binding.tilTitle.error = null
        binding.tilSkill.error = null
        binding.tilHours.error = null
        binding.tilLocation.error = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
