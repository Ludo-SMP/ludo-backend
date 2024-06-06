import { faker } from "@faker-js/faker";
import { v4 as uuid } from "uuid";

export function randStr(len: number): string;
export function randStr(minLen: number, maxLen: number): string;

export function randStr(lenOrMinLen: number, maxLen?: number): string {
  if (typeof maxLen === "undefined") {
    return _fixedLenRandStr(lenOrMinLen);
  } else {
    return _dynamicLenRandStr(lenOrMinLen, maxLen);
  }
}

export function _fixedLenRandStr(len: number = 20) {
  return _dynamicLenRandStr(len, len);
}

export function _dynamicLenRandStr(minLen: number = 10, maxLen: number = 20) {
  return faker.string.alpha({
    length: {
      min: minLen,
      max: maxLen,
    },
  });
}

export function randInt(min: number = 0, max: number = 100) {
  return Math.floor(Math.random() * (max - min + 1)) + min;
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

export function randText(min: number = 10, max: number = 20) {
  return faker.word.sample({ length: { min, max } });
}

export function randParagraph(len: number = 10) {
  return faker.lorem.paragraphs(len);
}

export function randUuid() {
  return uuid();
}

function _randEmailAddr() {
  const emails = faker.definitions.internet.example_email;
  return emails[randInt() % emails.length];
}
