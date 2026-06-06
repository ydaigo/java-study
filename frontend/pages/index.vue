<script setup lang="ts">
import type { Task } from '~/types/task'
import { ApiError } from '~/composables/useTaskApi'

const taskApi = useTaskApi()

const tasks = ref<Task[]>([])
const loading = ref(true)
const error = ref<string | null>(null)
const submitting = ref(false)
const processingIds = ref<Set<number>>(new Set())

const getErrorMessage = (err: unknown, defaultMessage: string): string => {
  if (err instanceof ApiError) {
    return err.message
  }
  if (err instanceof TypeError) {
    return 'ネットワークエラー: サーバーに接続できません'
  }
  return defaultMessage
}

const loadTasks = async () => {
  try {
    loading.value = true
    const data = await taskApi.getAll()
    tasks.value = data
    error.value = null
  } catch (err) {
    error.value = getErrorMessage(err, 'タスクの取得に失敗しました')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadTasks()
})

const handleCreate = async (title: string, description: string) => {
  try {
    submitting.value = true
    error.value = null
    const newTask = await taskApi.create({
      title,
      description: description || undefined,
    })
    tasks.value = [...tasks.value, newTask]
  } catch (err) {
    error.value = getErrorMessage(err, 'タスクの作成に失敗しました')
  } finally {
    submitting.value = false
  }
}

const handleToggle = async (id: number, completed: boolean) => {
  if (processingIds.value.has(id)) return

  try {
    processingIds.value = new Set(processingIds.value).add(id)
    error.value = null
    const updated = await taskApi.update(id, { completed })
    tasks.value = tasks.value.map((t) => (t.id === id ? updated : t))
  } catch (err) {
    error.value = getErrorMessage(err, 'タスクの更新に失敗しました')
  } finally {
    const next = new Set(processingIds.value)
    next.delete(id)
    processingIds.value = next
  }
}

const handleDelete = async (id: number) => {
  if (processingIds.value.has(id)) return

  try {
    processingIds.value = new Set(processingIds.value).add(id)
    error.value = null
    await taskApi.remove(id)
    tasks.value = tasks.value.filter((t) => t.id !== id)
  } catch (err) {
    error.value = getErrorMessage(err, 'タスクの削除に失敗しました')
  } finally {
    const next = new Set(processingIds.value)
    next.delete(id)
    processingIds.value = next
  }
}

const clearError = () => {
  error.value = null
}
</script>

<template>
  <div class="min-h-screen bg-gray-100">
    <div class="max-w-2xl mx-auto py-8 px-4">
      <header>
        <h1 class="text-3xl font-bold text-gray-900 mb-8 text-center">
          タスク管理
        </h1>
      </header>

      <main>
        <section
          class="bg-white rounded-lg shadow p-6 mb-6"
          aria-label="タスク作成フォーム"
        >
          <TaskForm :disabled="submitting" @submit="handleCreate" />
        </section>

        <div
          v-if="error"
          role="alert"
          class="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg mb-6"
        >
          <span>{{ error }}</span>
          <button
            class="float-right text-red-500 hover:text-red-700"
            aria-label="エラーメッセージを閉じる"
            @click="clearError"
          >
            <span aria-hidden="true">×</span>
          </button>
        </div>

        <section aria-label="タスク一覧">
          <div
            v-if="loading"
            class="text-center py-8 text-gray-500"
            role="status"
            aria-live="polite"
          >
            読み込み中...
          </div>

          <TaskList
            v-else
            :tasks="tasks"
            :processing-ids="processingIds"
            @toggle="handleToggle"
            @delete="handleDelete"
          />
        </section>
      </main>

      <footer class="mt-8 text-center text-sm text-gray-500">
        <a
          href="http://localhost:8080/swagger-ui.html"
          target="_blank"
          rel="noopener noreferrer"
          class="text-blue-600 hover:underline"
        >
          Swagger UI でAPIを確認
        </a>
      </footer>
    </div>
  </div>
</template>
