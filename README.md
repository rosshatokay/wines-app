# WinesApp

### 1. Introduction

Project name: WinesApp
Course & Instructor: Introduction to Systems Programming, Dror Ben Ami
Developed by: Ross Hatokay

Description:
WinesApp is a Spring MVC-based web application for managing and filtering wine data. The system allows users to query wines based on a varierty of fields, and visualize them with a simple to use interface.

### 2. Installation & Setup

##### Prerequisites:

* Java 17+
* PostgreSQL

### 3. Features

✅ Filter wines by a variety of fields.
✅ Dynamic field selection in API responses
✅ Multi-threaded processing of queries
✅ Responsive web interface with HTML/CSS/JS

### 4. API Endpoints

| method | Endpoint | Description |
|--------|----------|-------------|
GET | /api/wines | Get all wines
GET | /api/wines?color= | Get wines filtered by color {red/white}
GET | /api/wines?startDate=&endDate= | Filter by range of added date
GET | /api/wines?maxPh= | Get wines with pH ≤ 
GET | /api/wines?color=white&fields=alcohol,quality | Select specific fields


Example API Response:

```json
[
  {
    "id": 1,
    "color": "Red",
    "dateAdded": "2023-05-12",
    "ph": 3.4
  },
  {
    "id": 2,
    "color": "White",
    "dateAdded": "2023-07-18",
    "ph": 3.1
  }
]
```

