<script setup lang="ts">
const MAX_TITLE_LENGTH = 255
const MAX_DESCRIPTION_LENGTH = 1000

const props = defineProps<{
  disabled?: boolean
}>()

const emit = defineEmits<{
  submit: [title: string, description: string]
}>()

const title = ref('')
const description = ref('')
const titleError = ref<string | null>(null)

const validateTitle = (value: string): boolean => {
  if (!value.trim()) {
    titleError.value = 'タスク名は必須です'
    return false
  }
  if (value.length > MAX_TITLE_LENGTH) {
    titleError.value = `タスク名は${MAX_TITLE_LENGTH}文字以内で入力してください`
    return false
  }
  titleError.value = null
  return true
}

const handleTitleInput = () => {
  if (titleError.value) {
    validateTitle(title.value)
  }
}

const handleTitleBlur = () => {
  if (title.value) {
    validateTitle(title.value)
  }
}

const handleSubmit = () => {
  if (!validateTitle(title.value)) return
  emit('submit', title.value.trim(), description.value.trim())
  title.value = ''
  description.value = ''
  titleError.value = null
}

const isSubmitDisabled = computed(() => {
  return props.disabled || !title.value.trim() || title.value.length > MAX_TITLE_LENGTH
})
</script>

<template>
  <form @submit.prevent="handleSubmit" class="space-y-4">
    <div>
      <label for="task-title" class="sr-only">タスク名</label>
      <input
        id="task-title"
        v-model="title"
        type="text"
        placeholder="タスク名を入力..."
        :disabled="disabled"
        :aria-invalid="!!titleError"
        :aria-describedby="titleError ? 'title-error' : undefined"
        @input="handleTitleInput"
        @blur="handleTitleBlur"
        :class="[
          'w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none disabled:bg-gray-100 disabled:cursor-not-allowed',
          titleError ? 'border-red-500' : 'border-gray-300'
        ]"
      />
      <div class="flex justify-between mt-1">
        <span v-if="titleError" id="title-error" class="text-sm text-red-500">
          {{ titleError }}
        </span>
        <span v-else />
        <span
          :class="[
            'text-sm',
            title.length > MAX_TITLE_LENGTH ? 'text-red-500' : 'text-gray-400'
          ]"
        >
          {{ title.length }}/{{ MAX_TITLE_LENGTH }}
        </span>
      </div>
    </div>

    <div>
      <label for="task-description" class="sr-only">説明</label>
      <textarea
        id="task-description"
        v-model="description"
        placeholder="説明（任意）"
        :disabled="disabled"
        rows="2"
        class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none resize-none disabled:bg-gray-100 disabled:cursor-not-allowed"
      />
      <div class="flex justify-end mt-1">
        <span
          :class="[
            'text-sm',
            description.length > MAX_DESCRIPTION_LENGTH ? 'text-red-500' : 'text-gray-400'
          ]"
        >
          {{ description.length }}/{{ MAX_DESCRIPTION_LENGTH }}
        </span>
      </div>
    </div>

    <button
      type="submit"
      :disabled="isSubmitDisabled"
      class="w-full px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors font-medium disabled:bg-blue-300 disabled:cursor-not-allowed"
    >
      {{ disabled ? '追加中...' : 'タスクを追加' }}
    </button>
  </form>
</template>
