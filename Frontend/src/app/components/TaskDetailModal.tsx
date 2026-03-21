import { useState } from "react";
import { X, Trash2, Calendar, User } from "lucide-react";
import { Button } from "./ui/button";
import { Input } from "./ui/input";
import { Textarea } from "./ui/textarea";
import { Label } from "./ui/label";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "./ui/select";
import type { Task, Priority, TeamMember } from "../pages/BoardPage";

interface TaskDetailModalProps {
  task: Task;
  columnId: string;
  teamMembers: TeamMember[];
  onClose: () => void;
  onUpdate: (task: Task) => void;
  onDelete: () => void;
}

const priorityLabels: Record<string, string> = {
  urgent: "Dringend",
  high: "Hoch",
  medium: "Mittel",
  low: "Niedrig",
};

export function TaskDetailModal({
  task,
  columnId,
  teamMembers,
  onClose,
  onUpdate,
  onDelete,
}: TaskDetailModalProps) {
  const [title, setTitle] = useState(task.title);
  const [description, setDescription] = useState(task.description || "");
  const [priority, setPriority] = useState(task.priority);
  const [dueDate, setDueDate] = useState(task.dueDate || "");
  const [assigneeId, setAssigneeId] = useState<string>(
    task.assigneeId ? String(task.assigneeId) : "unassigned"
  );

  const handleSave = () => {
    const selectedMember = teamMembers.find((m) => String(m.userID) === assigneeId);
    onUpdate({
      ...task,
      title,
      description,
      priority,
      dueDate: dueDate || undefined,
      assigneeId: assigneeId !== "unassigned" ? Number(assigneeId) : undefined,
      assignee: selectedMember
        ? `${selectedMember.userName} ${selectedMember.userLastName}`
        : undefined,
    });
  };

  return (
    <div className="fixed inset-0 bg-black/50 flex items-center justify-center p-4 z-50">
      <div className="bg-white rounded-xl shadow-xl w-full max-w-2xl max-h-[90vh] overflow-y-auto">
        {/* Header */}
        <div className="flex items-center justify-between px-6 py-4 border-b">
          <h2 className="text-base font-semibold text-gray-900">Aufgaben-Details</h2>
          <button
            onClick={onClose}
            className="text-gray-400 hover:text-gray-700 transition-colors cursor-pointer p-1 rounded hover:bg-gray-100"
          >
            <X className="w-5 h-5" />
          </button>
        </div>

        {/* Content */}
        <div className="p-6 space-y-5">
          {/* Title */}
          <div>
            <Label htmlFor="title" className="text-xs font-semibold text-gray-500 uppercase tracking-wide">
              Titel
            </Label>
            <Input
              id="title"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              className="mt-1"
            />
          </div>

          {/* Description */}
          <div>
            <Label htmlFor="description" className="text-xs font-semibold text-gray-500 uppercase tracking-wide">
              Beschreibung
            </Label>
            <Textarea
              id="description"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              className="mt-1 min-h-28"
              placeholder="Detaillierte Beschreibung..."
            />
          </div>

          {/* Priority + Due Date row */}
          <div className="grid grid-cols-2 gap-4">
            <div>
              <Label className="text-xs font-semibold text-gray-500 uppercase tracking-wide">Priorität</Label>
              <Select value={priority} onValueChange={(value) => setPriority(value as Priority)}>
                <SelectTrigger className="mt-1">
                  <SelectValue />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="urgent">🔴 Dringend</SelectItem>
                  <SelectItem value="high">🟠 Hoch</SelectItem>
                  <SelectItem value="medium">🟡 Mittel</SelectItem>
                  <SelectItem value="low">🟢 Niedrig</SelectItem>
                </SelectContent>
              </Select>
            </div>

            <div>
              <Label htmlFor="dueDate" className="text-xs font-semibold text-gray-500 uppercase tracking-wide flex items-center gap-1">
                <Calendar className="w-3 h-3" /> Fälligkeitsdatum
              </Label>
              <Input
                id="dueDate"
                type="date"
                value={dueDate}
                onChange={(e) => setDueDate(e.target.value)}
                className="mt-1"
              />
            </div>
          </div>

          {/* Assignee Dropdown */}
          <div>
            <Label className="text-xs font-semibold text-gray-500 uppercase tracking-wide flex items-center gap-1">
              <User className="w-3 h-3" /> Zugewiesen an
            </Label>
            <Select value={assigneeId} onValueChange={setAssigneeId}>
              <SelectTrigger className="mt-1">
                <SelectValue placeholder="Kein Mitglied zugewiesen" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="unassigned">— Kein Mitglied —</SelectItem>
                {teamMembers.map((member) => (
                  <SelectItem key={member.userID} value={String(member.userID)}>
                    <div className="flex items-center gap-2">
                      <div className="w-5 h-5 rounded-full bg-blue-100 text-blue-700 text-xs font-bold flex items-center justify-center flex-shrink-0">
                        {member.userName.charAt(0)}
                      </div>
                      {member.userName} {member.userLastName}
                    </div>
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
          </div>
        </div>

        {/* Footer */}
        <div className="flex items-center justify-between px-6 py-4 border-t bg-gray-50 rounded-b-xl">
          <Button
            variant="destructive"
            onClick={onDelete}
            className="gap-2"
          >
            <Trash2 className="w-4 h-4" />
            Löschen
          </Button>

          <div className="flex gap-2">
            <Button variant="outline" onClick={onClose}>
              Abbrechen
            </Button>
            <Button
              onClick={handleSave}
              className="bg-[#0052CC] text-white hover:bg-[#0747A6]"
            >
              Speichern
            </Button>
          </div>
        </div>
      </div>
    </div>
  );
}
