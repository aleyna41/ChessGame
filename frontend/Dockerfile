#################
# Build the app #
#################
FROM node:latest as build

WORKDIR /app
COPY . .
RUN npm install
RUN npm install -g @angular/cli
RUN npm i @angular/forms
RUN ng add @angular/material
RUN npm install rxjs
RUN npm run build

################
# Run in NGINX #
################
FROM nginx:latest
COPY --from=build /dist/out /usr/share/nginx/html
COPY ./nginx-custom.conf /etc/nginx/conf.d/default.conf


EXPOSE 80

