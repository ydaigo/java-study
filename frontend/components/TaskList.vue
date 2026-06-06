<script setup lang="ts">
import type { Task } from '~/types/task'

defineProps<{
  tasks: Task[]
  processingIds: Set<number>
}>()

const emit = defineEmits<{
  toggle: [id: number, completed: boolean]
  delete: [id: number]
}>()
</script>

<template>
  <div v-if="tasks.length === 0" class="text-center py-8 text-gray-500" role="status">
    タスクがありません
  </div>

  <ul v-else class="space-y-3" role="list" aria-label="タスク一覧">
    <TaskItem
      v-for="task in tasks"
      :key="task.id"
      :task="task"
      :is-processing="processingIds.has(task.id)"
      @toggle="(id, completed) => emit('toggle', id, completed)"
      @delete="(id) => emit('delete', id)"
    />
  </ul>
</template>
