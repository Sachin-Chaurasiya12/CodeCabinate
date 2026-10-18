import { useState, useEffect, useRef } from "react";
import logo from "./assets/CodeCabinateMain.png";
import footerlogo from "./assets/CodeCabinateLogo.png"
import { useNavigate, Outlet, useLocation } from "react-router-dom";
import { motion, AnimatePresence } from "framer-motion";

export default function LayoutShell() {
  const [notifOpen, setNotifOpen] = useState(false);
  const [profileOpen, setProfileOpen] = useState(false);
  const [profile, setProfile] = useState({ name: "", avatarSecureUrl: null });
  const notifRef = useRef(null);
  const profileRef = useRef(null);
  const searchRef = useRef(null);
  const [menus, setMenus] = useState([]);
  const navigate = useNavigate();
  const location = useLocation();
  const [searchFocused, setSearchFocused] = useState(false);
  const [mobileNavOpen, setMobileNavOpen] = useState(false);
  const [mobileSearchOpen, setMobileSearchOpen] = useState(false);

  const [windowWidth, setWindowWidth] = useState(window.innerWidth);
  useEffect(() => {
    const handleResize = () => setWindowWidth(window.innerWidth);
    window.addEventListener("resize", handleResize);
    return () => window.removeEventListener("resize", handleResize);
  }, []);

  const isMobile = windowWidth < 640;
  const isTablet = windowWidth >= 640 && windowWidth < 1024;
  const isCompactNav = windowWidth < 1024;

  const handleLogout = async () => {
    try {
      await fetch("/api/auth/logout", {
        method: "POST",
        credentials: "include",
        headers: {
          "Content-Type": "application/json",
        },
      });
    } catch (error) {
      console.error("Logout failed : ", error);
    } finally {
      localStorage.removeItem("accessToken");
      navigate("/login", { replace: true });
    }
  };

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const token = localStorage.getItem("accessToken");
        const response = await fetch("/profile", {
          method: "GET",
          credentials: "include",
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        if (!response.ok) {
          console.error("Profile fetch failed:", response.status, response.statusText);
          return;
        }

        const data = await response.json();

        setProfile({
          name: data.fullname || data.name || data.fullName || "",
          avatarSecureUrl: data.avatarSecureUrl || null,
        });
      } catch (error) {
        console.error("Profile fetch error:", error);
      }
    };
    fetchProfile();
  }, []);

  useEffect(() => {
    const fetchMenus = async () => {
      try {
        const token = localStorage.getItem("accessToken");
        const response = await fetch("/layout/menu", {
          method: "GET",
          credentials: "include",
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        if (!response.ok) {
          throw new Error("Failed to fetch menus");
        }

        const data = await response.json();
        setMenus(data);
      } catch (error) {
        console.error("Menu fetch failed:", error);
      }
    };

    fetchMenus();
  }, []);

  const activePath = location.pathname;

  useEffect(() => {
    const handler = (e) => {
      if (notifRef.current && !notifRef.current.contains(e.target)) {
        setNotifOpen(false);
      }
      if (profileRef.current && !profileRef.current.contains(e.target)) {
        setProfileOpen(false);
      }
      if (searchRef.current && !searchRef.current.contains(e.target)) {
        setSearchFocused(false);
      }
    };
    document.addEventListener("mousedown", handler);
    return () => document.removeEventListener("mousedown", handler);
  }, []);

  useEffect(() => {
    setMobileNavOpen(false);
  }, [activePath]);

  const goToProfile = () => {
    setProfileOpen(false);
    navigate("/internal/profile");
  };

  // Items placed in the top navbar
  const topbarItems = menus.filter((item) => item.placement === "topbar");

  // Everything that isn't topbar goes into the profile dropdown grid
  const gridItems = menus.filter((item) => item.placement !== "topbar");

  const renderNavItems = (forDrawer) =>
    topbarItems.map((item) => {
      const isLogout = item.name === "Logout";
      const active =
        !isLogout &&
        (activePath === item.route || activePath.startsWith(item.route + "/"));

      return (
        <motion.div
          key={item.id}
          onClick={() => {
            if (isLogout) {
              handleLogout();
              return;
            }
            if (item.route) {
              navigate(item.route);
            }
          }}
          whileHover={{
            backgroundColor: isLogout ? "#FEF2F2" : active ? "#F0EDFF" : "#F8F7FC",
          }}
          whileTap={{ scale: 0.97 }}
          style={{
            ...styles.navItem,
            ...(forDrawer ? styles.navItemDrawer : {}),
            background: active ? "#F0EDFF" : "transparent",
            color: isLogout ? "#DC2626" : active ? "#534AB7" : "#5F5E5A",
            fontWeight: active ? 600 : 500,
          }}
        >
          <span
            style={{
              ...styles.navIcon,
              color: isLogout ? "#DC2626" : active ? "#534AB7" : "#77736E",
            }}
          >
            <i className={`bi ${item.icon}`}></i>
          </span>
          {(forDrawer || !isCompactNav) && <span style={styles.navLabel}>{item.name}</span>}
        </motion.div>
      );
    });

  return (
    <div style={styles.wrapper}>
      {/* TOPBAR */}
      <header style={{ ...styles.topbar, padding: isMobile ? "0 10px" : "0 16px" }}>
        {/* MOBILE MENU TOGGLE */}
        {isCompactNav && (
          <motion.div
            whileTap={{ scale: 0.9 }}
            onClick={() => setMobileNavOpen((v) => !v)}
            style={styles.hamburgerBtn}
          >
            <i className={`bi ${mobileNavOpen ? "bi-x-lg" : "bi-list"}`}></i>
          </motion.div>
        )}

        {/* LOGO */}
        <div
          style={{
            ...styles.brandContainer,
            height: 80,
            overflow: "hidden",
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
          }}
          onClick={() => navigate("/dashboard")}
        >
          <img
            src={logo}
            alt="CodeCabinate Logo"
            style={{
              ...styles.logo,
              width: isMobile ? 140 : isTablet ? 180 : 220,
              height: "auto",
              maxHeight: 180,
              objectFit: "contain",
            }}
          />
        </div>

        {/* NAVIGATION MENU - desktop inline, tablet icon-only, mobile hidden (drawer) */}
        {!isMobile && (
          <nav style={{ ...styles.navList, gap: isTablet ? 2 : 4 }}>{renderNavItems(false)}</nav>
        )}

        {/* RIGHT SIDE - SEARCH, NOTIFICATIONS, PROFILE */}
        <div style={{ ...styles.rightSection, gap: isMobile ? 8 : 12 }}>
          {/* SEARCH BAR - collapses to icon on mobile */}
          {isMobile ? (
            <div ref={searchRef} style={{ position: "relative" }}>
              <motion.div
                whileTap={{ scale: 0.9 }}
                style={styles.iconBtn}
                onClick={() => setMobileSearchOpen((v) => !v)}
              >
                <i className="bi bi-search" style={{ fontSize: 16 }}></i>
              </motion.div>
              <AnimatePresence>
                {mobileSearchOpen && (
                  <motion.div
                    initial={{ opacity: 0, y: -10 }}
                    animate={{ opacity: 1, y: 0 }}
                    exit={{ opacity: 0, y: -10 }}
                    style={styles.mobileSearchPanel}
                  >
                    <i className="bi bi-search" style={styles.searchIcon} />
                    <input
                      autoFocus
                      placeholder="Search..."
                      style={{ ...styles.search, marginLeft: 8 }}
                    />
                  </motion.div>
                )}
              </AnimatePresence>
            </div>
          ) : (
            <div
              ref={searchRef}
              style={{
                ...styles.searchWrapper,
                width: searchFocused ? (isTablet ? 200 : 280) : isTablet ? 110 : 140,
              }}
            >
              <i className="bi bi-search" style={styles.searchIcon} />
              <motion.input
                placeholder="Search..."
                style={styles.search}
                onFocus={() => setSearchFocused(true)}
                onBlur={() => setSearchFocused(false)}
              />
            </div>
          )}

          {/* NOTIFICATIONS ICON */}
          <div ref={notifRef} style={styles.notifContainer}>
            <motion.div
              whileTap={{ scale: 0.9 }}
              style={styles.iconBtn}
              onClick={() => setNotifOpen((v) => !v)}
            >
              🔔
            </motion.div>

            <AnimatePresence>
              {notifOpen && (
                <motion.div
                  initial={{ opacity: 0, y: -10 }}
                  animate={{ opacity: 1, y: 0 }}
                  exit={{ opacity: 0, y: -10 }}
                  style={{
                    ...styles.notifPanel,
                    width: isMobile ? 200 : 220,
                    right: isMobile ? -40 : 0,
                  }}
                >
                  <p>No new notifications</p>
                </motion.div>
              )}
            </AnimatePresence>
          </div>

          {/* PROFILE DROPDOWN */}
          <div ref={profileRef} style={styles.profileContainer}>
            <motion.div
              whileHover={{ scale: 1.1 }}
              onClick={() => setProfileOpen((v) => !v)}
              style={styles.avatar}
            >
              {profile.avatarSecureUrl ? (
                <img
                  src={profile.avatarSecureUrl}
                  alt="Profile Avatar"
                  style={{
                    width: "100%",
                    height: "100%",
                    borderRadius: "50%",
                    objectFit: "cover",
                  }}
                />
              ) : (
                "U"
              )}
            </motion.div>

            <AnimatePresence>
              {profileOpen && (
                <motion.div
                  initial={{ opacity: 0, y: -10 }}
                  animate={{ opacity: 1, y: 0 }}
                  exit={{ opacity: 0, y: -10 }}
                  style={{
                    ...styles.profilePanel,
                    width: isMobile ? "calc(100vw - 32px)" : 300,
                    right: isMobile ? 8 : 0,
                  }}
                >
                  {/* PROFILE HEADER */}
                  <div style={styles.profileHeader} onClick={goToProfile}>
                    <div style={styles.profileHeaderAvatar}>
                      {profile.avatarSecureUrl ? (
                        <img
                          src={profile.avatarSecureUrl}
                          alt="Profile Avatar"
                          style={{
                            width: "100%",
                            height: "100%",
                            borderRadius: "50%",
                            objectFit: "cover",
                          }}
                        />
                      ) : (
                        "U"
                      )}
                    </div>
                    <div style={styles.profileName}>{profile.name}</div>
                  </div>

                  {/* GRID ITEMS - everything not in the topbar */}
                  {gridItems.length > 0 && (
                    <div style={styles.profileGrid}>
                      {gridItems.map((item) => {
                        const isLogout = item.name === "Logout";
                        return (
                          <div
                            key={item.id}
                            style={styles.profileGridItem}
                            onClick={() => {
                              setProfileOpen(false);
                              if (isLogout) {
                                handleLogout();
                                return;
                              }
                              if (item.route) navigate(item.route);
                            }}
                          >
                            <div
                              style={{
                                ...styles.profileGridIcon,
                                background: isLogout ? "#FEF2F2" : styles.profileGridIcon.background,
                                color: isLogout ? "#DC2626" : styles.profileGridIcon.color,
                              }}
                            >
                              <i className={`bi ${item.icon}`}></i>
                            </div>
                            <span
                              style={{
                                ...styles.profileGridLabel,
                                color: isLogout ? "#DC2626" : styles.profileGridLabel.color,
                              }}
                            >
                              {item.name}
                            </span>
                          </div>
                        );
                      })}
                    </div>
                  )}
                </motion.div>
              )}
            </AnimatePresence>
          </div>
        </div>
      </header>

      {/* MOBILE NAV DRAWER */}
      <AnimatePresence>
        {isMobile && mobileNavOpen && (
          <motion.nav
            initial={{ height: 0, opacity: 0 }}
            animate={{ height: "auto", opacity: 1 }}
            exit={{ height: 0, opacity: 0 }}
            style={styles.mobileDrawer}
          >
            {renderNavItems(true)}
          </motion.nav>
        )}
      </AnimatePresence>

      {/* BODY */}
      <div style={styles.body}>
        <main
          style={{
            ...styles.main,
            padding: isMobile ? "16px 14px" : isTablet ? "22px 20px" : "28px 32px",
          }}
        >
          <Outlet />
        </main>
      </div>

      {/* FOOTER */}
      <footer
        style={{
          ...styles.footer,
          flexDirection: isMobile ? "column" : "row",
          alignItems: isMobile ? "flex-start" : "center",
          padding: isMobile ? "14px 16px" : "10px 24px",
        }}
      >
        <div style={styles.footerLeft}>
          <img src={footerlogo} alt="CodeCabinate Logo" style={styles.footerLogoImg} />
          <span>© Copyright 2026</span>
          <a href="tel:9833724659" style={styles.footerLink}>9833724659</a>
          <a href="mailto:sachinchau444@gmail.com" style={styles.footerLink}>sachinchau444@gmail.com</a>
          <a href="https://instagram.com/lookitssaching" target="_blank" rel="noopener noreferrer" style={styles.footerLink}>@lookitssaching</a>
          <a href="/privacy-policy" style={styles.footerLink}>Privacy Policy</a>
          <a href="/terms" style={styles.footerLink}>Terms</a>
        </div>

        <div style={styles.footerRight}>
          Developed by Sachin Chaurasiya Productions
        </div>
      </footer>
    </div>
  );
}

/* ─── STYLES ─── */

const styles = {
  wrapper: {
    minHeight: "100vh",
    display: "flex",
    flexDirection: "column",
    fontFamily: "Inter, sans-serif",
    background: "#f4f6fb",
  },

  topbar: {
    height: 80,
    display: "flex",
    alignItems: "center",
    justifyContent: "flex-start",
    gap: 16,
    background: "white",
    borderBottom: "1px solid rgba(0,0,0,0.06)",
    position: "relative",
    flexWrap: "nowrap",
  },

  hamburgerBtn: {
    width: 34,
    height: 34,
    borderRadius: 10,
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    cursor: "pointer",
    background: "#f5f5f5",
    fontSize: 18,
    flexShrink: 0,
  },

  brandContainer: {
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    gap: 8,
    cursor: "pointer",
    flexShrink: 0,
    padding: "0 4px",
  },

  logo: {
    objectFit: "contain",
    display: "block",
    flexShrink: 0,
  },

  navList: {
    display: "flex",
    alignItems: "center",
    overflowX: "auto",
    scrollbarWidth: "none",
  },

  navItem: {
    position: "relative",
    display: "flex",
    alignItems: "center",
    gap: 5,
    height: 40,
    padding: "0 10px",
    cursor: "pointer",
    fontSize: 13,
    borderRadius: 9,
    whiteSpace: "nowrap",
    transition: "all 0.2s ease",
    userSelect: "none",
    flexShrink: 0,
  },

  navItemDrawer: {
    width: "100%",
    padding: "10px 14px",
  },

  navIcon: {
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    width: 25,
    height: 20,
    flexShrink: 0,
    fontSize: 16,
    lineHeight: 1,
    transition: "color 0.2s ease",
  },

  navLabel: {
    overflow: "hidden",
    textOverflow: "ellipsis",
    fontSize: 17,
    letterSpacing: "-0.2px",
    whiteSpace: "nowrap",
  },

  mobileDrawer: {
    display: "flex",
    flexDirection: "column",
    background: "white",
    borderBottom: "1px solid rgba(0,0,0,0.06)",
    padding: "6px 10px",
    overflow: "hidden",
  },

  rightSection: {
    marginLeft: "auto",
    display: "flex",
    alignItems: "center",
    flexShrink: 0,
  },

  searchWrapper: {
    height: 35,
    display: "flex",
    alignItems: "center",
    background: "#F7F6F2",
    border: "1px solid #DCD9D2",
    borderRadius: 15,
    boxSizing: "border-box",
    transition: "width 0.3s ease",
    padding: "0 12px",
  },

  search: {
    flex: 1,
    width: "100%",
    height: "100%",
    border: "none",
    outline: "none",
    background: "transparent",
    color: "#333",
    fontSize: 15,
    fontFamily: "Inter, sans-serif",
    boxSizing: "border-box",
  },

  searchIcon: {
    color: "#85827B",
    fontSize: 14,
    flexShrink: 0,
  },

  mobileSearchPanel: {
    position: "absolute",
    top: 42,
    right: 0,
    width: 220,
    height: 40,
    display: "flex",
    alignItems: "center",
    background: "#F7F6F2",
    border: "1px solid #DCD9D2",
    borderRadius: 15,
    padding: "0 12px",
    boxShadow: "0 10px 30px rgba(0,0,0,0.10)",
    zIndex: 60,
  },

  notifContainer: {
    position: "relative",
  },

  iconBtn: {
    width: 30,
    height: 30,
    borderRadius: 12,
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    cursor: "pointer",
    background: "#f5f5f5",
    fontSize: 18,
    lineHeight: 1,
    userSelect: "none",
  },

  notifPanel: {
    position: "absolute",
    top: 40,
    width: 220,
    background: "white",
    border: "1px solid rgba(0,0,0,0.08)",
    borderRadius: 12,
    padding: 12,
    boxShadow: "0 10px 30px rgba(0,0,0,0.08)",
  },

  profileContainer: {
    position: "relative",
  },

  avatar: {
    width: 32,
    height: 32,
    borderRadius: "50%",
    background: "#534AB7",
    color: "white",
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    cursor: "pointer",
    flexShrink: 0,
  },

  profilePanel: {
    position: "absolute",
    top: 44,
    maxHeight: 480,
    overflowY: "auto",
    background: "#FFFFFF",
    color: "#2A2A2E",
    borderRadius: 16,
    padding: 20,
    boxShadow: "0 20px 50px rgba(0,0,0,0.15)",
    border: "1px solid rgba(0,0,0,0.06)",
    zIndex: 50,
  },

  profileHeader: {
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    gap: 12,
    cursor: "pointer",
    marginBottom: 14,
    paddingBottom: 14,
    borderBottom: "1px solid rgba(0,0,0,0.06)",
  },

  profileHeaderAvatar: {
    width: 56,
    height: 56,
    borderRadius: "50%",
    background: "#534AB7",
    color: "#fff",
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    fontSize: 20,
    flexShrink: 0,
    overflow: "hidden",
  },

  profileName: {
    fontSize: 22,
    fontWeight: 700,
    color: "#2A2A2E",
    maxWidth: 220,
    wordWrap: "break-word",
    wordBreak: "break-word",
    whiteSpace: "normal",
    lineHeight: 1.3,
    textAlign: "center",
  },

  profileGrid: {
    display: "grid",
    gridTemplateColumns: "1fr 1fr",
    gap: 10,
    marginBottom: 14,
  },

  profileGridItem: {
    background: "#F8F7FC",
    borderRadius: 12,
    padding: "14px 10px",
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    gap: 8,
    cursor: "pointer",
    fontSize: 12,
  },

  profileGridIcon: {
    width: 36,
    height: 36,
    borderRadius: "50%",
    background: "#F0EDFF",
    color: "#534AB7",
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    fontSize: 16,
  },

  profileGridLabel: {
    fontSize: 12,
    color: "#5F5E5A",
  },

  profileList: {
    display: "flex",
    flexDirection: "column",
    borderTop: "1px solid rgba(0,0,0,0.06)",
    paddingTop: 10,
  },

  profileListItem: {
    display: "flex",
    alignItems: "center",
    gap: 10,
    padding: "10px 6px",
    borderRadius: 8,
    cursor: "pointer",
    fontSize: 13,
  },

  profileListIcon: {
    fontSize: 15,
    width: 18,
    textAlign: "center",
  },

  body: {
    display: "flex",
    flex: 1,
    overflow: "hidden",
    background: "#F4F6FB",
  },

  main: {
    flex: 1,
    overflowY: "auto",
    background: "#F4F6FB",
  },

  footer: {
    background: "white",
    borderTop: "1px solid rgba(0,0,0,0.06)",
    display: "flex",
    justifyContent: "space-between",
    gap: 16,
    fontSize: 12,
    color: "#77736E",
    flexWrap: "wrap",
  },

  footerLeft: {
    display: "flex",
    alignItems: "center",
    flexWrap: "wrap",
    gap: 18,
  },

  footerLogoImg: {
    height: 42,
    width: "auto",
    objectFit: "contain",
  },

  footerLink: {
    color: "#77736E",
    textDecoration: "none",
  },

  footerRight: {
    fontSize: 11,
    color: "#A6A29B",
    flexShrink: 0,
    whiteSpace: "nowrap",
  },
};