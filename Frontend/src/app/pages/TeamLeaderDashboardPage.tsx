import { useState, useEffect } from "react";
import { fetchApi } from "../services/api";
import { Briefcase, LayoutGrid, Plus, Users, UserPlus, LogOut, Trash2 } from "lucide-react";
import { Button } from "../components/ui/button";
import { Input } from "../components/ui/input";
import { Link, useNavigate } from "react-router";

interface Board {
  boardID: number;
  boardName: string;
}

interface Team {
  teamID: number;
  teamName: string;
  boards?: Board[];
}

interface Employee {
  userID: number;
  userName: string;
  userLastName: string;
  userEmail: string;
}

export function TeamLeaderDashboardPage() {
  const [teams, setTeams] = useState<Team[]>([]);
  const [potentialMembers, setPotentialMembers] = useState<Record<number, Employee[]>>({});
  const [newTeamName, setNewTeamName] = useState("");
  const [newBoardNames, setNewBoardNames] = useState<Record<number, string>>({});
  const [boardLoading, setBoardLoading] = useState<Record<number, boolean>>({});
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  const loadData = async () => {
    setLoading(true);
    try {
      const teamsData = await fetchApi('/api/teams/my-teams');
      console.log("Teams loaded:", teamsData);
      setTeams(teamsData);
      
      // Für jedes Team die potenziellen Mitglieder laden
      const membersMap: Record<number, Employee[]> = {};
      for (const team of teamsData) {
        try {
          const potData = await fetchApi(`/api/team-members/potential/${team.teamID}`);
          membersMap[team.teamID] = potData;
        } catch (e) {
          console.error(`Fehler beim Laden potenzieller Mitglieder für Team ${team.teamID}`, e);
        }
      }
      setPotentialMembers(membersMap);
    } catch (e: any) {
      console.error("Fehler beim Laden der Teams:", e.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const handleCreateTeam = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!newTeamName.trim()) return;
    try {
      await fetchApi('/api/teams/create', {
        method: 'POST',
        body: JSON.stringify({ teamName: newTeamName })
      });
      setNewTeamName("");
      loadData(); // Alles neu laden um Auto-Enrollment etc. zu sehen
    } catch (err: any) {
      alert("Fehler beim Erstellen des Teams: " + err.message);
    }
  };

  const handleAddMember = async (teamId: number, userId: number) => {
    try {
      await fetchApi('/api/team-members/add', {
        method: 'POST',
        body: JSON.stringify({
          teamID: teamId,
          userID: userId
        })
      });
      // Liste aktualisieren für dieses Team
      const potData = await fetchApi(`/api/team-members/potential/${teamId}`);
      setPotentialMembers(prev => ({ ...prev, [teamId]: potData }));
      alert("Mitarbeiter erfolgreich hinzugefügt!");
    } catch (e: any) {
      alert("Fehler: " + e.message);
    }
  };

  const handleCreateBoard = async (teamId: number) => {
    const boardName = newBoardNames[teamId];
    console.log(`Versuch Board zu erstellen: Team ${teamId}, Name: ${boardName}`);
    if (!boardName || !boardName.trim()) {
      console.warn("Board Name ist leer!");
      return;
    }
    
    setBoardLoading(prev => ({ ...prev, [teamId]: true }));
    try {
      await fetchApi('/api/board/create', {
        method: 'POST',
        body: JSON.stringify({
          boardName: boardName,
          teamID: teamId
        })
      });
      setNewBoardNames(prev => ({ ...prev, [teamId]: "" }));
      loadData();
    } catch (err: any) {
      alert("Fehler beim Erstellen des Boards: " + err.message);
    } finally {
      setBoardLoading(prev => ({ ...prev, [teamId]: false }));
    }
  };

  const handleDeleteBoard = async (boardId: number) => {
    if (!window.confirm("Bist du sicher, dass du dieses Board löschen möchtest? (Soft-Delete)")) return;
    try {
      await fetchApi(`/api/board/${boardId}`, { method: 'DELETE' });
      loadData();
    } catch (err: any) {
      alert("Fehler beim Löschen des Boards: " + err.message);
    }
  };

  const handleDeleteTeam = async (teamId: number) => {
    if (!window.confirm("Bist du sicher, dass du dieses gesamte Team inkl. aller Boards löschen möchtest? (Soft-Delete)")) return;
    try {
      await fetchApi(`/api/teams/${teamId}`, { method: 'DELETE' });
      loadData();
    } catch (err: any) {
      alert("Fehler beim Löschen des Teams: " + err.message);
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
          <Briefcase className="w-6 h-6" />
          <span>Team Leader Dashboard</span>
        </div>
        <div className="flex items-center gap-4">
          <Link to="/board">
             <Button variant="outline" className="text-gray-600">
               <LayoutGrid className="w-4 h-4 mr-2" />
               Zum Kanban
             </Button>
          </Link>
          <Button variant="ghost" onClick={logout} className="text-gray-600">
            <LogOut className="w-4 h-4 mr-2" />
            Abmelden
          </Button>
        </div>
      </header>
      
      <main className="flex-1 p-8 max-w-6xl mx-auto w-full flex flex-col gap-8">
        <div className="grid grid-cols-1 lg:grid-cols-4 gap-8">
          {/* Sidebar: New Team */}
          <div className="lg:col-span-1 flex flex-col gap-6">
            <div className="bg-white p-6 rounded-2xl shadow-sm border border-gray-100">
              <h2 className="text-lg font-bold text-gray-900 mb-4 flex items-center">
                <Plus className="w-5 h-5 mr-2 text-indigo-500" />
                Neues Team
              </h2>
              <form onSubmit={handleCreateTeam} className="flex flex-col gap-4">
                <Input 
                  placeholder="Team Name..." 
                  value={newTeamName} 
                  onChange={(e) => setNewTeamName(e.target.value)}
                  className="bg-gray-50 border-gray-200"
                />
                <Button type="submit" className="w-full bg-indigo-600 hover:bg-indigo-700 h-10">Anlegen</Button>
              </form>
            </div>
          </div>

          {/* Main Area: Teams & Members */}
          <div className="lg:col-span-3 flex flex-col gap-8">
            <h2 className="text-2xl font-bold text-gray-900 flex items-center gap-2">
              <Users className="w-6 h-6 text-indigo-500" />
              Verwaltung deiner Teams
            </h2>

            {loading ? (
              <div className="flex items-center justify-center p-12">
                <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-indigo-600"></div>
              </div>
            ) : teams.length === 0 ? (
              <div className="bg-white p-12 text-center rounded-2xl shadow-sm border border-gray-100">
                <p className="text-gray-400">Du hast noch keine Teams erstellt.</p>
              </div>
            ) : (
              <div className="flex flex-col gap-6">
                {teams.map((team) => (
                  <div key={team.teamID} className="bg-white rounded-2xl shadow-sm border border-gray-100 overflow-hidden">
                    <div className="p-6 border-b border-gray-50 flex items-center justify-between bg-indigo-50/30">
                      <div>
                        <div className="flex items-center gap-3">
                          <h3 className="text-xl font-bold text-gray-900">{team.teamName}</h3>
                          <Button 
                            variant="ghost" 
                            size="sm" 
                            onClick={() => handleDeleteTeam(team.teamID)}
                            className="text-gray-400 hover:text-red-500 hover:bg-red-50 p-1 h-8 w-8"
                            title="Team löschen"
                          >
                            <Trash2 className="w-4 h-4" />
                          </Button>
                        </div>
                        <p className="text-xs text-indigo-600 font-medium">TEAM ID: {team.teamID}</p>
                      </div>
                      <div className="flex gap-2">
                        <Input 
                          placeholder="Board Name..." 
                          value={newBoardNames[team.teamID] || ""}
                          onChange={(e) => setNewBoardNames(prev => ({ ...prev, [team.teamID]: e.target.value }))}
                          className="h-9 w-40 bg-white"
                        />
                        <Button 
                          size="sm" 
                          onClick={() => handleCreateBoard(team.teamID)} 
                          className="bg-indigo-600"
                          disabled={boardLoading[team.teamID]}
                        >
                          {boardLoading[team.teamID] ? "..." : <><Plus className="w-4 h-4 mr-1" /> Board</>}
                        </Button>
                      </div>
                    </div>

                    <div className="p-6 grid grid-cols-1 md:grid-cols-2 gap-8">
                      {/* Boards List */}
                      <div>
                        <h4 className="text-sm font-bold text-gray-400 uppercase tracking-wider mb-4 flex items-center">
                          <LayoutGrid className="w-4 h-4 mr-2" /> Boards
                        </h4>
                        <div className="space-y-2">
                          {team.boards && team.boards.length > 0 ? (
                            team.boards.map(board => (
                              <div key={board.boardID} className="flex items-center justify-between p-3 bg-gray-50 rounded-xl group hover:bg-indigo-50 transition-colors">
                                <span className="font-medium text-gray-700">{board.boardName}</span>
                                <Button 
                                  variant="ghost" 
                                  size="sm" 
                                  onClick={() => handleDeleteBoard(board.boardID)}
                                  className="text-gray-400 hover:text-red-500 hover:bg-red-50 p-1 h-8 w-8 opacity-0 group-hover:opacity-100 transition-opacity"
                                >
                                  <Trash2 className="w-4 h-4" />
                                </Button>
                              </div>
                            ))
                          ) : (
                            <p className="text-sm text-gray-400 italic">Keine Boards vorhanden.</p>
                          )}
                        </div>
                      </div>

                      {/* Potential Members List */}
                      <div>
                        <h4 className="text-sm font-bold text-gray-400 uppercase tracking-wider mb-4 flex items-center">
                          <UserPlus className="w-4 h-4 mr-2" /> Mitglieder hinzufügen
                        </h4>
                        <div className="space-y-2 max-h-48 overflow-y-auto pr-2 custom-scrollbar">
                          {potentialMembers[team.teamID]?.length > 0 ? (
                            potentialMembers[team.teamID].map(emp => (
                              <div key={emp.userID} className="flex items-center justify-between p-3 bg-gray-50 rounded-xl hover:bg-indigo-50 transition-colors">
                                <div className="flex flex-col">
                                  <span className="text-sm font-semibold text-gray-800">{emp.userName} {emp.userLastName}</span>
                                  <span className="text-[10px] text-gray-400">{emp.userEmail}</span>
                                </div>
                                <Button 
                                  variant="ghost" 
                                  size="sm" 
                                  onClick={() => handleAddMember(team.teamID, emp.userID)}
                                  className="text-indigo-600 hover:bg-indigo-100 p-1 h-8 w-8"
                                >
                                  <Plus className="w-5 h-5" />
                                </Button>
                              </div>
                            ))
                          ) : (
                            <p className="text-sm text-gray-400 italic">Alle Mitarbeiter sind bereits im Team oder keine gefunden.</p>
                          )}
                        </div>
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        </div>
      </main>
    </div>
  );
}
