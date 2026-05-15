package com.example.skillexchangeapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.skillexchangeapp.R
import com.example.skillexchangeapp.SkillExchangeApplication
import com.example.skillexchangeapp.databinding.FragmentDashboardBinding
import com.example.skillexchangeapp.ui.adapter.NeedAdapter
import com.example.skillexchangeapp.ui.viewmodel.MainViewModel
import com.example.skillexchangeapp.ui.viewmodel.ViewModelFactory
import com.example.skillexchangeapp.utils.SessionManager
import kotlinx.coroutines.launch

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels {
        ViewModelFactory((requireActivity().application as SkillExchangeApplication).repository)
    }

    private lateinit var adapter: NeedAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        lifecycleScope.launch {
            val userId = SessionManager(requireContext()).getUserId()
            viewModel.getUser(userId).collect { user ->
                user?.let {
                    binding.tvWelcomeName.text = getString(R.string.hello_user, it.fullName.split(" ")[0])
                    binding.tvTrustScore.text = getString(R.string.trust_score_format, it.trustScore)
                    binding.tvCompletedSwaps.text = getString(R.string.swaps_completed_format, it.completedSwaps)
                }
            }
        }

        binding.btnPostNeed.setOnClickListener {
            findNavController().navigate(R.id.action_dashboard_to_postNeed)
        }

        binding.btnViewFeed.setOnClickListener {
            findNavController().navigate(R.id.action_dashboard_to_needFeed)
        }

        binding.btnSwapManagement.setOnClickListener {
            findNavController().navigate(R.id.action_dashboard_to_swapManagement)
        }

        binding.tvSeeAll.setOnClickListener {
            findNavController().navigate(R.id.action_dashboard_to_needFeed)
        }

        binding.fabProfile.setOnClickListener {
            findNavController().navigate(R.id.action_dashboard_to_profile)
        }

        // Add history navigation if needed, or put it in profile. 
        // Let's add a button in the dashboard or just keep FAB for profile for now.
        // Actually, let's add it to the profile fragment as part of the user options.

        lifecycleScope.launch {
            viewModel.allOpenNeeds.collect { needs ->
                adapter.submitList(needs.take(5))
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = NeedAdapter { need ->
            val bundle = Bundle().apply { putLong("postId", need.id) }
            findNavController().navigate(R.id.action_dashboard_to_needFeed, bundle)
        }
        binding.rvRecentNeeds.layoutManager = LinearLayoutManager(context)
        binding.rvRecentNeeds.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
