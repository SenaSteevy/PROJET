FROM node:16-alpine AS builder

WORKDIR /app

COPY package.json ./

RUN npm install

COPY . .

RUN ng build --prod --output-path=dist

FROM nginx:alpine

COPY --from=builder /app/dist/ /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
