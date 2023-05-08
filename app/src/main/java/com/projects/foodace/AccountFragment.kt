package com.projects.foodace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.projects.foodace.database.FoodAceRepository
import com.projects.foodace.database.User
import com.projects.foodace.databinding.FragmentAccountBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Fragment parameters keys
const val USERNAME_PARAM = "username"

@AndroidEntryPoint
class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAccountBinding.inflate(inflater, container, false)

        initAccountInfo()

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(username: String) =
            AccountFragment().apply {
                arguments = Bundle().apply {
                    putString(USERNAME_PARAM, username)
                }
            }
    }

    private fun initAccountInfo() {
            val user = MutableLiveData<User>()
            CoroutineScope(Dispatchers.IO).launch {
                user.postValue(
                    FoodAceRepository(requireActivity().application)
                        .getUser(arguments?.getString(USERNAME_PARAM)!!)
                )
            }

            user.observe(viewLifecycleOwner) {
                binding.textView.text = resources.getString(
                    R.string.accountFragmentSalutation, user.value?.username
                )
                // todo: immagine
                binding.profilePic.setImageResource(R.drawable.gamer)
            }
    }
}