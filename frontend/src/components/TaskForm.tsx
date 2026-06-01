/**
 * タスク作成フォームコンポーネント
 * 新しいタスクを入力して追加するためのフォーム
 */
import { useState } from 'react';

/**
 * TaskFormコンポーネントのプロパティ
 */
interface TaskFormProps {
  /** フォーム送信時のコールバック */
  onSubmit: (title: string, description: string) => void;
}

/**
 * タスク作成フォームコンポーネント
 * - タスク名（必須）と説明（任意）を入力
 * - 送信後、フォームをクリア
 */
export function TaskForm({ onSubmit }: TaskFormProps) {
  // フォームの状態管理
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');

  /**
   * フォーム送信ハンドラ
   * タスク名が空の場合は何もしない
   */
  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    // タスク名が空の場合は送信しない
    if (!title.trim()) return;
    // 親コンポーネントに通知
    onSubmit(title.trim(), description.trim());
    // フォームをクリア
    setTitle('');
    setDescription('');
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      {/* タスク名入力 */}
      <div>
        <input
          type="text"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          placeholder="タスク名を入力..."
          className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none"
        />
      </div>

      {/* 説明入力（任意） */}
      <div>
        <input
          type="text"
          value={description}
          onChange={(e) => setDescription(e.target.value)}
          placeholder="説明（任意）"
          className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none"
        />
      </div>

      {/* 送信ボタン */}
      <button
        type="submit"
        className="w-full px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors font-medium"
      >
        タスクを追加
      </button>
    </form>
  );
}
