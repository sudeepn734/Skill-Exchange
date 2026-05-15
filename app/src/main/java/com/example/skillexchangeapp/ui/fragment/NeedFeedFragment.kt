package com.example.skillexchangeapp.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.skillexchangeapp.R
import com.example.skillexchangeapp.SkillExchangeApplication
import com.example.skillexchangeapp.data.local.entity.NeedPost
import com.example.skillexchangeapp.databinding.FragmentNeedFeedBinding
import com.example.skillexchangeapp.ui.adapter.NeedAdapter
import com.example.skillexchangeapp.ui.viewmodel.MainViewModel
import com.example.skillexchangeapp.ui.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch

class NeedFeedFragment : Fragment() {

    private var _binding: FragmentNeedFeedBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels {
        ViewModelFactory((requireActivity().application as SkillExchangeApplication).repository)
    }
    private lateinit var adapter: NeedAdapter
    private var allNeeds: List<NeedPost> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNeedFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        adapter = NeedAdapter { need ->
            val bundle = Bundle().apply { putLong("postId", need.id) }
            findNavController().navigate(R.id.action_needFeed_to_offer, bundle)
        }
        binding.rvNeeds.adapter = adapter

        binding.etSkillFilter.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                applyFilter(s?.toString().orEmpty())
            }
            override fun afterTextChanged(s: Editable?) = Unit
        })

        lifecycleScope.launch {
            viewModel.allOpenNeeds.collect { needs ->
                allNeeds = needs
                applyFilter(binding.etSkillFilter.text?.toString().orEmpty())
            }
        }
    }

    private fun applyFilter(query: String) {
        val normalizedQuery = query.trim().lowercase()
        val filteredNeeds = if (normalizedQuery.isBlank()) {
            allNeeds
        } else {
            allNeeds.filter { need ->
                need.skillRequired.lowercase().contains(normalizedQuery) ||
                    need.location.lowercase().contains(normalizedQuery) ||
                    need.title.lowercase().contains(normalizedQuery)
            }
        }

        adapter.submitList(filteredNeeds)
        binding.tvEmptyState.visibility = if (filteredNeeds.isEmpty()) View.VISIBLE else View.GONE
        binding.tvFeedCount.text = resources.getQuantityString(
            R.plurals.open_needs_count,
            filteredNeeds.size,
            filteredNeeds.size
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
