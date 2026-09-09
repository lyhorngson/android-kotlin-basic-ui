package com.cambonex.android_basic_ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar

/**
 * Demo activity showcasing 7 different AppBar / Toolbar styles.
 *
 * Styles included:
 *  1. Small Top App Bar (standard)
 *  2. Primary Colored App Bar
 *  3. Surface Variant / Tonal App Bar
 *  4. Transparent / Flat App Bar
 *  5. Center-Aligned App Bar
 *  6. Medium Top App Bar (CollapsingToolbar, 128dp)
 *  7. Large Top App Bar  (CollapsingToolbar, 180dp)
 */
class AppBarDemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appbar_demo)

        // Wire up navigation & menu clicks for every toolbar
        val toolbarIds = listOf(
            R.id.topAppBarSmall,
            R.id.topAppBarPrimary,
            R.id.topAppBarTonal,
            R.id.topAppBarFlat,
            R.id.topAppBarCentered,
            R.id.topAppBarMedium,
            R.id.topAppBarLarge
        )

        toolbarIds.forEach { id ->
            val toolbar = findViewById<MaterialToolbar>(id)

            // Navigation icon click → go back
            toolbar.setNavigationOnClickListener {
                finish()
            }

            // Menu item clicks
            toolbar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_search -> {
                        Toast.makeText(this, "Search clicked", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.action_more -> {
                        Toast.makeText(this, "More options clicked", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.action_settings -> {
                        Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.action_about -> {
                        Toast.makeText(this, "About clicked", Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> false
                }
            }
        }
    }
}
