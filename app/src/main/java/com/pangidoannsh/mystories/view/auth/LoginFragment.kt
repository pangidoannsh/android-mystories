package com.pangidoannsh.mystories.view.auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.pangidoannsh.mystories.R
import com.pangidoannsh.mystories.databinding.FragmentLoginBinding
import com.pangidoannsh.mystories.view.ViewModelFactory
import com.pangidoannsh.mystories.view.home.HomeActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding
    private val viewModel: LoginViewModel by activityViewModels {
        ViewModelFactory.getInstance(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupComponent()
        subscribeObserver()
        setupAction()
    }

    private fun setupComponent() {
        binding?.let {
            it.btnLogin.isEnabled = false
            it.passwordInput.addTextChangedListener(object : TextWatcher {
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
                        it.btnLogin.isEnabled = true
                    }
                }

            })
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun subscribeObserver() {
        viewModel.message.observe(viewLifecycleOwner) { text ->
            Toast.makeText(requireActivity(), text, Toast.LENGTH_SHORT).show()
        }
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            activity?.findViewById<RelativeLayout>(R.id.auth_progress_bar)?.let {
                it.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
            binding?.let { bind ->
                bind.btnLogin.setOnClickListener {
                    Log.d("FiturLogin", "Btn Submit Clicked")
                    val email = bind.emailInput.text.toString()
                    val password = bind.passwordInput.text.toString()
                    if (!isLoading) {
                        if (email.isNotEmpty() && password.isNotEmpty()) {
                            Log.d("FiturLogin", "function login run...")
                            viewModel.submitLogin(email, password)
                        }
                    }
                }
            }
        }
        viewModel.isLogin.observe(viewLifecycleOwner) { isLogin ->
            if (isLogin) {
                CoroutineScope(Dispatchers.IO).launch {
                    delay(500)
                    withContext(Dispatchers.Main) {
                        startActivity(Intent(requireActivity(), HomeActivity::class.java))
                        requireActivity().finish()
                    }
                }
            }
        }
    }

    private fun setupAction() {
        binding?.let { bind ->
            bind.btnRegister.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }
        }
    }
}