import { useDrag } from "react-dnd";
import { Calendar, User, GripVertical } from "lucide-react";
import type { Task } from "../pages/BoardPage";

interface TaskCardProps {
  task: Task;
  columnId: string;
  onClick: () => void;
}

const priorityConfig = {
  urgent: { label: "Dringend", bg: "#FEE2E2", text: "#991B1B", bar: "#EF4444" },
  high:   { label: "Hoch",     bg: "#FFEDD5", text: "#9A3412", bar: "#F97316" },
  medium: { label: "Mittel",   bg: "#FEF9C3", text: "#854D0E", bar: "#EAB308" },
  low:    { label: "Niedrig",  bg: "#DCFCE7", text: "#166534", bar: "#22C55E" },
};

export function TaskCard({ task, columnId, onClick }: TaskCardProps) {
  const [{ isDragging }, drag] = useDrag(() => ({
    type: "TASK",
    item: { taskId: task.id, columnId },
    collect: (monitor) => ({ isDragging: !!monitor.isDragging() }),
  }));

  const p = priorityConfig[task.priority] ?? priorityConfig["medium"];

  return (
    <div
      ref={drag}
      onClick={onClick}
      className={`bg-white rounded-xl border border-gray-100 cursor-pointer group transition-all duration-150 overflow-hidden ${
        isDragging ? "opacity-40 scale-95 shadow-lg" : "hover:shadow-md hover:-translate-y-0.5"
      }`}
      style={{ boxShadow: "0 1px 3px rgba(0,0,0,0.06)" }}
    >
      {/* Priority color bar */}
      <div className="h-1 w-full" style={{ background: p.bar }} />

      <div className="p-3">
        {/* Priority badge + drag handle */}
        <div className="flex items-center justify-between mb-2">
          <span
            className="text-xs font-semibold px-2 py-0.5 rounded-full"
            style={{ background: p.bg, color: p.text }}
          >
            {p.label}
          </span>
          <GripVertical className="w-4 h-4 text-gray-300 opacity-0 group-hover:opacity-100 transition-opacity" />
        </div>

        {/* Title */}
        <p className="text-sm font-medium text-gray-800 leading-snug mb-2">{task.title}</p>

        {/* Description preview */}
        {task.description && (
          <p className="text-xs text-gray-400 mb-2 line-clamp-2">{task.description}</p>
        )}

        {/* Meta row */}
        {(task.dueDate || task.assignee) && (
          <div className="flex items-center gap-3 text-xs text-gray-400 pt-2 border-t border-gray-50">
            {task.dueDate && (
              <div className="flex items-center gap-1">
                <Calendar className="w-3 h-3" />
                <span>{new Date(task.dueDate).toLocaleDateString("de-DE")}</span>
              </div>
            )}
            {task.assignee && (
              <div className="flex items-center gap-1 ml-auto">
                <div className="w-5 h-5 rounded-full bg-blue-100 text-blue-700 text-xs flex items-center justify-center font-semibold">
                  {task.assignee.charAt(0)}
                </div>
                <span>{task.assignee}</span>
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  );
}
