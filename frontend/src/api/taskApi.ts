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
  /** タスクID */
  id: number;
  /** タスク名 */
  title: string;
  /** タスクの説明（任意） */
  description: string | null;
  /** 完了フラグ */
  completed: boolean;
  /** 作成日時 */
  createdAt: string;
}

/**
 * タスク作成リクエストの型定義
 */
export interface CreateTaskRequest {
  /** タスク名（必須） */
  title: string;
  /** タスクの説明（任意） */
  description?: string;
}

/**
 * タスク更新リクエストの型定義
 */
export interface UpdateTaskRequest {
  /** タスク名 */
  title?: string;
  /** タスクの説明 */
  description?: string;
  /** 完了フラグ */
  completed?: boolean;
}

/**
 * タスクAPIクライアント
 * CRUD操作を提供する
 */
export const taskApi = {
  /**
   * 全タスクを取得する
   * @returns タスクの配列
   */
  async getAll(): Promise<Task[]> {
    const res = await fetch(API_BASE);
    if (!res.ok) throw new Error('Failed to fetch tasks');
    return res.json();
  },

  /**
   * IDを指定してタスクを取得する
   * @param id タスクID
   * @returns タスク
   */
  async getById(id: number): Promise<Task> {
    const res = await fetch(`${API_BASE}/${id}`);
    if (!res.ok) throw new Error('Task not found');
    return res.json();
  },

  /**
   * 新しいタスクを作成する
   * @param data 作成するタスクのデータ
   * @returns 作成されたタスク
   */
  async create(data: CreateTaskRequest): Promise<Task> {
    const res = await fetch(API_BASE, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
    });
    if (!res.ok) throw new Error('Failed to create task');
    return res.json();
  },

  /**
   * タスクを更新する
   * @param id タスクID
   * @param data 更新するデータ
   * @returns 更新されたタスク
   */
  async update(id: number, data: UpdateTaskRequest): Promise<Task> {
    const res = await fetch(`${API_BASE}/${id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
    });
    if (!res.ok) throw new Error('Failed to update task');
    return res.json();
  },

  /**
   * タスクを削除する
   * @param id タスクID
   */
  async delete(id: number): Promise<void> {
    const res = await fetch(`${API_BASE}/${id}`, {
      method: 'DELETE',
    });
    if (!res.ok) throw new Error('Failed to delete task');
  },
};
