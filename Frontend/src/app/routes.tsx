import { createBrowserRouter } from "react-router";
import { LandingPage } from "./pages/LandingPage";
import { LoginPage } from "./pages/LoginPage";
import { RegisterPage } from "./pages/RegisterPage";
import { OrganizationRegisterPage } from "./pages/OrganizationRegisterPage";
import { BoardPage } from "./pages/BoardPage";
import { AdminDashboardPage } from "./pages/AdminDashboardPage";
import { TeamLeaderDashboardPage } from "./pages/TeamLeaderDashboardPage";
import { AboutPage } from "./pages/AboutPage";
import { ProfileSettingsPage } from "./pages/ProfileSettingsPage";
import { ProtectedRoute } from "./components/ProtectedRoute";

export const router = createBrowserRouter([
  {
    path: "/",
    Component: LandingPage,
  },
  {
    path: "/login",
    Component: LoginPage,
  },
  {
    path: "/register",
    Component: RegisterPage,
  },
  {
    path: "/register/organization",
    Component: OrganizationRegisterPage,
  },
  {
    path: "/board",
    element: (
      <ProtectedRoute allowedRoles={["USER", "TEAM_LEADER", "ORG_ADMIN"]}>
        <BoardPage />
      </ProtectedRoute>
    ),
  },
  {
    path: "/admin",
    element: (
      <ProtectedRoute allowedRoles={["ORG_ADMIN"]}>
        <AdminDashboardPage />
      </ProtectedRoute>
    ),
  },
  {
    path: "/leader",
    element: (
      <ProtectedRoute allowedRoles={["TEAM_LEADER", "ORG_ADMIN"]}>
        <TeamLeaderDashboardPage />
      </ProtectedRoute>
    ),
  },
  {
    path: "/about",
    Component: AboutPage,
  },
  {
    path: "/profile",
    element: (
      <ProtectedRoute>
        <ProfileSettingsPage />
      </ProtectedRoute>
    ),
  },
]);
