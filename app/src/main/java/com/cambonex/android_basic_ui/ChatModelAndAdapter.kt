package com.cambonex.android_basic_ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

enum class MessageStatus {
    NONE,
    SENT,       // single checkmark
    READ,       // double checkmark
    VOICE,      // voice note microphone
    MEDIA       // media attachment paperclip
}

data class ChatItem(
    val id: Long,
    val name: String,
    val initials: String,
    val avatarBgColor: Int,
    val isOnline: Boolean,
    val isVerified: Boolean,
    val timestamp: String,
    val lastMessage: String,
    val status: MessageStatus,
    var unreadCount: Int = 0,
    var isPinned: Boolean = false,
    var isMuted: Boolean = false
)

class ChatListAdapter(
    private val items: MutableList<ChatItem>,
    private val onItemClick: (ChatItem) -> Unit
) : RecyclerView.Adapter<ChatListAdapter.ChatViewHolder>() {

    inner class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val avatarCard: MaterialCardView = view.findViewById(R.id.avatarCard)
        val tvAvatarInitials: TextView = view.findViewById(R.id.tvAvatarInitials)
        val viewOnlineDot: View = view.findViewById(R.id.viewOnlineDot)
        val tvChatName: TextView = view.findViewById(R.id.tvChatName)
        val ivVerified: ImageView = view.findViewById(R.id.ivVerified)
        val tvTimestamp: TextView = view.findViewById(R.id.tvTimestamp)
        val ivStatus: ImageView = view.findViewById(R.id.ivStatus)
        val tvLastMessage: TextView = view.findViewById(R.id.tvLastMessage)
        val tvUnreadBadge: TextView = view.findViewById(R.id.tvUnreadBadge)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_row, parent, false)
        return ChatViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val item = items[position]

        holder.tvChatName.text = item.name
        holder.tvAvatarInitials.text = item.initials
        holder.avatarCard.setCardBackgroundColor(item.avatarBgColor)
        holder.tvAvatarInitials.setTextColor(Color.WHITE)

        holder.viewOnlineDot.visibility = if (item.isOnline) View.VISIBLE else View.GONE
        holder.ivVerified.visibility = if (item.isVerified) View.VISIBLE else View.GONE
        holder.tvTimestamp.text = item.timestamp
        holder.tvLastMessage.text = item.lastMessage

        // Message status delivery icon
        when (item.status) {
            MessageStatus.SENT -> {
                holder.ivStatus.visibility = View.VISIBLE
                holder.ivStatus.setImageResource(R.drawable.ic_msg_check)
            }
            MessageStatus.READ -> {
                holder.ivStatus.visibility = View.VISIBLE
                holder.ivStatus.setImageResource(R.drawable.ic_msg_double_check)
            }
            MessageStatus.VOICE -> {
                holder.ivStatus.visibility = View.VISIBLE
                holder.ivStatus.setImageResource(R.drawable.ic_microphone)
            }
            MessageStatus.MEDIA -> {
                holder.ivStatus.visibility = View.VISIBLE
                holder.ivStatus.setImageResource(R.drawable.ic_paperclip)
            }
            MessageStatus.NONE -> {
                holder.ivStatus.visibility = View.GONE
            }
        }

        // Unread Badge
        if (item.unreadCount > 0) {
            holder.tvUnreadBadge.visibility = View.VISIBLE
            holder.tvUnreadBadge.text = if (item.unreadCount > 99) "99+" else item.unreadCount.toString()
        } else {
            holder.tvUnreadBadge.visibility = View.GONE
        }

        holder.itemView.setOnClickListener { onItemClick(item) }
    }

    fun removeItem(position: Int): ChatItem {
        val removed = items.removeAt(position)
        notifyItemRemoved(position)
        return removed
    }

    fun archiveItem(position: Int): ChatItem {
        val archived = items.removeAt(position)
        notifyItemRemoved(position)
        return archived
    }

    fun getItem(position: Int): ChatItem = items[position]
}
