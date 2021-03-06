package com.homework.coursework.presentation.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.load
import com.homework.coursework.databinding.FragmentProfileBinding
import com.homework.coursework.presentation.adapter.data.UserItem
import com.homework.coursework.presentation.interfaces.BottomNavigationController
import com.homework.coursework.presentation.interfaces.NavigateController
import com.homework.coursework.presentation.ui.profile.elm.Effect
import com.homework.coursework.presentation.ui.profile.elm.Event
import com.homework.coursework.presentation.ui.profile.elm.State
import com.homework.coursework.presentation.utils.*
import vivid.money.elmslie.android.base.ElmFragment

abstract class BaseProfileFragment : ElmFragment<Event, Effect, State>() {

    private var _binding: FragmentProfileBinding? = null
    protected val binding get() = _binding!!
    protected var navigateController: NavigateController? = null
    protected var bottomNavigationController: BottomNavigationController? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is NavigateController) {
            navigateController = context
        }
        if (context is BottomNavigationController) {
            bottomNavigationController = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initErrorRepeat()
    }

    override fun render(state: State) {
        if (state.isLoading) {
            showLoadingScreen()
            return
        }
        if (state.isError) {
            showErrorScreen()
            return
        }
        if (state.isUpdate && state.item is UserItem) {
            showResultScreen()
            updateView(state.item)
        }
    }

    override fun handleEffect(effect: Effect) {
        when (effect) {
            is Effect.UserLoadError -> showToast(effect.error.message)
            Effect.ErrorLogout -> {
                showToast("Something went wrong, try again...")
            }
            Effect.SuccessLogout -> {
                navigateController?.navigateFragment(
                    customFragmentFactory = CustomFragmentFactory.create(
                        FragmentTag.AUTH_FRAGMENT_TAG
                    )
                )
            }
        }
    }

    override fun onStop() {
        super.onStop()
        store.accept(Event.Ui.OnStop)
    }

    override fun onDetach() {
        super.onDetach()
        navigateController = null
    }

    abstract fun initErrorRepeat()

    /**
     * show error layout
     */
    private fun showErrorScreen() {
        with(binding) {
            shStreams.off()
            clResult.visibility = View.GONE
            nsvErrorConnection.visibility = View.VISIBLE
        }
    }

    /**
     * show loading layout
     */
    private fun showLoadingScreen() {
        with(binding) {
            clResult.visibility = View.GONE
            nsvErrorConnection.visibility = View.GONE
            shStreams.startShimmer()
        }
    }

    /**
     * show result screen
     */
    private fun showResultScreen() {
        with(binding) {
            nsvErrorConnection.visibility = View.GONE
            shStreams.off()
            clResult.visibility = View.VISIBLE
        }
    }

    private fun updateView(userItem: UserItem) {
        with(binding) {
            with(userItem) {
                imgProfile.load(avatarUrl)
                tvName.text = name
                tvState.text = getStatusString(userStatus)
                tvState.setTextColor(
                    resources.getColorWrapper(
                        getStatusColor(userStatus),
                        requireContext()
                    )
                )
            }
        }
    }
}
