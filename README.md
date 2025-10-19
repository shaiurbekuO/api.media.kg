Media.kg – Online Media Platform

Duration: Jan 2025 – May 2025
Tech Stack: Java Spring Boot, JavaScript, HTML, CSS, PostgreSQL, Flyway
Overview
Media.kg – онлайн медиа платформа. Колдонуучулар катталып, кире алышат, профильдерин башкарса болот, постторду түзүп, чыпкалап, файл жүктөй алышат. Коопсуздук SMS жана Email аркылуу OTP код менен камсыздалган.

Key Features
Authentication & Registration
User registration, login, password reset
OTP verification via SMS and email
Profile Management
Activate, update, and delete profiles
Change username, password, photo, and profile details
Filter profiles
Post Management
Create, update, delete posts
Get similar posts, filter posts (public & admin)
Get posts by profile or ID
File Management
Upload and open files
API Endpoints

Authentication
POST /api/auth/registration        → Register a new user
POST /api/auth/reg-smsVerification → Verify SMS code
POST /api/auth/reg-smsVerification-resend → Resend SMS code
GET  /api/auth/reg-emailVerification/{token} → Verify email
POST /api/auth/login               → User login
POST /api/auth/reset-password      → Request password reset
POST /api/auth/reset-password-confirm → Confirm password reset

Profile Management
PUT    /profile/{id}            → Activate profile
DELETE /profile/{id}            → Delete profile
PUT    /profile/username        → Request username change
PUT    /profile/username/confirm → Confirm username change
PUT    /profile/status/{id}     → Change profile status
PUT    /profile/photo           → Update profile picture
PUT    /profile/password        → Update password
PUT    /profile/detail          → Update profile details
POST   /profile/filter          → Filter profiles

Post Management
POST   /post                     → Create post
PUT    /post/{id}                 → Update post
DELETE /post/{id}                 → Delete post
POST   /post/public/similar       → Get similar posts (public)
POST   /post/public/filter        → Filter posts (public)
POST   /post/filter               → Filter posts (admin)
GET    /post/public/{id}          → Get post by ID (public)
GET    /post/profile              → Get all posts by profile

File Management
POST /attach/upload            → Upload file
GET  /attach/open/{fileName}   → Open file

Technical Details
Database: PostgreSQL + Flyway (migration, no need to recreate DB)
Logging: Console and file logging; logs persist after restart
Security: OTP verification via SMS/email
Frontend: JavaScript, HTML, CSS (responsive UI)
Backend: Java Spring Boot
Getting Started
Clone the repository:
git clone <repository_url>


Configure application.properties with PostgreSQL settings

Run project via IDE or command line

Access API endpoints via Postman or frontend
