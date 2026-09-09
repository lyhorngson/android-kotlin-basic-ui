package com.cambonex.android_basic_ui

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.graphics.Typeface
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import com.google.android.material.card.MaterialCardView

/**
 * Demo activity showing Telegram-style floating capsule pill bottom navigation:
 * - Each navbar item has a full container background (rounded pill when active)
 * - Zero overlap between icon and label, with tight 3dp spacing
 * - True edge-to-edge layout with WindowCompat
 * - Fragment content scrolls transparently underneath the floating pill bar
 * - Dynamic window insets handling for system navigation bar / gesture pill
 */
class BottomNavDemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge display so fragment content flows behind system navigation
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContentView(R.layout.activity_bottom_nav_demo)

        val fragmentContainer = findViewById<View>(R.id.fragmentContainer)
        val floatingBarCard = findViewById<MaterialCardView>(R.id.floatingBarCard)

        // Apply top status bar inset to fragment container
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

        // Load default fragment
        if (savedInstanceState == null) {
            selectTab(tabChats, ChatsFragment())
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
