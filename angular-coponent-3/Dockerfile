### STAGE 1: Build ###
FROM node:12.7-alpine AS build
WORKDIR /usr/src/app
COPY package.json package-lock.json ./
RUN npm install
RUN npm install --only=dev
COPY . .
RUN npm run build
### STAGE 2: Run ###
FROM nginx:1.17.1-alpine
COPY nginx.conf /etc/nginx/nginx.conf
COPY --from=build /usr/src/app/dist/tweet-application /usr/share/nginx/html

# FROM nginx:alpine
# COPY /dist/tweet-application /usr/share/nginx/html
# COPY nginx.conf /etc/nginx/nginx.conf

