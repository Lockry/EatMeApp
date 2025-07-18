package com.romeo.eatmeapp.adminpanel.ui

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.romeo.eatmeapp.R

class AdminPanelSettings : Fragment() {

    companion object {
        fun newInstance() = AdminPanelSettings()
    }

    private val viewModel: AdminPanelSettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_admin_panel_settings, container, false)
    }
}