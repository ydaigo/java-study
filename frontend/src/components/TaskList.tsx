/**
 * タスク一覧コンポーネント
 * タスクの配列を受け取り、TaskItemコンポーネントで表示する
 */
import type { Task } from "../api/taskApi";
import { TaskItem } from "./TaskItem";

interface TaskListProps {
  tasks: Task[];
  onToggle: (id: number, completed: boolean) => void;
  onDelete: (id: number) => void;
  processingIds: Set<number>;
}

export function TaskList({
  tasks,
  onToggle,
  onDelete,
  processingIds,
}: TaskListProps) {
  if (tasks.length === 0) {
    return (
      <div className="text-center py-8 text-gray-500" role="status">
        タスクがありません
      </div>
    );
  }

  return (
    <ul className="space-y-3" role="list" aria-label="タスク一覧">
      {tasks.map((task) => (
        <TaskItem
          key={task.id}
          task={task}
          onToggle={onToggle}
          onDelete={onDelete}
          isProcessing={processingIds.has(task.id)}
        />
      ))}
    </ul>
  );
}
