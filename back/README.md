# Back installation

- Clone repo
- Import project in IDEA

## DATABASE
#### Install PostgreSQL:
https://www.postgresql.org/download/
- User: "postgres" (default) 
- Password: "password"

#### Start server and run:
```
$ psql -U postgres
$ create database agoradb;  // Creates agoradb database
$ \l                        // list dbs
```

We can now connect to the db in IDEA with the previous info.

#### POSTMAN
Install postman, used to test rest APIs
https://www.postman.com/downloads/

# API docs
- Run spring
- Go to: http://localhost:8080/swagger-ui.html

# StartUpDatabase
Edit Configurations > Enviroment > Enviroment Variables: -initdb=true > Apply

# Docs

#### Spring: https://www.baeldung.com/spring-boot
