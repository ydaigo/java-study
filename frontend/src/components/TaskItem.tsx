/**
 * タスクアイテムコンポーネント
 * 個別のタスクを表示し、完了切り替えと削除の操作を提供する
 */
import type { Task } from "../api/taskApi";

interface TaskItemProps {
  task: Task;
  onToggle: (id: number, completed: boolean) => void;
  onDelete: (id: number) => void;
  isProcessing?: boolean;
}

export function TaskItem({
  task,
  onToggle,
  onDelete,
  isProcessing = false,
}: TaskItemProps) {
  const handleToggle = () => {
    if (!isProcessing) {
      onToggle(task.id, !task.completed);
    }
  };

  const handleDelete = () => {
    if (!isProcessing) {
      onDelete(task.id);
    }
  };

  return (
    <li
      className={`flex items-center gap-4 p-4 bg-white rounded-lg shadow border border-gray-200 ${
        isProcessing ? "opacity-50" : ""
      }`}
    >
      <input
        type="checkbox"
        id={`task-${task.id}`}
        checked={task.completed}
        onChange={handleToggle}
        disabled={isProcessing}
        aria-label={`「${task.title}」を${task.completed ? "未完了" : "完了"}にする`}
        className="w-5 h-5 text-blue-600 rounded focus:ring-blue-500 cursor-pointer disabled:cursor-not-allowed"
      />

      <div className="flex-1 min-w-0">
        <label
          htmlFor={`task-${task.id}`}
          className={`font-medium cursor-pointer ${
            task.completed ? "line-through text-gray-400" : "text-gray-900"
          }`}
        >
          {task.title}
        </label>
        {task.description && (
          <p className="text-sm text-gray-500 truncate">{task.description}</p>
        )}
      </div>

      <button
        onClick={handleDelete}
        disabled={isProcessing}
        aria-label={`「${task.title}」を削除`}
        className="px-3 py-1 text-sm text-red-600 hover:bg-red-50 rounded transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
      >
        {isProcessing ? "処理中..." : "削除"}
      </button>
    </li>
  );
}
