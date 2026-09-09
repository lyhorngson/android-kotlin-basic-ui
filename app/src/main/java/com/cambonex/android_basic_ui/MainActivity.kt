package com.cambonex.android_basic_ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.google.android.material.color.DynamicColors

/**
 * Main Activity hosting the Telegram-style floating capsule pill bottom navigation bar:
 * - Each navbar item has a full container background (rounded pill when active)
 * - Zero overlap between icon and label, with tight 3dp spacing
 * - True edge-to-edge layout with WindowCompat.setDecorFitsSystemWindows(window, false)
 * - Dynamic system insets handling for top status bar and bottom gesture bar
 * - Seamless fragment switching (Chats, Contact, Settings, Profile)
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Apply Material You dynamic colors if supported
        DynamicColors.applyToActivitiesIfAvailable(application)
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge display so fragment content scrolls transparently behind navigation
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContentView(R.layout.activity_main)

        val fragmentContainer = findViewById<View>(R.id.fragmentContainer)
        val floatingBarCard = findViewById<MaterialCardView>(R.id.floatingBarCard)

        // Apply top status bar inset to fragment container so appbars start cleanly below status bar
        ViewCompat.setOnApplyWindowInsetsListener(fragmentContainer) { view, insets ->
            val statusBarInset = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            view.updatePadding(top = statusBarInset)
            insets
        }

        // Dynamically add navigation bar inset to floating pill bottom margin
        val baseMarginBottom = (20 * resources.displayMetrics.density).toInt()
        ViewCompat.setOnApplyWindowInsetsListener(floatingBarCard) { view, insets ->
            val navBarInset = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
            view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                bottomMargin = baseMarginBottom + navBarInset
            }
            insets
        }

        // Setup full container navbar items
        val tabChats = findViewById<View>(R.id.tabChats)
        val tabContact = findViewById<View>(R.id.tabContact)
        val tabSettings = findViewById<View>(R.id.tabSettings)
        val tabProfile = findViewById<View>(R.id.tabProfile)

        val labelChats = findViewById<TextView>(R.id.labelChats)
        val labelContact = findViewById<TextView>(R.id.labelContact)
        val labelSettings = findViewById<TextView>(R.id.labelSettings)
        val labelProfile = findViewById<TextView>(R.id.labelProfile)

        val tabsWithLabels = listOf(
            tabChats to labelChats,
            tabContact to labelContact,
            tabSettings to labelSettings,
            tabProfile to labelProfile
        )

        fun selectTab(selectedTab: View, fragment: Fragment) {
            tabsWithLabels.forEach { (tab, label) ->
                val isSelected = (tab == selectedTab)
                tab.isSelected = isSelected
                label.setTypeface(null, if (isSelected) android.graphics.Typeface.BOLD else android.graphics.Typeface.NORMAL)
            }
            loadFragment(fragment)
        }

        tabChats.setOnClickListener { selectTab(tabChats, ChatsFragment()) }
        tabContact.setOnClickListener { selectTab(tabContact, ContactFragment()) }
        tabSettings.setOnClickListener { selectTab(tabSettings, SettingsFragment()) }
        tabProfile.setOnClickListener { selectTab(tabProfile, ProfileFragment()) }

        // Select default tab on fresh start
        if (savedInstanceState == null) {
            selectTab(tabChats, ChatsFragment())
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    /**
     * Show interactive Material 3 custom modal bottom sheet
     */
    fun showCustomBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_layout, null)
        bottomSheetDialog.setContentView(view)

        val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
        val tvContent = view.findViewById<TextView>(R.id.tvContent)
        val etInput = view.findViewById<EditText>(R.id.etInput)
        val btnCancel = view.findViewById<Button>(R.id.btnCancel)
        val btnSubmit = view.findViewById<Button>(R.id.btnSubmit)

        tvTitle.text = "Custom Bottom Sheet"
        tvContent.text = "Enter your details below"

        btnCancel.setOnClickListener {
            bottomSheetDialog.dismiss()
            Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
        }

        btnSubmit.setOnClickListener {
            val inputText = etInput.text.toString()
            if (inputText.isNotEmpty()) {
                Toast.makeText(this, "Submitted: $inputText", Toast.LENGTH_SHORT).show()
                bottomSheetDialog.dismiss()
            } else {
                Toast.makeText(this, "Please enter something", Toast.LENGTH_SHORT).show()
            }
        }

        bottomSheetDialog.show()
    }
}