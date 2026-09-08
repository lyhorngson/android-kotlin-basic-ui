package com.cambonex.android_basic_ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * Demo activity showing Telegram-style bottom navigation:
 *
 * - Transparent bottom bar that content scrolls behind
 * - Thin divider line instead of elevation shadow
 * - No Material 3 pill indicator (just color change)
 * - Each fragment has its OWN AppBar style:
 *     Chats    → flat surface, hamburger menu
 *     Contact  → primary colored
 *     Settings → large collapsing title
 *     Profile  → transparent, centered title
 */
class BottomNavDemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_nav_demo)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        // Load default fragment
        if (savedInstanceState == null) {
            loadFragment(ChatsFragment())
        }

        // Switch fragments on tab selection
        bottomNav.setOnItemSelectedListener { item ->
            val fragment: Fragment = when (item.itemId) {
                R.id.nav_chats -> ChatsFragment()
                R.id.nav_contact -> ContactFragment()
                R.id.nav_settings -> SettingsFragment()
                R.id.nav_profile -> ProfileFragment()
                else -> ChatsFragment()
            }
            loadFragment(fragment)
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
