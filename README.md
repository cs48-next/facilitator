# facilitator
Facilitates backend services
## Requirements
- Java 8
- Apache Maven >= 3
## Build
### From a terminal
```
mvn clean install
```
### From an IDE
Import the projects as existing Apache Maven projects
## Setup
We can run facilitator service directly from Maven:
```
mvn spring-boot:run 
```

We can also run from the generated executable JAR file:
```
java -jar target/facilitator-0.0.1-SNAPSHOT.jar
```
# API
The API can be accessed at `https://api.song.buzz`
## Venue
### Create Venue
Create a venue.

### Endpoint
`POST /venue`
### Request Body
| Name | Type | Description | Required |
| --- | --- | --- | --- |
| `name` | String | Name of Venue | Yes |
| `latitude` | Double | User location latitude | Yes |
| `longitude` | Double | User location longitude | Yes |

### Example
```bash
curl -v -XPOST localhost:8080/venue -H'Content-Type: application/json' -d '
{
	"name": "my venue",
	"latitude" : 0,
	"longitude" : 0
}
'
```
### Reponse
```json
{  
   "id":"89300432-97c2-4a2a-9787-795a96e5ad60",
   "name":"my venue",
   "playlist":[],
   "latitude":0.0,
   "longitude":0.0
}
```

### List Venues
Create a venue.

### Endpoint
`GET /venue`
### Request Parameters
| Name | Type | Description | Required |
| --- | --- | --- | --- |
| `latitude` | Double | User location latitude | Yes |
| `longitude` | Double | User location longitude | Yes |

### Example
```bash
curl -v localhost:8080/venue?latitude=12&longitude=13
```
### Response
Returns `distance` in miles.
```json
{  
   "venues":[  
      {  
         "venue_id":"89300432-97c2-4a2a-9787-795a96e5ad60",
         "venue_name":"my venue",
         "distance":1217.4615442168708
      }
   ]
}
```

### Fetch Venue
Create a venue.

### Endpoint
`GET /venue/:venueId`
### Request Parameters
| Name | Type | Description | Required |
| --- | --- | --- | --- |
| `latitude` | Double | User location latitude | Yes |
| `longitude` | Double | User location longitude | Yes |

### Example
```bash
curl -v localhost:8080/venue/89300432-97c2-4a2a-9787-795a96e5ad60
```
### Reponse
```json
{  
   "id":"89300432-97c2-4a2a-9787-795a96e5ad60",
   "name":"my venue",
   "playlist":[],
   "latitude":0.0,
   "longitude":0.0
}
```
## Track
### Propose Track
### Endpoint
`PUT /track/:venueId/:trackId`
### Request Parameters
| Name | Type | Description | Required |
| --- | --- | --- | --- |
| `venue_id` | String | Venue to propose track to | Yes |
| `track_id` | String | Track ID to be proposed | Yes |
### Example 
```bash
curl -v -XPUT localhost:8080/track/89300432-97c2-4a2a-9787-795a96e5ad60/Track_123
```
### Response
```json
{  
   "venue_id":"89300432-97c2-4a2a-9787-795a96e5ad60",
   "track_id":"Track_123",
   "votes":[]
}
```
## Vote
### Upvote track
### Endpoint
`PUT /vote/:venueId/:trackId/upvote`
### Request Parameters
| Name | Type | Description | Required |
| --- | --- | --- | --- |
| `venue_id` | String | Venue that track exists on | Yes |
| `track_id` | String | Track ID to be voted on | Yes |
### Example
```bash
curl -v -XPUT localhost:8080/vote/89300432-97c2-4a2a-9787-795a96e5ad60/Track_123/upvote
```
### Response
```json
{  
   "venue_id":"89300432-97c2-4a2a-9787-795a96e5ad60",
   "track_id":"Track_123",
   "user_id":"f538b1b1-8713-4214-85ad-839ea4a3988b",
   "upvote":true
}
```
### Downvote track
### Endpoint
`PUT /vote/:venueId/:trackId/downvote`
### Request Parameters
| Name | Type | Description | Required |
| --- | --- | --- | --- |
| `venue_id` | String | Venue that track exists on | Yes |
| `track_id` | String | Track ID to be voted on | Yes |
### Example
```bash
curl -v -XPUT localhost:8080/vote/89300432-97c2-4a2a-9787-795a96e5ad60/Track_123/downvote
```
### Response
```json
{  
   "venue_id":"89300432-97c2-4a2a-9787-795a96e5ad60",
   "track_id":"Track_123",
   "user_id":"f538b1b1-8713-4214-85ad-839ea4a3988c",
   "upvote":false
}
```

### Delete track vote
### Endpoint
`DELETE /vote/:venueId/:trackId`
### Request Parameters
| Name | Type | Description | Required |
| --- | --- | --- | --- |
| `venue_id` | String | Venue that track exists on | Yes |
| `track_id` | String | Track to delete vote from | Yes |
### Example
```bash
curl -v -XDELETE localhost:8080/vote/89300432-97c2-4a2a-9787-795a96e5ad60/Track_123
```
### Response
`OK`
	
# License
[Apache License 2.0](LICENSE)
