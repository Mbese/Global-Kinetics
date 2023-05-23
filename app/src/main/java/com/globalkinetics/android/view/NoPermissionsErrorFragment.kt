package com.globalkinetics.android.view

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.globalkinetics.android.R


class NoPermissionsErrorFragment : Fragment(R.layout.fragment_no_permissions_error){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btn = view.findViewById<Button>(R.id.go_to_settings_button)
        btn.setOnClickListener {
            openSettings()
        }
    }

    private fun openSettings() {
        val intent = Intent(Settings.ACTION_SETTINGS)
        startActivity(intent)
    }
}