import { useState } from "react";
import { DndProvider } from "react-dnd";
import { HTML5Backend } from "react-dnd-html5-backend";
import { Link } from "react-router";
import logo from "../../assets/logo.png";
import { Search, LogOut, Bell, ChevronDown, LayoutGrid } from "lucide-react";
import { Button } from "../components/ui/button";
import { Input } from "../components/ui/input";
import { KanbanColumn } from "../components/KanbanColumn";
import { TaskDetailModal } from "../components/TaskDetailModal";

export type Priority = "high" | "medium" | "low" | "urgent";

export interface Task {
  id: string;
  title: string;
  description?: string;
  priority: Priority;
  dueDate?: string;
  assignee?: string;
}

export interface Column {
  id: string;
  title: string;
  tasks: Task[];
}

const initialColumns: Column[] = [
  {
    id: "todo",
    title: "Zu erledigen",
    tasks: [
      { id: "1", title: "Design Review durchführen", description: "Überprüfung des neuen Dashboard-Designs", priority: "high", dueDate: "2026-03-20", assignee: "Anna M." },
      { id: "2", title: "Dokumentation aktualisieren", description: "API-Dokumentation auf den neuesten Stand bringen", priority: "medium", dueDate: "2026-03-22" },
      { id: "6", title: "Onboarding-Prozess überarbeiten", priority: "low", assignee: "Tom B." },
    ],
  },
  {
    id: "in-progress",
    title: "In Bearbeitung",
    tasks: [
      { id: "3", title: "Neue Authentifizierung implementieren", description: "JWT + RBAC Integration", priority: "urgent", dueDate: "2026-03-19", assignee: "Max K." },
      { id: "7", title: "Performance-Optimierung DB", description: "Slow Queries analysieren", priority: "high", assignee: "Lisa W." },
    ],
  },
  {
    id: "review",
    title: "Review",
    tasks: [
      { id: "4", title: "Code Review für PR #123", priority: "medium", assignee: "Sarah L." },
    ],
  },
  {
    id: "done",
    title: "Erledigt",
    tasks: [
      { id: "5", title: "Login-Seite optimiert", description: "Performance-Verbesserungen implementiert", priority: "low" },
      { id: "8", title: "CI/CD Pipeline aufgesetzt", priority: "medium", assignee: "Max K." },
    ],
  },
];

export function BoardPage() {
  const [columns, setColumns] = useState<Column[]>(initialColumns);
  const [searchQuery, setSearchQuery] = useState("");
  const [selectedTask, setSelectedTask] = useState<Task | null>(null);
  const [selectedColumnId, setSelectedColumnId] = useState<string | null>(null);

  const totalTasks = columns.reduce((sum, col) => sum + col.tasks.length, 0);
  const doneTasks = columns.find((c) => c.id === "done")?.tasks.length ?? 0;

  const moveTask = (taskId: string, fromColumnId: string, toColumnId: string) => {
    setColumns((prev) => {
      const cols = prev.map((c) => ({ ...c, tasks: [...c.tasks] }));
      const from = cols.find((c) => c.id === fromColumnId);
      const to = cols.find((c) => c.id === toColumnId);
      if (!from || !to) return prev;
      const idx = from.tasks.findIndex((t) => t.id === taskId);
      if (idx === -1) return prev;
      const [task] = from.tasks.splice(idx, 1);
      to.tasks.push(task);
      return cols;
    });
  };

  const updateTask = (columnId: string, updatedTask: Task) => {
    setColumns((prev) =>
      prev.map((col) =>
        col.id === columnId
          ? { ...col, tasks: col.tasks.map((t) => (t.id === updatedTask.id ? updatedTask : t)) }
          : col
      )
    );
  };

  const deleteTask = (columnId: string, taskId: string) => {
    setColumns((prev) =>
      prev.map((col) =>
        col.id === columnId ? { ...col, tasks: col.tasks.filter((t) => t.id !== taskId) } : col
      )
    );
  };

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
              <button className="flex items-center gap-1 font-semibold text-gray-800 hover:text-blue-600 transition-colors">
                Mein Projekt
                <ChevronDown className="w-3.5 h-3.5" />
              </button>
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
            <div className="w-8 h-8 rounded-full bg-blue-600 text-white text-xs font-bold flex items-center justify-center">
              ÖY
            </div>
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
          <div className="flex gap-4 h-full min-w-max items-start">
            {columns.map((column) => (
              <KanbanColumn
                key={column.id}
                column={column}
                onMoveTask={moveTask}
                onTaskClick={(task, colId) => { setSelectedTask(task); setSelectedColumnId(colId); }}
                searchQuery={searchQuery}
              />
            ))}
          </div>
        </div>

        {selectedTask && selectedColumnId && (
          <TaskDetailModal
            task={selectedTask}
            columnId={selectedColumnId}
            onClose={() => { setSelectedTask(null); setSelectedColumnId(null); }}
            onUpdate={(updatedTask) => { updateTask(selectedColumnId, updatedTask); setSelectedTask(null); setSelectedColumnId(null); }}
            onDelete={() => { deleteTask(selectedColumnId, selectedTask.id); setSelectedTask(null); setSelectedColumnId(null); }}
          />
        )}
      </div>
    </DndProvider>
  );
}
