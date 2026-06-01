# Task Management App

Spring Boot + React + PostgreSQL によるタスク管理アプリケーションです。

## プロジェクト構成

```
java-study/
├── backend/          # Spring Boot API (Java)
│   ├── pom.xml
│   ├── mvnw.cmd
│   └── src/
│
├── frontend/         # React App (TypeScript)
│   ├── package.json
│   └── src/
│
└── README.md
```

## 必要環境

- Java 17以上（推奨: Java 21）
- Node.js 18以上
- PostgreSQL 17

## 起動方法

### 1. PostgreSQL 準備

```sql
CREATE DATABASE taskdb;
```

### 2. Backend 起動

```powershell
cd backend
.\mvnw.cmd spring-boot:run
```

- API: http://localhost:8080/api/tasks
- Swagger UI: http://localhost:8080/swagger-ui.html

### 3. Frontend 起動

```powershell
cd frontend
npm install
npm run dev
```

- React UI: http://localhost:5173

## APIエンドポイント

| メソッド | URL | 説明 |
|---------|-----|------|
| GET | `/api/tasks` | 全タスク取得 |
| GET | `/api/tasks/{id}` | ID指定で取得 |
| GET | `/api/tasks/status?completed=true` | 完了状態で絞り込み |
| GET | `/api/tasks/search?keyword=xxx` | タイトル検索 |
| POST | `/api/tasks` | タスク作成 |
| PUT | `/api/tasks/{id}` | タスク更新 |
| DELETE | `/api/tasks/{id}` | タスク削除 |

## PostgreSQL 接続情報

| 項目 | 値 |
|------|-----|
| ホスト | localhost |
| ポート | 5433 |
| データベース名 | taskdb |
| ユーザー | postgres |
| パスワード | postgres |

## Backend アーキテクチャ（クリーンアーキテクチャ）

```
backend/src/main/java/com/example/demo/
├── domain/           # ドメイン層（ビジネスロジック）
├── application/      # アプリケーション層（ユースケース）
├── infrastructure/   # インフラ層（DB実装）
├── presentation/     # プレゼンテーション層（REST API）
└── config/           # 設定（CORS等）
```

## Frontend 構成

```
frontend/src/
├── api/              # APIクライアント
├── components/       # Reactコンポーネント
│   ├── TaskList.tsx
│   ├── TaskForm.tsx
│   └── TaskItem.tsx
└── App.tsx           # メインコンポーネント
```

## 技術スタック

| 層 | 技術 |
|---|------|
| Frontend | React 18 + TypeScript + Vite + Tailwind CSS |
| API Docs | springdoc-openapi (Swagger UI) |
| Backend | Spring Boot 3.5 + Spring Data JPA |
| Database | PostgreSQL 17 |
