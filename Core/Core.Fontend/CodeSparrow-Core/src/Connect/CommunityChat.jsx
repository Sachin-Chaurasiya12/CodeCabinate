import React, { useState, useEffect, useRef } from "react";
import { Globe2, Users, User, Search, Send, ShieldCheck } from "lucide-react";
import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";
import "./Community.css";

const ACCENT = "#6C5CE7";
const API_BASE = "/Community/messages";
const WS_URL = "/ws";

const communityRules = [
  "Be respectful. No harassment, hate speech, or personal attacks.",
  "No spam, self-promotion, or unsolicited advertising.",
  "Do not share another member's private information.",
  "Keep discussions relevant to coding, DSA, and CodeCabinate topics.",
  "No plagiarized or paid solutions posing as your own work.",
  "Report abusive behavior instead of engaging in conflict.",
  "Violation of these rules may result in a temporary or permanent ban.",
];

const formatTime = (createdAt) => {
  if (!createdAt) return "";
  const d = new Date(createdAt);
  return d.toLocaleTimeString([], { hour: "2-digit", minute: "2-digit" });
};

const getCurrentUserId = () => {
  try {
    const token = localStorage.getItem("accessToken");
    if (!token) return null;

    const payload = JSON.parse(atob(token.split(".")[1]));

    const id =
      payload.userId ??
      payload.id ??
      payload.sub ??
      null;

    return id != null ? Number(id) : null;
  } catch {
    return null;
  }
};

