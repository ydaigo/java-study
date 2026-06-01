/**
 * タスクAPI クライアント
 * バックエンドのREST APIと通信するための関数群
 */

/** APIのベースURL */
const API_BASE = 'http://localhost:8080/api/tasks';

/**
 * タスクの型定義
 * バックエンドのTaskResponseに対応
 */
export interface Task {
  id: number;
  title: string;
  description: string | null;
  completed: boolean;
  createdAt: string;
}

/**
 * タスク作成リクエストの型定義
 */
export interface CreateTaskRequest {
  title: string;
  description?: string;
}

/**
 * タスク更新リクエストの型定義
 */
export interface UpdateTaskRequest {
  title?: string;
  description?: string;
  completed?: boolean;
}

/**
 * APIエラーレスポンスの型定義
 */
export interface ApiErrorResponse {
  timestamp: string;
  status: number;
  error: string;
  message: string;
  path: string;
  details?: Record<string, string>;
}

/**
 * APIエラークラス
 */
export class ApiError extends Error {
  status: number;
  details?: Record<string, string>;

  constructor(
    message: string,
    status: number,
    details?: Record<string, string>
  ) {
    super(message);
    this.name = 'ApiError';
    this.status = status;
    this.details = details;
  }
}

/**
 * レスポンスを処理する共通関数
 */
async function handleResponse<T>(response: Response): Promise<T> {
  if (!response.ok) {
    let errorMessage = 'エラーが発生しました';
    let details: Record<string, string> | undefined;

    try {
      const errorData: ApiErrorResponse = await response.json();
      errorMessage = errorData.message || errorMessage;
      details = errorData.details;
    } catch {
      // JSONパースに失敗した場合はデフォルトメッセージを使用
    }

    throw new ApiError(errorMessage, response.status, details);
  }
  return response.json();
}

/**
 * タスクAPIクライアント
 * CRUD操作を提供する
 */
export const taskApi = {
  /**
   * 全タスクを取得する
   */
  async getAll(): Promise<Task[]> {
    const res = await fetch(API_BASE);
    return handleResponse<Task[]>(res);
  },

  /**
   * IDを指定してタスクを取得する
   */
  async getById(id: number): Promise<Task> {
    const res = await fetch(`${API_BASE}/${id}`);
    return handleResponse<Task>(res);
  },

  /**
   * 新しいタスクを作成する
   */
  async create(data: CreateTaskRequest): Promise<Task> {
    const res = await fetch(API_BASE, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
    });
    return handleResponse<Task>(res);
  },

  /**
   * タスクを更新する
   */
  async update(id: number, data: UpdateTaskRequest): Promise<Task> {
    const res = await fetch(`${API_BASE}/${id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
    });
    return handleResponse<Task>(res);
  },

  /**
   * タスクを削除する
   */
  async delete(id: number): Promise<void> {
    const res = await fetch(`${API_BASE}/${id}`, {
      method: 'DELETE',
    });
    if (!res.ok) {
      throw new ApiError('タスクの削除に失敗しました', res.status);
    }
  },
};
