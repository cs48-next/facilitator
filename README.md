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
java -jar target\facilitator-0.0.1-SNAPSHOT.jar
```
# API
## Venue
### Venue creation
Create a venue.

### Endpoint
`POST /v1/venue`
### Request Body
| Name | Type | Description | Required |
| --- | --- | --- | --- |
| `latitude` | Double | User location latitude | Yes |
| `longitude` | Double | User location longitude | Yes |

### Example
```bash
curl -v -XPOST locahost:8080/v1/venue -H'Content-Type: application/json' -d '
{
	"latitude" : 0,
	"longitude" : 0
}
'
```
	
# License
[Apache License 2.0](LICENSE)
