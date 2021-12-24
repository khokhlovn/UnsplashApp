package com.example.unsplashapp.ui.auth

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.unsplashapp.Constants
import com.example.unsplashapp.R
import com.example.unsplashapp.databinding.FragmentAuthBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import net.openid.appauth.*
import javax.inject.Inject

@AndroidEntryPoint
class AuthFragment : Fragment() {

    private lateinit var binding: FragmentAuthBinding
    private val authViewModel by viewModels<AuthViewModel>()

    @Inject
    lateinit var authService: AuthorizationService

    @Inject
    lateinit var serviceConfig: AuthorizationServiceConfiguration

    private var someActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { data ->
        data.data?.let {
            AuthorizationResponse.fromIntent(it)?.let { resp ->
                resp.authorizationCode?.let { code -> makeTokenRequest(code) }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkIfTokenExists()

        val authRequestBuilder = AuthorizationRequest.Builder(
            serviceConfig,
            Constants.CLIENT_ID,
            ResponseTypeValues.CODE,
            Uri.parse(Constants.REDIRECT_URI)
        )

        val authRequest = authRequestBuilder
            .setScope(Constants.SCOPE)
            .build()

        binding.button.setOnClickListener {
            val authIntent = authService.getAuthorizationRequestIntent(authRequest)
            someActivityResultLauncher.launch(authIntent)
        }
    }

    private fun checkIfTokenExists() {
        lifecycleScope.launch {
            if (authViewModel.checkToken())
                findNavController().navigate(R.id.login)
        }
    }

    private fun makeTokenRequest(code: String) {
        val tokenRequestBuilder = TokenRequest.Builder(
            serviceConfig,
            Constants.CLIENT_ID
        )
        val additionalParams = HashMap<String, String>()
        additionalParams["client_secret"] = Constants.CLIENT_SECRET
        val tokenRequest = tokenRequestBuilder
            .setGrantType("authorization_code")
            .setRedirectUri(Uri.parse(Constants.REDIRECT_URI))
            .setAuthorizationCode(code)
            .setAdditionalParameters(additionalParams)
            .build()

        authService.performTokenRequest(tokenRequest)
        { res, ex ->
            if (res != null) {
                saveToken(res.accessToken.toString())
            } else {
                ex?.printStackTrace()
            }
        }
    }

    private fun saveToken(token: String) {
        lifecycleScope.launch {
            authViewModel.saveToken(token)
            findNavController().navigate(R.id.login)
        }
    }
}
