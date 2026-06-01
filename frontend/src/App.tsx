/**
 * メインアプリケーションコンポーネント
 * タスク管理アプリのルートコンポーネント
 * - タスクの一覧表示
 * - タスクの作成・更新・削除
 */
import { useState, useEffect } from 'react';
import { Task, taskApi } from './api/taskApi';
import { TaskForm } from './components/TaskForm';
import { TaskList } from './components/TaskList';

/**
 * Appコンポーネント
 * アプリケーション全体の状態管理とUIを担当
 */
function App() {
  // アプリケーションの状態
  const [tasks, setTasks] = useState<Task[]>([]);      // タスク一覧
  const [loading, setLoading] = useState(true);         // 読み込み中フラグ
  const [error, setError] = useState<string | null>(null); // エラーメッセージ

  /**
   * タスク一覧を取得する
   * コンポーネントのマウント時に呼び出される
   */
  const fetchTasks = async () => {
    try {
      setLoading(true);
      const data = await taskApi.getAll();
      setTasks(data);
      setError(null);
    } catch (err) {
      setError('タスクの取得に失敗しました');
    } finally {
      setLoading(false);
    }
  };

  // コンポーネントマウント時にタスク一覧を取得
  useEffect(() => {
    fetchTasks();
  }, []);

  /**
   * タスク作成ハンドラ
   * @param title タスク名
   * @param description タスクの説明
   */
  const handleCreate = async (title: string, description: string) => {
    try {
      const newTask = await taskApi.create({ title, description: description || undefined });
      // 作成したタスクを一覧に追加
      setTasks((prev) => [...prev, newTask]);
    } catch (err) {
      setError('タスクの作成に失敗しました');
    }
  };

  /**
   * タスク完了状態切り替えハンドラ
   * @param id タスクID
   * @param completed 新しい完了状態
   */
  const handleToggle = async (id: number, completed: boolean) => {
    try {
      const updated = await taskApi.update(id, { completed });
      // 更新したタスクで一覧を更新
      setTasks((prev) => prev.map((t) => (t.id === id ? updated : t)));
    } catch (err) {
      setError('タスクの更新に失敗しました');
    }
  };

  /**
   * タスク削除ハンドラ
   * @param id タスクID
   */
  const handleDelete = async (id: number) => {
    try {
      await taskApi.delete(id);
      // 削除したタスクを一覧から除外
      setTasks((prev) => prev.filter((t) => t.id !== id));
    } catch (err) {
      setError('タスクの削除に失敗しました');
    }
  };

  return (
    <div className="min-h-screen bg-gray-100">
      <div className="max-w-2xl mx-auto py-8 px-4">
        {/* ヘッダー */}
        <h1 className="text-3xl font-bold text-gray-900 mb-8 text-center">
          タスク管理
        </h1>

        {/* タスク作成フォーム */}
        <div className="bg-white rounded-lg shadow p-6 mb-6">
          <TaskForm onSubmit={handleCreate} />
        </div>

        {/* エラーメッセージ */}
        {error && (
          <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg mb-6">
            {error}
            <button
              onClick={() => setError(null)}
              className="float-right text-red-500 hover:text-red-700"
            >
              ✕
            </button>
          </div>
        )}

        {/* タスク一覧（読み込み中は「読み込み中...」を表示） */}
        {loading ? (
          <div className="text-center py-8 text-gray-500">読み込み中...</div>
        ) : (
          <TaskList tasks={tasks} onToggle={handleToggle} onDelete={handleDelete} />
        )}

        {/* フッター（Swagger UIへのリンク） */}
        <div className="mt-8 text-center text-sm text-gray-500">
          <a
            href="http://localhost:8080/swagger-ui.html"
            target="_blank"
            rel="noopener noreferrer"
            className="text-blue-600 hover:underline"
          >
            Swagger UI でAPIを確認
          </a>
        </div>
      </div>
    </div>
  );
}

export default App;
