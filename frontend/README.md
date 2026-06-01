# Frontend - タスク管理アプリ

React 18 + TypeScript + Vite + Tailwind CSS によるタスク管理フロントエンドです。

## 必要環境

- Node.js 18以上
- npm 9以上

## 起動方法

```powershell
# 依存関係のインストール（初回のみ）
npm install

# 開発サーバー起動
npm run dev
```

開発サーバー: http://localhost:5173

## スクリプト一覧

| コマンド | 説明 |
|---------|------|
| `npm run dev` | 開発サーバー起動 |
| `npm run build` | 本番ビルド |
| `npm run preview` | ビルド結果のプレビュー |
| `npm run lint` | ESLintでコードチェック |

## プロジェクト構造

```
frontend/
├── public/                 # 静的ファイル
│   ├── favicon.svg
│   └── icons.svg
│
├── src/
│   ├── api/               # APIクライアント
│   │   └── taskApi.ts     # タスクAPI通信処理
│   │
│   ├── components/        # Reactコンポーネント
│   │   ├── TaskList.tsx   # タスク一覧
│   │   ├── TaskForm.tsx   # タスク作成フォーム
│   │   └── TaskItem.tsx   # 個別タスク表示
│   │
│   ├── App.tsx            # メインコンポーネント
│   ├── main.tsx           # エントリーポイント
│   └── index.css          # グローバルスタイル（Tailwind）
│
├── index.html             # HTMLテンプレート
├── package.json           # 依存関係
├── vite.config.ts         # Vite設定
├── tsconfig.json          # TypeScript設定
└── eslint.config.js       # ESLint設定
```

## コンポーネント説明

### App.tsx
メインコンポーネント。アプリケーション全体の状態管理を担当。
- タスク一覧の取得・表示
- タスクの作成・更新・削除
- エラーハンドリング

### TaskForm.tsx
タスク作成フォーム。
- タスク名（必須）と説明（任意）を入力
- 送信後、フォームをクリア

### TaskList.tsx
タスク一覧表示。
- タスクがない場合は「タスクがありません」と表示
- TaskItemコンポーネントを並べて表示

### TaskItem.tsx
個別タスクの表示。
- チェックボックスで完了/未完了を切り替え
- 完了したタスクは打ち消し線で表示
- 削除ボタン

## API通信

`src/api/taskApi.ts` でバックエンドAPIと通信します。

### エンドポイント

| 関数 | HTTPメソッド | URL | 説明 |
|-----|-------------|-----|------|
| `getAll()` | GET | `/api/tasks` | 全タスク取得 |
| `getById(id)` | GET | `/api/tasks/{id}` | ID指定で取得 |
| `create(data)` | POST | `/api/tasks` | タスク作成 |
| `update(id, data)` | PUT | `/api/tasks/{id}` | タスク更新 |
| `delete(id)` | DELETE | `/api/tasks/{id}` | タスク削除 |

### 型定義

```typescript
interface Task {
  id: number;
  title: string;
  description: string | null;
  completed: boolean;
  createdAt: string;
}
```

## 技術スタック

| 技術 | 用途 |
|-----|------|
| React 18 | UIライブラリ |
| TypeScript | 型安全な開発 |
| Vite | ビルドツール・開発サーバー |
| Tailwind CSS | ユーティリティファーストCSS |
| ESLint | コード品質チェック |

## バックエンドとの連携

バックエンドAPIは http://localhost:8080 で動作している必要があります。

CORS設定により、http://localhost:5173 からのアクセスが許可されています。

### 起動順序

1. バックエンド起動（http://localhost:8080）
2. フロントエンド起動（http://localhost:5173）

## VS Code での開発

### 開発サーバー起動

1. `Ctrl + Shift + P` → 「Tasks: Run Task」
2. 「Frontend: 開発サーバー起動」を選択

### デバッグ

1. 開発サーバーを起動
2. F5 → 「Frontend: Chrome でデバッグ」を選択
3. Chromeが起動し、ブレークポイントが使用可能

## ビルド

```powershell
npm run build
```

ビルド結果は `dist/` ディレクトリに出力されます。
