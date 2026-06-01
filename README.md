[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
# LibMan

<!-- TABLE OF CONTENTS -->
1. [Introduction](#Introduction)
2. [Features](#Features)
3. [Challenges](#Challenges)

<!-- <details>
<summary>Table of Contents</summary>
<ol>
    <li>
        <a href="#Introduction">Introduction</a>
        <ul>
            <li><a href="#Team-Members">Team Members</a></li>
            <li><a href="#How-to-Run">How to Run</a></li>
        </ul>
    </li>
    <li><a href="#Features">Features</a></li>
    <li><a href="#Challenges">Challenges</a></li>
</ol>
</details> -->

<!-- ABOUT THE PROJECT -->
## Introduction <a name="Introduction"></a>
<div style = "text-align: justify">
LibMan is a library management system that helps librarians manage books, members, and borrowing/returning books. 
The system is built using Java SpringBoot for the backend and React for frontend.
</div>

### Team Members
| Order |          Name           | Student ID  |
|:-----:|:-----------------------:|:-----------:|
|   1   |    Tran Nguyen Phuc     | ITCSIU21097 |
|   2   |  Nguyen Mach Khang Huy  | ITCSIU21072 |
|   3   |      Bui Cong Vinh      | ITCSIU22165 |
|   4   | Nguyen Bach Dong Phuong | ITCSIU22118 |
|   5   |  Nguyen Thi Quynh Nga   | ITCSIU22094 |
|   6   |       Le Minh Duy       | ITCSIU22037 |
|   7   |    Nguyen Minh Viet     | ITDSIU21130 |
|   8   |      Tran Quoc Bao      | ITITWE20033 |

### How to Run

#### Option 1: Docker (Recommended)

Only requires [Docker](https://docs.docker.com/get-docker/) installed.

1. Clone the repo
   ```sh
   git clone https://github.com/tnphucccc/lib-man.git
   cd lib-man
   ```
2. Start the full stack
   ```sh
   docker compose up --build
   ```
3. Open your browser and go to `http://localhost:5173/`
   - API: `http://localhost:8080/api/v1`
   - Swagger: `http://localhost:8080/swagger-ui.html`
   - Credentials: `librarian` / `CHANGE_ME`

To stop and reset data:
```sh
docker compose down -v
```

#### Option 2: Manual Setup

Requires Java 17+, Maven, Node.js 20+, and PostgreSQL 16+.

1. Clone the repo
   ```sh
   git clone https://github.com/tnphucccc/lib-man.git
   ```
2. Set up the database
   ```sh
   createdb libman
   psql -d libman -f backend/src/main/resources/schema.sql
   psql -d libman -f backend/src/main/resources/query.sql  # optional seed data
   ```
3. Configure the backend
   ```sh
   cp backend/src/main/resources/application.properties.example backend/src/main/resources/application.properties
   # Edit application.properties with your database credentials
   ```
4. Run the backend
   ```sh
   cd backend
   ./mvnw spring-boot:run
   ```
5. Configure and run the frontend
   ```sh
   cd frontend
   cp .env.example .env
   npm install
   npm run start
   ```
6. Open your browser and go to `http://localhost:5173/`

## Features <a name="Features"></a>
- ✅ Add, update, delete books
- ✅ Add, update, delete authors
- ✅ Add, update, delete members
- ✅ Borrow, return books
- ✅ View borrowing history
- ✅ Search books

## Challenges <a name="Challenges"></a>

<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[contributors-shield]: https://img.shields.io/github/contributors/tnphucccc/lib-man.svg?style=for-the-badge
[contributors-url]: https://github.com/tnphucccc/lib-man/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/tnphucccc/lib-man.svg?style=for-the-badge
[forks-url]: https://github.com/tnphucccc/lib-man/network/members
[stars-shield]: https://img.shields.io/github/stars/tnphucccc/lib-man.svg?style=for-the-badge
[stars-url]: https://github.com/tnphucccc/lib-man/stargazers
[issues-shield]: https://img.shields.io/github/issues/tnphucccc/lib-man.svg?style=for-the-badge
[issues-url]: https://github.com/tnphucccc/lib-man/issues