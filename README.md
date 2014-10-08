
## Introduction

Based on The Eventual PlayFramework-AngularJS-Bootstrap-MongoDB Seed Project by **[Sari Haj Hussein](http://sarihh.info)**.


## Features
The seed has the following features (to recall a few):

1. It offers a complete [single-page application](http://en.wikipedia.org/wiki/Single-page_application) experience.

2. It uses the asynchronous and non-blocking [ReactiveMongo driver](http://reactivemongo.org/).

3. It supports the new HTML5 routing and histoty API (i.e., no hashbangs, no bullshit).

4. It packs JavaScript libraries inside the solution (i.e., no WebJars, no download time, and again no bullshit).

5. It makes minimal usage of [Play Scala templates](http://www.playframework.com/documentation/2.1.5/ScalaTemplates), thereby clearing the space for AngularJS directives in your HTML.

6. It cleanly separates between Play routes that serve HTML and those that serve JSON.

7. It cleanly separates and optimally maps AngularJS routes to Play routes.

8. It conceals Play routes from end-users, thereby ensuring that all pages are properly styled before they are presented.

## Ingredients
The seed uses the following software components:

1. Play Framework 2.2.3

2. MongoDB 2.6.3

3. AngularJS 1.2.19

4. Bootstrap 3.2.0

5. jQuery 1.11.1 (used by 4)

## Deployment
Follow these steps in order to deploy the seed on your machine:

1. Download and extract Play Framework 2.2.3.

2. Download and extract MongoDB 2.6.3.

3. Clone the project: <code>git clone https://github.com/angyjoe/eventual.git</code>.

4. Start <code>mongod</code> (the daemon process for the MongoDB system).

5. Create and populate the celebrities’ database using the script <code>/eventual/db/script.js</code>.

6. Go to the root of the project: <code>cd eventual</code>.

7. Launch Play at the default 9000 port: <code>/PATH-TO-PLAY/play run</code>.

8. Enjoy the seed: [http://localhost:9000](http://localhost:9000) or the following screenshots!

## Screenshots

