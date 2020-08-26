FROM node:latest as node
WORKDIR /frontend
COPY package.json /frontend/package.json
RUN npm install
COPY . /frontend
RUN npm run build

# Stage 2
FROM nginx:1.15.12-alpine
COPY --from=node /frontend/dist usr/share/nginx/html
COPY ./nginx.conf /etc/nginx/conf.d/default.conf

