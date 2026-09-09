package com.cambonex.android_basic_ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.view.HapticFeedbackConstants
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

/**
 * ItemTouchHelper callback for Telegram-style chat swipe actions:
 * - Swipe Right: Archive (Green with ic_folder_minus)
 * - Swipe Left: Delete (Red with ic_trash)
 * - Haptic feedback when dragging past activation threshold
 */
class TelegramChatSwipeCallback(
    private val context: Context,
    private val onSwipeArchive: (Int) -> Unit,
    private val onSwipeDelete: (Int) -> Unit
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 13f * context.resources.displayMetrics.density
        isFakeBoldText = true
    }

    private val archiveIcon: Drawable? = ContextCompat.getDrawable(context, R.drawable.ic_folder_minus)?.mutate()
    private val deleteIcon: Drawable? = ContextCompat.getDrawable(context, R.drawable.ic_trash)?.mutate()

    private var hasHapticTriggered = false

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        if (position == RecyclerView.NO_POSITION) return
        if (direction == ItemTouchHelper.RIGHT) {
            onSwipeArchive(position)
        } else if (direction == ItemTouchHelper.LEFT) {
            onSwipeDelete(position)
        }
        hasHapticTriggered = false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView
        val cornerRadius = 16f * context.resources.displayMetrics.density

        // Trigger light haptic feedback once threshold is crossed
        val threshold = itemView.width * 0.35f
        if (Math.abs(dX) > threshold && !hasHapticTriggered) {
            itemView.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
            hasHapticTriggered = true
        } else if (Math.abs(dX) <= threshold) {
            hasHapticTriggered = false
        }

        if (dX > 0) {
            // Swipe Right → Archive (Emerald Green)
            bgPaint.color = Color.parseColor("#2E7D32")
            val bgRect = RectF(
                itemView.left.toFloat(),
                itemView.top.toFloat() + 4f,
                itemView.left.toFloat() + dX,
                itemView.bottom.toFloat() - 4f
            )
            c.drawRoundRect(bgRect, cornerRadius, cornerRadius, bgPaint)

            // Draw Archive Icon
            archiveIcon?.let { icon ->
                icon.setTint(Color.WHITE)
                val iconSize = (24 * context.resources.displayMetrics.density).toInt()
                val iconMargin = (24 * context.resources.displayMetrics.density).toInt()
                val iconTop = itemView.top + (itemView.height - iconSize) / 2
                val iconLeft = itemView.left + iconMargin
                val iconRight = iconLeft + iconSize
                val iconBottom = iconTop + iconSize
                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                if (dX > iconMargin) {
                    icon.draw(c)
                    c.drawText("Archive", iconRight + 20f, iconTop + (iconSize * 0.7f), textPaint)
                }
            }
        } else if (dX < 0) {
            // Swipe Left → Delete (Crimson Red)
            bgPaint.color = Color.parseColor("#D32F2F")
            val bgRect = RectF(
                itemView.right.toFloat() + dX,
                itemView.top.toFloat() + 4f,
                itemView.right.toFloat(),
                itemView.bottom.toFloat() - 4f
            )
            c.drawRoundRect(bgRect, cornerRadius, cornerRadius, bgPaint)

            // Draw Delete Icon
            deleteIcon?.let { icon ->
                icon.setTint(Color.WHITE)
                val iconSize = (24 * context.resources.displayMetrics.density).toInt()
                val iconMargin = (24 * context.resources.displayMetrics.density).toInt()
                val iconTop = itemView.top + (itemView.height - iconSize) / 2
                val iconRight = itemView.right - iconMargin
                val iconLeft = iconRight - iconSize
                val iconBottom = iconTop + iconSize
                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                if (Math.abs(dX) > iconMargin) {
                    icon.draw(c)
                    c.drawText("Delete", iconLeft - 100f, iconTop + (iconSize * 0.7f), textPaint)
                }
            }
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}
