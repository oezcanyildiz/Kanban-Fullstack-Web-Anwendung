import { Navigate } from "react-router";

interface ProtectedRouteProps {
  children: React.ReactNode;
  allowedRoles?: string[]; // optional: if set, also checks role
}

/**
 * Schützt eine Route vor nicht eingeloggten Nutzern.
 * Wenn kein JWT-Token vorhanden → Redirect zu /login
 * Wenn Rolle nicht erlaubt → Redirect zu /board
 */
export function ProtectedRoute({ children, allowedRoles }: ProtectedRouteProps) {
  const token = localStorage.getItem("jwtToken");
  const role = localStorage.getItem("userRole");

  if (!token) {
    return <Navigate to="/login" replace />;
  }

  if (allowedRoles && role && !allowedRoles.includes(role)) {
    // Falsche Rolle → zur passenden Seite leiten
    if (role === "ORG_ADMIN") return <Navigate to="/admin" replace />;
    if (role === "TEAM_LEADER") return <Navigate to="/leader" replace />;
    return <Navigate to="/board" replace />;
  }

  return <>{children}</>;
}
