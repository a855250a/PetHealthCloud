# 🐾 PetHealthCloud

PetHealthCloud 是我目前持續開發中的個人專案。

我希望它不是一個上課練習，而是一個可以真正持續開發、持續改善的產品，同時也是我轉職 Java Backend 工程師的重要作品集。

目前專案以 **Java Spring Boot** 開發，主要功能是讓使用者管理自己的寵物資料，並透過 JWT 做登入驗證與權限管理。

未來會持續加入更多功能，例如醫療紀錄、疫苗提醒、OCR、AI 分析以及手機 App。

---

# 為什麼做這個專案？

因為我自己很喜歡貓，也發現許多飼主會把寵物的健康資訊分散在不同地方，例如：

- 疫苗紀錄
- 體重變化
- 健康檢查報告
- 看診紀錄

因此我希望打造一個可以集中管理寵物健康資訊的平台。

目前先完成後端核心功能，之後會一步一步把它發展成真正可以使用的產品。

---

# 目前完成

✔ 使用者註冊

✔ 使用者登入

✔ BCrypt 密碼加密

✔ JWT Token 驗證

✔ 寵物 CRUD API

✔ 動態搜尋 API

✔ User 與 Pet 關聯

✔ 使用者只能管理自己的寵物

✔ Global Exception Handler

✔ Swagger API 文件

---

# 使用技術

### Backend

- Java 21
- Spring Boot
- Spring Data JPA
- Hibernate

### Database

- MySQL

### Security

- JWT
- BCrypt

### Tools

- Maven
- Swagger
- Git
- GitHub

---

# 專案架構

```
Controller
        │
        ▼
Service
        │
        ▼
Repository
        │
        ▼
MySQL
```

目前採用 Spring Boot 常見的三層式架構，讓程式更容易維護與擴充。

---

# API

### User

- POST /register
- POST /login

### Pet

- GET /pets
- GET /pets/{id}
- POST /pets
- PUT /pets/{id}
- DELETE /pets/{id}
- GET /pets/search

---

# 目前產品版本

## v0.1.2

已完成：

- CRUD API
- MySQL
- Spring Data JPA
- Validation
- Swagger
- JWT Authentication
- Authorization
- Global Exception Handler

---

# 接下來的規劃

接下來預計依序完成：

- 簡單 Web Demo（方便展示）
- Medical Record
- Vaccination Record
- 圖片上傳
- OCR 醫療報告辨識
- AI 健康分析
- Android / iOS App

---

# 學習目標

這個專案除了作品集之外，也是我學習軟體工程的重要紀錄。

希望透過持續開發，逐步學習：

- RESTful API 設計
- Spring Boot
- 資料庫設計
- 軟體架構
- Git 版本控制
- Docker
- AWS 雲端部署

---

# 作者

**啟華 蔡**

Backend Developer Portfolio
