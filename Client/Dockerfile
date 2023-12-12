FROM node:18.13

WORKDIR /app

COPY package*.json ./

RUN npm install -g @angular/cli

RUN npm install --force 

COPY . .

RUN ng build --project Client --configuration production

EXPOSE 4200

CMD ["npm", "start"]