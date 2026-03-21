import { useState, useEffect } from "react";
import { fetchApi } from "../services/api";
import { Shield, Trash2, UserCheck, Users, LogOut } from "lucide-react";
import { Button } from "../components/ui/button";
import { Link, useNavigate } from "react-router";

interface Employee {
  userID: number;
  userName: string;
  userLastName: string;
  userEmail: string;
  online: boolean;
  role: string;
  teams?: string[];
}

export function AdminDashboardPage() {
  const [employees, setEmployees] = useState<Employee[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    const orgId = localStorage.getItem("organizationID");
    if (!orgId) {
      setError("Keine Organisation gefunden. Bitte neu einloggen.");
      setLoading(false);
      return;
    }

    async function fetchEmployees() {
      try {
        const data = await fetchApi(`/api/organization/${orgId}`);
        setEmployees(data);
      } catch (err: any) {
        setError(err.message || "Fehler beim Laden der Mitarbeiter.");
      } finally {
        setLoading(false);
      }
    }
    fetchEmployees();
  }, []);

  const handlePromote = async (userId: number) => {
    try {
      await fetchApi(`/api/organization/promote/${userId}`, { method: 'PATCH' });
      setEmployees(prev => prev.map(emp => emp.userID === userId ? { ...emp, role: "TEAM_LEADER" } : emp));
      alert("Erfolgreich zum Team Leader befördert.");
    } catch (e: any) {
      alert("Fehler bei der Beförderung: " + e.message);
    }
  };

  const handleDemote = async (userId: number) => {
    try {
      await fetchApi(`/api/organization/demote/${userId}`, { method: 'PATCH' });
      setEmployees(prev => prev.map(emp => emp.userID === userId ? { ...emp, role: "USER" } : emp));
      alert("Rolle erfolgreich entfernt.");
    } catch (e: any) {
      alert("Fehler bei der Rollenentfernung: " + e.message);
    }
  };

  const handleRemove = async (userId: number) => {
    if (!window.confirm("Bist du sicher, dass du diesen Mitarbeiter entfernen willst?")) return;
    try {
      await fetchApi(`/api/organization/remove/${userId}`, { method: 'DELETE' });
      setEmployees(prev => prev.filter(emp => emp.userID !== userId));
    } catch (e: any) {
      alert("Fehler beim Entfernen: " + e.message);
    }
  };

  const logout = () => {
    localStorage.clear();
    navigate("/login");
  };

  return (
    <div className="min-h-screen bg-gray-50 flex flex-col">
      <header className="bg-white border-b border-gray-200 px-6 py-4 flex items-center justify-between shadow-sm">
        <div className="flex items-center gap-2 text-indigo-700 font-bold text-xl">
          <Shield className="w-6 h-6" />
          <span>{localStorage.getItem("organizationName") || "Admin Dashboard"}</span>
        </div>
        <div className="flex items-center gap-6">
          <div className="bg-indigo-50 px-3 py-1.5 rounded-lg border border-indigo-100 flex items-center gap-2">
            <span className="text-xs font-semibold text-indigo-400 uppercase tracking-wider">Einladungscode:</span>
            <code className="text-sm font-bold text-indigo-700">{localStorage.getItem("invitationCode") || "N/A"}</code>
          </div>
          <Button variant="ghost" onClick={logout} className="text-gray-600">
            <LogOut className="w-4 h-4 mr-2" />
            Abmelden
          </Button>
        </div>
      </header>
      
      <main className="flex-1 p-8 max-w-5xl mx-auto w-full">
        <div className="mb-6 flex justify-between items-end">
          <div>
            <h1 className="text-2xl font-bold text-gray-900 mb-1 flex items-center gap-2">
              <Users className="w-6 h-6 text-gray-400" />
              Mitarbeiterverwaltung
            </h1>
            <p className="text-gray-500">Verwalte die Rollen und Zugänge deiner Organisation.</p>
          </div>
        </div>

        {loading ? (
          <p>Lade Mitarbeiter...</p>
        ) : error ? (
          <p className="text-red-600">{error}</p>
        ) : (
          <div className="bg-white rounded-xl shadow-sm border border-gray-200 overflow-hidden">
            <table className="w-full text-left border-collapse">
              <thead>
                <tr className="bg-gray-50 text-gray-600 text-sm border-b">
                  <th className="py-3 px-4 font-semibold">Name</th>
                  <th className="py-3 px-4 font-semibold">Email</th>
                  <th className="py-3 px-4 font-semibold">Teams</th>
                  <th className="py-3 px-4 font-semibold">Rolle</th>
                  <th className="py-3 px-4 font-semibold text-right">Aktionen</th>
                </tr>
              </thead>
              <tbody>
                {employees.map(emp => (
                  <tr key={emp.userID} className="border-b last:border-b-0 hover:bg-gray-50 transition-colors">
                    <td className="py-3 px-4">
                      <div className="flex items-center gap-3">
                        <div className="w-8 h-8 rounded-full bg-indigo-100 text-indigo-700 flex items-center justify-center font-bold text-xs">
                          {emp.userName.charAt(0)}{emp.userLastName.charAt(0)}
                        </div>
                        <span className="font-medium text-gray-900">{emp.userName} {emp.userLastName}</span>
                      </div>
                    </td>
                    <td className="py-3 px-4 text-gray-500 text-sm">{emp.userEmail}</td>
                    <td className="py-3 px-4">
                      {emp.teams && emp.teams.length > 0 ? (
                        <div className="flex flex-wrap gap-1">
                          {emp.teams.map((t: string, i: number) => (
                            <span key={i} className="inline-flex items-center px-1.5 py-0.5 rounded text-[11px] font-medium bg-gray-100 text-gray-700 border border-gray-200">
                              {t}
                            </span>
                          ))}
                        </div>
                      ) : (
                        <span className="text-gray-400 text-xs">-</span>
                      )}
                    </td>
                    <td className="py-3 px-4">
                      <span className={`inline-flex items-center px-2 py-1 rounded-full text-xs font-medium ${
                        emp.role === 'ORG_ADMIN' ? 'bg-purple-100 text-purple-700' :
                        emp.role === 'TEAM_LEADER' ? 'bg-blue-100 text-blue-700' :
                        'bg-gray-100 text-gray-700'
                      }`}>
                        {emp.role}
                      </span>
                    </td>
                    <td className="py-3 px-4 text-right">
                      {emp.role !== 'ORG_ADMIN' && (
                        <div className="flex items-center justify-end gap-2">
                          {emp.role === 'USER' && (
                            <Button 
                              variant="outline" 
                              size="sm" 
                              onClick={() => handlePromote(emp.userID)}
                              className="text-indigo-600 border-indigo-200 hover:bg-indigo-50"
                            >
                              <UserCheck className="w-4 h-4 mr-1" /> Team Leader
                            </Button>
                          )}
                          {emp.role === 'TEAM_LEADER' && (
                            <Button 
                              variant="outline" 
                              size="sm" 
                              onClick={() => handleDemote(emp.userID)}
                              className="text-orange-600 border-orange-200 hover:bg-orange-50"
                            >
                              <UserCheck className="w-4 h-4 mr-1" /> Rolle entziehen
                            </Button>
                          )}
                          <Button 
                            variant="destructive" 
                            size="sm" 
                            onClick={() => handleRemove(emp.userID)}
                            className="bg-red-50 text-red-600 border border-red-200 hover:bg-red-100 hover:text-red-700"
                          >
                            <Trash2 className="w-4 h-4 mr-1" /> Entfernen
                          </Button>
                        </div>
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </main>
    </div>
  );
}
