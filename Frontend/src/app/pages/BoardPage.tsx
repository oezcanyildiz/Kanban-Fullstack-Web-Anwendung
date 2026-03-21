import { useState, useEffect } from "react";
import { DndProvider } from "react-dnd";
import { HTML5Backend } from "react-dnd-html5-backend";
import { Link } from "react-router";
import logo from "../../assets/logo.png";
import { Search, LogOut, Bell, ChevronDown, LayoutGrid, Plus } from "lucide-react";
import { Button } from "../components/ui/button";
import { Input } from "../components/ui/input";
import { KanbanColumn } from "../components/KanbanColumn";
import { TaskDetailModal } from "../components/TaskDetailModal";
import { fetchApi } from "../services/api";

export type Priority = "high" | "medium" | "low" | "urgent";

export interface TeamMember {
  userID: number;
  userName: string;
  userLastName: string;
}

export interface Task {
  id: string;
  title: string;
  description?: string;
  priority: Priority;
  dueDate?: string;
  assigneeId?: number;
  assignee?: string;
}

export interface Column {
  id: string;
  title: string;
  tasks: Task[];
  wipLimit?: number;
}

export function BoardPage() {
  const [columns, setColumns] = useState<Column[]>([]);
  const [searchQuery, setSearchQuery] = useState("");
  const [selectedTask, setSelectedTask] = useState<Task | null>(null);
  const [selectedColumnId, setSelectedColumnId] = useState<string | null>(null);
  const [boardName, setBoardName] = useState("Mein Projekt");
  const [boardId, setBoardId] = useState<number | null>(null);
  const [userBoards, setUserBoards] = useState<any[]>([]);
  const [teamMembers, setTeamMembers] = useState<TeamMember[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function loadBoardsList() {
      try {
        const boards = await fetchApi('/api/board/my-boards');
        setUserBoards(boards || []);
        if (boards && boards.length > 0) {
          setBoardId(boards[0].boardID);
        } else {
          setLoading(false); // Kein Board gefunden
        }
      } catch (err) {
        console.error("Failed to load boards list", err);
        setLoading(false);
      }
    }
    loadBoardsList();
  }, []);

  useEffect(() => {
    if (!boardId) return;

    async function loadBoardDetails() {
      setLoading(true);
      try {
        const currentBoard = userBoards.find(b => b.boardID === boardId) || userBoards[0];
        if (currentBoard) {
          setBoardName(currentBoard.boardName);
        }

        const detailsPath = `/api/board/details?boardID=${boardId}`;
        const details = await fetchApi(detailsPath);
        
        const mappedColumns = details.columns.map((c: any) => ({
          id: String(c.boardColumnID),
          title: c.columnTitle,
          wipLimit: c.wipLimit,
          tasks: (c.tasks || []).map((t: any) => ({
            id: String(t.taskID),
            title: t.title,
            description: t.description || "",
            priority: String(t.priority).toLowerCase() as Priority,
            assigneeId: t.assigneeID || undefined,
            assignee: t.assigneeName || undefined,
          }))
        }));
        setColumns(mappedColumns);

        // Load team members from the board details response
        if (details.teamMembers) {
          setTeamMembers(details.teamMembers.map((m: any) => ({
            userID: m.userID,
            userName: m.userName,
            userLastName: m.userLastName,
          })));
        }
      } catch (err) {
        console.error("Failed to load board details", err);
      } finally {
        setLoading(false);
      }
    }
    loadBoardDetails();
  }, [boardId, userBoards]);

  const totalTasks = columns.reduce((sum, col) => sum + col.tasks.length, 0);
  const doneTasks = columns.find((c) => c.title.toLowerCase().includes("done") || c.title.toLowerCase().includes("erledigt"))?.tasks.length ?? 0;

  const moveTask = async (taskId: string, fromColumnId: string, toColumnId: string) => {
    // Finde den Task für die API
    let taskToMove: Task | undefined;
    
    setColumns((prev) => {
      const cols = prev.map((c) => ({ ...c, tasks: [...c.tasks] }));
      const from = cols.find((c) => c.id === fromColumnId);
      const to = cols.find((c) => c.id === toColumnId);
      if (!from || !to) return prev;
      const idx = from.tasks.findIndex((t) => t.id === taskId);
      if (idx === -1) return prev;
      const [task] = from.tasks.splice(idx, 1);
      taskToMove = task;
      to.tasks.push(task);
      return cols;
    });

    if (taskToMove) {
      try {
        await fetchApi('/api/tasks/update', {
          method: 'PATCH',
          body: JSON.stringify({
            taskID: Number(taskToMove.id),
            title: taskToMove.title,
            description: taskToMove.description,
            columnID: Number(toColumnId),
            priority: taskToMove.priority.toUpperCase()
          })
        });
      } catch (e) {
        console.error("Error moving task:", e);
      }
    }
  };

  const updateTask = async (columnId: string, updatedTask: Task) => {
    setColumns((prev) =>
      prev.map((col) =>
        col.id === columnId
          ? { ...col, tasks: col.tasks.map((t) => (t.id === updatedTask.id ? updatedTask : t)) }
          : col
      )
    );
    try {
      await fetchApi('/api/tasks/update', {
        method: 'PATCH',
        body: JSON.stringify({
          taskID: Number(updatedTask.id),
          title: updatedTask.title,
          description: updatedTask.description,
          columnID: Number(columnId),
          priority: updatedTask.priority.toUpperCase(),
          assigneeID: updatedTask.assigneeId ?? null,
        })
      });
    } catch (e) {
      console.error(e);
    }
  };

  const deleteTask = async (columnId: string, taskId: string) => {
    setColumns((prev) =>
      prev.map((col) =>
        col.id === columnId ? { ...col, tasks: col.tasks.filter((t) => t.id !== taskId) } : col
      )
    );
    try {
      await fetchApi(`/api/tasks/${taskId}`, { method: 'DELETE' });
    } catch (e) {
      console.error(e);
    }
  };

  const handleDeleteColumn = async (columnId: string) => {
    if (!window.confirm("Bist du sicher, dass du diese Spalte löschen willst? Alle Aufgaben darin werden ebenfalls gelöscht.")) return;
    
    setColumns((prev) => prev.filter((col) => col.id !== columnId));
    try {
      await fetchApi(`/api/columns/${columnId}`, { method: 'DELETE' });
    } catch (e) {
      console.error(e);
    }
  };

  const handleCreateTask = async (columnId: string) => {
    // Erstelle provisorischen Task
    try {
      const response = await fetchApi('/api/tasks/create', {
        method: 'POST',
        body: JSON.stringify({
          title: "Neue Aufgabe",
          description: "",
          columnID: Number(columnId),
          priority: "MEDIUM"
        })
      });
      // Füge neuen Task lokal hinzu
      setColumns((prev) => prev.map((col) => {
        if (col.id === columnId) {
          return {
             ...col,
             tasks: [...col.tasks, {
               id: String(response.taskID),
               title: response.title,
               priority: String(response.priority).toLowerCase() as Priority,
               description: response.description
             }]
          };
        }
        return col;
      }));
    } catch (e: any) { 
      console.error(e); 
      alert("Fehler beim Erstellen des Tasks: " + e.message);
    }
  };

  const handleCreateColumn = async () => {
    if (!boardId) return;
    const title = window.prompt("Name der neuen Spalte:");
    if (!title) return;
    try {
      const response = await fetchApi('/api/columns/create', {
         method: 'POST',
         body: JSON.stringify({
            boardID: boardId,
            columnTitle: title
         })
      }).catch(() => fetchApi('/columns/create', {
         method: 'POST',
         body: JSON.stringify({
            boardID: boardId,
            columnTitle: title
         })
      }));
      
      setColumns(prev => [...prev, {
         id: String(response.boardColumnID),
         title: response.columnTitle,
         tasks: []
      }]);
    } catch (e: any) {
      console.error(e);
      alert("Fehler beim Erstellen der Spalte: " + e.message);
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center">
        <p className="text-gray-500 font-medium">Lade Board...</p>
      </div>
    );
  }

  if (!boardId && !loading) {
    const isLeader = localStorage.getItem("userRole") === "TEAM_LEADER";
    return (
      <div className="min-h-screen bg-gray-50 flex flex-col items-center justify-center p-6 text-center">
        <div className="bg-white p-10 rounded-2xl shadow-sm border border-gray-100 max-w-md w-full">
          <LayoutGrid className="w-16 h-16 text-indigo-200 mx-auto mb-6" />
          <h2 className="text-2xl font-bold text-gray-900 mb-3">Kein Board gefunden</h2>
          <p className="text-gray-500 mb-8">
            {isLeader 
              ? "Du hast noch keine Kanban Boards in deiner Organisation. Gehe jetzt zu deinem Team-Leader Dashboard, um dein erstes Team mit Board zu erstellen!" 
              : "Du wurdest noch keinem Team zugewiesen oder dein Team hat noch kein Board erstellt. Bitte wende dich an deinen Team-Leader."}
          </p>
          {isLeader ? (
            <Link to="/leader">
              <Button className="w-full bg-indigo-600 hover:bg-indigo-700 h-11 text-base">
                Zum Leader Dashboard
              </Button>
            </Link>
          ) : (
            <Link to="/login" onClick={() => localStorage.clear()}>
              <Button variant="outline" className="w-full text-gray-600 h-11 text-base">
                Abmelden
              </Button>
            </Link>
          )}
        </div>
      </div>
    );
  }

  return (
    <DndProvider backend={HTML5Backend}>
      <div className="min-h-screen flex flex-col" style={{ background: "#F8FAFC" }}>

        {/* Header */}
        <header className="bg-white border-b border-gray-200 px-6 py-0 flex items-center justify-between h-14 sticky top-0 z-10 shadow-sm">
          <div className="flex items-center gap-6">
            <img src={logo} alt="KanKan" className="h-7" />
            <div className="flex items-center gap-1 text-sm text-gray-500">
              <LayoutGrid className="w-4 h-4" />
              <span className="mx-1 text-gray-300">/</span>
              
              <div className="relative flex items-center">
                <select 
                  value={boardId || ""} 
                  onChange={(e) => setBoardId(Number(e.target.value))}
                  className="appearance-none bg-transparent font-semibold text-gray-800 hover:text-blue-600 transition-colors pr-6 cursor-pointer focus:outline-none"
                >
                  {userBoards.map(board => (
                    <option key={board.boardID} value={board.boardID}>
                      {board.boardName}
                    </option>
                  ))}
                </select>
                <ChevronDown className="w-3.5 h-3.5 absolute right-0 pointer-events-none text-gray-500" />
              </div>
              
            </div>
          </div>

          <div className="flex items-center gap-3">
            <div className="relative">
              <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400" />
              <Input
                type="text"
                placeholder="Suchen..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="pl-9 h-8 w-52 text-sm bg-gray-50 border-gray-200 focus:bg-white"
              />
            </div>
            <button className="relative p-2 text-gray-500 hover:text-gray-800 hover:bg-gray-100 rounded-lg transition-colors">
              <Bell className="w-4 h-4" />
              <span className="absolute top-1.5 right-1.5 w-1.5 h-1.5 bg-red-500 rounded-full" />
            </button>
            <div className="w-px h-6 bg-gray-200" />
            <Link to="/profile" title="Profileinstellungen">
              <div className="w-8 h-8 rounded-full bg-blue-600 text-white text-xs font-bold flex items-center justify-center hover:ring-2 hover:ring-blue-300 transition-all cursor-pointer">
                {`${(localStorage.getItem('userName') || '?').charAt(0)}${(localStorage.getItem('userLastName') || '').charAt(0)}`.toUpperCase()}
              </div>
            </Link>
            <Link to="/">
              <Button variant="ghost" size="sm" className="text-gray-500 hover:text-gray-800 text-xs">
                <LogOut className="w-3.5 h-3.5 mr-1.5" />
                Abmelden
              </Button>
            </Link>
          </div>
        </header>

        {/* Board Subheader */}
        <div className="bg-white border-b border-gray-100 px-6 py-3 flex items-center justify-between">
          <div>
            <h1 className="text-base font-semibold text-gray-900">Sprint Board</h1>
            <p className="text-xs text-gray-400 mt-0.5">
              {doneTasks} von {totalTasks} Aufgaben erledigt
            </p>
          </div>
          {/* Progress bar */}
          <div className="flex items-center gap-3">
            <div className="w-36 h-1.5 bg-gray-100 rounded-full overflow-hidden">
              <div
                className="h-full bg-emerald-500 rounded-full transition-all"
                style={{ width: totalTasks > 0 ? `${(doneTasks / totalTasks) * 100}%` : "0%" }}
              />
            </div>
            <span className="text-xs text-gray-500 font-medium">
              {totalTasks > 0 ? Math.round((doneTasks / totalTasks) * 100) : 0}%
            </span>
          </div>
        </div>

        {/* Kanban Board */}
        <div className="flex-1 overflow-x-auto px-6 py-5">
          <div className="flex gap-4 h-full w-full items-start">
            {columns.map((column) => (
              <KanbanColumn
                key={column.id}
                column={column}
                onMoveTask={moveTask}
                onTaskClick={(task, colId) => { setSelectedTask(task); setSelectedColumnId(colId); }}
                searchQuery={searchQuery}
                onAddTask={handleCreateTask}
                onDeleteColumn={handleDeleteColumn}
              />
            ))}
            {/* Add Column Button */}
            {columns.length < 5 && (
              <div className="flex-shrink-0 w-72 flex flex-col pt-2">
                <Button
                  variant="ghost"
                  onClick={handleCreateColumn}
                  className="w-full text-gray-500 hover:text-gray-800 hover:bg-gray-100 justify-start text-sm rounded-lg border border-dashed border-gray-300 hover:border-gray-400 transition-all h-12"
                >
                  <Plus className="w-4 h-4 mr-2" />
                  Spalte hinzufügen
                </Button>
              </div>
            )}
          </div>
        </div>

        {selectedTask && selectedColumnId && (
          <TaskDetailModal
            task={selectedTask}
            columnId={selectedColumnId}
            teamMembers={teamMembers}
            onClose={() => { setSelectedTask(null); setSelectedColumnId(null); }}
            onUpdate={(updatedTask) => { updateTask(selectedColumnId, updatedTask); setSelectedTask(null); setSelectedColumnId(null); }}
            onDelete={() => { deleteTask(selectedColumnId, selectedTask.id); setSelectedTask(null); setSelectedColumnId(null); }}
          />
        )}
      </div>
    </DndProvider>
  );
}
