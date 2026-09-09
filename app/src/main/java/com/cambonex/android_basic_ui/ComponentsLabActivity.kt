package com.cambonex.android_basic_ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class ComponentsLabActivity : AppCompatActivity() {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var bottomSheetView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_components_lab)

        findViewById<Button>(R.id.btnAppBarDemo).setOnClickListener {
            startActivity(Intent(this, AppBarDemoActivity::class.java))
        }

        findViewById<Button>(R.id.btnBottomNavDemo).setOnClickListener {
            startActivity(Intent(this, BottomNavDemoActivity::class.java))
        }

        findViewById<Button>(R.id.btnShowBottomSheet).setOnClickListener {
            showCustomBottomSheet()
        }

        findViewById<Button>(R.id.btnShowPersistentBottomSheet).setOnClickListener {
            setupPersistentBottomSheet()
        }
    }

    private fun showCustomBottomSheet() {
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

    private fun setupPersistentBottomSheet() {
        bottomSheetView = findViewById(R.id.bottomSheetView)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetView)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetBehavior.peekHeight = 150
        bottomSheetBehavior.isHideable = true

        findViewById<Button>(R.id.btnCollapse).setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        findViewById<Button>(R.id.btnExpand).setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        findViewById<Button>(R.id.btnHide).setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }
}
