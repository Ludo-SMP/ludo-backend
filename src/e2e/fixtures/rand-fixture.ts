import { faker } from "@faker-js/faker";

export function randInt(min: number = 0, max: number = 100) {
  return Math.floor(Math.random() * (max - min) + 1) + min;
}

export function randNickname(len: number = 20) {
  return faker.internet.displayName().slice(0, len);
}

export function randEmail(len: number = 20) {
  return `${randNickname(len)}@${_randEmailAddr()}`;
}

export function randPassword(len: number = 20) {
  return faker.internet.password({
    memorable: false,
    length: len,
  });
}

export function randUuid() {}

function _randEmailAddr() {
  const emails = faker.definitions.internet.example_email;
  return emails[randInt() % emails.length];
}
