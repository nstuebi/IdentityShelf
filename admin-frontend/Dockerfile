# Build React admin app
FROM node:20-alpine AS build
WORKDIR /app
COPY package.json package.json
COPY package-lock.json package-lock.json
RUN npm ci || npm install --no-audit --no-fund
COPY . .
RUN npm run build

# Serve with Nginx and proxy /api to the Spring Boot app
FROM nginx:alpine AS runtime
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist /usr/share/nginx/html
EXPOSE 80


