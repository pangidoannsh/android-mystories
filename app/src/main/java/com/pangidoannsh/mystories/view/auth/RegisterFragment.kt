package com.pangidoannsh.mystories.view.auth

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.pangidoannsh.mystories.R
import com.pangidoannsh.mystories.databinding.FragmentRegisterBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding
    private val viewModel by activityViewModels<RegisterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupComponent()
        setupObserver()
        setupAction()

    }

    private fun setupComponent() {
        binding?.let {
            it.btnSubmit.isEnabled = false

            it.etPassword.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable?) {
                    if ((s?.length ?: 0) >= 8) {
                        it.btnSubmit.isEnabled = true
                    }
                }

            })
        }
    }

    private fun setupAction() {
        binding?.apply {
            btnBack.setOnClickListener {
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }

    private fun setupObserver() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            activity?.findViewById<RelativeLayout>(R.id.auth_progress_bar)?.let {
                it.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
            binding?.let { bind ->
                bind.btnSubmit.setOnClickListener {
                    if (!isLoading) {
                        val name = bind.nameInput.text.toString()
                        val email = bind.emailInput.text.toString()
                        val password = bind.etPassword.text.toString()
                        viewModel.submitRegister(name, email, password)
                    }
                }
            }
        }

        viewModel.isError.observe(viewLifecycleOwner) {
            if (it) Toast.makeText(
                requireActivity(),
                getString(R.string.email_already_taken),
                Toast.LENGTH_SHORT
            )
                .show()
        }

        viewModel.message.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
        }

        viewModel.isSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                Toast.makeText(
                    requireActivity(),
                    requireActivity().getString(R.string.success_register_message),
                    Toast.LENGTH_SHORT
                ).show()
                CoroutineScope(Dispatchers.IO).launch {
                    delay(500)
                    requireActivity().supportFragmentManager.popBackStack()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}