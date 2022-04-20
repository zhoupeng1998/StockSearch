# syntax=docker/dockerfile:1

FROM node:lts

ENV NODE_ENV production
WORKDIR /app

COPY . .
RUN npm ci

EXPOSE 5000

CMD ["node", "index.js"]