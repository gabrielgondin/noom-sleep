# Sleep Logs API Documentation

## RUN ##
To run the app is needed to clone the project and run the docker compose

git clone https://github.com/gabrielgondin/noom-sleep.git
docker compose up -d

## Endpoints

This API provides endpoints to manage and retrieve sleep logs for users.

### 1. Create the sleep log for the last night

`POST /api/v1/sleeps`

Creates a new sleep log entry for a user. Mood could be GOOD, OK, BAD

Example request:
{
    "userId": 1,
    "day": "2025-05-12",
    "startAt": "2025-05-13T03:00:00",
    "endAt": "2025-05-13T06:25:00",
    "mood": "OK"
}

### 2.  Fetch information about the last night's sleep

`GET /api/v1/sleeps/{userId}/latest`

Retrieves the most recent sleep log for a specific user.

Example response:
{
    "day": "2025-05-12",
    "startAt": "03:00 am",
    "endAt": "06:25 am",
    "totalSleep": "3 h 25 min",
    "mood": "OK"
}

### 3. Get the last 30-day averages

`GET /api/v1/sleeps/{userId}/averages`

Retrieves the average sleep statistics for a user over the last 30 days.

Example response:
{
    "startAt": "2025-04-20",
    "endAt": "2025-05-20",
    "averageStartTime": "03:00",
    "averageEndTime": "06:25",
    "averageSleepDuration": "0 h 6 min",
    "moodCount": {
        "GOOD": 0,
        "BAD": 0,
        "OK": 1
    }
}

### DEFAULT VALUES ###
The database's initialized with 3 users that can be used to save and retrieve sleep log
data.
Ids: 1, 2 and 3

