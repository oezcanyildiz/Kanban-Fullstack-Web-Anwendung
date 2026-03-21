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
import type { Task, Priority } from "../pages/BoardPage";

interface TaskDetailModalProps {
  task: Task;
  columnId: string;
  onClose: () => void;
  onUpdate: (task: Task) => void;
  onDelete: () => void;
}

const priorityLabels = {
  high: "Hoch",
  medium: "Mittel",
  low: "Niedrig",
};

export function TaskDetailModal({
  task,
  columnId,
  onClose,
  onUpdate,
  onDelete,
}: TaskDetailModalProps) {
  const [title, setTitle] = useState(task.title);
  const [description, setDescription] = useState(task.description || "");
  const [priority, setPriority] = useState(task.priority);
  const [dueDate, setDueDate] = useState(task.dueDate || "");
  const [assignee, setAssignee] = useState(task.assignee || "");

  const handleSave = () => {
    onUpdate({
      ...task,
      title,
      description,
      priority,
      dueDate: dueDate || undefined,
      assignee: assignee || undefined,
    });
  };

  return (
    <div className="fixed inset-0 bg-black/50 flex items-center justify-center p-4 z-50">
      <div className="bg-white rounded-lg shadow-xl w-full max-w-2xl max-h-[90vh] overflow-y-auto">
        {/* Header */}
        <div className="flex items-center justify-between p-6 border-b">
          <h2 className="text-[#172B4D]">Aufgaben-Details</h2>
          <button
            onClick={onClose}
            className="text-[#5E6C84] hover:text-[#172B4D] transition-colors"
          >
            <X className="w-5 h-5" />
          </button>
        </div>

        {/* Content */}
        <div className="p-6 space-y-6">
          {/* Title */}
          <div>
            <Label htmlFor="title" className="text-[#172B4D]">
              Titel
            </Label>
            <Input
              id="title"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              className="mt-1 border-[#EBECF0] focus:border-[#0052CC]"
            />
          </div>

          {/* Description */}
          <div>
            <Label htmlFor="description" className="text-[#172B4D]">
              Beschreibung
            </Label>
            <Textarea
              id="description"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              className="mt-1 border-[#EBECF0] focus:border-[#0052CC] min-h-32"
              placeholder="Fügen Sie eine detaillierte Beschreibung hinzu..."
            />
          </div>

          {/* Priority */}
          <div>
            <Label className="text-[#172B4D]">Priorität</Label>
            <Select value={priority} onValueChange={(value) => setPriority(value as Priority)}>
              <SelectTrigger className="mt-1 border-[#EBECF0] focus:border-[#0052CC]">
                <SelectValue />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="low">{priorityLabels.low}</SelectItem>
                <SelectItem value="medium">{priorityLabels.medium}</SelectItem>
                <SelectItem value="high">{priorityLabels.high}</SelectItem>
              </SelectContent>
            </Select>
          </div>

          {/* Due Date */}
          <div>
            <Label htmlFor="dueDate" className="text-[#172B4D]">
              <Calendar className="w-4 h-4 inline mr-2" />
              Fälligkeitsdatum
            </Label>
            <Input
              id="dueDate"
              type="date"
              value={dueDate}
              onChange={(e) => setDueDate(e.target.value)}
              className="mt-1 border-[#EBECF0] focus:border-[#0052CC]"
            />
          </div>

          {/* Assignee */}
          <div>
            <Label htmlFor="assignee" className="text-[#172B4D]">
              <User className="w-4 h-4 inline mr-2" />
              Zugewiesen an
            </Label>
            <Input
              id="assignee"
              value={assignee}
              onChange={(e) => setAssignee(e.target.value)}
              className="mt-1 border-[#EBECF0] focus:border-[#0052CC]"
              placeholder="Name eingeben..."
            />
          </div>
        </div>

        {/* Footer */}
        <div className="flex items-center justify-between p-6 border-t bg-[#F4F5F7]">
          <Button
            variant="destructive"
            onClick={onDelete}
            className="bg-[#FF5630] hover:bg-[#DE350B]"
          >
            <Trash2 className="w-4 h-4 mr-2" />
            Löschen
          </Button>
          
          <div className="flex gap-2">
            <Button
              variant="outline"
              onClick={onClose}
              className="border-[#EBECF0] text-[#172B4D]"
            >
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
