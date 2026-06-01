/**
 * メインアプリケーションコンポーネント
 * タスク管理アプリのルートコンポーネント
 */
import { useState, useEffect, useCallback } from "react";
import type { Task } from "./api/taskApi";
import { taskApi, ApiError } from "./api/taskApi";
import { TaskForm } from "./components/TaskForm";
import { TaskList } from "./components/TaskList";

/**
 * Appコンポーネント
 * アプリケーション全体の状態管理とUIを担当
 */
function App() {
  const [tasks, setTasks] = useState<Task[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [submitting, setSubmitting] = useState(false);
  const [processingIds, setProcessingIds] = useState<Set<number>>(new Set());

  /**
   * エラーメッセージを取得する
   */
  const getErrorMessage = (err: unknown, defaultMessage: string): string => {
    if (err instanceof ApiError) {
      return err.message;
    }
    if (err instanceof TypeError) {
      return "ネットワークエラー: サーバーに接続できません";
    }
    return defaultMessage;
  };

  /**
   * タスク一覧を取得する
   */
  const loadTasks = useCallback(async () => {
    try {
      setLoading(true);
      const data = await taskApi.getAll();
      setTasks(data);
      setError(null);
    } catch (err) {
      setError(getErrorMessage(err, "タスクの取得に失敗しました"));
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    loadTasks();
  }, [loadTasks]);

  /**
   * タスク作成ハンドラ
   */
  const handleCreate = async (title: string, description: string) => {
    try {
      setSubmitting(true);
      setError(null);
      const newTask = await taskApi.create({
        title,
        description: description || undefined,
      });
      setTasks((prev) => [...prev, newTask]);
    } catch (err) {
      setError(getErrorMessage(err, "タスクの作成に失敗しました"));
    } finally {
      setSubmitting(false);
    }
  };

  /**
   * タスク完了状態切り替えハンドラ
   */
  const handleToggle = async (id: number, completed: boolean) => {
    if (processingIds.has(id)) return;

    try {
      setProcessingIds((prev) => new Set(prev).add(id));
      setError(null);
      const updated = await taskApi.update(id, { completed });
      setTasks((prev) => prev.map((t) => (t.id === id ? updated : t)));
    } catch (err) {
      setError(getErrorMessage(err, "タスクの更新に失敗しました"));
    } finally {
      setProcessingIds((prev) => {
        const next = new Set(prev);
        next.delete(id);
        return next;
      });
    }
  };

  /**
   * タスク削除ハンドラ
   */
  const handleDelete = async (id: number) => {
    if (processingIds.has(id)) return;

    try {
      setProcessingIds((prev) => new Set(prev).add(id));
      setError(null);
      await taskApi.delete(id);
      setTasks((prev) => prev.filter((t) => t.id !== id));
    } catch (err) {
      setError(getErrorMessage(err, "タスクの削除に失敗しました"));
    } finally {
      setProcessingIds((prev) => {
        const next = new Set(prev);
        next.delete(id);
        return next;
      });
    }
  };

  return (
    <div className="min-h-screen bg-gray-100">
      <div className="max-w-2xl mx-auto py-8 px-4">
        <header>
          <h1 className="text-3xl font-bold text-gray-900 mb-8 text-center">
            タスク管理
          </h1>
        </header>

        <main>
          <section
            className="bg-white rounded-lg shadow p-6 mb-6"
            aria-label="タスク作成フォーム"
          >
            <TaskForm onSubmit={handleCreate} disabled={submitting} />
          </section>

          {error && (
            <div
              role="alert"
              className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg mb-6"
            >
              <span>{error}</span>
              <button
                onClick={() => setError(null)}
                className="float-right text-red-500 hover:text-red-700"
                aria-label="エラーメッセージを閉じる"
              >
                <span aria-hidden="true">×</span>
              </button>
            </div>
          )}

          <section aria-label="タスク一覧">
            {loading ? (
              <div
                className="text-center py-8 text-gray-500"
                role="status"
                aria-live="polite"
              >
                読み込み中...
              </div>
            ) : (
              <TaskList
                tasks={tasks}
                onToggle={handleToggle}
                onDelete={handleDelete}
                processingIds={processingIds}
              />
            )}
          </section>
        </main>

        <footer className="mt-8 text-center text-sm text-gray-500">
          <a
            href="http://localhost:8080/swagger-ui.html"
            target="_blank"
            rel="noopener noreferrer"
            className="text-blue-600 hover:underline"
          >
            Swagger UI でAPIを確認
          </a>
        </footer>
      </div>
    </div>
  );
}

export default App;
