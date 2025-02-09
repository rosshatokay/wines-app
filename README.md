# WinesApp

### 1. Introduction

**Project name**: WinesApp
**Course & Instructor**: Introduction to Systems Programming, Dror Ben Ami
**Developed by**: Ross Hatokay

WinesApp is a Spring MVC-based web application for managing and filtering wine data. The system allows users to query wines based on a varierty of fields, and visualize them with a simple to use interface.

### 2. Installation & Setup

##### Prerequisites:

* Java 17+
* PostgreSQL

##### Installation Steps:

1. Run the WinesAppInstaller
2. Set your desired installation folder path. 
3. After the installation is complete, enter your PostgreSQL credentials (host 99% of the time can be left blank, port can also be left to default, db username, db password)
4. Wait for Spring to boot
5. The browser will open once all steps are completed (if not, the app should be live at http://localhost:8080)

 

### 3. Features

✅ Filter wines by a variety of fields.

✅ Dynamic field selection in API responses.

✅ Multi-threaded processing of queries.

✅ Responsive web interface with HTML/CSS/JS.

> [!CAUTION]
> When clicking on "All", "Red", "White" boxes in the landing page, they may take some time to load (depending on machine) as rendering the entire db in a tabular form takes a while. 

#### Screenshots

1. Landing page: ![Home page](https://img001.prntscr.com/file/img001/wRBEHlPMRBObDTQM_nN58Q.png)
2. Wines: ![Wines Screen](https://img001.prntscr.com/file/img001/Pzk_z6L7T52r6-s3PbbWVQ.png)
3. Filtering: ![Filtering](https://img001.prntscr.com/file/img001/_RvZqyhvRqeoD40Z48jmSg.png)
4. Multi-threading: ![Multi-Threading](https://img001.prntscr.com/file/img001/SyYurbLJR5iqYny6Kn85Qg.png)


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

### 5. Dependencies

The following technologies were used in the UI stack:

* [Thymeleaf](https://www.thymeleaf.org/) - templating engine
* [DataTables](https://datatables.net/) - For rendering tables
* [jQuery](https://jquery.com/) - The ol' reliable javascript library
* Straightforward HTML/CSS/JS