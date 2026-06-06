export interface Task {
  id: number
  title: string
  description: string | null
  completed: boolean
  createdAt: string
}

export interface CreateTaskRequest {
  title: string
  description?: string
}

export interface UpdateTaskRequest {
  title?: string
  description?: string
  completed?: boolean
}

export interface ApiErrorResponse {
  timestamp: string
  status: number
  error: string
  message: string
  path: string
  details?: Record<string, string>
}
