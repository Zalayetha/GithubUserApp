package com.zaghy.githubuser.ui.settings

import android.os.Bundle
import android.widget.CompoundButton
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.zaghy.githubuser.R
import com.zaghy.githubuser.data.datastore.SettingsPreferences
import com.zaghy.githubuser.data.datastore.dataStore
import com.zaghy.githubuser.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var  binding:ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val viewModel:SettingsViewModel by viewModels {
            SettingsViewModelFactory(SettingsPreferences.getInstance(this.dataStore))
        }

        val switchTheme = binding.switchTheme

        viewModel.getThemeSettings().observe(this){isDarkActivated->
            if (isDarkActivated) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
            }
        }
        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            viewModel.saveThemeSetting(isChecked)
        }
    }
}