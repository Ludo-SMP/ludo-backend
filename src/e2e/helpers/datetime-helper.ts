import { applyOffset } from "@formkit/tempo";

//
export function utcNow() {
  return new Date().toISOString().slice(0, 22);
}
export function localNow() {
  return applyOffset(new Date(), "+09:00").toISOString().slice(0, 22);
}

export type UtcArgs = {
  year: number;
  month: number;
  day: number;
  hour: number;
  minute: number;
  second: number;
  millisecond: number;
  microsecond: number;
  nanosecond: number;
};
export function getUtc({
  year = 0,
  month = 0,
  day = 0,
  hour = 0,
  minute = 0,
  second = 0,
  millisecond = 0,
  microsecond = 0,
  nanosecond = 0,
}: UtcArgs) {
  return new Date().toISOString();
}
