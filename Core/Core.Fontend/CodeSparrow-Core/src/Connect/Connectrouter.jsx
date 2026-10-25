import React, { useEffect, useRef, useState } from "react";
import { Search, X, UserPlus } from "lucide-react";
import CommunityChat from "./Communitychat";
import ConnectionsPage from "./Connectionspage";
import "./Community.css";
import noSearchImage from "./assets/search.webp";

const ACCENT = "#6C5CE7";
const PAGE_SIZE = 15;

export default function ConnectRouter({ onBack }) {
  const [page, setPage] = useState("community");
  const [searchOpen, setSearchOpen] = useState(false);
  const [connectedUsers, setConnectedUsers] = useState([]);
  const [query, setQuery] = useState("");
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(false);
  const [loadingMore, setLoadingMore] = useState(false);

  const [currentPage, setCurrentPage] = useState(0);
  const [hasMore, setHasMore] = useState(true);

  const [connected, setConnected] = useState({});
  const [currentUser, setCurrentUser] = useState(null);
  const [connectionsRefreshKey, setConnectionsRefreshKey] = useState(0);


  const searchResultsRef = useRef(null);

  useEffect(() => {
  const fetchCurrentUser = async () => {
    try {
      const token = localStorage.getItem("accessToken");

      const response = await fetch("/profile", {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (response.ok) {
        const data = await response.json();
        setCurrentUser(data);
      }
    } catch (error) {
      console.error(error);
    }
  };

  fetchCurrentUser();
}, []);
  const fetchUsers = async (
    searchQuery,
    pageNumber = 0,
    append = false
  ) => {
    try {
      if (append) {
        setLoadingMore(true);
      } else {
        setLoading(true);
      }

      const token = localStorage.getItem("accessToken");

      const response = await fetch(
        `/connect/userprofile?page=${pageNumber}&size=${PAGE_SIZE}&searchterm=${encodeURIComponent(
          searchQuery
        )}`,
        {
          method: "GET",
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      if (!response.ok) {
        throw new Error(
          `Failed to search users: ${response.status}`
        );
      }

      const data = await response.json();

      const newUsers = data.content || [];
      setConnected((prev) => {
          const next = { ...prev };
          newUsers.forEach((u) => {
            if (u.isFollowing) next[String(u.userId)] = true;
          });
          return next;
        });

      if (!append) {
        setUsers(newUsers);
      } else {
        setUsers((prevUsers) => [
          ...prevUsers,
          ...newUsers,
        ]);
      }

      setCurrentPage(pageNumber);
      setHasMore(!data.last);

    } catch (error) {
      console.error("Error searching users:", error);

      if (!append) {
        setUsers([]);
      }

      setHasMore(false);

    } finally {
      setLoading(false);
      setLoadingMore(false);
    }
  };
  useEffect(() => {
  fetchConnections();
}, []);
const fetchConnections = async () => {
  try {
    const token = localStorage.getItem("accessToken");

    const response = await fetch("/connect/displaychat", {
      method: "GET",
      headers: { Authorization: `Bearer ${token}` },
    });

    if (!response.ok) {
      throw new Error(`Failed to fetch connections: ${response.status}`);
    }

    const data = await response.json();
    setConnectedUsers(data || []);
  } catch (error) {
    console.error("Error fetching connections:", error);
    setConnectedUsers([]);
  }
};
useEffect(() => {
  if (page === "connections") {
    fetchConnections();
  }
}, [page]);
  useEffect(() => {
    if (!searchOpen) {
      return;
    }

    setCurrentPage(0);
    setHasMore(true);

    const timer = setTimeout(() => {
      fetchUsers(query, 0, false);
    }, 300);

    return () => clearTimeout(timer);

  }, [query, searchOpen]);

  const handleSearchScroll = () => {
    const element = searchResultsRef.current;

    if (!element) {
      return;
    }

    if (loading || loadingMore || !hasMore) {
      return;
    }

    const reachedBottom =
      element.scrollTop + element.clientHeight >=
      element.scrollHeight - 50;

    if (reachedBottom) {
      const nextPage = currentPage + 1;

      fetchUsers(query, nextPage, true);
    }
  };



const handleConnect = async (userId) => {
  try {
    const token = localStorage.getItem("accessToken");

    const response = await fetch("/connect/followuser", {
      method: "POST",
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ followingid: userId }),
    });

    if (!response.ok) {
      throw new Error("Failed to connect");
    }

    setConnected((prev) => ({
      ...prev,
      [String(userId)]: true,
    }));

    await fetchConnections();
  } catch (error) {
    console.error("Connect error:", error);
  }
};


  const openSearch = () => {
    setSearchOpen(true);
    setQuery("");
    setUsers([]);
    setCurrentPage(0);
    setHasMore(true);
  };

  const closeSearch = () => {
    setSearchOpen(false);
    setQuery("");
    setUsers([]);
    setCurrentPage(0);
    setHasMore(true);
  };

  if (page === "closed") {
    return (
      <div className="community-closed">
        <button
          onClick={() => setPage("community")}
          className="btn-open-community"
          style={{ backgroundColor: ACCENT }}
        >
          Open Community
        </button>
      </div>
    );
  }

  return (
    <>
      {page === "community" && (
        <CommunityChat
          onOpenConnections={() => setPage("connections")}
          onOpenSearch={openSearch}
        />
      )}

      {page === "connections" && (
  <ConnectionsPage
    onOpenCommunity={() => setPage("community")}
    onOpenSearch={openSearch}
    connectedUsers={connectedUsers}
    refreshConnections={fetchConnections}
  />
)}

      {searchOpen && (
        <div className="search-overlay">
          <div className="search-modal">

            <button
              onClick={closeSearch}
              className="search-close-btn"
              aria-label="Close"
            >
              <X size={18} />
            </button>

            <div className="search-input-row">
              <Search
                size={18}
                className="search-input-icon"
              />

              <input
                autoFocus
                value={query}
                onChange={(e) => setQuery(e.target.value)}
                placeholder="Search to Connect..."
                className="search-input"
              />
            </div>

            <hr className="search-divider" />

            <div
              className="search-results"
              ref={searchResultsRef}
              onScroll={handleSearchScroll}
            >
              {!loading &&
                  !loadingMore &&
                  users.length === 0 && (
                    <div className="search-no-results">
                      <img
                        src={noSearchImage}
                        alt="Search for users"
                        className="no-search-image"
                      />

                      <div>
                        {query
                          ? "No users found"
                          : "Search for people to connect"}
                      </div>
                    </div>
                  )}

              {!loading &&
                users.map((u) => (
                  <div
                    key={u.userId}
                    className="search-result-row"
                  >
                    <div className="search-result-user">

                      <div
                        className="search-result-avatar"
                        style={{
                          backgroundColor: ACCENT,
                          backgroundImage:
                            u.avatarsecureurl
                              ? `url(${u.avatarsecureurl})`
                              : "none",
                          backgroundSize: "cover",
                          backgroundPosition: "center",
                          backgroundRepeat: "no-repeat",
                        }}
                      />

                      <div>
                        <div className="search-result-username">
                          @{u.username}
                        </div>

                        <div className="search-result-fullname">
                          {u.fullname}
                        </div>
                      </div>

                    </div>

                    
                    
{currentUser && String(u.userId) !== String(currentUser.userId) && (
  <button
    onClick={() => handleConnect(u.userId)}
    disabled={!!connected[String(u.userId)]}
    className="connect-btn"
    style={{
      backgroundColor: connected[String(u.userId)] ? "#999" : ACCENT,
    }}
  >
    <UserPlus size={16} />
    {connected[String(u.userId)] ? "Connected" : "Connect"}
  </button>
)}


                  </div>
                ))}

              {loadingMore && (
                <div className="search-no-results">
                  Loading more users...
                </div>
              )}

              {!loading &&
                !loadingMore &&
                users.length > 0 &&
                !hasMore && (
                  <div className="search-no-results">
                  
                  </div>
                )}
            </div>
          </div>
        </div>
      )}
    </>
  );
}
