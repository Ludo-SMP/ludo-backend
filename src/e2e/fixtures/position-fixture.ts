import { faker } from "@faker-js/faker";
import { randInt } from "./rand-fixture";

export interface PersistPosition {
  positionId: number;
  name: string;
}

export const POSITIONS: PersistPosition[] = [
  { positionId: 1, name: "프론트엔드" },
  { positionId: 2, name: "백엔드" },
  { positionId: 3, name: "디자인" },
  { positionId: 4, name: "데브옵스" },
];
export function randUrl() {
  return faker.internet.url();
}

export function randPosition(): PersistPosition {
  return POSITIONS[randInt(0, POSITIONS.length - 1)]!;
}

export function randPositionId() {
  return randPosition().positionId;
}

export function randPositionIds(count: number = 3): number[] {
  const ids = new Set<number>();
  while (ids.size < count) {
    ids.add(randPositionId());
  }
  return Array.from(ids);
}
export function randPositions(count: number) {
  const mp = new Map<number, PersistPosition>();

  while (mp.size < count) {
    const p = randPosition();
    if (!mp.has(p.positionId)) {
      mp.set(p.positionId, p);
    }
  }
  return Array.from(mp.values());
}

export function randOneOf<T>(...elems: T[]): T {
  return elems[randInt(0, elems.length - 1)]!;
}
