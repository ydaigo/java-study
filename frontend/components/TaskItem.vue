<script setup lang="ts">
import type { Task } from '~/types/task'

const props = defineProps<{
  task: Task
  isProcessing?: boolean
}>()

const emit = defineEmits<{
  toggle: [id: number, completed: boolean]
  delete: [id: number]
}>()

const handleToggle = () => {
  if (!props.isProcessing) {
    emit('toggle', props.task.id, !props.task.completed)
  }
}

const handleDelete = () => {
  if (!props.isProcessing) {
    emit('delete', props.task.id)
  }
}
</script>

<template>
  <li
    :class="[
      'flex items-center gap-4 p-4 bg-white rounded-lg shadow border border-gray-200',
      isProcessing ? 'opacity-50' : ''
    ]"
  >
    <input
      type="checkbox"
      :id="`task-${task.id}`"
      :checked="task.completed"
      :disabled="isProcessing"
      :aria-label="`「${task.title}」を${task.completed ? '未完了' : '完了'}にする`"
      class="w-5 h-5 text-blue-600 rounded focus:ring-blue-500 cursor-pointer disabled:cursor-not-allowed"
      @change="handleToggle"
    />

    <div class="flex-1 min-w-0">
      <label
        :for="`task-${task.id}`"
        :class="[
          'font-medium cursor-pointer',
          task.completed ? 'line-through text-gray-400' : 'text-gray-900'
        ]"
      >
        {{ task.title }}
      </label>
      <p v-if="task.description" class="text-sm text-gray-500 truncate">
        {{ task.description }}
      </p>
    </div>

    <button
      :disabled="isProcessing"
      :aria-label="`「${task.title}」を削除`"
      class="px-3 py-1 text-sm text-red-600 hover:bg-red-50 rounded transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
      @click="handleDelete"
    >
      {{ isProcessing ? '処理中...' : '削除' }}
    </button>
  </li>
</template>
