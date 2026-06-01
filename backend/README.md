# Backend - Task Management API

Spring Boot 3.5 + Spring Data JPA によるタスク管理APIです。
DDD（ドメイン駆動設計）のクリーンアーキテクチャで実装しています。

## 必要環境

- Java 17以上（推奨: Java 21）
- PostgreSQL 17
- Maven 3.9以上（Maven Wrapper同梱）

## 起動方法

```powershell
.\mvnw.cmd spring-boot:run
```

## エンドポイント

| URL | 説明 |
|-----|------|
| http://localhost:8080/api/tasks | REST API |
| http://localhost:8080/swagger-ui.html | Swagger UI（APIドキュメント） |
| http://localhost:8080/actuator/health | ヘルスチェック |

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

設定ファイル: `src/main/resources/application.properties`

## プロジェクト構造（クリーンアーキテクチャ）

```
src/main/java/com/example/demo/
├── DemoApplication.java          # エントリーポイント
│
├── domain/                       # ドメイン層（ビジネスロジック）
│   ├── model/
│   │   ├── Task.java            # エンティティ
│   │   └── TaskId.java          # 値オブジェクト
│   └── repository/
│       └── TaskRepository.java  # リポジトリインターフェース
│
├── application/                  # アプリケーション層（ユースケース）
│   ├── dto/
│   │   └── TaskDto.java         # データ転送オブジェクト
│   └── usecase/
│       ├── CreateTaskUseCase.java
│       ├── GetTaskUseCase.java
│       ├── UpdateTaskUseCase.java
│       └── DeleteTaskUseCase.java
│
├── infrastructure/               # インフラ層（技術的詳細）
│   ├── entity/
│   │   └── TaskJpaEntity.java   # JPAエンティティ
│   └── repository/
│       ├── TaskJpaRepository.java    # Spring Data JPA
│       └── TaskRepositoryImpl.java   # リポジトリ実装
│
├── presentation/                 # プレゼンテーション層（API）
│   ├── controller/
│   │   ├── TaskController.java       # RESTコントローラー
│   │   └── GlobalExceptionHandler.java
│   ├── request/
│   │   ├── CreateTaskRequest.java
│   │   └── UpdateTaskRequest.java
│   └── response/
│       └── TaskResponse.java
│
└── config/                       # 設定
    └── WebConfig.java           # CORS設定
```

## 依存関係の方向

```
Presentation → Application → Domain ← Infrastructure
```

- **Domain層**: ビジネスロジック（他の層に依存しない）
- **Application層**: ユースケース（Domain層のみに依存）
- **Infrastructure層**: 技術的詳細（Domain層のリポジトリIFを実装）
- **Presentation層**: API（Application層のUseCaseを利用）

## 主要な依存ライブラリ

| ライブラリ | 用途 |
|-----------|------|
| spring-boot-starter-web | REST API |
| spring-boot-starter-data-jpa | データベースアクセス |
| spring-boot-starter-validation | バリデーション |
| spring-boot-starter-actuator | ヘルスチェック等 |
| springdoc-openapi | Swagger UI / OpenAPI |
| postgresql | PostgreSQLドライバー |

## ビルド・テスト

```powershell
# ビルド
.\mvnw.cmd compile

# テスト実行
.\mvnw.cmd test

# パッケージ作成
.\mvnw.cmd package

# クリーンビルド
.\mvnw.cmd clean compile
```

## VS Code での開発

Spring Boot Dashboard から起動：
1. 左サイドバーの葉っぱアイコン🍃をクリック
2. APPS → demo を右クリック → Run または Debug
