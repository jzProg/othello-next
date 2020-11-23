# Othello Next

[![Build Status](https://travis-ci.org/jzProg/othello-next.svg?branch=main)](https://travis-ci.org/jzProg/othello-next)
[![Demo](https://img.shields.io/badge/demo-online-green.svg)](https://othello-next.herokuapp.com/)

Upgrade of my older [Othello](https://github.com/jzProg/othello-game) project, using web technologies. I re-implemented the backend logic to follow the State Design Pattern and added new features (e.g available moves for player, difficulty choice).

## Technologies and Tools

<img src="https://img.shields.io/badge/vuejs%20-%2335495e.svg?&style=for-the-badge&logo=vue.js&logoColor=%234FC08D"/><img src="https://img.shields.io/badge/spring%20-%236DB33F.svg?&style=for-the-badge&logo=spring&logoColor=white"/>

- **Vue CLI** (v4.5.7) / **Vue** (v3.0.0) for frontend. 
- **Spring Boot** (v2.3.4), for backend.

## Setup Instructions

**server:**
- Build with `mvn clean install`.
- cd to `/server` folder.
- Run with `java -jar target/server-0.0.1-SNAPSHOT.jar`.

**client:**
- cd to `/web` folder.
- Run `npm install`.
- Build with `npm run build` command.
- For *dev server*, run `npm run serve`.
