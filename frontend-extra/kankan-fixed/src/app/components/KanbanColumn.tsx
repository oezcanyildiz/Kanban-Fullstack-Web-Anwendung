import { useDrop } from "react-dnd";
import { Plus, MoreHorizontal } from "lucide-react";
import { TaskCard } from "./TaskCard";
import { Button } from "./ui/button";
import type { Column, Task } from "../pages/BoardPage";

interface KanbanColumnProps {
  column: Column;
  onMoveTask: (taskId: string, fromColumnId: string, toColumnId: string) => void;
  onTaskClick: (task: Task, columnId: string) => void;
  searchQuery: string;
}

const columnAccents: Record<string, { dot: string; header: string; badge: string }> = {
  todo:        { dot: "#94A3B8", header: "#F1F5F9", badge: "bg-slate-100 text-slate-600" },
  "in-progress":{ dot: "#3B82F6", header: "#EFF6FF", badge: "bg-blue-100 text-blue-700" },
  review:      { dot: "#F59E0B", header: "#FFFBEB", badge: "bg-amber-100 text-amber-700" },
  done:        { dot: "#10B981", header: "#F0FDF4", badge: "bg-emerald-100 text-emerald-700" },
};

export function KanbanColumn({ column, onMoveTask, onTaskClick, searchQuery }: KanbanColumnProps) {
  const [{ isOver }, drop] = useDrop(() => ({
    accept: "TASK",
    drop: (item: { taskId: string; columnId: string }) => {
      if (item.columnId !== column.id) onMoveTask(item.taskId, item.columnId, column.id);
    },
    collect: (monitor) => ({ isOver: !!monitor.isOver() }),
  }));

  const accent = columnAccents[column.id] ?? columnAccents["todo"];
  const filteredTasks = column.tasks.filter((t) =>
    t.title.toLowerCase().includes(searchQuery.toLowerCase())
  );

  return (
    <div className="flex-shrink-0 w-72 flex flex-col" style={{ maxHeight: "calc(100vh - 80px)" }}>
      {/* Column Header */}
      <div
        className="rounded-t-xl px-4 py-3 flex items-center justify-between border border-b-0 border-gray-200"
        style={{ background: accent.header }}
      >
        <div className="flex items-center gap-2">
          <span className="w-2.5 h-2.5 rounded-full flex-shrink-0" style={{ background: accent.dot }} />
          <h2 className="font-semibold text-sm text-gray-800 tracking-wide">{column.title}</h2>
        </div>
        <div className="flex items-center gap-2">
          <span className={`text-xs font-semibold px-2 py-0.5 rounded-full ${accent.badge}`}>
            {filteredTasks.length}
          </span>
          <button className="text-gray-400 hover:text-gray-600 transition-colors">
            <MoreHorizontal className="w-4 h-4" />
          </button>
        </div>
      </div>

      {/* Drop Zone */}
      <div
        ref={drop}
        className={`flex-1 overflow-y-auto px-3 py-3 space-y-2 border border-t-0 border-gray-200 rounded-b-xl transition-colors ${
          isOver ? "bg-blue-50 border-blue-300" : "bg-gray-50"
        }`}
        style={{ minHeight: "200px" }}
      >
        {filteredTasks.length === 0 && (
          <div className="flex items-center justify-center h-24 text-gray-400 text-xs border-2 border-dashed border-gray-200 rounded-lg">
            Keine Aufgaben
          </div>
        )}
        {filteredTasks.map((task) => (
          <TaskCard key={task.id} task={task} columnId={column.id} onClick={() => onTaskClick(task, column.id)} />
        ))}
      </div>

      {/* Add Task */}
      <Button
        variant="ghost"
        className="mt-2 w-full text-gray-500 hover:text-gray-800 hover:bg-gray-100 justify-start text-sm rounded-lg border border-dashed border-gray-300 hover:border-gray-400 transition-all"
      >
        <Plus className="w-4 h-4 mr-2" />
        Aufgabe hinzufügen
      </Button>
    </div>
  );
}
