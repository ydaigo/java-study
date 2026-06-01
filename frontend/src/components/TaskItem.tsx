/**
 * タスクアイテムコンポーネント
 * 個別のタスクを表示し、完了切り替えと削除の操作を提供する
 */
import type { Task } from "../api/taskApi";

/**
 * TaskItemコンポーネントのプロパティ
 */
interface TaskItemProps {
  /** 表示するタスク */
  task: Task;
  /** 完了状態を切り替えるコールバック */
  onToggle: (id: number, completed: boolean) => void;
  /** タスクを削除するコールバック */
  onDelete: (id: number) => void;
}

/**
 * タスクアイテムコンポーネント
 * - チェックボックスで完了/未完了を切り替え
 * - 完了したタスクは打ち消し線で表示
 * - 削除ボタンでタスクを削除
 */
export function TaskItem({ task, onToggle, onDelete }: TaskItemProps) {
  return (
    <div className="flex items-center gap-4 p-4 bg-white rounded-lg shadow border border-gray-200">
      {/* 完了チェックボックス */}
      <input
        type="checkbox"
        checked={task.completed}
        onChange={() => onToggle(task.id, !task.completed)}
        className="w-5 h-5 text-blue-600 rounded focus:ring-blue-500"
      />

      {/* タスク情報 */}
      <div className="flex-1 min-w-0">
        <h3
          className={`font-medium ${task.completed ? "line-through text-gray-400" : "text-gray-900"}`}
        >
          {task.title}
        </h3>
        {task.description && (
          <p className="text-sm text-gray-500 truncate">{task.description}</p>
        )}
      </div>

      {/* 削除ボタン */}
      <button
        onClick={() => onDelete(task.id)}
        className="px-3 py-1 text-sm text-red-600 hover:bg-red-50 rounded transition-colors"
      >
        削除
      </button>
    </div>
  );
}
