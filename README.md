# Othello Next

[![Build Status](https://travis-ci.org/jzProg/othello-next.svg?branch=main)](https://travis-ci.org/jzProg/othello-next)
[![Heroku](https://heroku-badge.herokuapp.com/?app=othello-next)]()
[![Demo](https://img.shields.io/badge/demo-online-green.svg)](https://othello-next.herokuapp.com/)

Re-implementation of my older [Othello](https://github.com/jzProg/othello-game) project, using web technologies.

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