export default function CommunityChat({ onOpenConnections, onOpenSearch }) {
  const [messages, setMessages] = useState([]);
  const [draft, setDraft] = useState("");
  const [hasJoined, setHasJoined] = useState(false);
  const [agreed, setAgreed] = useState(false);
  const [termsLoading, setTermsLoading] = useState(true);
  const stompClientRef = useRef(null);
  const messagesEndRef = useRef(null);
  const currentUserId = getCurrentUserId();

  useEffect(() => {
    if (!hasJoined) return;

    const token = localStorage.getItem("accessToken");

    const client = new Client({
        webSocketFactory: () => new SockJS(WS_URL),
        connectHeaders: {
            Authorization: `Bearer ${token}`,
        },
        reconnectDelay: 5000,

        onConnect: () => {

            client.subscribe("/topic/community", (msg) => {
                const body = JSON.parse(msg.body);

                setMessages((prev) => {
                    const exists = prev.some(
                      (m) => String(m.id) === String(body.id)
                    );
                  
                    if (exists) {
                      return prev;
                    }
                  
                    return [...prev, body];
                  });
            });
        },

        onStompError: (frame) => {
            console.error("STOMP error:", frame);
        },

        onWebSocketError: (error) => {
            console.error("WebSocket error:", error);
        },
    });

    stompClientRef.current = client;
    fetch(API_BASE, {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        })
            .then((res) => {
                if (!res.ok) {
                    throw new Error(`HTTP ${res.status}`);
                }
              
                return res.json();
            })
            .then((data) => {

                if (Array.isArray(data)) {
                    data.forEach((message, index) => {
                    });
                }
              
                if (!Array.isArray(data)) {
                    setMessages([]);
                    return;
                }
              
                const uniqueMessages = Array.from(
                    new Map(
                        data.map((message) => [
                            String(message.id),
                            message,
                        ])
                    ).values()
                );
              
              
                setMessages(
                  uniqueMessages.sort(
                    (a, b) => new Date(a.createdAt) - new Date(b.createdAt)
                  )
                );
            })
            .catch((error) => {
                setMessages([]);
            });
          
        client.activate();  
          
        return () => {
        
            client.deactivate();
        
            stompClientRef.current = null;
        };
    }, [hasJoined]);

  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  const sendMessage = () => {
    if (!draft.trim() || !stompClientRef.current?.connected) return;
    stompClientRef.current.publish({
      destination: "/app/community.send",
      body: JSON.stringify({ content: draft.trim(), replyToMessageId: null }),
    });
    setDraft("");
  };
  useEffect(() => {
    const token = localStorage.getItem("accessToken");

    if (!token) {
        setTermsLoading(false);
        return;
    }

    fetch("/Community/terms", {
        method: "GET",
        headers: {
            Authorization: `Bearer ${token}`,
        },
    })
        .then((res) => {
            if (!res.ok) {
                throw new Error(`HTTP ${res.status}`);
            }

            return res.json();
        })
        .then((termsChecked) => {
              setHasJoined(termsChecked === true);
          })
        .catch((error) => {
            console.error("Failed to check community terms:", error);
            setHasJoined(false);
        })
        .finally(() => {
            setTermsLoading(false);
        });
}, []);
const joinCommunity = async () => {
    if (!agreed) return;

    const token = localStorage.getItem("accessToken");

    try {
        const response = await fetch("/Community/terms", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
            },
            body: JSON.stringify({
                termreq: true,
            }),
        });

        if (!response.ok) {
            throw new Error(`HTTP ${response.status}`);
        }

        // Only show the community after backend successfully saves it
        setHasJoined(true);
    } catch (error) {
        console.error("Failed to save community terms:", error);
    }
};
if (termsLoading) {
    return null;
}
  return (
    <div className="community-page">
      <div className="community-topbar">
        <User size={28} />
        <span className="community-topbar-title">Connect</span>
      </div>

      <div className="community-body">
        <div className={`chat-panel ${!hasJoined ? "chat-panel-terms" : ""}`}>
          <div className="chat-header">
            <div className="chat-header-bar" style={{ backgroundColor: ACCENT }} />
            <h2 className="chat-header-title">Community</h2>
          </div>

          {!hasJoined ? (
            <div className="rules-gate">
              <div className="rules-card">
                <div className="rules-card-icon" style={{ backgroundColor: ACCENT }}>
                  <ShieldCheck size={28} className="icon-white" />
                </div>

                <h2 className="rules-title">Welcome to the Community</h2>
                <p className="rules-description">
                  The Community space exists so CodeCabinate members can connect, ask questions,
                  share DSA insights, and help each other grow as developers. To keep this a
                  safe and productive space for everyone, please read and agree to the rules
                  below before you start chatting.
                </p>

                <h3 className="rules-subtitle">Community Guidelines</h3>
                <ul className="rules-list">
                  {communityRules.map((rule, idx) => (
                    <li key={idx} className="rules-list-item">
                      {rule}
                    </li>
                  ))}
                </ul>

                <label className="rules-agree-row">
                  <input
                    type="checkbox"
                    checked={agreed}
                    onChange={(e) => setAgreed(e.target.checked)}
                    className="rules-agree-checkbox"
                  />
                  <span>I have read and agree to follow the Community Guidelines</span>
                </label>

                <button
                  className="join-community-btn"
                  style={{ backgroundColor: ACCENT }}
                  disabled={!agreed}
                  onClick={joinCommunity}
                >
                  Join Community
                </button>
              </div>
            </div>
          ) : (
            <>
              <div className="messages-list">
                {messages.map((m, idx) => {
                  const isMe = m.senderUserId === currentUserId;
                  return (
                    <div
                      key={m.id ?? idx}
                      className={`message-row ${isMe ? "message-row-me" : "message-row-other"}`}
                    >
                      {!isMe && (
                        <span className="message-meta" style={{ color: ACCENT }}>
                          {formatTime(m.createdAt)} | User {m.senderUserId}
                        </span>
                      )}
                      <div
                        className={`message-bubble ${isMe ? "message-bubble-me" : "message-bubble-other"}`}
                        style={isMe ? { backgroundColor: "#D9D3FA" } : undefined}
                      >
                        {m.content}
                      </div>
                    </div>
                  );
                })}
                <div ref={messagesEndRef} />
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
          )}
        </div>

        <div className="sidebar">
          <button
            className="sidebar-btn"
            style={{ backgroundColor: ACCENT }}
            aria-label="Community"
          >
            <Globe2 size={20} className="icon-white" />
          </button>
          <button
            onClick={onOpenConnections}
            className="sidebar-btn sidebar-btn-hover"
            aria-label="Connections"
          >
            <Users size={20} className="icon-accent" />
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