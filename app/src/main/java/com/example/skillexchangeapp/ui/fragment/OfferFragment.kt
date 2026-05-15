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
import com.example.skillexchangeapp.data.local.entity.Offer
import com.example.skillexchangeapp.databinding.FragmentOfferBinding
import com.example.skillexchangeapp.ui.viewmodel.MainViewModel
import com.example.skillexchangeapp.ui.viewmodel.ViewModelFactory
import com.example.skillexchangeapp.utils.SessionManager

class OfferFragment : Fragment() {

    private var _binding: FragmentOfferBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels {
        ViewModelFactory((requireActivity().application as SkillExchangeApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOfferBinding.inflate(inflater, container, false)
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

            val message = binding.etMessage.text.toString().trim()
            val skillsOffered = binding.etSkillsOffered.text.toString().trim()
            val hoursOffered = binding.etHoursOffered.text.toString().toIntOrNull() ?: 0
            val postId = arguments?.getLong("postId") ?: -1L

            if (message.isNotEmpty() && skillsOffered.isNotEmpty() && hoursOffered > 0 && postId > 0) {
                val userId = SessionManager(requireContext()).getUserId()
                val offer = Offer(
                    needPostId = postId,
                    offeredByUserId = userId,
                    offeredSkill = skillsOffered,
                    offeredHours = hoursOffered,
                    message = message
                )
                viewModel.submitOffer(offer)
                Toast.makeText(context, "Swap offer submitted", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            } else {
                if (skillsOffered.isEmpty()) binding.tilSkillsOffered.error = "Add your offered skill"
                if (hoursOffered <= 0) binding.tilHoursOffered.error = "Enter offered hours"
                if (message.isEmpty()) binding.tilMessage.error = "Add a short message"
                Toast.makeText(context, "Please complete the highlighted fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupSkillDropdown() {
        val skills = resources.getStringArray(R.array.exchange_skills)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, skills)
        binding.etSkillsOffered.setAdapter(adapter)
    }

    private fun clearErrors() {
        binding.tilSkillsOffered.error = null
        binding.tilHoursOffered.error = null
        binding.tilMessage.error = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
