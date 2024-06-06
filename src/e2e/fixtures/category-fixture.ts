import { Category } from "../types/study-types";
import { randInt } from "./rand-fixture";

export type FakeCategory = Pick<Category, "name">;

interface PersistCategory {
  categoryId: number;
  name: string;
}

export const CATEGORIES: PersistCategory[] = [
  { categoryId: 1, name: "개발 프로젝트" },
  { categoryId: 2, name: "모의 면접" },
  { categoryId: 3, name: "코딩 테스트" },
  { categoryId: 4, name: "CS" },
  { categoryId: 5, name: "알고리즘" },
  { categoryId: 6, name: "폐차장" },
  { categoryId: 7, name: "독서 모임" },
  { categoryId: 8, name: "실험실" },
];

export function pickCategory(categoryId: number): PersistCategory | undefined {
  return CATEGORIES.find((c) => c.categoryId === categoryId);
}

export function randCategory(): PersistCategory {
  return CATEGORIES[randInt(0, CATEGORIES.length - 1)]!;
}

export function randCategoryIds(count: number = 3): number[] {
  const set = new Set<number>();
  while (set.size < count) {
    const categoryId = randCategory().categoryId;
    if (set.has(categoryId)) {
      set.add(categoryId);
    }
  }
  return Array.from(set.values());
}

export function randCategoryId() {
  return randCategory().categoryId;
}
