import React, { useEffect, useRef, useState } from "react";
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import {
  Globe2,
  Users,
  User,
  Search,
  Send,
  ArrowLeft,
  MessageCircle,
} from "lucide-react";
import "./Community.css";

const ACCENT = "#6C5CE7";
const WS_URL = "/ws"

const getToken = () => localStorage.getItem("accessToken");

const formatTime = (value) =>
  value
    ? new Date(value).toLocaleTimeString([], {
        hour: "2-digit",
        minute: "2-digit",
      })
    : "";

export default function ConnectionsPage({
  onOpenCommunity,
  onOpenSearch,
  connectedUsers,
  refreshConnections,
}) {
  const [view, setView] = useState("connections");
  const [activeChatUser, setActiveChatUser] = useState(null);
  const [messages, setMessages] = useState([]);
  const [draft, setDraft] = useState("");

  const clientRef = useRef(null);
  const activeChatUserRef = useRef(null);
  const bottomRef = useRef(null);

  // map backend message -> UI message
  const toUiMessage = (m, otherUserId) => ({
    id: m.id ?? `${m.createdAt}-${m.senderId}-${Math.random()}`,
    sender: m.senderId === otherUserId ? "other" : "me",
    time: formatTime(m.createdAt),
    text: m.content,
  });

  // keep ref in sync so the websocket callback sees the current chat
  useEffect(() => {
    activeChatUserRef.current = activeChatUser;
  }, [activeChatUser]);

  // websocket connection (once)
  useEffect(() => {
    const client = new Client({
    webSocketFactory: () => new SockJS(WS_URL),
    connectHeaders: { Authorization: `Bearer ${getToken()}` },
    reconnectDelay: 5000,
    onConnect: () => {
      client.subscribe("/user/queue/messages", (frame) => {
        const m = JSON.parse(frame.body);
        const current = activeChatUserRef.current;
      
        // only show it if that chat is open
        if (current && m.senderId === current.userId) {
          setMessages((prev) => [...prev, toUiMessage(m, current.userId)]);
        }
      });
    },
    onStompError: (frame) => console.error("STOMP error:", frame),
    onWebSocketError: (error) => console.error("WebSocket error:", error),
  });

    client.activate();
    clientRef.current = client;

    return () => client.deactivate();
  }, []);

  // load history when a chat is opened
  useEffect(() => {
    if (!activeChatUser) return;

    const loadHistory = async () => {
      try {
        const res = await fetch(
          `/Community/privatemessage/${activeChatUser.userId}?page=0&size=50`,
          { headers: { Authorization: `Bearer ${getToken()}` } }
        );
        if (!res.ok) throw new Error("Failed to load messages");

        const data = await res.json();
        const sorted = [...data].sort(
          (a, b) => new Date(a.createdAt) - new Date(b.createdAt)
        );
        setMessages(sorted.map((m) => toUiMessage(m, activeChatUser.userId)));
      } catch (err) {
        console.error(err);
        setMessages([]);
      }
    };

    loadHistory();
  }, [activeChatUser]);

  // scroll to latest message
  useEffect(() => {
    bottomRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  const openChat = (user) => {
    setMessages([]);
    setActiveChatUser(user);
    setView("chat");
  };

  const backToList = () => {
    setActiveChatUser(null);
    setView("connections");
  };

  const sendMessage = () => {
    const content = draft.trim();
    if (!content || !activeChatUser) return;

    const client = clientRef.current;
    if (!client || !client.connected) return;

    client.publish({
      destination: "/app/private.send",
      body: JSON.stringify({
        recipientId: activeChatUser.userId,
        content,
      }),
    });

    // the server only pushes to the recipient, so add our own message locally
    setMessages((prev) => [
      ...prev,
      {
        id: Date.now(),
        sender: "me",
        time: formatTime(new Date()),
        text: content,
      },
    ]);

    setDraft("");
  };

  const isChatView = view === "chat";

  return (
    <div className="community-page">
      <div className="community-topbar">
        <User size={28} />
        <span className="community-topbar-title">Connect</span>
      </div>

      <div className="community-body">
        <div className="chat-panel">
          <div className="chat-header">
            {isChatView && (
              <button
                onClick={backToList}
                className="back-btn"
                aria-label="Back"
              >
                <ArrowLeft size={20} className="back-icon" />
              </button>
            )}

            {isChatView ? (
              <>
                <div
                  className="chat-header-avatar"
                  style={{
                    backgroundColor: ACCENT,
                    backgroundImage: activeChatUser?.avatarsecureurl
                      ? `url(${activeChatUser.avatarsecureurl})`
                      : "none",
                    backgroundSize: "cover",
                    backgroundPosition: "center",
                  }}
                />

                <h2 className="chat-header-title chat-header-title-muted">
                  {activeChatUser?.fullname}
                </h2>
              </>
            ) : (
              <>
                <div
                  className="chat-header-bar"
                  style={{ backgroundColor: ACCENT }}
                />

                <h2 className="chat-header-title">Connections</h2>
              </>
            )}
          </div>

          {isChatView ? (
            <>
              <div className="messages-list">
                {messages.map((m) => (
                  <div
                    key={m.id}
                    className={`message-row ${
                      m.sender === "me" ? "message-row-me" : "message-row-other"
                    }`}
                    style={{
                      display: "flex",
                      justifyContent: m.sender === "me" ? "flex-end" : "flex-start",
                    }}
                    >
                    <div
                      className={`message-bubble ${
                        m.sender === "me"
                          ? "message-bubble-me"
                          : "message-bubble-other"
                      }`}
                      style={
                        m.sender === "me"
                          ? { backgroundColor: "#D9D3FA", color: "#2D2A4A" }
                          : undefined
                      }
                    >
                      {m.text}
                    </div>
                  </div>
                ))}
                <div ref={bottomRef} />
              </div>

              <div className="composer">
                <div className="composer-row">
                  <input
                    value={draft}
                    onChange={(e) => setDraft(e.target.value)}
                    onKeyDown={(e) => e.key === "Enter" && sendMessage()}
                    placeholder="Send Message here..."
                    className="composer-input"
                  />

                  <button
                    onClick={sendMessage}
                    className="composer-send-btn"
                    style={{ backgroundColor: ACCENT }}
                    aria-label="Send"
                  >
                    <Send size={18} />
                  </button>
                </div>
              </div>
            </>
          ) : (
            <div className="connections-list">
              {connectedUsers.map((user) => (
                <div key={user.userId} className="connection-row">
                  <div className="connection-avatar-wrap">
                    <div
                      className="connection-avatar"
                      style={{
                        backgroundColor: ACCENT,
                        backgroundImage: user.avatarsecureurl
                          ? `url(${user.avatarsecureurl})`
                          : "none",
                        backgroundSize: "cover",
                        backgroundPosition: "center",
                        backgroundRepeat: "no-repeat",
                      }}
                    />

                    <span className="connection-status-dot" />
                  </div>

                  <div className="connection-info">
                    <div className="connection-name">{user.fullname}</div>

                    <div className="connection-lastmsg">@{user.username}</div>
                  </div>

                  <button
                    className="connection-message-btn"
                    style={{ backgroundColor: ACCENT }}
                    onClick={() => openChat(user)}
                    aria-label={`Message ${user.fullname}`}
                  >
                    <MessageCircle size={16} />
                    Message
                  </button>
                </div>
              ))}
            </div>
          )}
        </div>

        <div className="sidebar">
          <button
            onClick={onOpenCommunity}
            className="sidebar-btn sidebar-btn-hover"
            aria-label="Community"
          >
            <Globe2 size={20} className="icon-accent" />
          </button>

          <button
            className="sidebar-btn"
            style={{ backgroundColor: ACCENT }}
            aria-label="Connections"
          >
            <Users size={20} className="icon-white" />
          </button>

          <button
            onClick={onOpenSearch}
            className="sidebar-btn sidebar-btn-hover"
            aria-label="Search"
          >
            <Search size={20} className="icon-accent" />
          </button>
        </div>
      </div>
    </div>
  );
}