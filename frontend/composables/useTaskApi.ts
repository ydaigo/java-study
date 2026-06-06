import type { Task, CreateTaskRequest, UpdateTaskRequest, ApiErrorResponse } from '~/types/task'

const API_BASE = 'http://localhost:8080/api/tasks'

export class ApiError extends Error {
  status: number
  details?: Record<string, string>

  constructor(message: string, status: number, details?: Record<string, string>) {
    super(message)
    this.name = 'ApiError'
    this.status = status
    this.details = details
  }
}

async function handleResponse<T>(response: Response): Promise<T> {
  if (!response.ok) {
    let errorMessage = 'エラーが発生しました'
    let details: Record<string, string> | undefined

    try {
      const errorData: ApiErrorResponse = await response.json()
      errorMessage = errorData.message || errorMessage
      details = errorData.details
    } catch {
      // JSONパースに失敗した場合はデフォルトメッセージを使用
    }

    throw new ApiError(errorMessage, response.status, details)
  }
  return response.json()
}

export function useTaskApi() {
  const getAll = async (): Promise<Task[]> => {
    const res = await fetch(API_BASE)
    return handleResponse<Task[]>(res)
  }

  const getById = async (id: number): Promise<Task> => {
    const res = await fetch(`${API_BASE}/${id}`)
    return handleResponse<Task>(res)
  }

  const create = async (data: CreateTaskRequest): Promise<Task> => {
    const res = await fetch(API_BASE, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
    })
    return handleResponse<Task>(res)
  }

  const update = async (id: number, data: UpdateTaskRequest): Promise<Task> => {
    const res = await fetch(`${API_BASE}/${id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
    })
    return handleResponse<Task>(res)
  }

  const remove = async (id: number): Promise<void> => {
    const res = await fetch(`${API_BASE}/${id}`, {
      method: 'DELETE',
    })
    if (!res.ok) {
      throw new ApiError('タスクの削除に失敗しました', res.status)
    }
  }

  return {
    getAll,
    getById,
    create,
    update,
    remove,
  }
}
