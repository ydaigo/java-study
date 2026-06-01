/**
 * タスク作成フォームコンポーネント
 * 新しいタスクを入力して追加するためのフォーム
 */
import { useState } from "react";

const MAX_TITLE_LENGTH = 255;
const MAX_DESCRIPTION_LENGTH = 1000;

interface TaskFormProps {
  onSubmit: (title: string, description: string) => void;
  disabled?: boolean;
}

export function TaskForm({ onSubmit, disabled = false }: TaskFormProps) {
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [titleError, setTitleError] = useState<string | null>(null);

  const validateTitle = (value: string): boolean => {
    if (!value.trim()) {
      setTitleError("タスク名は必須です");
      return false;
    }
    if (value.length > MAX_TITLE_LENGTH) {
      setTitleError(`タスク名は${MAX_TITLE_LENGTH}文字以内で入力してください`);
      return false;
    }
    setTitleError(null);
    return true;
  };

  const handleTitleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    setTitle(value);
    if (titleError) {
      validateTitle(value);
    }
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!validateTitle(title)) return;
    onSubmit(title.trim(), description.trim());
    setTitle("");
    setDescription("");
    setTitleError(null);
  };

  const isSubmitDisabled =
    disabled || !title.trim() || title.length > MAX_TITLE_LENGTH;

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      <div>
        <label htmlFor="task-title" className="sr-only">
          タスク名
        </label>
        <input
          id="task-title"
          type="text"
          value={title}
          onChange={handleTitleChange}
          onBlur={() => title && validateTitle(title)}
          placeholder="タスク名を入力..."
          disabled={disabled}
          aria-invalid={!!titleError}
          aria-describedby={titleError ? "title-error" : undefined}
          className={`w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none disabled:bg-gray-100 disabled:cursor-not-allowed ${
            titleError ? "border-red-500" : "border-gray-300"
          }`}
        />
        <div className="flex justify-between mt-1">
          {titleError ? (
            <span id="title-error" className="text-sm text-red-500">
              {titleError}
            </span>
          ) : (
            <span />
          )}
          <span
            className={`text-sm ${
              title.length > MAX_TITLE_LENGTH ? "text-red-500" : "text-gray-400"
            }`}
          >
            {title.length}/{MAX_TITLE_LENGTH}
          </span>
        </div>
      </div>

      <div>
        <label htmlFor="task-description" className="sr-only">
          説明
        </label>
        <textarea
          id="task-description"
          value={description}
          onChange={(e) => setDescription(e.target.value)}
          placeholder="説明（任意）"
          disabled={disabled}
          rows={2}
          className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none resize-none disabled:bg-gray-100 disabled:cursor-not-allowed"
        />
        <div className="flex justify-end mt-1">
          <span
            className={`text-sm ${
              description.length > MAX_DESCRIPTION_LENGTH
                ? "text-red-500"
                : "text-gray-400"
            }`}
          >
            {description.length}/{MAX_DESCRIPTION_LENGTH}
          </span>
        </div>
      </div>

      <button
        type="submit"
        disabled={isSubmitDisabled}
        className="w-full px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors font-medium disabled:bg-blue-300 disabled:cursor-not-allowed"
      >
        {disabled ? "追加中..." : "タスクを追加"}
      </button>
    </form>
  );
}
