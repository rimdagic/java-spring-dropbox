# Java Spring File API

## Description
This project was made during a course in Java Spring. 
It is a basic file storage API where users can register, 
create folders and store files.

There are ten different endpoints with varying levels of security. 
They can be used to create, read, register and delete accounts, but also login, create folders and upload and delete files,
users can also see a list of their folders or the content of a specific folder.

## Endpoints

### Admin level 
    
#### - Get a list of all accounts
    Method: GET
    Path: "/account/all"

    Required header: 
        KEY: Authorization
        VALUE: Admin account JWT string

#### - Delete account
    Method: DELETE
    Path: "/account/{username}"

    Required header: 
        KEY: Authorization
        VALUE: Admin account JWT string

### Open endpoints

#### - Register account
    Method: POST
    Path: "/account/register"

    Content-Type: application/json
    Body (example):
    {
        "username": "user",
        "password": "password"
    }

#### - Login
    Method: POST
    Path: "/account/login"

    Content-Type: application/json
    Body (example):
    {
        "username": "user",
        "password": "password"
    }

### - Authenticated endpoints

#### - Create folder
    Method: POST
    Path: "/folder/{foldername}"

    Required header:
        KEY: Authorization 
        VALUE: A valid JWT string

#### - Upload file
    Method: POST
    Path: "/file/{foldername}"

    Required header:
        KEY: Authorization 
        VALUE: A valid JWT string

        KEY: Content-Type
        VALUE: multipart/form-data

    Body:
        KEY: file
        VALUE: The file you want to upload

#### - Download file
    Method: GET
    Path: "/file/{foldername}/{filename}"

    Required header:
        KEY: Authorization 
        VALUE: A valid JWT string

#### - See a list of all of your folders
    Method: GET
    Path: "/folder/all"

    Required header:
        KEY: Authorization 
        VALUE: A valid JWT string

#### - See content of a specific folder
    Method: GET
    Path: "/folder/{foldername}"
    
    Required header:
        KEY: Authorization 
        VALUE: A valid JWT string

#### - Delete file
    Method: DELETE
    Path "/file/{foldername}/{filename}"

    Required header:
        KEY: Authorization 
        VALUE: A valid JWT string
