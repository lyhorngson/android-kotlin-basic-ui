package com.cambonex.android_basic_ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

/** Chats tab — flat AppBar with hamburger menu, interactive chat list & swipe actions */
class ChatsFragment : Fragment() {

    private lateinit var adapter: ChatListAdapter
    private val chatList = mutableListOf<ChatItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_chats, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbarChats)
        toolbar?.setNavigationOnClickListener {
            Toast.makeText(requireContext(), "Menu clicked", Toast.LENGTH_SHORT).show()
        }
        toolbar?.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_chats_search -> {
                    Toast.makeText(requireContext(), "Search chats", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.action_chats_more -> {
                    Toast.makeText(requireContext(), "Chats options", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

        // Setup Chat RecyclerView
        val rvChatList = view.findViewById<RecyclerView>(R.id.rvChatList)
        setupSampleChats()

        adapter = ChatListAdapter(chatList) { chat ->
            Toast.makeText(requireContext(), "Opened chat: ${chat.name}", Toast.LENGTH_SHORT).show()
        }

        rvChatList.layoutManager = LinearLayoutManager(requireContext())
        rvChatList.adapter = adapter

        // Setup Telegram-style Swipe to Archive / Delete
        val swipeCallback = TelegramChatSwipeCallback(
            context = requireContext(),
            onSwipeArchive = { position ->
                val item = adapter.archiveItem(position)
                Snackbar.make(view, "${item.name} archived", Snackbar.LENGTH_LONG)
                    .setAction("Undo") {
                        chatList.add(position, item)
                        adapter.notifyItemInserted(position)
                    }
                    .setAnchorView(view.findViewById(R.id.fabNewChat))
                    .show()
            },
            onSwipeDelete = { position ->
                val item = adapter.removeItem(position)
                Snackbar.make(view, "${item.name} deleted", Snackbar.LENGTH_LONG)
                    .setAction("Undo") {
                        chatList.add(position, item)
                        adapter.notifyItemInserted(position)
                    }
                    .setAnchorView(view.findViewById(R.id.fabNewChat))
                    .show()
            }
        )
        ItemTouchHelper(swipeCallback).attachToRecyclerView(rvChatList)

        // New Chat FAB
        view.findViewById<FloatingActionButton>(R.id.fabNewChat)?.setOnClickListener {
            Toast.makeText(requireContext(), "Compose new message", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupSampleChats() {
        if (chatList.isNotEmpty()) return

        chatList.addAll(
            listOf(
                ChatItem(
                    id = 1,
                    name = "Telegram News",
                    initials = "TG",
                    avatarBgColor = Color.parseColor("#2AABEE"),
                    isOnline = false,
                    isVerified = true,
                    timestamp = "15:40",
                    lastMessage = "Discover the new Telegram Stories feature with dual camera support!",
                    status = MessageStatus.READ,
                    unreadCount = 2
                ),
                ChatItem(
                    id = 2,
                    name = "Saved Messages",
                    initials = "★",
                    avatarBgColor = Color.parseColor("#7E57C2"),
                    isOnline = false,
                    isVerified = false,
                    timestamp = "14:12",
                    lastMessage = "Project specs, design links, and research files",
                    status = MessageStatus.SENT,
                    unreadCount = 0
                ),
                ChatItem(
                    id = 3,
                    name = "Anna Smith",
                    initials = "AS",
                    avatarBgColor = Color.parseColor("#26A69A"),
                    isOnline = true,
                    isVerified = false,
                    timestamp = "13:45",
                    lastMessage = "Voice message (0:24)",
                    status = MessageStatus.VOICE,
                    unreadCount = 1
                ),
                ChatItem(
                    id = 4,
                    name = "Android Dev Community",
                    initials = "AD",
                    avatarBgColor = Color.parseColor("#FF7043"),
                    isOnline = false,
                    isVerified = true,
                    timestamp = "11:20",
                    lastMessage = "Material 3 expressive components and Jetpack libraries update",
                    status = MessageStatus.READ,
                    unreadCount = 14
                ),
                ChatItem(
                    id = 5,
                    name = "Design System Lab",
                    initials = "DS",
                    avatarBgColor = Color.parseColor("#EC407A"),
                    isOnline = false,
                    isVerified = false,
                    timestamp = "Yesterday",
                    lastMessage = "Shared vector assets and layout specs",
                    status = MessageStatus.MEDIA,
                    unreadCount = 0
                ),
                ChatItem(
                    id = 6,
                    name = "Alexander Wright",
                    initials = "AW",
                    avatarBgColor = Color.parseColor("#66BB6A"),
                    isOnline = true,
                    isVerified = false,
                    timestamp = "Yesterday",
                    lastMessage = "See you at the tech conference tomorrow 👍",
                    status = MessageStatus.READ,
                    unreadCount = 0
                ),
                ChatItem(
                    id = 7,
                    name = "Support Team",
                    initials = "ST",
                    avatarBgColor = Color.parseColor("#5C6BC0"),
                    isOnline = false,
                    isVerified = false,
                    timestamp = "Mon",
                    lastMessage = "Your ticket #48102 has been resolved successfully",
                    status = MessageStatus.SENT,
                    unreadCount = 0
                ),
                ChatItem(
                    id = 8,
                    name = "Cloud Storage Bot",
                    initials = "CB",
                    avatarBgColor = Color.parseColor("#FFA726"),
                    isOnline = false,
                    isVerified = false,
                    timestamp = "Sun",
                    lastMessage = "Backup completed successfully (1.4 GB)",
                    status = MessageStatus.READ,
                    unreadCount = 0
                )
            )
        )
    }
}

/** Contact tab — primary colored AppBar */
class ContactFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_contact, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbarContact)
        toolbar?.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_contact_search -> {
                    Toast.makeText(requireContext(), "Search contacts", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.action_contact_add -> {
                    Toast.makeText(requireContext(), "Add new contact", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.action_contact_grid -> {
                    Toast.makeText(requireContext(), "Toggle contact grid", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.action_contact_more -> {
                    Toast.makeText(requireContext(), "Contact options", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }
}

/** Settings tab — large collapsing title AppBar */
class SettingsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_settings, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbarSettings)
        toolbar?.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_settings_theme -> {
                    val isNight = (resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK) == android.content.res.Configuration.UI_MODE_NIGHT_YES
                    val newMode = if (isNight) AppCompatDelegate.MODE_NIGHT_NO else AppCompatDelegate.MODE_NIGHT_YES
                    AppCompatDelegate.setDefaultNightMode(newMode)
                    true
                }
                R.id.action_settings_more -> {
                    Toast.makeText(requireContext(), "Settings options", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

        view.findViewById<View?>(R.id.cardAppBarDemo)?.setOnClickListener {
            startActivity(Intent(requireContext(), AppBarDemoActivity::class.java))
        }

        view.findViewById<View?>(R.id.cardBottomSheet)?.setOnClickListener {
            (activity as? MainActivity)?.showCustomBottomSheet()
        }

        view.findViewById<View?>(R.id.cardComponentsLab)?.setOnClickListener {
            startActivity(Intent(requireContext(), ComponentsLabActivity::class.java))
        }
    }
}

/** Profile tab — transparent, center-aligned AppBar */
class ProfileFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_profile, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbarProfile)
        toolbar?.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_profile_edit -> {
                    Toast.makeText(requireContext(), "Edit profile", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.action_profile_more -> {
                    Toast.makeText(requireContext(), "Profile options", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }
}
