/**
 * タスク一覧コンポーネント
 * タスクの配列を受け取り、TaskItemコンポーネントで表示する
 */
import type { Task } from "../api/taskApi";
import { TaskItem } from "./TaskItem";

/**
 * TaskListコンポーネントのプロパティ
 */
interface TaskListProps {
  /** 表示するタスクの配列 */
  tasks: Task[];
  /** 完了状態を切り替えるコールバック */
  onToggle: (id: number, completed: boolean) => void;
  /** タスクを削除するコールバック */
  onDelete: (id: number) => void;
}

/**
 * タスク一覧コンポーネント
 * - タスクがない場合は「タスクがありません」と表示
 * - タスクがある場合はTaskItemを並べて表示
 */
export function TaskList({ tasks, onToggle, onDelete }: TaskListProps) {
  // タスクが0件の場合
  if (tasks.length === 0) {
    return (
      <div className="text-center py-8 text-gray-500">タスクがありません</div>
    );
  }

  // タスク一覧を表示
  return (
    <div className="space-y-3">
      {tasks.map((task) => (
        <TaskItem
          key={task.id}
          task={task}
          onToggle={onToggle}
          onDelete={onDelete}
        />
      ))}
    </div>
  );
}
